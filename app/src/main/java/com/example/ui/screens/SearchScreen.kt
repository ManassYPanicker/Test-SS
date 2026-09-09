package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.VaultCategory
import com.example.model.VaultItem
import com.example.ui.components.EmptyVaultState
import com.example.ui.components.VaultItemCard
import com.example.ui.theme.EmeraldPrimary

@Composable
fun SearchScreen(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedCategoryFilter: VaultCategory,
    onCategoryFilterChange: (VaultCategory) -> Unit,
    recentSearches: List<String>,
    onRecentSearchClick: (String) -> Unit,
    onClearRecentSearches: () -> Unit,
    searchResults: List<VaultItem>,
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
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp),
          ) {
            if (onBack != null) {
              IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
              }
            }
            Text(
                text = "Search Vault",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(start = if (onBack != null) 0.dp else 0.dp),
            )
          }

          OutlinedTextField(
              value = searchQuery,
              onValueChange = onSearchQueryChange,
              modifier =
                  Modifier.fillMaxWidth()
                      .padding(horizontal = if (onBack != null) 8.dp else 0.dp)
                      .testTag("vault_search_input"),
              placeholder = { Text("Search logins, notes, cards, IDs...") },
              leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
              },
              trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                  IconButton(
                      onClick = { onSearchQueryChange("") },
                      modifier = Modifier.testTag("search_clear_btn"),
                  ) {
                    Icon(Icons.Default.Clear, contentDescription = "Clear search")
                  }
                }
              },
              singleLine = true,
              shape = RoundedCornerShape(14.dp),
              colors =
                  OutlinedTextFieldDefaults.colors(
                      focusedBorderColor = EmeraldPrimary,
                      unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                      focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                      unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                  ),
          )

          Spacer(modifier = Modifier.height(10.dp))

          // Filter Chips Row
          LazyRow(
              horizontalArrangement = Arrangement.spacedBy(8.dp),
              modifier = Modifier.fillMaxWidth(),
          ) {
            items(VaultCategory.entries) { category ->
              val isSelected = category == selectedCategoryFilter
              FilterChip(
                  selected = isSelected,
                  onClick = { onCategoryFilterChange(category) },
                  label = { Text(category.title) },
                  modifier = Modifier.testTag("search_filter_chip_${category.name.lowercase()}"),
                  colors =
                      FilterChipDefaults.filterChipColors(
                          selectedContainerColor = EmeraldPrimary.copy(alpha = 0.2f),
                          selectedLabelColor = EmeraldPrimary,
                      ),
                  border =
                      FilterChipDefaults.filterChipBorder(
                          enabled = true,
                          selected = isSelected,
                          borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                          selectedBorderColor = EmeraldPrimary,
                      ),
              )
            }
          }
        }
      },
      containerColor = MaterialTheme.colorScheme.background,
  ) { padding ->
    LazyColumn(
        modifier =
            Modifier.fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
      if (searchQuery.isEmpty()) {
        // Show Recent Searches
        if (recentSearches.isNotEmpty()) {
          item {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
              Text(
                  text = "Recent Searches",
                  style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                  color = MaterialTheme.colorScheme.onBackground,
              )
              TextButton(onClick = onClearRecentSearches) {
                Text("Clear All", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
              }
            }
          }

          items(recentSearches) { query ->
            Row(
                modifier =
                    Modifier.fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { onRecentSearchClick(query) }
                        .padding(vertical = 10.dp, horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
              Icon(
                  imageVector = Icons.Default.History,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.onSurfaceVariant,
                  modifier = Modifier.size(18.dp),
              )
              Spacer(modifier = Modifier.width(12.dp))
              Text(
                  text = query,
                  style = MaterialTheme.typography.bodyMedium,
                  color = MaterialTheme.colorScheme.onSurface,
              )
            }
          }
        } else {
          item {
            EmptyVaultState(
                title = "Search Your Vault",
                subtitle = "Type a service name, username, card nickname, or keyword to find items quickly.",
                icon = Icons.Default.Search,
            )
          }
        }
      } else {
        // Show Results or No-Results Empty State
        if (searchResults.isEmpty()) {
          item {
            EmptyVaultState(
                title = "No Matches Found",
                subtitle = "We couldn't find any entries matching '$searchQuery' in ${selectedCategoryFilter.title}.",
                icon = Icons.Default.SearchOff,
            )
          }
        } else {
          item {
            Text(
                text = "${searchResults.size} matches found",
                style = MaterialTheme.typography.labelMedium,
                color = EmeraldPrimary,
                modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
            )
          }

          items(searchResults) { item ->
            VaultItemCard(
                item = item,
                onClick = { onItemClick(item) },
                onFavoriteToggle = { onFavoriteToggle(item.id) },
            )
          }
        }
      }

      item {
        Spacer(modifier = Modifier.height(72.dp))
      }
    }
  }
}
