package com.example

import android.app.Activity
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.VaultCategory
import com.example.state.ScreenDestination
import com.example.state.VaultViewModel
import com.example.ui.screens.AddEditItemScreen
import com.example.ui.screens.BackupScreen
import com.example.ui.screens.CategoryListScreen
import com.example.ui.screens.CreateVaultScreen
import com.example.ui.screens.FavoritesScreen
import com.example.ui.screens.ItemDetailScreen
import com.example.ui.screens.MainVaultScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.PrivacyTrustScreen
import com.example.ui.screens.RecentlyUsedScreen
import com.example.ui.screens.RecoveryPhraseConfirmationScreen
import com.example.ui.screens.RestoreScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.screens.SecurityCenterScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.UnlockScreen
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.SeedSafeTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      SeedSafeApp()
    }
  }
}

@Composable
fun SeedSafeApp(viewModel: VaultViewModel = viewModel()) {
  val currentDestination by viewModel.currentDestination.collectAsState()
  val themeSetting by viewModel.themeSetting.collectAsState()
  val items by viewModel.items.collectAsState()
  val favoriteItems by viewModel.favoriteItems.collectAsState()
  val recentlyAccessedItems by viewModel.recentlyAccessedItems.collectAsState()
  val securityScore by viewModel.securityScore.collectAsState()
  val isVaultLocked by viewModel.isVaultLocked.collectAsState()
  val screenshotProtection by viewModel.screenshotProtection.collectAsState()
  val context = LocalContext.current

  // Dynamic FLAG_SECURE enforcement according to security policy & user setting
  LaunchedEffect(screenshotProtection, currentDestination) {
    val activity = context as? Activity ?: return@LaunchedEffect
    val isSensitiveScreen = currentDestination is ScreenDestination.CreateVault ||
        currentDestination is ScreenDestination.RecoveryPhraseQuiz ||
        currentDestination is ScreenDestination.Unlock ||
        currentDestination is ScreenDestination.ItemDetail ||
        currentDestination is ScreenDestination.AddEditItem

    if (screenshotProtection || isSensitiveScreen) {
      activity.window.setFlags(
          WindowManager.LayoutParams.FLAG_SECURE,
          WindowManager.LayoutParams.FLAG_SECURE,
      )
    } else {
      activity.window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
    }
  }

  // Back button handling
  BackHandler(enabled = currentDestination !is ScreenDestination.Home && currentDestination !is ScreenDestination.Splash) {
    viewModel.popBackStack()
  }

  SeedSafeTheme(themeSetting = themeSetting) {
    val showBottomBar =
        currentDestination is ScreenDestination.Home ||
            currentDestination is ScreenDestination.Search ||
            currentDestination is ScreenDestination.Favorites ||
            currentDestination is ScreenDestination.Settings

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
          if (showBottomBar && !isVaultLocked) {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurface,
            ) {
              NavigationBarItem(
                  selected = currentDestination is ScreenDestination.Home,
                  onClick = { viewModel.navigateTo(ScreenDestination.Home) },
                  icon = {
                    Icon(
                        imageVector = if (currentDestination is ScreenDestination.Home) Icons.Filled.Home else Icons.Outlined.Home,
                        contentDescription = "Vault",
                    )
                  },
                  label = { Text("Vault") },
                  colors = NavigationBarItemDefaults.colors(
                      selectedIconColor = EmeraldPrimary,
                      selectedTextColor = EmeraldPrimary,
                      indicatorColor = EmeraldPrimary.copy(alpha = 0.15f),
                  ),
                  modifier = Modifier.testTag("nav_tab_vault"),
              )

              NavigationBarItem(
                  selected = currentDestination is ScreenDestination.Search,
                  onClick = { viewModel.navigateTo(ScreenDestination.Search) },
                  icon = {
                    Icon(
                        imageVector = if (currentDestination is ScreenDestination.Search) Icons.Filled.Search else Icons.Outlined.Search,
                        contentDescription = "Search",
                    )
                  },
                  label = { Text("Search") },
                  colors = NavigationBarItemDefaults.colors(
                      selectedIconColor = EmeraldPrimary,
                      selectedTextColor = EmeraldPrimary,
                      indicatorColor = EmeraldPrimary.copy(alpha = 0.15f),
                  ),
                  modifier = Modifier.testTag("nav_tab_search"),
              )

              NavigationBarItem(
                  selected = currentDestination is ScreenDestination.Favorites,
                  onClick = { viewModel.navigateTo(ScreenDestination.Favorites) },
                  icon = {
                    Icon(
                        imageVector = if (currentDestination is ScreenDestination.Favorites) Icons.Filled.Star else Icons.Outlined.StarOutline,
                        contentDescription = "Favorites",
                    )
                  },
                  label = { Text("Favorites") },
                  colors = NavigationBarItemDefaults.colors(
                      selectedIconColor = EmeraldPrimary,
                      selectedTextColor = EmeraldPrimary,
                      indicatorColor = EmeraldPrimary.copy(alpha = 0.15f),
                  ),
                  modifier = Modifier.testTag("nav_tab_favorites"),
              )

              NavigationBarItem(
                  selected = currentDestination is ScreenDestination.Settings,
                  onClick = { viewModel.navigateTo(ScreenDestination.Settings) },
                  icon = {
                    Icon(
                        imageVector = if (currentDestination is ScreenDestination.Settings) Icons.Filled.Settings else Icons.Outlined.Settings,
                        contentDescription = "Settings",
                    )
                  },
                  label = { Text("Settings") },
                  colors = NavigationBarItemDefaults.colors(
                      selectedIconColor = EmeraldPrimary,
                      selectedTextColor = EmeraldPrimary,
                      indicatorColor = EmeraldPrimary.copy(alpha = 0.15f),
                  ),
                  modifier = Modifier.testTag("nav_tab_settings"),
              )
            }
          }
        },
    ) { innerPadding ->
      Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
        when (val dest = currentDestination) {
          is ScreenDestination.Splash -> {
            SplashScreen(
                onSplashFinished = { viewModel.onSplashCompleted() },
            )
          }

          is ScreenDestination.Onboarding -> {
            OnboardingScreen(
                onCreateVaultClick = { viewModel.navigateTo(ScreenDestination.CreateVault) },
                onRestoreVaultClick = { viewModel.navigateTo(ScreenDestination.RestoreVault) },
            )
          }

          is ScreenDestination.CreateVault -> {
            CreateVaultScreen(
                onContinueToQuiz = { viewModel.navigateTo(ScreenDestination.RecoveryPhraseQuiz) },
                onBack = { viewModel.popBackStack() },
            )
          }

          is ScreenDestination.RecoveryPhraseQuiz -> {
            RecoveryPhraseConfirmationScreen(
                onQuizPassed = { viewModel.completeOnboarding() },
                onBack = { viewModel.popBackStack() },
            )
          }

          is ScreenDestination.Unlock -> {
            val attempts by viewModel.pinAttemptsRemaining.collectAsState()
            val isLockout by viewModel.isLockoutActive.collectAsState()
            UnlockScreen(
                onUnlockSuccess = { viewModel.unlockWithBiometrics() },
                onRestoreWithRecoveryPhrase = { viewModel.navigateTo(ScreenDestination.RestoreVault) },
                pinAttemptsRemaining = attempts,
                isLockoutActive = isLockout,
                onAttemptPin = { pin -> viewModel.attemptUnlockWithPin(pin) },
                onResetLockout = { viewModel.resetLockout() },
            )
          }

          is ScreenDestination.Home -> {
            MainVaultScreen(
                items = items,
                favoriteItems = favoriteItems,
                recentlyAccessedItems = recentlyAccessedItems,
                securityScore = securityScore,
                onItemClick = { item ->
                  viewModel.recordItemAccess(item.id)
                  viewModel.navigateTo(ScreenDestination.ItemDetail(item.id))
                },
                onFavoriteToggle = { itemId -> viewModel.toggleFavorite(itemId) },
                onCategoryClick = { category ->
                  viewModel.navigateTo(ScreenDestination.CategoryList(category))
                },
                onSearchClick = { viewModel.navigateTo(ScreenDestination.Search) },
                onSecurityCenterClick = { viewModel.navigateTo(ScreenDestination.SecurityCenter) },
                onSeeAllFavorites = { viewModel.navigateTo(ScreenDestination.Favorites) },
                onSeeAllRecents = { viewModel.navigateTo(ScreenDestination.RecentlyUsed) },
                onQuickAdd = { category ->
                  viewModel.navigateTo(ScreenDestination.AddEditItem(category = category))
                },
                onLockVault = { viewModel.lockVault() },
                themeSetting = themeSetting,
                onToggleTheme = { viewModel.toggleTheme() },
            )
          }

          is ScreenDestination.Search -> {
            val query by viewModel.searchQuery.collectAsState()
            val categoryFilter by viewModel.selectedCategoryFilter.collectAsState()
            val recentSearches by viewModel.recentSearches.collectAsState()
            val searchResults by viewModel.filteredItems.collectAsState()

            SearchScreen(
                searchQuery = query,
                onSearchQueryChange = { viewModel.updateSearchQuery(it) },
                selectedCategoryFilter = categoryFilter,
                onCategoryFilterChange = { viewModel.setCategoryFilter(it) },
                recentSearches = recentSearches,
                onRecentSearchClick = {
                  viewModel.updateSearchQuery(it)
                },
                onClearRecentSearches = { viewModel.clearRecentSearches() },
                searchResults = searchResults,
                onItemClick = { item ->
                  viewModel.recordItemAccess(item.id)
                  viewModel.navigateTo(ScreenDestination.ItemDetail(item.id))
                },
                onFavoriteToggle = { itemId -> viewModel.toggleFavorite(itemId) },
                onBack = { viewModel.navigateBack() },
            )
          }

          is ScreenDestination.Favorites -> {
            FavoritesScreen(
                favoriteItems = favoriteItems,
                onItemClick = { item ->
                  viewModel.recordItemAccess(item.id)
                  viewModel.navigateTo(ScreenDestination.ItemDetail(item.id))
                },
                onFavoriteToggle = { itemId -> viewModel.toggleFavorite(itemId) },
                onBack = { viewModel.navigateBack() },
            )
          }

          is ScreenDestination.CategoryList -> {
            CategoryListScreen(
                category = dest.category,
                items = items,
                onItemClick = { item ->
                  viewModel.recordItemAccess(item.id)
                  viewModel.navigateTo(ScreenDestination.ItemDetail(item.id))
                },
                onFavoriteToggle = { itemId -> viewModel.toggleFavorite(itemId) },
                onAddItem = {
                  viewModel.navigateTo(ScreenDestination.AddEditItem(category = dest.category))
                },
                onBack = { viewModel.popBackStack() },
            )
          }

          is ScreenDestination.ItemDetail -> {
            val item = viewModel.getItemById(dest.itemId)
            if (item != null) {
              ItemDetailScreen(
                  item = item,
                  onEditClick = {
                    viewModel.navigateTo(ScreenDestination.AddEditItem(category = item.category, itemId = item.id))
                  },
                  onDeleteConfirm = {
                    viewModel.deleteItem(item.id)
                    viewModel.popBackStack()
                  },
                  onFavoriteToggle = { viewModel.toggleFavorite(item.id) },
                  onBack = { viewModel.popBackStack() },
              )
            } else {
              viewModel.popBackStack()
            }
          }

          is ScreenDestination.AddEditItem -> {
            val existingItem = dest.itemId?.let { viewModel.getItemById(it) }
            AddEditItemScreen(
                category = dest.category,
                existingItem = existingItem,
                onSaveItem = { item ->
                  if (dest.itemId != null) {
                    viewModel.updateItem(item)
                  } else {
                    viewModel.addItem(item)
                  }
                },
                onBack = { viewModel.popBackStack() },
            )
          }

          is ScreenDestination.SecurityCenter -> {
            val weakItems = viewModel.getWeakPasswordItems()
            val reusedItems = viewModel.getReusedPasswordItems()
            SecurityCenterScreen(
                securityScore = securityScore,
                weakItems = weakItems,
                reusedItems = reusedItems,
                onFixItem = { item ->
                  viewModel.navigateTo(ScreenDestination.AddEditItem(category = item.category, itemId = item.id))
                },
                onBack = { viewModel.popBackStack() },
            )
          }

          is ScreenDestination.BackupManagement -> {
            val backupStatus by viewModel.backupStatus.collectAsState()
            val lastBackupTime by viewModel.lastBackupTime.collectAsState()
            val googleAccount by viewModel.googleAccount.collectAsState()
            val isBackupConfigured by viewModel.isBackupConfigured.collectAsState()

            BackupScreen(
                backupStatus = backupStatus,
                lastBackupTime = lastBackupTime,
                googleAccount = googleAccount,
                isBackupConfigured = isBackupConfigured,
                onToggleBackupConfigured = { viewModel.toggleBackupConfigured(it) },
                onTriggerBackup = { simulateFailure -> viewModel.triggerManualBackup(simulateFailure) },
                onBack = { viewModel.popBackStack() },
            )
          }

          is ScreenDestination.RestoreVault -> {
            RestoreScreen(
                onRestoreSuccess = {
                  viewModel.unlockWithBiometrics()
                  viewModel.navigateTo(ScreenDestination.Home)
                },
                onBack = { viewModel.popBackStack() },
            )
          }

          is ScreenDestination.PrivacyTrust -> {
            PrivacyTrustScreen(
                onBack = { viewModel.popBackStack() },
            )
          }

          is ScreenDestination.RecentlyUsed -> {
            RecentlyUsedScreen(
                recentItems = recentlyAccessedItems,
                onItemClick = { item ->
                  viewModel.recordItemAccess(item.id)
                  viewModel.navigateTo(ScreenDestination.ItemDetail(item.id))
                },
                onFavoriteToggle = { itemId -> viewModel.toggleFavorite(itemId) },
                onBack = { viewModel.popBackStack() },
            )
          }

          is ScreenDestination.Settings -> {
            val biometricsEnabled by viewModel.biometricsEnabled.collectAsState()
            val autoLockDuration by viewModel.autoLockDuration.collectAsState()
            val screenshotProtection by viewModel.screenshotProtection.collectAsState()
            val clipboardTimeout by viewModel.clipboardTimeout.collectAsState()
            val wipeOnFailedAttempts by viewModel.wipeOnFailedAttempts.collectAsState()
            val backupStatus by viewModel.backupStatus.collectAsState()
            val lastBackupTime by viewModel.lastBackupTime.collectAsState()
            val unlockPin by viewModel.unlockPin.collectAsState()

            SettingsScreen(
                themeSetting = themeSetting,
                onThemeSettingChange = { viewModel.setThemeSetting(it) },
                biometricsEnabled = biometricsEnabled,
                onBiometricsEnabledChange = { viewModel.setBiometricsEnabled(it) },
                autoLockDuration = autoLockDuration,
                onAutoLockDurationChange = { viewModel.setAutoLockDuration(it) },
                screenshotProtection = screenshotProtection,
                onScreenshotProtectionChange = { viewModel.setScreenshotProtection(it) },
                clipboardTimeout = clipboardTimeout,
                onClipboardTimeoutChange = { viewModel.setClipboardTimeout(it) },
                wipeOnFailedAttempts = wipeOnFailedAttempts,
                onWipeOnFailedAttemptsChange = { viewModel.setWipeOnFailedAttempts(it) },
                backupStatus = backupStatus,
                lastBackupTime = lastBackupTime,
                onNavigateToBackup = { viewModel.navigateTo(ScreenDestination.BackupManagement) },
                onNavigateToRestore = { viewModel.navigateTo(ScreenDestination.RestoreVault) },
                onNavigateToSecurityCenter = { viewModel.navigateTo(ScreenDestination.SecurityCenter) },
                onNavigateToPrivacyTrust = { viewModel.navigateTo(ScreenDestination.PrivacyTrust) },
                onNavigateToRecoveryPhrase = { viewModel.navigateTo(ScreenDestination.CreateVault) },
                onResetToOnboarding = { viewModel.resetToOnboarding() },
                currentPin = unlockPin,
                onChangePin = { viewModel.updatePin(it) },
            )
          }
        }
      }
    }
  }
}
