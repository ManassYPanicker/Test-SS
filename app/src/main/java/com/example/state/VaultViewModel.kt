package com.example.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.MockVaultData
import com.example.model.BackupStatus
import com.example.model.CardItem
import com.example.model.DocumentItem
import com.example.model.IdItem
import com.example.model.LoginItem
import com.example.model.NoteItem
import com.example.model.SecurityScore
import com.example.model.VaultCategory
import com.example.model.VaultItem
import com.example.ui.theme.AppThemeSetting
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface ScreenDestination {
  object Splash : ScreenDestination
  object Onboarding : ScreenDestination
  object CreateVault : ScreenDestination
  object RecoveryPhraseQuiz : ScreenDestination
  object Unlock : ScreenDestination
  object Home : ScreenDestination
  object Search : ScreenDestination
  object Favorites : ScreenDestination
  object Settings : ScreenDestination
  data class CategoryList(val category: VaultCategory) : ScreenDestination
  data class ItemDetail(val itemId: String) : ScreenDestination
  data class AddEditItem(val category: VaultCategory, val itemId: String? = null) : ScreenDestination
  object SecurityCenter : ScreenDestination
  object BackupManagement : ScreenDestination
  object RestoreVault : ScreenDestination
  object PrivacyTrust : ScreenDestination
  object RecentlyUsed : ScreenDestination
}

class VaultViewModel : ViewModel() {

  // Current active destination and navigation stack
  private val _currentDestination = MutableStateFlow<ScreenDestination>(ScreenDestination.Splash)
  val currentDestination: StateFlow<ScreenDestination> = _currentDestination.asStateFlow()

  private val navigationStack = mutableListOf<ScreenDestination>(ScreenDestination.Splash)

  private val _hasCompletedOnboarding = MutableStateFlow(true)
  val hasCompletedOnboarding: StateFlow<Boolean> = _hasCompletedOnboarding.asStateFlow()

  fun onSplashCompleted() {
    if (_hasCompletedOnboarding.value) {
      _currentDestination.value = ScreenDestination.Home
      navigationStack.clear()
      navigationStack.add(ScreenDestination.Home)
    } else {
      _currentDestination.value = ScreenDestination.Onboarding
      navigationStack.clear()
      navigationStack.add(ScreenDestination.Onboarding)
    }
  }

  fun resetToOnboarding() {
    _hasCompletedOnboarding.value = false
    _currentDestination.value = ScreenDestination.Onboarding
    navigationStack.clear()
    navigationStack.add(ScreenDestination.Onboarding)
  }

  fun completeOnboarding() {
    _hasCompletedOnboarding.value = true
    _currentDestination.value = ScreenDestination.Home
    navigationStack.clear()
    navigationStack.add(ScreenDestination.Home)
  }

  // In-memory vault items
  private val _items = MutableStateFlow<List<VaultItem>>(MockVaultData.getInitialItems())
  val items: StateFlow<List<VaultItem>> = _items.asStateFlow()

  // Recently accessed item IDs (in order of access)
  private val _recentlyAccessedIds = MutableStateFlow<List<String>>(
      listOf("login-1", "note-1", "card-1", "login-2", "doc-1")
  )
  val recentlyAccessedIds: StateFlow<List<String>> = _recentlyAccessedIds.asStateFlow()

  val recentlyAccessedItems: StateFlow<List<VaultItem>> =
      combine(_items, _recentlyAccessedIds) { allItems, recentIds ->
        recentIds.mapNotNull { id -> allItems.find { it.id == id } }
      }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

  val favoriteItems: StateFlow<List<VaultItem>> =
      _items.map { list -> list.filter { it.isFavorite } }
          .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

  // Lock State
  private val _isVaultLocked = MutableStateFlow(false)
  val isVaultLocked: StateFlow<Boolean> = _isVaultLocked.asStateFlow()

  private val _unlockPin = MutableStateFlow("1234")
  val unlockPin: StateFlow<String> = _unlockPin.asStateFlow()

  private val _pinAttemptsRemaining = MutableStateFlow(5)
  val pinAttemptsRemaining: StateFlow<Int> = _pinAttemptsRemaining.asStateFlow()

  private val _isLockoutActive = MutableStateFlow(false)
  val isLockoutActive: StateFlow<Boolean> = _isLockoutActive.asStateFlow()

  // Search State
  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  private val _selectedCategoryFilter = MutableStateFlow(VaultCategory.ALL)
  val selectedCategoryFilter: StateFlow<VaultCategory> = _selectedCategoryFilter.asStateFlow()

  private val _recentSearches = MutableStateFlow(listOf("Google", "Passport", "Wi-Fi", "Sapphire"))
  val recentSearches: StateFlow<List<String>> = _recentSearches.asStateFlow()

  // Filtered search results
  val searchResults: StateFlow<List<VaultItem>> =
      combine(_items, _searchQuery, _selectedCategoryFilter) { allItems, query, categoryFilter ->
        val trimmed = query.trim().lowercase()
        allItems.filter { item ->
          val matchesCategory = (categoryFilter == VaultCategory.ALL || item.category == categoryFilter)
          val matchesQuery = if (trimmed.isEmpty()) true else {
            item.title.lowercase().contains(trimmed) ||
                when (item) {
                  is LoginItem -> item.username.lowercase().contains(trimmed) || item.website.lowercase().contains(trimmed)
                  is NoteItem -> item.content.lowercase().contains(trimmed)
                  is IdItem -> item.idType.lowercase().contains(trimmed) || item.idNumber.lowercase().contains(trimmed)
                  is CardItem -> item.cardholderName.lowercase().contains(trimmed) || item.cardType.lowercase().contains(trimmed)
                  is DocumentItem -> item.fileName.lowercase().contains(trimmed) || item.fileType.lowercase().contains(trimmed)
                }
          }
          matchesCategory && matchesQuery
        }
      }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

  val filteredItems: StateFlow<List<VaultItem>> = searchResults

  // Settings State
  private val _themeSetting = MutableStateFlow(AppThemeSetting.SYSTEM)
  val themeSetting: StateFlow<AppThemeSetting> = _themeSetting.asStateFlow()

  private val _biometricsEnabled = MutableStateFlow(true)
  val biometricsEnabled: StateFlow<Boolean> = _biometricsEnabled.asStateFlow()

  private val _autoLockDuration = MutableStateFlow("5 minutes")
  val autoLockDuration: StateFlow<String> = _autoLockDuration.asStateFlow()

  private val _screenshotProtection = MutableStateFlow(true)
  val screenshotProtection: StateFlow<Boolean> = _screenshotProtection.asStateFlow()

  private val _clipboardTimeout = MutableStateFlow("30 seconds")
  val clipboardTimeout: StateFlow<String> = _clipboardTimeout.asStateFlow()

  private val _wipeOnFailedAttempts = MutableStateFlow(false)
  val wipeOnFailedAttempts: StateFlow<Boolean> = _wipeOnFailedAttempts.asStateFlow()

  // Backup State
  private val _backupStatus = MutableStateFlow(BackupStatus.SUCCESS)
  val backupStatus: StateFlow<BackupStatus> = _backupStatus.asStateFlow()

  private val _lastBackupTime = MutableStateFlow("Today at 08:30 AM")
  val lastBackupTime: StateFlow<String> = _lastBackupTime.asStateFlow()

  private val _googleDriveAccount = MutableStateFlow("alex.miller@gmail.com")
  val googleDriveAccount: StateFlow<String> = _googleDriveAccount.asStateFlow()
  val googleAccount: StateFlow<String> = googleDriveAccount

  private val _isBackupConfigured = MutableStateFlow(true)
  val isBackupConfigured: StateFlow<Boolean> = _isBackupConfigured.asStateFlow()

  // Security score
  val securityScore: StateFlow<SecurityScore> =
      _items.map { allItems ->
        val logins = allItems.filterIsInstance<LoginItem>()
        val weak = logins.count { it.password.length < 10 || it.password.contains("1234") }
        val passwords = logins.map { it.password }
        val reused = logins.count { item -> passwords.count { it == item.password } > 1 }
        val old = logins.count { it.updatedAt.contains("month") }
        val score = (100 - (weak * 12) - (reused * 8) - (old * 5)).coerceIn(40, 100)
        SecurityScore(
            score = score,
            weakCount = weak,
            reusedCount = reused,
            oldCount = old,
            compromisedCount = 0,
            lastAudit = "Audited just now"
        )
      }.stateIn(viewModelScope, SharingStarted.Eagerly, SecurityScore())

  // Navigation functions
  fun navigateTo(destination: ScreenDestination) {
    if (destination != _currentDestination.value) {
      navigationStack.add(destination)
      _currentDestination.value = destination
    }
  }

  fun navigateBack(): Boolean {
    if (navigationStack.size > 1) {
      navigationStack.removeAt(navigationStack.size - 1)
      val previous = navigationStack.last()
      _currentDestination.value = previous
      return true
    } else if (_currentDestination.value != ScreenDestination.Home) {
      _currentDestination.value = ScreenDestination.Home
      navigationStack.clear()
      navigationStack.add(ScreenDestination.Home)
      return true
    }
    return false
  }

  fun popBackStack(): Boolean = navigateBack()

  fun resetToHome() {
    navigationStack.clear()
    navigationStack.add(ScreenDestination.Home)
    _currentDestination.value = ScreenDestination.Home
  }

  fun getItemById(itemId: String): VaultItem? = _items.value.find { it.id == itemId }

  fun addItem(item: VaultItem) = saveOrUpdateItem(item)

  fun updateItem(item: VaultItem) = saveOrUpdateItem(item)

  fun getWeakPasswordItems(): List<VaultItem> =
      _items.value.filterIsInstance<LoginItem>().filter { it.password.length < 10 || it.password.contains("1234") }

  fun getReusedPasswordItems(): List<VaultItem> {
    val logins = _items.value.filterIsInstance<LoginItem>()
    val passwords = logins.map { it.password }
    return logins.filter { item -> passwords.count { it == item.password } > 1 }
  }

  // Vault Item operations
  fun toggleFavorite(itemId: String) {
    _items.value = _items.value.map { item ->
      if (item.id == itemId) {
        item.copyWithFavorite(!item.isFavorite)
      } else item
    }
  }

  fun deleteItem(itemId: String) {
    _items.value = _items.value.filter { it.id != itemId }
    _recentlyAccessedIds.value = _recentlyAccessedIds.value.filter { it != itemId }
  }

  fun saveOrUpdateItem(item: VaultItem) {
    val existingIndex = _items.value.indexOfFirst { it.id == item.id }
    if (existingIndex >= 0) {
      _items.value = _items.value.toMutableList().also { it[existingIndex] = item }
    } else {
      _items.value = listOf(item) + _items.value
    }
    recordItemAccess(item.id)
  }

  fun recordItemAccess(itemId: String) {
    val current = _recentlyAccessedIds.value.toMutableList()
    current.remove(itemId)
    current.add(0, itemId)
    _recentlyAccessedIds.value = current.take(10)
  }

  // Search actions
  fun updateSearchQuery(query: String) {
    _searchQuery.value = query
  }

  fun setCategoryFilter(category: VaultCategory) {
    _selectedCategoryFilter.value = category
  }

  fun addRecentSearch(query: String) {
    if (query.isNotBlank()) {
      val list = _recentSearches.value.toMutableList()
      list.remove(query)
      list.add(0, query)
      _recentSearches.value = list.take(8)
    }
  }

  fun clearRecentSearches() {
    _recentSearches.value = emptyList()
  }

  // Vault Lock/Unlock
  fun lockVault() {
    _isVaultLocked.value = true
    _currentDestination.value = ScreenDestination.Unlock
  }

  fun unlockWithBiometrics(): Boolean {
    _isVaultLocked.value = false
    _pinAttemptsRemaining.value = 5
    _isLockoutActive.value = false
    _currentDestination.value = ScreenDestination.Home
    return true
  }

  fun attemptUnlockWithPin(enteredPin: String): Boolean {
    if (enteredPin == _unlockPin.value) {
      _isVaultLocked.value = false
      _pinAttemptsRemaining.value = 5
      _isLockoutActive.value = false
      _currentDestination.value = ScreenDestination.Home
      return true
    } else {
      val remaining = _pinAttemptsRemaining.value - 1
      _pinAttemptsRemaining.value = remaining
      if (remaining <= 0) {
        _isLockoutActive.value = true
      }
      return false
    }
  }

  fun updatePin(newPin: String) {
    _unlockPin.value = newPin
  }

  fun resetLockout() {
    _pinAttemptsRemaining.value = 5
    _isLockoutActive.value = false
  }

  // Settings updates
  fun setThemeSetting(setting: AppThemeSetting) {
    _themeSetting.value = setting
  }

  fun toggleTheme() {
    _themeSetting.value = when (_themeSetting.value) {
      AppThemeSetting.LIGHT -> AppThemeSetting.DARK
      AppThemeSetting.DARK -> AppThemeSetting.LIGHT
      AppThemeSetting.SYSTEM -> AppThemeSetting.DARK
    }
  }

  fun setBiometricsEnabled(enabled: Boolean) {
    _biometricsEnabled.value = enabled
  }

  fun setAutoLockDuration(duration: String) {
    _autoLockDuration.value = duration
  }

  fun setScreenshotProtection(enabled: Boolean) {
    _screenshotProtection.value = enabled
  }

  fun setClipboardTimeout(timeout: String) {
    _clipboardTimeout.value = timeout
  }

  fun setWipeOnFailedAttempts(enabled: Boolean) {
    _wipeOnFailedAttempts.value = enabled
  }

  // Backup simulation
  fun toggleBackupConfigured(configured: Boolean) {
    _isBackupConfigured.value = configured
    if (!configured) {
      _backupStatus.value = BackupStatus.DISABLED
    } else {
      _backupStatus.value = BackupStatus.IDLE
    }
  }

  fun triggerManualBackup(shouldFail: Boolean = false) {
    viewModelScope.launch {
      _backupStatus.value = BackupStatus.IN_PROGRESS
      delay(1800)
      if (shouldFail) {
        _backupStatus.value = BackupStatus.FAILED
      } else {
        _backupStatus.value = BackupStatus.SUCCESS
        _lastBackupTime.value = "Just now"
      }
    }
  }
}
