import Foundation

struct EmojiCategory: Identifiable {
    let id: String
    let title: String
    let icon: String // SF Symbol shown in the category tab bar
    let emojis: [EmojiItem]
}

struct EmojiItem: Identifiable, Hashable {
    var id: String { char }
    let char: String
    let keywords: [String]
}

enum EmojiData {
    static let categories: [EmojiCategory] = [
        EmojiCategory(id: "recent", title: "Recently Used", icon: "clock", emojis: []),
        EmojiCategory(id: "smileys", title: "Smileys & People", icon: "face.smiling", emojis: [
            e("😀", "grin", "happy", "smile"),
            e("😃", "smile", "happy"),
            e("😄", "smile", "happy", "laugh"),
            e("😁", "grin", "happy"),
            e("😆", "laugh", "happy"),
            e("😅", "sweat", "laugh"),
            e("🤣", "rofl", "laugh"),
            e("😂", "joy", "laugh", "tears"),
            e("🙂", "slight", "smile"),
            e("🙃", "upside down"),
            e("😉", "wink"),
            e("😊", "blush", "smile"),
            e("😇", "angel", "halo"),
            e("🥰", "love", "hearts"),
            e("😍", "love", "heart eyes"),
            e("🤩", "star struck"),
            e("😘", "kiss"),
            e("😗", "kiss"),
            e("😚", "kiss"),
            e("😙", "kiss"),
            e("😋", "yum", "tongue"),
            e("😛", "tongue"),
            e("😜", "wink", "tongue"),
            e("🤪", "zany", "crazy"),
            e("😝", "tongue", "squint"),
            e("🤑", "money"),
            e("🤗", "hug"),
            e("🤭", "giggle"),
            e("🤫", "shush", "quiet"),
            e("🤔", "think", "thinking"),
            e("🤐", "zipper mouth"),
            e("🤨", "eyebrow", "skeptical"),
            e("😐", "neutral"),
            e("😑", "expressionless"),
            e("😶", "no mouth"),
            e("😏", "smirk"),
            e("😒", "unamused"),
            e("🙄", "eye roll"),
            e("😬", "grimace"),
            e("🤥", "lying", "pinocchio"),
            e("😌", "relieved"),
            e("😔", "pensive", "sad"),
            e("😪", "sleepy"),
            e("🤤", "drool"),
            e("😴", "sleep", "zzz"),
            e("😷", "mask", "sick"),
            e("🤒", "sick", "thermometer"),
            e("🤕", "hurt", "bandage"),
            e("🤢", "nauseous", "sick"),
            e("🤮", "vomit", "sick"),
            e("🥵", "hot"),
            e("🥶", "cold"),
            e("😵", "dizzy"),
            e("🤯", "mind blown"),
            e("🤠", "cowboy"),
            e("😎", "cool", "sunglasses"),
            e("🥳", "party", "celebrate"),
            e("😢", "cry", "sad"),
            e("😭", "sob", "cry"),
            e("😱", "scream", "shocked"),
            e("😡", "angry", "mad"),
            e("😠", "angry"),
            e("🤬", "curse", "angry"),
            e("😳", "flushed"),
            e("🥺", "pleading", "puppy eyes"),
            e("😨", "fearful"),
            e("😰", "anxious", "sweat"),
            e("😥", "sad", "relieved"),
            e("😓", "sweat"),
            e("🤗", "hug"),
            e("👋", "wave", "hello", "bye"),
            e("👍", "thumbs up", "like"),
            e("👎", "thumbs down", "dislike"),
            e("👏", "clap"),
            e("🙌", "praise", "hands up"),
            e("🙏", "pray", "please", "thanks"),
            e("💪", "muscle", "strong"),
            e("🤝", "handshake", "deal")
        ]),
        EmojiCategory(id: "animals", title: "Animals & Nature", icon: "leaf", emojis: [
            e("🐶", "dog"), e("🐱", "cat"), e("🐭", "mouse"), e("🐹", "hamster"),
            e("🐰", "rabbit", "bunny"), e("🦊", "fox"), e("🐻", "bear"), e("🐼", "panda"),
            e("🐨", "koala"), e("🐯", "tiger"), e("🦁", "lion"), e("🐮", "cow"),
            e("🐷", "pig"), e("🐸", "frog"), e("🐵", "monkey"), e("🐔", "chicken"),
            e("🐧", "penguin"), e("🐦", "bird"), e("🦄", "unicorn"), e("🐝", "bee"),
            e("🦋", "butterfly"), e("🐢", "turtle"), e("🐍", "snake"), e("🐳", "whale"),
            e("🐬", "dolphin"), e("🐠", "fish"), e("🌸", "blossom", "flower"),
            e("🌻", "sunflower"), e("🌵", "cactus"), e("🌴", "palm tree"),
            e("🍀", "clover", "lucky"), e("🌈", "rainbow"), e("☀️", "sun"),
            e("⭐️", "star"), e("🌙", "moon")
        ]),
        EmojiCategory(id: "food", title: "Food & Drink", icon: "fork.knife", emojis: [
            e("🍏", "apple"), e("🍎", "apple"), e("🍌", "banana"), e("🍉", "watermelon"),
            e("🍇", "grapes"), e("🍓", "strawberry"), e("🍒", "cherry"), e("🍍", "pineapple"),
            e("🥭", "mango"), e("🥝", "kiwi"), e("🍅", "tomato"), e("🥑", "avocado"),
            e("🌽", "corn"), e("🥕", "carrot"), e("🍞", "bread"), e("🧀", "cheese"),
            e("🍔", "burger", "hamburger"), e("🍟", "fries"), e("🍕", "pizza"),
            e("🌭", "hot dog"), e("🌮", "taco"), e("🍣", "sushi"), e("🍦", "ice cream"),
            e("🍩", "donut"), e("🍪", "cookie"), e("🎂", "cake", "birthday"),
            e("☕️", "coffee"), e("🍵", "tea"), e("🧋", "boba", "bubble tea"),
            e("🍺", "beer"), e("🍷", "wine")
        ]),
        EmojiCategory(id: "activities", title: "Activities", icon: "sportscourt", emojis: [
            e("⚽️", "soccer", "football"), e("🏀", "basketball"), e("🏈", "football"),
            e("⚾️", "baseball"), e("🎾", "tennis"), e("🏐", "volleyball"),
            e("🏓", "ping pong"), e("🏸", "badminton"), e("🥊", "boxing"),
            e("🎮", "game", "controller"), e("🎲", "dice", "game"), e("🎸", "guitar"),
            e("🎧", "headphones", "music"), e("🎤", "microphone", "sing"),
            e("🎨", "art", "paint"), e("🎬", "movie", "film"), e("🏆", "trophy", "win"),
            e("🎯", "target", "dart"), e("🚴", "bike", "cycling")
        ]),
        EmojiCategory(id: "travel", title: "Travel & Places", icon: "airplane", emojis: [
            e("🚗", "car"), e("🚕", "taxi"), e("🚌", "bus"), e("🚓", "police car"),
            e("🚑", "ambulance"), e("🚒", "fire truck"), e("🚲", "bike", "bicycle"),
            e("✈️", "plane", "airplane"), e("🚀", "rocket", "space"), e("🚁", "helicopter"),
            e("⛵️", "boat", "sailboat"), e("🚢", "ship"), e("🚆", "train"),
            e("🗽", "statue of liberty"), e("🗼", "tower"), e("🏝️", "island", "beach"),
            e("🏔️", "mountain"), e("🌋", "volcano"), e("🏰", "castle")
        ]),
        EmojiCategory(id: "objects", title: "Objects", icon: "lightbulb", emojis: [
            e("📱", "phone", "mobile"), e("💻", "laptop", "computer"), e("⌚️", "watch"),
            e("📷", "camera"), e("💡", "bulb", "idea", "light"), e("🔋", "battery"),
            e("💰", "money", "bag"), e("💳", "card", "credit"), e("🔑", "key"),
            e("🔒", "lock"), e("🔓", "unlock"), e("📌", "pin"), e("📎", "clip"),
            e("✏️", "pencil"), e("📚", "books"), e("🎁", "gift", "present"),
            e("⏰", "alarm", "clock"), e("🧭", "compass")
        ]),
        EmojiCategory(id: "symbols", title: "Symbols", icon: "heart", emojis: [
            e("❤️", "heart", "love"), e("🧡", "heart", "orange"), e("💛", "heart", "yellow"),
            e("💚", "heart", "green"), e("💙", "heart", "blue"), e("💜", "heart", "purple"),
            e("🖤", "heart", "black"), e("🤍", "heart", "white"), e("💔", "broken heart"),
            e("✨", "sparkle", "star"), e("💯", "hundred", "perfect"), e("✅", "check", "done"),
            e("❌", "cross", "no"), e("❓", "question"), e("❗️", "exclamation"),
            e("🔥", "fire", "lit"), e("💧", "drop", "water"), e("⚡️", "lightning", "bolt")
        ])
    ]

    private static func e(_ char: String, _ keywords: String...) -> EmojiItem {
        EmojiItem(char: char, keywords: keywords)
    }
}
