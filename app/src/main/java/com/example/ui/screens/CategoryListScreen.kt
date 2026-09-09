package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.VaultCategory
import com.example.model.VaultItem
import com.example.ui.components.EmptyVaultState
import com.example.ui.components.VaultItemCard
import com.example.ui.components.getCategoryColor
import com.example.ui.components.getCategoryIcon
import com.example.ui.theme.EmeraldPrimary

@Composable
fun CategoryListScreen(
    category: VaultCategory,
    items: List<VaultItem>,
    onItemClick: (VaultItem) -> Unit,
    onFavoriteToggle: (String) -> Unit,
    onAddItem: () -> Unit,
    onBack: () -> Unit,
) {
  val categoryItems = items.filter { it.category == category }
  val catColor = getCategoryColor(category)
  val catIcon = getCategoryIcon(category)

  val categoryItemNoun =
      when (category) {
        VaultCategory.LOGIN -> "Login"
        VaultCategory.NOTE -> "Secure Note"
        VaultCategory.ID_CARD -> "ID Card"
        VaultCategory.PAYMENT_CARD -> "Payment Card"
        VaultCategory.DOCUMENT -> "Document"
        VaultCategory.ALL -> "Item"
      }

  Scaffold(
      topBar = {
        Row(
            modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(horizontal = 8.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
          IconButton(onClick = onBack, modifier = Modifier.testTag("category_list_back_btn")) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
          }
          Box(
              modifier =
                  Modifier.size(36.dp)
                      .clip(RoundedCornerShape(10.dp))
                      .background(catColor.copy(alpha = 0.15f)),
              contentAlignment = Alignment.Center,
          ) {
            Icon(catIcon, contentDescription = null, tint = catColor, modifier = Modifier.size(20.dp))
          }
          Spacer(modifier = Modifier.width(12.dp))
          Column(modifier = Modifier.weight(1f)) {
            Text(
                text = category.title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            )
            Text(
                text = "${categoryItems.size} items protected",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
          }
        }
      },
      floatingActionButton = {
        FloatingActionButton(
            onClick = onAddItem,
            containerColor = EmeraldPrimary,
            contentColor = Color(0xFF003324),
            modifier = Modifier.testTag("category_add_fab"),
        ) {
          Icon(Icons.Default.Add, contentDescription = "Add to ${category.title}")
        }
      },
      containerColor = MaterialTheme.colorScheme.background,
  ) { padding ->
    if (categoryItems.isEmpty()) {
      EmptyVaultState(
          title = "No ${category.title} Stored",
          subtitle = "Tap the button below to store an encrypted $categoryItemNoun entry.",
          icon = catIcon,
          actionLabel = "Add New $categoryItemNoun",
          onAction = onAddItem,
          modifier = Modifier.padding(padding),
      )
    } else {
      LazyColumn(
          modifier =
              Modifier.fillMaxSize()
                  .padding(padding)
                  .padding(horizontal = 20.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp),
      ) {
        items(categoryItems) { item ->
          VaultItemCard(
              item = item,
              onClick = { onItemClick(item) },
              onFavoriteToggle = { onFavoriteToggle(item.id) },
          )
        }
        item {
          Spacer(modifier = Modifier.height(72.dp))
        }
      }
    }
  }
}
