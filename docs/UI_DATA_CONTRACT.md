# SeedSafe UI Data Contract Specification

This document details the data contracts, models, state streams, and persistence representations for SeedSafe v0.1.

---

## 1. Domain Model Hierarchy

All vault entries implement the sealed interface `VaultItem` in `app/src/main/java/com/example/model/VaultModels.kt`.

```kotlin
sealed interface VaultItem {
  val id: String
  val title: String
  val isFavorite: Boolean
  val updatedAt: String
  val category: VaultCategory

  fun copyWithFavorite(isFav: Boolean): VaultItem
}
```

### Concrete Entry Implementations

#### 1. `LoginItem`
Represents website, application, or service credentials:
```kotlin
data class LoginItem(
    override val id: String,
    override val title: String,
    val username: String,
    val password: String,
    val website: String = "",
    val notes: String = "",
    override val isFavorite: Boolean = false,
    override val updatedAt: String = "Today, 10:42 AM",
    val serviceBadge: String = "",
) : VaultItem {
  override val category: VaultCategory = VaultCategory.LOGIN
  override fun copyWithFavorite(isFav: Boolean): VaultItem = copy(isFavorite = isFav)
}
```

#### 2. `NoteItem`
Represents private encrypted memos, recovery keys, or plain secrets:
```kotlin
data class NoteItem(
    override val id: String,
    override val title: String,
    val content: String,
    val notes: String = "",
    override val isFavorite: Boolean = false,
    override val updatedAt: String = "Yesterday, 3:15 PM",
) : VaultItem {
  override val category: VaultCategory = VaultCategory.NOTE
  override fun copyWithFavorite(isFav: Boolean): VaultItem = copy(isFavorite = isFav)
}
```

#### 3. `IdItem`
Represents official government documents, passports, driver licenses, or national IDs:
```kotlin
data class IdItem(
    override val id: String,
    override val title: String,
    val idType: String, // "Passport", "Driver's License", "SSN", "National ID", "Tax ID"
    val idNumber: String,
    val expiryDate: String = "",
    val issuingCountry: String = "",
    val notes: String = "",
    override val isFavorite: Boolean = false,
    override val updatedAt: String = "3 days ago",
) : VaultItem {
  override val category: VaultCategory = VaultCategory.ID_CARD
  override fun copyWithFavorite(isFav: Boolean): VaultItem = copy(isFavorite = isFav)
}
```

#### 4. `CardItem`
Represents credit, debit, or hardware payment cards:
```kotlin
data class CardItem(
    override val id: String,
    override val title: String,
    val cardholderName: String,
    val cardNumber: String, // e.g. "4532 8901 2345 6789"
    val expiry: String, // MM/YY
    val cvv: String,
    val cardType: String, // "Visa", "Mastercard", "Amex"
    val notes: String = "",
    override val isFavorite: Boolean = false,
    override val updatedAt: String = "1 week ago",
) : VaultItem {
  override val category: VaultCategory = VaultCategory.PAYMENT_CARD
  override fun copyWithFavorite(isFav: Boolean): VaultItem = copy(isFavorite = isFav)
}
```

#### 5. `DocumentItem`
Represents encrypted identity scans, emergency PDF packets, or cryptographic backup files:
```kotlin
data class DocumentItem(
    override val id: String,
    override val title: String,
    val fileName: String,
    val fileType: String, // "PDF", "ENC", "SCAN", "KEY"
    val fileSize: String,
    val dateAdded: String,
    val notes: String = "",
    override val isFavorite: Boolean = false,
    override val updatedAt: String = "2 weeks ago",
) : VaultItem {
  override val category: VaultCategory = VaultCategory.DOCUMENT
  override fun copyWithFavorite(isFav: Boolean): VaultItem = copy(isFavorite = isFav)
}
```

---

## 2. Supporting Enums & Data Types

### `VaultCategory`
Categorizes entries into functional security silos:
* `ALL`: Universal aggregate ("All Items").
* `LOGIN`: Credentials ("Logins").
* `NOTE`: Secure memos ("Secure Notes").
* `ID_CARD`: Identity records ("ID Numbers").
* `PAYMENT_CARD`: Financial instruments ("Cards").
* `DOCUMENT`: Identity & backup files ("Documents").

### `BackupStatus`
State machine for cloud synchronization:
* `DISABLED`: Cloud sync deactivated by user.
* `CONNECTING`: Authenticating with Google Drive API.
* `IDLE`: Configured and awaiting scheduled sync.
* `IN_PROGRESS`: Encrypting and uploading payload envelope.
* `SUCCESS`: Last backup synced successfully.
* `FAILED`: Synchronization error (timeout, network, or auth error).

### `SecurityScore`
Security Center audit evaluation:
```kotlin
data class SecurityScore(
    val score: Int = 92, // 0 to 100
    val weakCount: Int = 1,
    val reusedCount: Int = 1,
    val oldCount: Int = 1,
    val compromisedCount: Int = 0,
    val lastAudit: String = "Checked today",
)
```

### `AppThemeSetting`
User preference for theme rendering:
* `SYSTEM`: Matches system `isSystemInDarkTheme()`.
* `DARK`: Forces dark vault obsidian canvas.
* `LIGHT`: Forces clean slate modern off-white theme.

---

## 3. Reactive ViewModel StateFlow Contract

The UI consumes state exclusively via Kotlin Coroutines `StateFlow` primitives exposed by `VaultViewModel`:

| StateFlow Property | Emitted Type | UI Consumers | Description |
|---|---|---|---|
| `currentDestination` | `ScreenDestination` | `MainActivity` | Active screen for routing |
| `items` | `List<VaultItem>` | `MainVaultScreen`, `CategoryListScreen` | All active decrypted vault entries |
| `favoriteItems` | `List<VaultItem>` | `MainVaultScreen`, `FavoritesScreen` | Entries where `isFavorite == true` |
| `recentlyAccessedItems` | `List<VaultItem>` | `MainVaultScreen`, `RecentlyUsedScreen` | Ordered entries by access timestamp |
| `searchQuery` | `String` | `SearchScreen` | Live search input text |
| `selectedCategoryFilter` | `VaultCategory` | `SearchScreen` | Selected category filter chip |
| `recentSearches` | `List<String>` | `SearchScreen` | History chip list |
| `filteredItems` | `List<VaultItem>` | `SearchScreen` | Computed search match list |
| `securityScore` | `SecurityScore` | `MainVaultScreen`, `SecurityCenterScreen` | Computed vault health score |
| `isVaultLocked` | `Boolean` | Root navigation / lock gate | True if vault requires authentication |
| `unlockPin` | `String` | `UnlockScreen`, `SettingsScreen` | Current master PIN (default "1234") |
| `pinAttemptsRemaining` | `Int` | `UnlockScreen` | Remaining attempts before lockout (starts at 5) |
| `isLockoutActive` | `Boolean` | `UnlockScreen` | Lockout state flag |
| `themeSetting` | `AppThemeSetting` | Root `SeedSafeTheme` | Active theme setting |
| `biometricsEnabled` | `Boolean` | `SettingsScreen`, `UnlockScreen` | Biometric unlock availability |
| `autoLockDuration` | `String` | `SettingsScreen` | Auto-lock timeout setting |
| `screenshotProtection` | `Boolean` | `SettingsScreen` | FLAG_SECURE window protection flag |
| `clipboardTimeout` | `String` | `SettingsScreen` | Clipboard auto-wipe duration |
| `wipeOnFailedAttempts` | `Boolean` | `SettingsScreen` | Auto-wipe setting |
| `backupStatus` | `BackupStatus` | `BackupScreen`, `SettingsScreen` | Cloud backup synchronization state |
| `lastBackupTime` | `String` | `BackupScreen`, `SettingsScreen` | Human-readable last sync timestamp |
| `googleAccount` | `String` | `BackupScreen` | Connected Google Drive account |
| `isBackupConfigured` | `Boolean` | `BackupScreen` | Backup activation switch state |

---

## 4. Production Room Database Schema Mapping

When migrating to production Room persistence with SQLCipher encryption, the domain models map to the following relational schema:

```sql
CREATE TABLE vault_items (
    id TEXT PRIMARY KEY NOT NULL,
    category TEXT NOT NULL,
    title TEXT NOT NULL,
    is_favorite INTEGER NOT NULL DEFAULT 0,
    created_at INTEGER NOT NULL,
    updated_at INTEGER NOT NULL,
    last_accessed_at INTEGER NOT NULL,
    notes TEXT,
    encrypted_payload BLOB NOT NULL, -- AES-256-GCM encrypted category fields
    iv BLOB NOT NULL,
    auth_tag BLOB NOT NULL
);

CREATE INDEX idx_vault_items_category ON vault_items(category);
CREATE INDEX idx_vault_items_favorite ON vault_items(is_favorite);
CREATE INDEX idx_vault_items_accessed ON vault_items(last_accessed_at DESC);
```

### Sensitive Field Encryption Mapping
* Plaintext fields stored in encrypted database: `id`, `category`, `title`, `is_favorite`, timestamps.
* Encrypted payload contents:
  * `LoginItem`: `username`, `password`, `website`, `serviceBadge`.
  * `NoteItem`: `content`.
  * `IdItem`: `idType`, `idNumber`, `expiryDate`, `issuingCountry`.
  * `CardItem`: `cardholderName`, `cardNumber`, `expiry`, `cvv`, `cardType`.
  * `DocumentItem`: `fileName`, `fileType`, `fileSize`, raw binary document attachment.
