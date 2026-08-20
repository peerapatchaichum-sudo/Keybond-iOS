package com.keybond.android.ime

import androidx.compose.ui.graphics.Color

/** Colors and metrics tuned to match the stock iOS/Android dark keyboard look. */
object KeyboardColors {
    val background = Color(0xFF1B1B1D)
    val letterKey = Color(0xFF4A4A4E)
    val letterKeyPressed = Color(0xFF6B6B70)
    val specialKey = Color(0xFF29292D)
    val specialKeyPressed = Color(0xFF4D4D51)
    val specialKeyActive = Color.White
    val keyText = Color.White
    val keyTextOnActive = Color.Black
    val suggestionDivider = Color.White.copy(alpha = 0.25f)
    val placeholderText = Color.White.copy(alpha = 0.55f)
}

object KeyboardMetrics {
    const val KEY_CORNER_RADIUS_DP = 5
    const val ROW_SPACING_DP = 10
    const val KEY_SPACING_DP = 6
    const val KEY_HEIGHT_DP = 42
}
