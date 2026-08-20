package com.keybond.android.ime

data class EmojiItem(val char: String, val keywords: List<String>)

data class EmojiCategory(
    val id: String,
    val title: String,
    val icon: String, // Material icon name shown in the category tab bar
    val emojis: List<EmojiItem>
)

object EmojiData {

    private fun e(char: String, vararg keywords: String) = EmojiItem(char, keywords.toList())

    val categories: List<EmojiCategory> = listOf(
        EmojiCategory("recent", "Recently Used", "History", emptyList()),
        EmojiCategory("smileys", "Smileys & People", "Face", listOf(
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
            e("👋", "wave", "hello", "bye"),
            e("👍", "thumbs up", "like"),
            e("👎", "thumbs down", "dislike"),
            e("👏", "clap"),
            e("🙌", "praise", "hands up"),
            e("🙏", "pray", "please", "thanks"),
            e("💪", "muscle", "strong"),
            e("🤝", "handshake", "deal")
        )),
        EmojiCategory("animals", "Animals & Nature", "Pets", listOf(
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
        )),
        EmojiCategory("food", "Food & Drink", "Restaurant", listOf(
            e("🍏", "apple"), e("🍎", "apple"), e("🍌", "banana"), e("🍉", "watermelon"),
            e("🍇", "grapes"), e("🍓", "strawberry"), e("🍒", "cherry"), e("🍍", "pineapple"),
            e("🥭", "mango"), e("🥝", "kiwi"), e("🍅", "tomato"), e("🥑", "avocado"),
            e("🌽", "corn"), e("🥕", "carrot"), e("🍞", "bread"), e("🧀", "cheese"),
            e("🍔", "burger", "hamburger"), e("🍟", "fries"), e("🍕", "pizza"),
            e("🌭", "hot dog"), e("🌮", "taco"), e("🍣", "sushi"), e("🍦", "ice cream"),
            e("🍩", "donut"), e("🍪", "cookie"), e("🎂", "cake", "birthday"),
            e("☕️", "coffee"), e("🍵", "tea"), e("🧋", "boba", "bubble tea"),
            e("🍺", "beer"), e("🍷", "wine")
        )),
        EmojiCategory("activities", "Activities", "EmojiEvents", listOf(
            e("⚽️", "soccer", "football"), e("🏀", "basketball"), e("🏈", "football"),
            e("⚾️", "baseball"), e("🎾", "tennis"), e("🏐", "volleyball"),
            e("🏓", "ping pong"), e("🏸", "badminton"), e("🥊", "boxing"),
            e("🎮", "game", "controller"), e("🎲", "dice", "game"), e("🎸", "guitar"),
            e("🎧", "headphones", "music"), e("🎤", "microphone", "sing"),
            e("🎨", "art", "paint"), e("🎬", "movie", "film"), e("🏆", "trophy", "win"),
            e("🎯", "target", "dart"), e("🚴", "bike", "cycling")
        )),
        EmojiCategory("travel", "Travel & Places", "Flight", listOf(
            e("🚗", "car"), e("🚕", "taxi"), e("🚌", "bus"), e("🚓", "police car"),
            e("🚑", "ambulance"), e("🚒", "fire truck"), e("🚲", "bike", "bicycle"),
            e("✈️", "plane", "airplane"), e("🚀", "rocket", "space"), e("🚁", "helicopter"),
            e("⛵️", "boat", "sailboat"), e("🚢", "ship"), e("🚆", "train"),
            e("🗽", "statue of liberty"), e("🗼", "tower"), e("🏝️", "island", "beach"),
            e("🏔️", "mountain"), e("🌋", "volcano"), e("🏰", "castle")
        )),
        EmojiCategory("objects", "Objects", "Lightbulb", listOf(
            e("📱", "phone", "mobile"), e("💻", "laptop", "computer"), e("⌚️", "watch"),
            e("📷", "camera"), e("💡", "bulb", "idea", "light"), e("🔋", "battery"),
            e("💰", "money", "bag"), e("💳", "card", "credit"), e("🔑", "key"),
            e("🔒", "lock"), e("🔓", "unlock"), e("📌", "pin"), e("📎", "clip"),
            e("✏️", "pencil"), e("📚", "books"), e("🎁", "gift", "present"),
            e("⏰", "alarm", "clock"), e("🧭", "compass")
        )),
        EmojiCategory("symbols", "Symbols", "Favorite", listOf(
            e("❤️", "heart", "love"), e("🧡", "heart", "orange"), e("💛", "heart", "yellow"),
            e("💚", "heart", "green"), e("💙", "heart", "blue"), e("💜", "heart", "purple"),
            e("🖤", "heart", "black"), e("🤍", "heart", "white"), e("💔", "broken heart"),
            e("✨", "sparkle", "star"), e("💯", "hundred", "perfect"), e("✅", "check", "done"),
            e("❌", "cross", "no"), e("❓", "question"), e("❗️", "exclamation"),
            e("🔥", "fire", "lit"), e("💧", "drop", "water"), e("⚡️", "lightning", "bolt")
        ))
    )
}
