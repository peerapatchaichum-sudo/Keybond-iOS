package com.keybond.android.ime

enum class KeyboardLanguage(val displayName: String) {
    ENGLISH("English"),
    THAI("ไทย");

    fun next(): KeyboardLanguage {
        val all = entries
        return all[(all.indexOf(this) + 1) % all.size]
    }
}

enum class KeyboardPage {
    LETTERS, NUMBERS, SYMBOLS
}

/**
 * Static key layout data. Thai rows use the standard Kedmanee (TIS 820-2538)
 * mapping, the same layout iOS/Android ship for Thai.
 */
object KeyLayouts {

    fun letterRows(language: KeyboardLanguage, shifted: Boolean): List<List<String>> =
        when (language) {
            KeyboardLanguage.ENGLISH -> if (shifted) englishShifted else englishLowercase
            KeyboardLanguage.THAI -> if (shifted) thaiShifted else thaiUnshifted
        }

    private val englishLowercase = listOf(
        listOf("q", "w", "e", "r", "t", "y", "u", "i", "o", "p"),
        listOf("a", "s", "d", "f", "g", "h", "j", "k", "l"),
        listOf("z", "x", "c", "v", "b", "n", "m")
    )

    private val englishShifted = listOf(
        listOf("Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"),
        listOf("A", "S", "D", "F", "G", "H", "J", "K", "L"),
        listOf("Z", "X", "C", "V", "B", "N", "M")
    )

    private val thaiUnshifted = listOf(
        listOf("ๆ", "ไ", "ำ", "พ", "ะ", "ั", "ี", "ร", "น", "ย", "บ", "ล"),
        listOf("ฟ", "ห", "ก", "ด", "เ", "้", "่", "า", "ส", "ว", "ง"),
        listOf("ผ", "ป", "แ", "อ", "ิ", "ื", "ท", "ม", "ใ", "ฝ")
    )

    private val thaiShifted = listOf(
        listOf("๐", "\"", "ฎ", "ฑ", "ธ", "ํ", "๊", "ณ", "ฯ", "ญ", "ฐ", ","),
        listOf("ฤ", "ฆ", "ฏ", "โ", "ฌ", "็", "๋", "ษ", "ศ", "ซ"),
        listOf("(", ")", "ฉ", "ฮ", "ฺ", "์", "?", "ฒ", "ฬ", "ฦ")
    )

    val numberRows = listOf(
        listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0"),
        listOf("-", "/", ":", ";", "(", ")", "฿", "&", "@", "\""),
        listOf(".", ",", "?", "!", "'")
    )

    val symbolRows = listOf(
        listOf("[", "]", "{", "}", "#", "%", "^", "*", "+", "="),
        listOf("_", "\\", "|", "~", "<", ">", "€", "£", "¥", "·"),
        listOf(".", ",", "?", "!", "'")
    )
}
