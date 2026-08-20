package com.keybond.android.ime

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EmojiPickerScreen(state: KeyboardState) {
    var selectedCategoryId by remember { mutableStateOf(EmojiData.categories[1].id) }
    var searchText by remember { mutableStateOf("") }
    var recentEmojis by remember { mutableStateOf(listOf<String>()) }

    val currentEmojis: List<EmojiItem> = when {
        searchText.isNotEmpty() -> {
            val lower = searchText.lowercase()
            EmojiData.categories.flatMap { it.emojis }
                .filter { item -> item.keywords.any { it.contains(lower) } }
        }
        selectedCategoryId == "recent" -> recentEmojis.map { EmojiItem(it, emptyList()) }
        else -> EmojiData.categories.firstOrNull { it.id == selectedCategoryId }?.emojis.orEmpty()
    }

    Column(modifier = Modifier.fillMaxSize().padding(top = 6.dp)) {
        SearchBar(query = searchText, onQueryChange = { searchText = it })

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.weight(1f).padding(horizontal = 6.dp)
        ) {
            items(currentEmojis, key = { it.char }) { item ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .clickable {
                            state.insertEmoji(item.char)
                            recentEmojis = (listOf(item.char) + recentEmojis.filter { it != item.char }).take(30)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = item.char, fontSize = 26.sp)
                }
            }
        }

        CategoryBar(
            categories = EmojiData.categories,
            selectedId = if (searchText.isEmpty()) selectedCategoryId else "",
            onSelect = { id ->
                selectedCategoryId = id
                searchText = ""
            },
            onBack = { state.toggleEmojiPanel() },
            onDelete = { state.deleteBackward() }
        )
    }
}

@Composable
private fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .background(KeyboardColors.specialKey, RoundedCornerShape(10.dp))
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Filled.Search, contentDescription = null, tint = KeyboardColors.placeholderText)
        Spacer(modifier = Modifier.width(8.dp))
        Box(modifier = Modifier.weight(1f)) {
            if (query.isEmpty()) {
                Text("Search Emoji", color = KeyboardColors.placeholderText, fontSize = 15.sp)
            }
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                singleLine = true,
                textStyle = TextStyle(color = Color.White, fontSize = 15.sp),
                cursorBrush = SolidColor(Color.White)
            )
        }
        if (query.isNotEmpty()) {
            IconButton(onClick = { onQueryChange("") }) {
                Icon(Icons.Filled.Close, contentDescription = null, tint = KeyboardColors.placeholderText)
            }
        }
    }
}

@Composable
private fun CategoryBar(
    categories: List<EmojiCategory>,
    selectedId: String,
    onSelect: (String) -> Unit,
    onBack: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(horizontal = 6.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SpecialKey(modifier = Modifier.width(54.dp).fillMaxHeight(), text = "ABC", onClick = onBack)
        Spacer(modifier = Modifier.width(6.dp))
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            categories.forEach { category ->
                Icon(
                    imageVector = categoryIcon(category.icon),
                    contentDescription = category.title,
                    tint = if (category.id == selectedId) Color.White else KeyboardColors.placeholderText,
                    modifier = Modifier.clickable { onSelect(category.id) }
                )
            }
        }
        Spacer(modifier = Modifier.width(6.dp))
        SpecialKey(modifier = Modifier.width(54.dp).fillMaxHeight(), icon = Icons.Filled.Backspace, onClick = onDelete)
    }
}

private fun categoryIcon(name: String): ImageVector = when (name) {
    "History" -> Icons.Filled.History
    "Face" -> Icons.Filled.Face
    "Pets" -> Icons.Filled.Pets
    "Restaurant" -> Icons.Filled.Restaurant
    "EmojiEvents" -> Icons.Filled.EmojiEvents
    "Flight" -> Icons.Filled.Flight
    "Lightbulb" -> Icons.Filled.Lightbulb
    "Favorite" -> Icons.Filled.Favorite
    else -> Icons.Filled.Face
}
