import SwiftUI

/// Background + press-state styling shared by every key on the keyboard.
struct KeyBackgroundStyle: ButtonStyle {
    var color: Color
    var pressedColor: Color
    var textColor: Color = Theme.keyText

    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .foregroundColor(textColor)
            .background(configuration.isPressed ? pressedColor : color)
            .cornerRadius(Theme.keyCornerRadius)
            .shadow(color: .black.opacity(0.55), radius: 0, x: 0, y: 1)
    }
}

/// A standard letter/number/symbol key. Fills whatever width its parent gives it.
struct CharacterKeyView: View {
    let label: String
    var fontSize: CGFloat = 22
    let action: () -> Void

    var body: some View {
        Button(action: action) {
            Text(label)
                .font(.system(size: fontSize))
                .frame(maxWidth: .infinity)
                .frame(height: 42)
        }
        .buttonStyle(KeyBackgroundStyle(color: Theme.letterKey, pressedColor: Theme.letterKeyPressed))
    }
}

/// A darker "special" key (shift, backspace, 123, globe, return, emoji, space).
struct SpecialKeyView: View {
    var systemImage: String? = nil
    var text: String? = nil
    var isActive: Bool = false
    let action: () -> Void

    var body: some View {
        Button(action: action) {
            Group {
                if let systemImage {
                    Image(systemName: systemImage)
                        .font(.system(size: 18, weight: .regular))
                } else if let text {
                    Text(text)
                        .font(.system(size: 16, weight: .medium))
                }
            }
            .frame(maxWidth: .infinity)
            .frame(height: 42)
        }
        .buttonStyle(KeyBackgroundStyle(
            color: isActive ? Theme.specialKeyActive : Theme.specialKey,
            pressedColor: isActive ? Theme.specialKeyActive.opacity(0.7) : Theme.specialKeyPressed,
            textColor: isActive ? Theme.keyTextOnActive : Theme.keyText
        ))
    }
}

/// Lays out children in a row where each child gets a width proportional to
/// its weight (used for rows that mix normal keys with wider special keys,
/// e.g. shift/backspace, or the space bar).
struct WeightedKeyRow<Content: View>: View {
    let weights: [CGFloat]
    var spacing: CGFloat = Theme.keySpacing
    @ViewBuilder let content: (Int, CGFloat) -> Content

    var body: some View {
        GeometryReader { geo in
            let totalSpacing = spacing * CGFloat(max(weights.count - 1, 0))
            let availableWidth = max(geo.size.width - totalSpacing, 0)
            let totalWeight = weights.reduce(0, +)
            HStack(spacing: spacing) {
                ForEach(weights.indices, id: \.self) { index in
                    content(index, totalWeight > 0 ? availableWidth * (weights[index] / totalWeight) : 0)
                        .frame(width: totalWeight > 0 ? availableWidth * (weights[index] / totalWeight) : 0)
                }
            }
        }
        .frame(height: 42)
    }
}
