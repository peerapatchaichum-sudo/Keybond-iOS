import SwiftUI

struct EmojiPickerView: View {
    @ObservedObject var model: KeyboardModel
    @State private var selectedCategory: String = EmojiData.categories[1].id
    @State private var searchText: String = ""
    @State private var recentEmojis: [String] = []

    private let columns = Array(repeating: GridItem(.flexible(), spacing: 4), count: 7)

    var body: some View {
        VStack(spacing: 6) {
            searchBar

            ScrollView {
                LazyVGrid(columns: columns, spacing: 10) {
                    ForEach(currentEmojis) { item in
                        Button {
                            insert(item.char)
                        } label: {
                            Text(item.char)
                                .font(.system(size: 28))
                                .frame(maxWidth: .infinity)
                        }
                    }
                }
                .padding(.horizontal, 6)
            }

            categoryBar
        }
        .padding(.top, 6)
    }

    private var currentEmojis: [EmojiItem] {
        if !searchText.isEmpty {
            let lower = searchText.lowercased()
            return EmojiData.categories.flatMap { $0.emojis }.filter { item in
                item.keywords.contains { $0.contains(lower) }
            }
        }
        if selectedCategory == "recent" {
            return recentEmojis.map { EmojiItem(char: $0, keywords: []) }
        }
        return EmojiData.categories.first { $0.id == selectedCategory }?.emojis ?? []
    }

    private var searchBar: some View {
        HStack(spacing: 6) {
            Image(systemName: "magnifyingglass")
                .foregroundColor(Theme.placeholderText)
            TextField("", text: $searchText, prompt: Text("Search Emoji").foregroundColor(Theme.placeholderText))
                .foregroundColor(.white)
            if !searchText.isEmpty {
                Button {
                    searchText = ""
                } label: {
                    Image(systemName: "xmark.circle.fill")
                        .foregroundColor(Theme.placeholderText)
                }
            }
        }
        .padding(8)
        .background(Theme.specialKey)
        .cornerRadius(10)
        .padding(.horizontal, 8)
    }

    private var categoryBar: some View {
        HStack(spacing: 4) {
            SpecialKeyView(text: "ABC") {
                model.showEmojiPanel = false
            }
            .frame(width: 54, height: 36)

            ScrollView(.horizontal, showsIndicators: false) {
                HStack(spacing: 20) {
                    ForEach(EmojiData.categories) { category in
                        Button {
                            selectedCategory = category.id
                            searchText = ""
                        } label: {
                            Image(systemName: category.icon)
                                .font(.system(size: 16))
                                .foregroundColor(selectedCategory == category.id && searchText.isEmpty ? .white : Theme.placeholderText)
                        }
                    }
                }
                .padding(.horizontal, 10)
            }

            SpecialKeyView(systemImage: "delete.left") {
                model.deleteBackward()
            }
            .frame(width: 54, height: 36)
        }
        .frame(height: 40)
        .padding(.horizontal, 6)
        .padding(.bottom, 4)
    }

    private func insert(_ emoji: String) {
        model.insertEmoji(emoji)
        recentEmojis.removeAll { $0 == emoji }
        recentEmojis.insert(emoji, at: 0)
        if recentEmojis.count > 30 {
            recentEmojis.removeLast()
        }
    }
}
