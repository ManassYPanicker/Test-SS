with open('app/src/main/java/com/example/ui/components/CommonComponents.kt', 'r') as f:
    content = f.read()

# Fix the import clip error and the syntax error around line 75
import re
content = re.sub(r'import androidx\.compose\.ui\.text\.font\.FontWeightButton', r'import androidx.compose.ui.text.font.FontWeight\nimport androidx.compose.material3.Button', content)

if "import androidx.compose.ui.draw.clip" not in content:
    content = content.replace("import androidx.compose.ui.draw.shadow", "import androidx.compose.ui.draw.clip\nimport androidx.compose.ui.draw.shadow")

with open('app/src/main/java/com/example/ui/components/CommonComponents.kt', 'w') as f:
    f.write(content)
