with open('app/src/main/java/com/example/ui/components/CommonComponents.kt', 'r') as f:
    content = f.read()

# Fix the broken function definition "fun VaultItem.compose.material3.Card("
content = content.replace("fun VaultItem.compose.material3.Card(", "fun VaultItemCard(")
content = content.replace("androidx.compose.material3.androidx.compose.material3.Card", "androidx.compose.material3.Card")

with open('app/src/main/java/com/example/ui/components/CommonComponents.kt', 'w') as f:
    f.write(content)
