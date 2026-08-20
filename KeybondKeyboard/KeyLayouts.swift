import Foundation

enum KeyboardLanguage: String, CaseIterable, Hashable {
    case english = "EN"
    case thai = "TH"

    var displayName: String {
        switch self {
        case .english: return "English"
        case .thai: return "ไทย"
        }
    }
}

enum KeyboardPage: Equatable {
    case letters
    case numbers
    case symbols
}

/// Static key layout data. Thai rows use the standard Kedmanee (TIS 820-2538)
/// mapping, the same layout iOS ships for Thai.
enum KeyLayouts {

    // MARK: Letters

    static func letterRows(for language: KeyboardLanguage, shifted: Bool) -> [[String]] {
        switch language {
        case .english:
            return shifted ? englishShifted : englishLowercase
        case .thai:
            return shifted ? thaiShifted : thaiUnshifted
        }
    }

    private static let englishLowercase: [[String]] = [
        ["q", "w", "e", "r", "t", "y", "u", "i", "o", "p"],
        ["a", "s", "d", "f", "g", "h", "j", "k", "l"],
        ["z", "x", "c", "v", "b", "n", "m"]
    ]

    private static let englishShifted: [[String]] = [
        ["Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"],
        ["A", "S", "D", "F", "G", "H", "J", "K", "L"],
        ["Z", "X", "C", "V", "B", "N", "M"]
    ]

    private static let thaiUnshifted: [[String]] = [
        ["ๆ", "ไ", "ำ", "พ", "ะ", "ั", "ี", "ร", "น", "ย", "บ", "ล"],
        ["ฟ", "ห", "ก", "ด", "เ", "้", "่", "า", "ส", "ว", "ง"],
        ["ผ", "ป", "แ", "อ", "ิ", "ื", "ท", "ม", "ใ", "ฝ"]
    ]

    private static let thaiShifted: [[String]] = [
        ["๐", "\"", "ฎ", "ฑ", "ธ", "ํ", "๊", "ณ", "ฯ", "ญ", "ฐ", ","],
        ["ฤ", "ฆ", "ฏ", "โ", "ฌ", "็", "๋", "ษ", "ศ", "ซ"],
        ["(", ")", "ฉ", "ฮ", "ฺ", "์", "?", "ฒ", "ฬ", "ฦ"]
    ]

    // MARK: Numbers / Symbols (shared across languages, like iOS)

    static let numberRows: [[String]] = [
        ["1", "2", "3", "4", "5", "6", "7", "8", "9", "0"],
        ["-", "/", ":", ";", "(", ")", "฿", "&", "@", "\""],
        [".", ",", "?", "!", "'"]
    ]

    static let symbolRows: [[String]] = [
        ["[", "]", "{", "}", "#", "%", "^", "*", "+", "="],
        ["_", "\\", "|", "~", "<", ">", "€", "£", "¥", "·"],
        [".", ",", "?", "!", "'"]
    ]
}
