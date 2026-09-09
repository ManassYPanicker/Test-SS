package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.SecurityScore
import com.example.model.VaultCategory
import com.example.model.VaultItem
import com.example.ui.components.EmptyVaultState
import com.example.ui.components.SecurityBadge
import com.example.ui.components.SeedSafeLogo
import com.example.ui.components.VaultItemCard
import com.example.ui.components.getCategoryColor
import com.example.ui.components.getCategoryIcon
import com.example.ui.theme.AppThemeSetting
import com.example.ui.theme.EmeraldPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainVaultScreen(
    items: List<VaultItem>,
    favoriteItems: List<VaultItem>,
    recentlyAccessedItems: List<VaultItem>,
    securityScore: SecurityScore,
    onItemClick: (VaultItem) -> Unit,
    onFavoriteToggle: (String) -> Unit,
    onCategoryClick: (VaultCategory) -> Unit,
    onSearchClick: () -> Unit,
    onSecurityCenterClick: () -> Unit,
    onSeeAllFavorites: () -> Unit,
    onSeeAllRecents: () -> Unit,
    onQuickAdd: (VaultCategory) -> Unit,
    onLockVault: () -> Unit,
    themeSetting: AppThemeSetting = AppThemeSetting.SYSTEM,
    onToggleTheme: () -> Unit = {},
) {
  var showQuickAddSheet by remember { mutableStateOf(false) }
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  if (showQuickAddSheet) {
    ModalBottomSheet(
        onDismissRequest = { showQuickAddSheet = false },
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
    ) {
      Column(
          modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp).padding(bottom = 36.dp),
      ) {
        Text(
            text = "Create Encrypted Entry",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Select credential type to securely isolate in your vault",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(20.dp))

        listOf(
            VaultCategory.LOGIN to "Web & app credentials with auto-fill credentials",
            VaultCategory.NOTE to "Encrypted secret notes, keys, or recovery data",
            VaultCategory.ID_CARD to "Passports, national identity & driver licenses",
            VaultCategory.PAYMENT_CARD to "Debit, credit & hardware cards with CVV lock",
            VaultCategory.DOCUMENT to "Identity PDFs, encrypted backups & documents",
        ).forEach { (cat, desc) ->
          Card(
              modifier =
                  Modifier.fillMaxWidth()
                      .padding(vertical = 5.dp)
                      .clip(RoundedCornerShape(14.dp))
                      .clickable {
                        showQuickAddSheet = false
                        onQuickAdd(cat)
                      }
                      .testTag("quick_add_option_${cat.name}"),
              colors =
                  CardDefaults.cardColors(
                      containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                  ),
              shape = RoundedCornerShape(14.dp),
          ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
              val catColor = getCategoryColor(cat)
              Box(
                  modifier =
                      Modifier.size(42.dp)
                          .clip(RoundedCornerShape(10.dp))
                          .background(catColor.copy(alpha = 0.15f)),
                  contentAlignment = Alignment.Center,
              ) {
                Icon(
                    imageVector = getCategoryIcon(cat),
                    contentDescription = null,
                    tint = catColor,
                    modifier = Modifier.size(22.dp),
                )
              }
              Spacer(modifier = Modifier.width(14.dp))
              Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = cat.title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = desc,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
              }
              Icon(
                  imageVector = Icons.Default.ChevronRight,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                  modifier = Modifier.size(18.dp),
              )
            }
          }
        }
      }
    }
  }

  Scaffold(
      topBar = {
        Row(
            modifier =
                Modifier.fillMaxWidth()
                    .statusBarsPadding()
                    .padding(start = 20.dp, end = 6.dp, top = 10.dp, bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
          SeedSafeLogo(size = 38, showText = true)

          Row(verticalAlignment = Alignment.CenterVertically) {
            SecurityBadge(text = "Offline", icon = Icons.Default.Shield)
            Spacer(modifier = Modifier.width(4.dp))
            IconButton(
                onClick = onToggleTheme,
                modifier = Modifier.testTag("top_bar_theme_toggle_btn"),
            ) {
              Icon(
                  imageVector =
                      when (themeSetting) {
                        AppThemeSetting.LIGHT -> Icons.Default.DarkMode
                        AppThemeSetting.DARK -> Icons.Default.LightMode
                        AppThemeSetting.SYSTEM -> Icons.Default.BrightnessAuto
                      },
                  contentDescription = "Toggle Light/Dark Theme",
                  tint = EmeraldPrimary,
              )
            }
            IconButton(
                onClick = onLockVault,
                modifier = Modifier.testTag("top_bar_lock_vault_btn"),
            ) {
              Icon(
                  imageVector = Icons.Default.Lock,
                  contentDescription = "Lock Vault",
                  tint = EmeraldPrimary,
              )
            }
          }
        }
      },
      floatingActionButton = {
        FloatingActionButton(
            onClick = { showQuickAddSheet = true },
            containerColor = EmeraldPrimary,
            contentColor = Color(0xFF003324),
            modifier = Modifier.testTag("main_quick_add_fab"),
        ) {
          Icon(Icons.Default.Add, contentDescription = "Add new vault entry")
        }
      },
      containerColor = MaterialTheme.colorScheme.background,
  ) { padding ->
    LazyColumn(
        modifier =
            Modifier.fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
      // Search Bar CTA
      item {
        Card(
            modifier =
                Modifier.fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable(onClick = onSearchClick)
                    .testTag("home_search_bar_cta"),
            colors =
                CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
                ),
            shape = RoundedCornerShape(16.dp),
        ) {
          Row(
              modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
              verticalAlignment = Alignment.CenterVertically,
          ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Search passwords, notes, IDs, cards...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
          }
        }
      }

      // Security Overview Widget
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
        ) {
          Row(
              modifier = Modifier.padding(16.dp).fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween,
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                  modifier =
                      Modifier.size(46.dp)
                          .clip(CircleShape)
                          .background(EmeraldPrimary.copy(alpha = 0.2f)),
                  contentAlignment = Alignment.Center,
              ) {
                Text(
                    text = "${securityScore.score}",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = EmeraldPrimary,
                )
              }
              Spacer(modifier = Modifier.width(14.dp))
              Column {
                Text(
                    text = "Vault Health: Strong",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = "${items.size} items protected • 0 compromised",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
              }
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "View Security Center",
                tint = EmeraldPrimary,
            )
          }
        }
      }

      // Vault Categories Section
      item {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
          Text(
              text = "Categories",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onBackground,
          )
          Text(
              text = "${items.size} total items",
              style = MaterialTheme.typography.labelMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }
      }

      item {
        val categories =
            listOf(
                VaultCategory.LOGIN,
                VaultCategory.NOTE,
                VaultCategory.ID_CARD,
                VaultCategory.PAYMENT_CARD,
                VaultCategory.DOCUMENT,
            )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
          items(categories) { category ->
            val count = items.count { it.category == category }
            val catColor = getCategoryColor(category)
            val catIcon = getCategoryIcon(category)

            Card(
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
            ) {
              Column(modifier = Modifier.padding(14.dp)) {
                Box(
                    modifier =
                        Modifier.size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(catColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center,
                ) {
                  Icon(catIcon, contentDescription = null, tint = catColor, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = category.title,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = "$count items",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
              }
            }
          }
        }
      }

      // Favorites Section (if any)
      if (favoriteItems.isNotEmpty()) {
        item {
          Row(
              modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically,
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                  text = "Favorites",
                  style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                  color = MaterialTheme.colorScheme.onBackground,
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                  text = "(${favoriteItems.size})",
                  style = MaterialTheme.typography.labelMedium,
                  color = EmeraldPrimary,
              )
            }
            TextButton(
                onClick = onSeeAllFavorites,
                modifier = Modifier.testTag("see_all_favorites_btn"),
            ) {
              Text("See All", color = EmeraldPrimary, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
            }
          }
        }

        items(favoriteItems.take(4)) { favItem ->
          VaultItemCard(
              item = favItem,
              onClick = { onItemClick(favItem) },
              onFavoriteToggle = { onFavoriteToggle(favItem.id) },
          )
        }
      }

      // Recently Accessed / Updated
      item {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
          Text(
              text = "Recently Accessed",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onBackground,
          )
          if (recentlyAccessedItems.isNotEmpty()) {
            TextButton(
                onClick = onSeeAllRecents,
                modifier = Modifier.testTag("see_all_recents_btn"),
            ) {
              Text("See All", color = EmeraldPrimary, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
            }
          }
        }
      }

      if (recentlyAccessedItems.isEmpty()) {
        item {
          EmptyVaultState(
              title = "No Recent Entries",
              subtitle = "Items you view or edit will appear here for quick access.",
              icon = Icons.Outlined.Lock,
          )
        }
      } else {
        items(recentlyAccessedItems.take(5)) { recentItem ->
          VaultItemCard(
              item = recentItem,
              onClick = { onItemClick(recentItem) },
              onFavoriteToggle = { onFavoriteToggle(recentItem.id) },
          )
        }
      }

      item {
        Spacer(modifier = Modifier.height(72.dp))
      }
    }
  }
}
