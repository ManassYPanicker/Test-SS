package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.VaultItem
import com.example.ui.components.EmptyVaultState
import com.example.ui.components.VaultItemCard
import com.example.ui.theme.EmeraldPrimary

@Composable
fun FavoritesScreen(
    favoriteItems: List<VaultItem>,
    onItemClick: (VaultItem) -> Unit,
    onFavoriteToggle: (String) -> Unit,
    onBack: (() -> Unit)? = null,
) {
  Scaffold(
      topBar = {
        Column(
            modifier =
                Modifier.fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = if (onBack != null) 8.dp else 20.dp, vertical = 10.dp),
        ) {
          Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically,
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              if (onBack != null) {
                IconButton(onClick = onBack, modifier = Modifier.testTag("favorites_back_btn")) {
                  Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
              }
              Text(
                  text = "Favorites",
                  style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                  color = MaterialTheme.colorScheme.onBackground,
              )
            }
            Text(
                text = "${favoriteItems.size} items",
                style = MaterialTheme.typography.labelMedium,
                color = EmeraldPrimary,
                modifier = Modifier.padding(end = if (onBack != null) 12.dp else 0.dp),
            )
          }
          Text(
              text = "Quick access to your most important encrypted credentials",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.padding(start = if (onBack != null) 48.dp else 0.dp),
          )
        }
      },
      containerColor = MaterialTheme.colorScheme.background,
  ) { padding ->
    if (favoriteItems.isEmpty()) {
      EmptyVaultState(
          title = "No Favorites Yet",
          subtitle = "Tap the star icon on any login, card, note, or ID to keep it pinned here.",
          icon = Icons.Outlined.StarBorder,
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
        items(favoriteItems) { item ->
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

@Composable
fun RecentlyUsedScreen(
    recentItems: List<VaultItem>,
    onItemClick: (VaultItem) -> Unit,
    onFavoriteToggle: (String) -> Unit,
    onBack: () -> Unit,
) {
  Scaffold(
      topBar = {
        Row(
            modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(horizontal = 8.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
          IconButton(onClick = onBack, modifier = Modifier.testTag("recently_used_back_btn")) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
          }
          Spacer(modifier = Modifier.width(4.dp))
          Column {
            Text(
                text = "Recently Accessed",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = "Decrypted vault entries accessed this session",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
          }
        }
      },
      containerColor = MaterialTheme.colorScheme.background,
  ) { padding ->
    if (recentItems.isEmpty()) {
      EmptyVaultState(
          title = "No Activity History",
          subtitle = "Items decrypted or viewed during this session will be listed here.",
          icon = Icons.Default.History,
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
        items(recentItems) { item ->
          VaultItemCard(
              item = item,
              onClick = { onItemClick(item) },
              onFavoriteToggle = { onFavoriteToggle(item.id) },
          )
        }
        item {
          Spacer(modifier = Modifier.height(32.dp))
        }
      }
    }
  }
}
