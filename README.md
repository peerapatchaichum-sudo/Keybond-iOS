# Keybond

An iOS custom keyboard, styled after the stock iOS keyboard, with English
(QWERTY) and Thai (Kedmanee) layouts, a working emoji panel, and simple
on-device word suggestions. Fully offline — no network access, no full
access permission required.

## Project layout

```
Keybond.xcodeproj/         Xcode project (app + keyboard extension targets)
Keybond/                   Container app (SwiftUI) — onboarding/setup screen
KeybondKeyboard/           The keyboard extension itself
  KeyboardViewController.swift   UIInputViewController that hosts the SwiftUI keyboard
  KeyboardModel.swift            State: shift/caps, page, language, suggestions
  KeyboardView.swift             Root layout: suggestion bar, key rows, bottom row
  KeyButtonView.swift            Reusable key styles + weighted row layout
  KeyLayouts.swift               English QWERTY + Thai Kedmanee + numbers/symbols
  EmojiPickerView.swift          Emoji panel: search, categories, grid
  EmojiData.swift                Bundled emoji + keywords, by category
  WordPredictor.swift            Prefix-based suggestions from a bundled word list
  Theme.swift                    Colors/metrics matching the iOS dark keyboard
  Resources/words_en.txt         English frequency word list
  Resources/words_th.txt         Thai frequency word list
```

## Opening the project

This was built and hand-verified on Linux (no Xcode available in that
environment), so it hasn't been compiled in Xcode yet. To open and run it:

1. Open `Keybond.xcodeproj` in Xcode 15+.
2. Select the **Keybond** scheme and set your own Team under
   **Signing & Capabilities** for both the `Keybond` and `KeybondKeyboard`
   targets (bundle IDs default to `com.keybond.app` and
   `com.keybond.app.KeybondKeyboard` — change these if they collide with an
   existing App ID in your account).
3. Build and run on a simulator or device.
4. On first run this just shows the container app with setup steps. To try
   the keyboard itself: Settings → General → Keyboard → Keyboards → Add New
   Keyboard… → Keybond, then switch to it via the globe key in any text
   field (Notes, Messages, etc.).

## Keyboard features

- **English (QWERTY)** and **Thai (Kedmanee)** layouts. Tap the globe key to
  cycle to the next system keyboard (standard iOS behavior); long-press the
  globe key to jump directly to English or Thai.
- **Shift / caps lock**: tap to shift once, tap again quickly (double-tap)
  to lock caps — same as iOS.
- **123 / #+=** numeric and symbol pages, mirroring the stock layout.
- **Suggestion bar**: three tappable word suggestions above the keys, from a
  small bundled frequency list per language (no dictionary/network calls).
  Frequently used words get boosted for the rest of the session.
- **Emoji panel**: tap the smiley key for a searchable, categorized emoji
  grid with a "Recently Used" tab; tap ABC to return to the keyboard.
- The return key label follows the host app's configured `returnKeyType`
  (Go / Search / Send / Done / etc.), matching native keyboard behavior.

## Notes / intentional omissions

- No dictation (mic) key — third-party keyboard extensions cannot invoke
  the system dictation UI, so a non-functional mic button was left out
  rather than shown and silently doing nothing.
- No "Allow Full Access" request — nothing in this keyboard needs it
  (suggestions are from a bundled word list, not a network or shared
  container).
