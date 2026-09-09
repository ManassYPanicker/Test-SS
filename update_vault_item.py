with open('app/src/main/java/com/example/ui/components/CommonComponents.kt', 'r') as f:
    content = f.read()

# Add shadow to VaultItemCard
old_card = """  Card(
      modifier =
          modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(16.dp))
              .clickable(onClick = onClick)
              .testTag("vault_item_${item.id}"),
      colors =
          CardDefaults.cardColors(
              containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f)
          ),
      shape = RoundedCornerShape(16.dp),
  ) {"""
new_card = """  Card(
      modifier =
          modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(20.dp))
              .androidx.compose.ui.draw.shadow(6.dp, RoundedCornerShape(20.dp), ambientColor = catColor.copy(alpha = 0.15f), spotColor = catColor.copy(alpha = 0.25f))
              .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(20.dp))
              .clickable(onClick = onClick)
              .testTag("vault_item_${item.id}"),
      colors =
          CardDefaults.cardColors(
              containerColor = MaterialTheme.colorScheme.surfaceVariant
          ),
      shape = RoundedCornerShape(20.dp),
  ) {"""
content = content.replace(old_card, new_card)

with open('app/src/main/java/com/example/ui/components/CommonComponents.kt', 'w') as f:
    f.write(content)

