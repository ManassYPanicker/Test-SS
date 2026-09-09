with open('app/src/main/java/com/example/ui/components/CommonComponents.kt', 'r') as f:
    content = f.read()

# I completely broke the imports. Instead of playing with regex/replace, let's just properly add what we need at the top.
# The python replace caused "VaultItemandroidx"

content = content.replace("VaultItemandroidx", "VaultItem")
content = content.replace("androidx.compose.material3.androidx.compose.material3.Card", "androidx.compose.material3.Card")
content = content.replace("import androidx.compose.material3.androidx.compose.material3.Card", "import androidx.compose.material3.Card")

# The easiest way is to add standard material imports to the top and let the IDE resolve them.
imports_to_add = """
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.ui.text.font.FontWeight
"""

if "import androidx.compose.material3.Card" not in content:
    content = content.replace("import androidx.compose.material3.Icon", "import androidx.compose.material3.Icon" + imports_to_add)

with open('app/src/main/java/com/example/ui/components/CommonComponents.kt', 'w') as f:
    f.write(content)
