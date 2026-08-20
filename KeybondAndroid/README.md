# Keybond (Android)

An Android custom keyboard (IME), styled after the stock iOS keyboard in
`../Keybond-iOS`, with English (QWERTY) and Thai (Kedmanee) layouts, a
working emoji panel, and simple on-device word suggestions. Fully offline —
no network access, no unusual permissions.

This is the Android counterpart to the iOS project in this repo. It's a
separate Gradle project (own language, own framework — Kotlin + Jetpack
Compose + `InputMethodService`, vs. Swift + SwiftUI + `UIInputViewController`
on iOS) but mirrors the same layouts, emoji data, and word lists.

## Project layout

```
app/
  src/main/
    AndroidManifest.xml          MainActivity + the IME service declaration
    java/com/keybond/android/
      ui/
        MainActivity.kt          Onboarding screen (Compose) — deep-links to
                                  Settings to enable/switch the keyboard
      ime/
        KeybondInputMethodService.kt  The IME itself: hosts a ComposeView,
                                       bridges InputConnection actions
        KeyboardState.kt              Page/language/shift/suggestions state
        KeyboardScreen.kt             Root layout: suggestion bar, key rows,
                                       bottom row, key building blocks
        EmojiPickerScreen.kt          Emoji panel: search, categories, grid
        KeyLayouts.kt                 English QWERTY + Thai Kedmanee +
                                       numbers/symbols
        EmojiData.kt                  Bundled emoji + keywords, by category
        WordPredictor.kt              Prefix-based suggestions from a
                                       bundled word list
        Theme.kt                      Colors/metrics for the dark keyboard
    res/
      raw/words_en.txt, words_th.txt  Frequency word lists
      xml/method.xml                  IME subtype declarations (EN/TH)
      mipmap-anydpi-v26/, drawable/   Adaptive launcher icon (vector only)
```

## Opening the project

This was written and reviewed without access to Android Studio or the
Android SDK in that environment, so it hasn't been built yet. To open and
run it:

1. Open this `KeybondAndroid/` folder in Android Studio (Koala/2024.1 or
   newer). Android Studio will generate the Gradle wrapper on first sync if
   it's missing — this project doesn't check in `gradle-wrapper.jar`.
2. Let Gradle sync. Versions used: AGP 8.3.2, Kotlin 1.9.24, Compose
   compiler 1.5.14, Compose BOM 2024.02.00, `compileSdk`/`targetSdk` 34,
   `minSdk` 26.
3. If the applicationId `com.keybond.android` collides with something
   already on your device/account, change it in `app/build.gradle.kts`
   (`defaultConfig.applicationId`) and in `AndroidManifest.xml`'s
   `settingsActivity` reference.
4. Run the `app` configuration on a device or emulator (API 26+).
5. On first run this just shows the container app with setup steps. To try
   the keyboard itself: Settings → System → Languages & input → On-screen
   keyboard → Manage keyboards → turn on **Keybond** → accept the standard
   third-party-keyboard security notice. Then in any text field, tap the
   keyboard-switch icon (or long-press the space bar) and choose Keybond.

## Keyboard features

- **English (QWERTY)** and **Thai (Kedmanee)** layouts, same character
  mapping as the iOS version. Tap the globe key to cycle to the next
  enabled system keyboard; long-press it for a menu to jump directly to
  English or Thai.
- **Shift / caps lock**: tap to shift once, tap again quickly (double-tap)
  to lock caps.
- **123 / #+=** numeric and symbol pages.
- **Suggestion bar**: three tappable word suggestions from a small bundled
  frequency list per language — no dictionary/network calls. Frequently
  used words get boosted for the rest of the session.
- **Emoji panel**: tap the emoji key for a searchable, categorized grid
  with a "Recently Used" tab; tap ABC to return to the keyboard.
- The return key label follows the host field's `imeOptions` action
  (Go / Search / Send / Next / Done), matching native keyboard behavior.

## Notes / intentional omissions

- No launcher icon PNGs — `minSdk` is 26, so the adaptive icon XML in
  `mipmap-anydpi-v26/` covers every supported device and no raster fallback
  is needed.
- No `gradle-wrapper.jar` checked in (it's a binary this environment
  couldn't fetch). Android Studio regenerates it on sync; alternatively
  run `gradle wrapper` once if you have Gradle installed locally.
