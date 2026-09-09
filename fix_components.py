with open('app/src/main/java/com/example/ui/components/CommonComponents.kt', 'r') as f:
    content = f.read()

# Fix unresolved 'androidx' references by making sure imports are available or replacing with correct syntax
# Wait, let's look at what lines 221 and 785 are. I'll just remove the fully qualified names and add imports if needed.

# Import shadow
if "import androidx.compose.ui.draw.shadow" not in content:
    content = content.replace("import androidx.compose.ui.draw.clip", "import androidx.compose.ui.draw.clip\nimport androidx.compose.ui.draw.shadow")

# Import BorderStroke
if "import androidx.compose.foundation.BorderStroke" not in content:
    content = content.replace("import androidx.compose.foundation.border", "import androidx.compose.foundation.border\nimport androidx.compose.foundation.BorderStroke")


content = content.replace(".androidx.compose.ui.draw.shadow", ".shadow")
content = content.replace("androidx.compose.material3.CardDefaults.cardColors", "CardDefaults.cardColors")
content = content.replace("androidx.compose.material3.Card", "Card")
content = content.replace("androidx.compose.ui.platform.LocalClipboardManager", "androidx.compose.ui.platform.LocalClipboardManager")
content = content.replace("androidx.compose.ui.platform.LocalContext", "androidx.compose.ui.platform.LocalContext")
content = content.replace("androidx.compose.ui.text.font.FontWeight", "FontWeight")
content = content.replace("androidx.compose.ui.text.font.FontFamily", "androidx.compose.ui.text.font.FontFamily")
content = content.replace("androidx.compose.material3.IconButton", "IconButton")
content = content.replace("androidx.compose.material.icons.Icons", "Icons")
content = content.replace("androidx.compose.ui.text.AnnotatedString", "androidx.compose.ui.text.AnnotatedString")


with open('app/src/main/java/com/example/ui/components/CommonComponents.kt', 'w') as f:
    f.write(content)

