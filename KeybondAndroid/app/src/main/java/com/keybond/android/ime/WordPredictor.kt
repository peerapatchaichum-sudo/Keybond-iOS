package com.keybond.android.ime

import android.content.Context
import com.keybond.android.R
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Simple on-device, prefix-based word predictor. No network access — just a
 * bundled frequency-ordered word list per language plus a small in-session
 * boost for recently used words.
 */
class WordPredictor(context: Context) {

    private val wordsByLanguage: Map<KeyboardLanguage, List<String>> = mapOf(
        KeyboardLanguage.ENGLISH to loadWords(context, R.raw.words_en),
        KeyboardLanguage.THAI to loadWords(context, R.raw.words_th)
    )

    private val sessionBoost = mutableMapOf<String, Int>()

    private fun loadWords(context: Context, resId: Int): List<String> {
        val seen = LinkedHashSet<String>()
        context.resources.openRawResource(resId).use { stream ->
            BufferedReader(InputStreamReader(stream, Charsets.UTF_8)).useLines { lines ->
                lines.forEach { raw ->
                    val word = raw.trim()
                    if (word.isNotEmpty()) seen.add(word)
                }
            }
        }
        return seen.toList()
    }

    /**
     * Returns up to [limit] suggestions for the given prefix, ranked by
     * session usage first, then dictionary (frequency) order.
     */
    fun suggestions(prefix: String, language: KeyboardLanguage, limit: Int = 3): List<String> {
        val words = wordsByLanguage[language].orEmpty()
        if (prefix.isEmpty()) {
            // Nothing typed yet: show the most common words, like a native
            // keyboard does before you start a sentence.
            return words.take(limit)
        }
        val lowerPrefix = prefix.lowercase()
        val matches = words.filter { it.lowercase().startsWith(lowerPrefix) }
        val ranked = matches.sortedByDescending { sessionBoost[it.lowercase()] ?: 0 }
        return ranked.take(limit)
    }

    /** Call when the user commits a word (types a space/punctuation after it). */
    fun recordUsage(word: String) {
        val key = word.lowercase()
        if (key.isEmpty()) return
        sessionBoost[key] = (sessionBoost[key] ?: 0) + 1
    }
}
