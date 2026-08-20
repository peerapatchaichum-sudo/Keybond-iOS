package com.keybond.android.ime

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.KeyboardCapslock
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun KeyboardRoot(state: KeyboardState) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            background = KeyboardColors.background,
            surface = KeyboardColors.background
        )
    ) {
        Surface(color = KeyboardColors.background) {
            if (state.showEmojiPanel) {
                EmojiPickerScreen(state = state)
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp)
                        .padding(top = 6.dp, bottom = 4.dp)
                ) {
                    SuggestionBar(state)
                    Spacer(KeyboardMetrics.ROW_SPACING_DP)
                    when (state.page) {
                        KeyboardPage.LETTERS -> LettersPage(state)
                        KeyboardPage.NUMBERS -> SymbolsPage(state, KeyLayouts.numberRows, "#+=")
                        KeyboardPage.SYMBOLS -> SymbolsPage(state, KeyLayouts.symbolRows, "123")
                    }
                    Spacer(KeyboardMetrics.ROW_SPACING_DP)
                    BottomRow(state)
                }
            }
        }
    }
}

@Composable
private fun Spacer(heightDp: Int) {
    Box(modifier = Modifier.height(heightDp.dp))
}

// MARK: - Suggestion bar

@Composable
private fun SuggestionBar(state: KeyboardState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(38.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        state.suggestions.forEachIndexed { index, word ->
            if (index > 0) {
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(20.dp)
                        .background(KeyboardColors.suggestionDivider)
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable { state.commitSuggestion(word) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = word,
                    color = KeyboardColors.keyText,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

// MARK: - Letters page

@Composable
private fun LettersPage(state: KeyboardState) {
    val rows = state.letterRows
    Column(verticalArrangement = Arrangement.spacedBy(KeyboardMetrics.KEY_SPACING_DP.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 2.dp),
            horizontalArrangement = Arrangement.spacedBy(KeyboardMetrics.KEY_SPACING_DP.dp)
        ) {
            rows[0].forEach { letter ->
                CharacterKey(label = letter, modifier = Modifier.weight(1f)) { state.insert(letter) }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(KeyboardMetrics.KEY_SPACING_DP.dp)
        ) {
            rows[1].forEach { letter ->
                CharacterKey(label = letter, modifier = Modifier.weight(1f)) { state.insert(letter) }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(KeyboardMetrics.KEY_SPACING_DP.dp)
        ) {
            ShiftKey(state = state, modifier = Modifier.weight(1.5f))
            rows[2].forEach { letter ->
                CharacterKey(label = letter, modifier = Modifier.weight(1f)) { state.insert(letter) }
            }
            SpecialKey(modifier = Modifier.weight(1.5f), icon = Icons.Filled.Backspace) {
                state.deleteBackward()
            }
        }
    }
}

// MARK: - Numbers / symbols page

@Composable
private fun SymbolsPage(state: KeyboardState, rows: List<List<String>>, toggleLabel: String) {
    Column(verticalArrangement = Arrangement.spacedBy(KeyboardMetrics.KEY_SPACING_DP.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(KeyboardMetrics.KEY_SPACING_DP.dp)
        ) {
            rows[0].forEach { key ->
                CharacterKey(label = key, modifier = Modifier.weight(1f), fontSize = 20.sp) { state.insert(key) }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 2.dp),
            horizontalArrangement = Arrangement.spacedBy(KeyboardMetrics.KEY_SPACING_DP.dp)
        ) {
            rows[1].forEach { key ->
                CharacterKey(label = key, modifier = Modifier.weight(1f), fontSize = 20.sp) { state.insert(key) }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(KeyboardMetrics.KEY_SPACING_DP.dp)
        ) {
            SpecialKey(modifier = Modifier.weight(1.5f), text = toggleLabel) {
                state.toggleSymbolPage()
            }
            rows[2].forEach { key ->
                CharacterKey(label = key, modifier = Modifier.weight(1f), fontSize = 20.sp) { state.insert(key) }
            }
            SpecialKey(modifier = Modifier.weight(1.5f), icon = Icons.Filled.Backspace) {
                state.deleteBackward()
            }
        }
    }
}

// MARK: - Bottom row (123 / globe / space / emoji / return)

@Composable
private fun BottomRow(state: KeyboardState) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(KeyboardMetrics.KEY_SPACING_DP.dp)
    ) {
        SpecialKey(
            modifier = Modifier.weight(1.4f),
            text = if (state.page == KeyboardPage.LETTERS) "123" else "ABC"
        ) {
            state.setPage(if (state.page == KeyboardPage.LETTERS) KeyboardPage.NUMBERS else KeyboardPage.LETTERS)
        }

        GlobeKey(state = state, modifier = Modifier.weight(1.2f))

        Box(
            modifier = Modifier
                .weight(5f)
                .height(KeyboardMetrics.KEY_HEIGHT_DP.dp)
                .clip(RoundedCornerShape(KeyboardMetrics.KEY_CORNER_RADIUS_DP.dp))
                .background(KeyboardColors.letterKey)
                .clickable { state.insert(" ") },
            contentAlignment = Alignment.Center
        ) {
            Text(text = state.language.displayName, color = KeyboardColors.placeholderText, fontSize = 13.sp)
        }

        SpecialKey(modifier = Modifier.weight(1.2f), icon = Icons.Filled.EmojiEmotions) {
            state.toggleEmojiPanel()
        }

        SpecialKey(modifier = Modifier.weight(1.6f), text = state.returnKeyLabel) {
            state.returnKeyTapped()
        }
    }
}

@Composable
private fun GlobeKey(state: KeyboardState, modifier: Modifier = Modifier) {
    var showMenu by remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        SpecialKey(
            modifier = Modifier.fillMaxWidth(),
            icon = Icons.Filled.Public,
            onLongClick = { showMenu = true },
            onClick = { state.handleGlobeTap() }
        )
        DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
            KeyboardLanguage.entries.forEach { language ->
                DropdownMenuItem(
                    text = { Text(language.displayName) },
                    onClick = {
                        state.switchLanguage(language)
                        showMenu = false
                    }
                )
            }
        }
    }
}

// MARK: - Shift key (double-tap = caps lock)

@Composable
private fun ShiftKey(state: KeyboardState, modifier: Modifier = Modifier) {
    var lastTapTime by remember { mutableLongStateOf(0L) }
    val icon = if (state.shiftState == ShiftState.CAPS_LOCKED) Icons.Filled.KeyboardCapslock else Icons.Filled.ArrowUpward
    SpecialKey(
        modifier = modifier,
        icon = icon,
        isActive = state.shiftState.isShifted
    ) {
        val now = System.currentTimeMillis()
        if (now - lastTapTime < 300) {
            state.lockShift()
        } else {
            state.toggleShift()
        }
        lastTapTime = now
    }
}

// MARK: - Key building blocks

@Composable
internal fun CharacterKey(
    label: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 22.sp,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    Box(
        modifier = modifier
            .height(KeyboardMetrics.KEY_HEIGHT_DP.dp)
            .clip(RoundedCornerShape(KeyboardMetrics.KEY_CORNER_RADIUS_DP.dp))
            .background(if (isPressed) KeyboardColors.letterKeyPressed else KeyboardColors.letterKey)
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(text = label, color = KeyboardColors.keyText, fontSize = fontSize)
    }
}

@Composable
internal fun SpecialKey(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    text: String? = null,
    isActive: Boolean = false,
    onLongClick: (() -> Unit)? = null,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val background = when {
        isActive -> KeyboardColors.specialKeyActive
        isPressed -> KeyboardColors.specialKeyPressed
        else -> KeyboardColors.specialKey
    }
    val foreground = if (isActive) KeyboardColors.keyTextOnActive else KeyboardColors.keyText
    Box(
        modifier = modifier
            .height(KeyboardMetrics.KEY_HEIGHT_DP.dp)
            .clip(RoundedCornerShape(KeyboardMetrics.KEY_CORNER_RADIUS_DP.dp))
            .background(background)
            .combinedClickable(
                interactionSource = interactionSource,
                indication = null,
                onLongClick = onLongClick,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        when {
            icon != null -> Icon(imageVector = icon, contentDescription = null, tint = foreground)
            text != null -> Text(
                text = text,
                color = foreground,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
