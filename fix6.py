with open('app/src/main/java/com/example/ui/components/CommonComponents.kt', 'r') as f:
    content = f.read()

# Fix the stray "import import androidx.compose.ui.draw.clip"
content = content.replace("import import androidx.compose.ui.draw.clip", "import androidx.compose.ui.draw.clip")

with open('app/src/main/java/com/example/ui/components/CommonComponents.kt', 'w') as f:
    f.write(content)
