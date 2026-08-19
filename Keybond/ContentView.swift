import SwiftUI
import UIKit

struct ContentView: View {
    private let steps = [
        "Open Settings and go to General > Keyboard > Keyboards.",
        "Tap \"Add New Keyboard…\" and choose Keybond.",
        "Tap Keybond in the list to enable it. No extra permissions needed — Keybond works fully offline.",
        "In any app, tap the globe key on the keyboard to switch to Keybond."
    ]

    var body: some View {
        NavigationView {
            ScrollView {
                VStack(alignment: .leading, spacing: 24) {
                    VStack(alignment: .leading, spacing: 8) {
                        Text("Keybond")
                            .font(.largeTitle.bold())
                        Text("An iOS-style keyboard with English and Thai layouts.")
                            .font(.subheadline)
                            .foregroundColor(.secondary)
                    }

                    VStack(alignment: .leading, spacing: 16) {
                        Text("Setup")
                            .font(.headline)
                        ForEach(Array(steps.enumerated()), id: \.offset) { index, step in
                            HStack(alignment: .top, spacing: 12) {
                                Text("\(index + 1)")
                                    .font(.caption.bold())
                                    .foregroundColor(.white)
                                    .frame(width: 22, height: 22)
                                    .background(Circle().fill(Color.accentColor))
                                Text(step)
                                    .font(.body)
                            }
                        }
                    }

                    Button {
                        if let url = URL(string: UIApplication.openSettingsURLString) {
                            UIApplication.shared.open(url)
                        }
                    } label: {
                        Text("Open Settings")
                            .font(.headline)
                            .frame(maxWidth: .infinity)
                            .padding()
                            .background(Color.accentColor)
                            .foregroundColor(.white)
                            .cornerRadius(12)
                    }
                    .padding(.top, 8)
                }
                .padding()
            }
            .navigationTitle("")
            .navigationBarHidden(true)
        }
    }
}

#Preview {
    ContentView()
}
