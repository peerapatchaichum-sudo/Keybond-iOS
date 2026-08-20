package com.keybond.android.ime

import android.graphics.Color
import android.inputmethodservice.InputMethodService
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import java.io.PrintWriter
import java.io.StringWriter

/**
 * The keyboard extension itself. Hosts a Compose UI inside the IME's input
 * view; a ComposeView outside of an Activity needs its own Lifecycle and
 * SavedStateRegistry owners wired up manually, which is what the two
 * interfaces below provide. (No ViewModelStoreOwner is needed since nothing
 * here uses androidx.lifecycle.viewmodel.)
 */
class KeybondInputMethodService :
    InputMethodService(),
    LifecycleOwner,
    SavedStateRegistryOwner,
    KeyboardActionHandler {

    companion object {
        private const val TAG = "KeybondIME"
    }

    private val lifecycleRegistry = LifecycleRegistry(this)
    private val savedStateRegistryController = SavedStateRegistryController.create(this)

    override val lifecycle: Lifecycle get() = lifecycleRegistry
    override val savedStateRegistry: SavedStateRegistry get() = savedStateRegistryController.savedStateRegistry

    private var state: KeyboardState? = null

    override fun onCreate() {
        savedStateRegistryController.performRestore(null)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        super.onCreate()
        try {
            val newState = KeyboardState(WordPredictor(this))
            newState.actionHandler = this
            state = newState
        } catch (t: Throwable) {
            Log.e(TAG, "Failed to initialize KeyboardState", t)
        }
    }

    override fun onCreateInputView(): View {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        return try {
            val currentState = state ?: error("KeyboardState was never initialized")
            ComposeView(this).apply {
                setViewTreeLifecycleOwner(this@KeybondInputMethodService)
                setViewTreeSavedStateRegistryOwner(this@KeybondInputMethodService)
                setContent {
                    KeyboardRoot(state = currentState)
                }
            }
        } catch (t: Throwable) {
            Log.e(TAG, "Failed to create keyboard view", t)
            crashFallbackView(t)
        }
    }

    private fun crashFallbackView(t: Throwable): View {
        val writer = StringWriter()
        t.printStackTrace(PrintWriter(writer))
        return TextView(this).apply {
            text = "Keybond crashed:\n\n$writer"
            setTextColor(Color.WHITE)
            setBackgroundColor(Color.BLACK)
            textSize = 10f
            gravity = Gravity.START
            setPadding(24, 24, 24, 24)
            setHorizontallyScrolling(true)
            isVerticalScrollBarEnabled = true
        }
    }

    override fun onStartInputView(info: EditorInfo?, restarting: Boolean) {
        super.onStartInputView(info, restarting)
        try {
            state?.resetForNewInputSession()
        } catch (t: Throwable) {
            Log.e(TAG, "resetForNewInputSession failed", t)
        }
    }

    override fun onDestroy() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        super.onDestroy()
    }

    // MARK: - KeyboardActionHandler

    override fun commitText(text: String) {
        currentInputConnection?.commitText(text, 1)
    }

    override fun deleteBackward() {
        currentInputConnection?.deleteSurroundingText(1, 0)
    }

    override fun performReturnAction() {
        val ic = currentInputConnection ?: return
        val info = currentInputEditorInfo
        if (info == null) {
            ic.commitText("\n", 1)
            return
        }
        val action = info.imeOptions and EditorInfo.IME_MASK_ACTION
        val noEnterFlag = (info.imeOptions and EditorInfo.IME_FLAG_NO_ENTER_ACTION) != 0
        if (!noEnterFlag && action != EditorInfo.IME_ACTION_NONE && action != EditorInfo.IME_ACTION_UNSPECIFIED) {
            ic.performEditorAction(action)
        } else {
            ic.commitText("\n", 1)
        }
    }

    override fun returnKeyLabel(): String {
        val info = currentInputEditorInfo ?: return "return"
        val noEnterFlag = (info.imeOptions and EditorInfo.IME_FLAG_NO_ENTER_ACTION) != 0
        if (noEnterFlag) return "return"
        return when (info.imeOptions and EditorInfo.IME_MASK_ACTION) {
            EditorInfo.IME_ACTION_GO -> "Go"
            EditorInfo.IME_ACTION_SEARCH -> "Search"
            EditorInfo.IME_ACTION_SEND -> "Send"
            EditorInfo.IME_ACTION_NEXT -> "Next"
            EditorInfo.IME_ACTION_DONE -> "Done"
            EditorInfo.IME_ACTION_PREVIOUS -> "Prev"
            else -> "return"
        }
    }

    override fun switchToNextInputMethod() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager ?: return
        val token = window?.window?.attributes?.token ?: return
        imm.switchToNextInputMethod(token, false)
    }
}
