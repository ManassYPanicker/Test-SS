with open('app/src/main/java/com/example/ui/components/CommonComponents.kt', 'r') as f:
    content = f.read()

# I messed up the imports globally. Let's fix them back.
content = content.replace("CardDefaults.cardColors", "androidx.compose.material3.CardDefaults.cardColors")
content = content.replace("Card(", "androidx.compose.material3.Card(")
content = content.replace("FontWeight", "androidx.compose.ui.text.font.FontWeight")
content = content.replace("androidx.compose.ui.text.font.androidx.compose.ui.text.font.FontWeight", "androidx.compose.ui.text.font.FontWeight")
content = content.replace("IconButton", "androidx.compose.material3.IconButton")
content = content.replace("Icons.", "androidx.compose.material.icons.Icons.")
content = content.replace("androidx.compose.material.icons.androidx.compose.material.icons.Icons", "androidx.compose.material.icons.Icons")

with open('app/src/main/java/com/example/ui/components/CommonComponents.kt', 'w') as f:
    f.write(content)
