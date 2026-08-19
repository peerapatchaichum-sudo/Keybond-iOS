import Foundation

/// Simple on-device, prefix-based word predictor. No network access, no
/// UITextChecker dependency — just a bundled frequency-ordered word list
/// per language plus a small in-session boost for recently typed words.
final class WordPredictor {
    private var wordsByLanguage: [KeyboardLanguage: [String]] = [:]
    private var sessionBoost: [String: Int] = [:]

    init() {
        wordsByLanguage[.english] = Self.loadWords(resource: "words_en")
        wordsByLanguage[.thai] = Self.loadWords(resource: "words_th")
    }

    private static func loadWords(resource: String) -> [String] {
        guard let url = Bundle.main.url(forResource: resource, withExtension: "txt"),
              let contents = try? String(contentsOf: url, encoding: .utf8) else {
            return []
        }
        var seen = Set<String>()
        var ordered: [String] = []
        for raw in contents.split(separator: "\n") {
            let word = String(raw).trimmingCharacters(in: .whitespaces)
            guard !word.isEmpty, !seen.contains(word) else { continue }
            seen.insert(word)
            ordered.append(word)
        }
        return ordered
    }

    /// Returns up to `limit` suggestions for the given prefix, ranked by
    /// session usage first, then dictionary (frequency) order.
    func suggestions(for prefix: String, language: KeyboardLanguage, limit: Int = 3) -> [String] {
        let words = wordsByLanguage[language] ?? []
        guard !prefix.isEmpty else {
            // Nothing typed yet: show the most common words, like iOS does
            // before you start a sentence.
            return Array(words.prefix(limit))
        }
        let lowerPrefix = prefix.lowercased()

        let matches = words.filter { $0.lowercased().hasPrefix(lowerPrefix) }
        let ranked = matches.sorted { a, b in
            let boostA = sessionBoost[a.lowercased()] ?? 0
            let boostB = sessionBoost[b.lowercased()] ?? 0
            if boostA != boostB { return boostA > boostB }
            return false // keep dictionary (frequency) order otherwise
        }
        return Array(ranked.prefix(limit))
    }

    /// Call when the user commits a word (types a space/punctuation after it)
    /// so it ranks higher next time.
    func recordUsage(of word: String) {
        let key = word.lowercased()
        guard !key.isEmpty else { return }
        sessionBoost[key, default: 0] += 1
    }
}
