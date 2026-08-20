package com.keybond.android.ime

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

enum class ShiftState {
    LOWERCASE, SHIFTED, CAPS_LOCKED;

    val isShifted: Boolean get() = this != LOWERCASE
}

/** Seam over the parts of the IME service that KeyboardState needs to act on. */
interface KeyboardActionHandler {
    fun commitText(text: String)
    fun deleteBackward()
    fun performReturnAction()
    fun returnKeyLabel(): String
    fun switchToNextInputMethod()
}

private val punctuationChars = setOf('.', ',', '?', '!', ';', ':', '"', '\'', '(', ')', '-', '/', '_')

/** Holds all mutable keyboard UI state: page, language, shift, suggestions, emoji panel. */
class KeyboardState(private val predictor: WordPredictor) {

    var actionHandler: KeyboardActionHandler? = null

    var page by mutableStateOf(KeyboardPage.LETTERS)
        private set
    var language by mutableStateOf(KeyboardLanguage.ENGLISH)
        private set
    var shiftState by mutableStateOf(ShiftState.SHIFTED)
        private set
    var showEmojiPanel by mutableStateOf(false)
        private set
    var suggestions by mutableStateOf(predictor.suggestions("", KeyboardLanguage.ENGLISH))
        private set
    var returnKeyLabel by mutableStateOf("return")
        private set

    private var typedWord = ""

    val letterRows: List<List<String>>
        get() = KeyLayouts.letterRows(language, shiftState.isShifted)

    fun resetForNewInputSession() {
        page = KeyboardPage.LETTERS
        showEmojiPanel = false
        shiftState = ShiftState.SHIFTED
        typedWord = ""
        returnKeyLabel = actionHandler?.returnKeyLabel() ?: "return"
        refreshSuggestions()
    }

    fun insert(text: String) {
        val handler = actionHandler ?: return
        handler.commitText(text)

        if (text == " " || isPunctuation(text)) {
            if (typedWord.isNotEmpty()) predictor.recordUsage(typedWord)
            typedWord = ""
            if (shiftState != ShiftState.CAPS_LOCKED) {
                shiftState = if (text == ".") ShiftState.SHIFTED else ShiftState.LOWERCASE
            }
        } else {
            typedWord += text
            if (shiftState == ShiftState.SHIFTED) {
                shiftState = ShiftState.LOWERCASE // one-shot shift, like a native keyboard
            }
        }
        refreshSuggestions()
    }

    fun deleteBackward() {
        val handler = actionHandler ?: return
        handler.deleteBackward()
        if (typedWord.isNotEmpty()) {
            typedWord = typedWord.dropLast(1)
        }
        refreshSuggestions()
    }

    fun commitSuggestion(word: String) {
        val handler = actionHandler ?: return
        repeat(typedWord.length) { handler.deleteBackward() }
        handler.commitText("$word ")
        predictor.recordUsage(word)
        typedWord = ""
        if (shiftState != ShiftState.CAPS_LOCKED) shiftState = ShiftState.LOWERCASE
        refreshSuggestions()
    }

    fun toggleShift() {
        shiftState = when (shiftState) {
            ShiftState.LOWERCASE -> ShiftState.SHIFTED
            ShiftState.SHIFTED -> ShiftState.LOWERCASE
            ShiftState.CAPS_LOCKED -> ShiftState.LOWERCASE
        }
    }

    /** Double-tap on shift engages caps lock, matching iOS/native behavior. */
    fun lockShift() {
        shiftState = ShiftState.CAPS_LOCKED
    }

    fun setPage(newPage: KeyboardPage) {
        page = newPage
    }

    fun toggleSymbolPage() {
        page = if (page == KeyboardPage.SYMBOLS) KeyboardPage.NUMBERS else KeyboardPage.SYMBOLS
    }

    fun handleGlobeTap() {
        actionHandler?.switchToNextInputMethod()
    }

    fun switchLanguage(to: KeyboardLanguage) {
        language = to
        typedWord = ""
        refreshSuggestions()
    }

    fun switchToNextLanguage() {
        language = language.next()
        typedWord = ""
        refreshSuggestions()
    }

    fun toggleEmojiPanel() {
        showEmojiPanel = !showEmojiPanel
    }

    fun insertEmoji(emoji: String) {
        actionHandler?.commitText(emoji)
    }

    fun returnKeyTapped() {
        if (typedWord.isNotEmpty()) predictor.recordUsage(typedWord)
        typedWord = ""
        if (shiftState != ShiftState.CAPS_LOCKED) shiftState = ShiftState.SHIFTED
        actionHandler?.performReturnAction()
        refreshSuggestions()
    }

    private fun refreshSuggestions() {
        suggestions = predictor.suggestions(typedWord, language)
    }

    private fun isPunctuation(text: String) = text.length == 1 && text[0] in punctuationChars
}
