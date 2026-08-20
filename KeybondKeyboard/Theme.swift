import SwiftUI

/// Colors and metrics tuned to match the stock iOS dark keyboard.
enum Theme {
    static let background = Color(red: 0.106, green: 0.106, blue: 0.114) // iOS keyboard dark background
    static let letterKey = Color(red: 0.290, green: 0.290, blue: 0.306) // light gray key
    static let letterKeyPressed = Color(red: 0.42, green: 0.42, blue: 0.44)
    static let specialKey = Color(red: 0.161, green: 0.161, blue: 0.176) // darker key (shift, backspace, 123)
    static let specialKeyPressed = Color(red: 0.30, green: 0.30, blue: 0.32)
    static let specialKeyActive = Color.white // shift/caps active state
    static let keyText = Color.white
    static let keyTextOnActive = Color.black
    static let suggestionBarBackground = Color(red: 0.106, green: 0.106, blue: 0.114)
    static let suggestionDivider = Color.white.opacity(0.25)
    static let placeholderText = Color.white.opacity(0.55)

    static let keyCornerRadius: CGFloat = 5
    static let rowSpacing: CGFloat = 10
    static let keySpacing: CGFloat = 6
}
