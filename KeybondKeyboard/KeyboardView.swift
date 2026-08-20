import SwiftUI

struct KeyboardRootView: View {
    @ObservedObject var model: KeyboardModel

    var body: some View {
        ZStack {
            Theme.background.ignoresSafeArea()

            if model.showEmojiPanel {
                EmojiPickerView(model: model)
            } else {
                VStack(spacing: Theme.rowSpacing) {
                    SuggestionBar(model: model)

                    switch model.page {
                    case .letters:
                        LettersPage(model: model)
                    case .numbers:
                        SymbolsPage(model: model, rows: KeyLayouts.numberRows, toggleLabel: "#+=")
                    case .symbols:
                        SymbolsPage(model: model, rows: KeyLayouts.symbolRows, toggleLabel: "123")
                    }

                    BottomRow(model: model)
                }
                .padding(.horizontal, 4)
                .padding(.top, 6)
                .padding(.bottom, 4)
            }
        }
    }
}

// MARK: - Suggestion bar

private struct SuggestionBar: View {
    @ObservedObject var model: KeyboardModel

    var body: some View {
        HStack(spacing: 0) {
            ForEach(Array(model.suggestions.enumerated()), id: \.offset) { index, word in
                if index > 0 {
                    Rectangle()
                        .fill(Theme.suggestionDivider)
                        .frame(width: 1, height: 20)
                }
                Button {
                    model.commitSuggestion(word)
                } label: {
                    Text(word)
                        .font(.system(size: 16))
                        .foregroundColor(.white)
                        .lineLimit(1)
                        .minimumScaleFactor(0.7)
                        .frame(maxWidth: .infinity)
                }
            }
        }
        .frame(height: 38)
        .background(Theme.suggestionBarBackground)
    }
}

// MARK: - Letters page

private struct LettersPage: View {
    @ObservedObject var model: KeyboardModel

    var body: some View {
        VStack(spacing: Theme.keySpacing) {
            let rows = model.letterRows

            HStack(spacing: Theme.keySpacing) {
                ForEach(rows[0], id: \.self) { letter in
                    CharacterKeyView(label: letter) { model.insert(letter) }
                }
            }
            .padding(.horizontal, 2)

            HStack(spacing: Theme.keySpacing) {
                ForEach(rows[1], id: \.self) { letter in
                    CharacterKeyView(label: letter) { model.insert(letter) }
                }
            }
            .padding(.horizontal, 16)

            WeightedKeyRow(weights: rowThreeWeights(letterCount: rows[2].count)) { index, _ in
                if index == 0 {
                    ShiftKeyView(model: model)
                } else if index == rows[2].count + 1 {
                    SpecialKeyView(systemImage: "delete.left") {
                        model.deleteBackward()
                    }
                } else {
                    let letter = rows[2][index - 1]
                    CharacterKeyView(label: letter) { model.insert(letter) }
                }
            }
        }
    }

    private func rowThreeWeights(letterCount: Int) -> [CGFloat] {
        [1.5] + Array(repeating: CGFloat(1), count: letterCount) + [1.5]
    }
}

/// Shift key with manual double-tap detection for caps lock (a Button's tap
/// gesture and a competing onTapGesture(count: 2) don't reliably coexist).
private struct ShiftKeyView: View {
    @ObservedObject var model: KeyboardModel
    @State private var lastTapTime: Date = .distantPast

    var body: some View {
        SpecialKeyView(
            systemImage: model.shiftState == .capsLocked ? "capslock.fill" : (model.shiftState.isShifted ? "shift.fill" : "shift"),
            isActive: model.shiftState.isShifted
        ) {
            let now = Date()
            if now.timeIntervalSince(lastTapTime) < 0.3 {
                model.lockShift()
            } else {
                model.toggleShift()
            }
            lastTapTime = now
        }
    }
}

// MARK: - Numbers / symbols page

private struct SymbolsPage: View {
    @ObservedObject var model: KeyboardModel
    let rows: [[String]]
    let toggleLabel: String

    var body: some View {
        VStack(spacing: Theme.keySpacing) {
            HStack(spacing: Theme.keySpacing) {
                ForEach(rows[0], id: \.self) { key in
                    CharacterKeyView(label: key, fontSize: 20) { model.insert(key) }
                }
            }

            HStack(spacing: Theme.keySpacing) {
                ForEach(rows[1], id: \.self) { key in
                    CharacterKeyView(label: key, fontSize: 20) { model.insert(key) }
                }
            }
            .padding(.horizontal, 2)

            WeightedKeyRow(weights: rowThreeWeights(count: rows[2].count)) { index, _ in
                if index == 0 {
                    SpecialKeyView(text: toggleLabel) {
                        model.toggleSymbolPage()
                    }
                } else if index == rows[2].count + 1 {
                    SpecialKeyView(systemImage: "delete.left") {
                        model.deleteBackward()
                    }
                } else {
                    let key = rows[2][index - 1]
                    CharacterKeyView(label: key, fontSize: 20) { model.insert(key) }
                }
            }
        }
    }

    private func rowThreeWeights(count: Int) -> [CGFloat] {
        [1.5] + Array(repeating: CGFloat(1), count: count) + [1.5]
    }
}

// MARK: - Bottom row (123 / globe / space / emoji / return)

private struct BottomRow: View {
    @ObservedObject var model: KeyboardModel
    @State private var showLanguageMenu = false

    var body: some View {
        WeightedKeyRow(weights: [1.4, 1.2, 5, 1.2, 1.6]) { index, _ in
            switch index {
            case 0:
                SpecialKeyView(text: model.page == .letters ? "123" : "ABC") {
                    if model.page == .letters {
                        model.page = .numbers
                    } else {
                        model.page = .letters
                    }
                }
            case 1:
                SpecialKeyView(systemImage: "globe") {
                    model.handleGlobeTap()
                }
                .onLongPressGesture {
                    showLanguageMenu = true
                }
                .confirmationDialog("Switch Language", isPresented: $showLanguageMenu, titleVisibility: .visible) {
                    ForEach(KeyboardLanguage.allCases, id: \.self) { language in
                        Button(language.displayName) {
                            model.switchLanguage(to: language)
                        }
                    }
                }
            case 2:
                Button {
                    model.insert(" ")
                } label: {
                    Text(model.language.displayName)
                        .font(.system(size: 13))
                        .foregroundColor(Theme.placeholderText)
                        .frame(maxWidth: .infinity)
                        .frame(height: 42)
                }
                .buttonStyle(KeyBackgroundStyle(color: Theme.letterKey, pressedColor: Theme.letterKeyPressed))
            case 3:
                SpecialKeyView(systemImage: "face.smiling") {
                    model.toggleEmojiPanel()
                }
            default:
                SpecialKeyView(
                    systemImage: model.returnKeyLabel == "return" ? "return" : nil,
                    text: model.returnKeyLabel == "return" ? nil : model.returnKeyLabel
                ) {
                    model.returnKeyTapped()
                }
            }
        }
    }
}
