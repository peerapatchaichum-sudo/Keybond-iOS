import UIKit
import Combine

/// Thin seam over the parts of UIInputViewController the model needs, so
/// KeyboardModel doesn't have to hold the view controller itself.
protocol KeyboardHost: AnyObject {
    var textDocumentProxy: UITextDocumentProxy { get }
    var needsInputModeSwitchKey: Bool { get }
    var hasFullAccess: Bool { get }
    func advanceToNextInputModeIfPossible()
    func dismissKeyboardIfPossible()
}

enum ShiftState: Equatable {
    case lowercase
    case shifted
    case capsLocked

    var isShifted: Bool { self != .lowercase }
}

final class KeyboardModel: ObservableObject {
    @Published var page: KeyboardPage = .letters
    @Published var language: KeyboardLanguage = .english
    @Published var shiftState: ShiftState = .shifted // start capitalized, like iOS
    @Published var showEmojiPanel: Bool = false
    @Published var suggestions: [String] = []
    @Published var typedWord: String = ""

    weak var host: KeyboardHost?
    private let predictor = WordPredictor()
    private var lastCommittedWasSentenceEnd = true

    init() {
        refreshSuggestions()
    }

    var needsInputModeSwitchKey: Bool { host?.needsInputModeSwitchKey ?? false }
    var hasFullAccess: Bool { host?.hasFullAccess ?? false }

    var returnKeyLabel: String {
        switch host?.textDocumentProxy.returnKeyType ?? .default {
        case .go: return "Go"
        case .google, .yahoo, .search: return "Search"
        case .join: return "Join"
        case .next: return "Next"
        case .route: return "Route"
        case .send: return "Send"
        case .done: return "Done"
        case .emergencyCall: return "Emergency"
        case .continue: return "Continue"
        default: return "return"
        }
    }

    // MARK: - Key input

    var letterRows: [[String]] {
        KeyLayouts.letterRows(for: language, shifted: shiftState.isShifted)
    }

    func insert(_ text: String) {
        guard let proxy = host?.textDocumentProxy else { return }
        proxy.insertText(text)

        if text == " " || text == "\n" || isPunctuation(text) {
            if !typedWord.isEmpty {
                predictor.recordUsage(of: typedWord)
            }
            typedWord = ""
            lastCommittedWasSentenceEnd = (text == "\n") || text == "." || text == "!" || text == "?"
            if shiftState != .capsLocked {
                shiftState = lastCommittedWasSentenceEnd ? .shifted : .lowercase
            }
        } else {
            typedWord += text
            if shiftState == .shifted {
                shiftState = .lowercase // one-shot shift like iOS
            }
        }
        refreshSuggestions()
    }

    func deleteBackward() {
        guard let proxy = host?.textDocumentProxy else { return }
        proxy.deleteBackward()
        if !typedWord.isEmpty {
            typedWord.removeLast()
        } else {
            // Deleted past the current word boundary; re-derive from context.
            typedWord = currentWordFromContext()
        }
        refreshSuggestions()
    }

    func commitSuggestion(_ word: String) {
        guard let proxy = host?.textDocumentProxy else { return }
        // Delete the partially typed word, then insert the full suggestion + space.
        for _ in 0..<typedWord.count {
            proxy.deleteBackward()
        }
        proxy.insertText(word + " ")
        predictor.recordUsage(of: word)
        typedWord = ""
        if shiftState != .capsLocked {
            shiftState = .lowercase
        }
        refreshSuggestions()
    }

    func toggleShift() {
        switch shiftState {
        case .lowercase: shiftState = .shifted
        case .shifted: shiftState = .lowercase
        case .capsLocked: shiftState = .lowercase
        }
    }

    /// Double-tap on shift engages caps lock, matching iOS behavior.
    func lockShift() {
        shiftState = .capsLocked
    }

    func toggleNumberPage() {
        page = (page == .letters) ? .numbers : .letters
    }

    func toggleSymbolPage() {
        page = (page == .symbols) ? .numbers : .symbols
    }

    func handleGlobeTap() {
        if needsInputModeSwitchKey {
            host?.advanceToNextInputModeIfPossible()
        } else {
            switchToNextLanguage()
        }
    }

    func switchToNextLanguage() {
        let all = KeyboardLanguage.allCases
        guard let index = all.firstIndex(of: language) else { return }
        language = all[(index + 1) % all.count]
        typedWord = ""
        refreshSuggestions()
    }

    func switchLanguage(to newLanguage: KeyboardLanguage) {
        language = newLanguage
        typedWord = ""
        refreshSuggestions()
    }

    func toggleEmojiPanel() {
        showEmojiPanel.toggle()
    }

    func insertEmoji(_ emoji: String) {
        host?.textDocumentProxy.insertText(emoji)
    }

    func returnKeyTapped() {
        insert("\n")
    }

    // MARK: - Suggestions

    private func refreshSuggestions() {
        suggestions = predictor.suggestions(for: typedWord, language: language)
    }

    private func currentWordFromContext() -> String {
        guard let before = host?.textDocumentProxy.documentContextBeforeInput else { return "" }
        let separators = CharacterSet(charactersIn: " \n\t.,!?;:")
        if let lastSeparator = before.rangeOfCharacter(from: separators, options: .backwards) {
            return String(before[lastSeparator.upperBound...])
        }
        return before
    }

    private func isPunctuation(_ text: String) -> Bool {
        guard text.count == 1, let scalar = text.unicodeScalars.first else { return false }
        return CharacterSet.punctuationCharacters.contains(scalar)
    }
}
