import re

with open('app/src/main/java/com/example/ui/components/CommonComponents.kt', 'r') as f:
    content = f.read()

# I am going to nuke the broken imports block and reset it with clean imports.
start_idx = content.find("import Icons")
if start_idx != -1:
    end_idx = content.find("import androidx.compose.material3.LinearProgressIndicator")
    if end_idx != -1:
        clean_imports = """
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Key
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Note
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
"""
        content = content[:start_idx] + clean_imports + content[end_idx:]

# Also fix the inner class usage of VaultItemCard
content = content.replace("VaultItemandroidx.compose.material3.Card", "VaultItemCard")

# Also fix any remaining local compose imports that may be broken
content = content.replace("androidx.compose.ui.draw.clip\nimport androidx.compose.ui.draw.shadow", "import androidx.compose.ui.draw.clip\nimport androidx.compose.ui.draw.shadow")

with open('app/src/main/java/com/example/ui/components/CommonComponents.kt', 'w') as f:
    f.write(content)
