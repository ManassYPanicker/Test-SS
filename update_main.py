import re

with open('app/src/main/java/com/example/ui/screens/MainVaultScreen.kt', 'r') as f:
    content = f.read()

# Add imports
imports = """import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.BorderStroke"""
if "import androidx.compose.ui.draw.shadow" not in content:
    content = content.replace("import androidx.compose.ui.draw.clip", "import androidx.compose.ui.draw.clip\nimport androidx.compose.ui.draw.shadow")

# Update FloatingActionButton to glow
fab_old = """      floatingActionButton = {
        FloatingActionButton(
            onClick = { showQuickAddSheet = true },
            containerColor = EmeraldPrimary,
            contentColor = Color(0xFF003324),
            shape = CircleShape,
            modifier = Modifier.testTag("fab_add_item"),
        ) {
          Icon(Icons.Default.Add, contentDescription = "Add Item", modifier = Modifier.size(28.dp))
        }
      },"""
fab_new = """      floatingActionButton = {
        FloatingActionButton(
            onClick = { showQuickAddSheet = true },
            containerColor = EmeraldPrimary,
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier.testTag("fab_add_item").shadow(16.dp, CircleShape, ambientColor = EmeraldPrimary, spotColor = EmeraldPrimary),
        ) {
          Icon(Icons.Default.Add, contentDescription = "Add Item", modifier = Modifier.size(28.dp))
        }
      },"""
content = content.replace(fab_old, fab_new)

# Update Category Cards
cat_old = """            Card(
                modifier =
                    Modifier.width(135.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onCategoryClick(category) }
                        .testTag("cat_card_${category.name}"),
                colors =
                    CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                    ),
                shape = RoundedCornerShape(16.dp),
            ) {"""
cat_new = """            Card(
                modifier = Modifier
                    .width(135.dp)
                    .height(130.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .shadow(8.dp, RoundedCornerShape(22.dp), ambientColor = catColor.copy(alpha = 0.3f), spotColor = catColor.copy(alpha = 0.6f))
                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(22.dp))
                    .clickable { onCategoryClick(category) }
                    .testTag("cat_card_${category.name}"),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                shape = RoundedCornerShape(22.dp),
            ) {"""
content = content.replace(cat_old, cat_new)

# Fix Security Overview Widget glow
sec_old = """      // Security Overview Widget
      item {
        Card(
            modifier =
                Modifier.fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .clickable(onClick = onSecurityCenterClick)
                    .testTag("home_security_center_widget"),
            colors =
                CardDefaults.cardColors(
                    containerColor = EmeraldPrimary.copy(alpha = 0.12f)
                ),
            border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.3f)),
            shape = RoundedCornerShape(18.dp),
        ) {"""
sec_new = """      // Security Overview Widget
      item {
        Card(
            modifier =
                Modifier.fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .shadow(12.dp, RoundedCornerShape(18.dp), ambientColor = EmeraldPrimary.copy(alpha = 0.2f), spotColor = EmeraldPrimary.copy(alpha = 0.4f))
                    .clickable(onClick = onSecurityCenterClick)
                    .testTag("home_security_center_widget"),
            colors =
                CardDefaults.cardColors(
                    containerColor = EmeraldPrimary.copy(alpha = 0.12f)
                ),
            border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.3f)),
            shape = RoundedCornerShape(18.dp),
        ) {"""
content = content.replace(sec_old, sec_new)

with open('app/src/main/java/com/example/ui/screens/MainVaultScreen.kt', 'w') as f:
    f.write(content)

