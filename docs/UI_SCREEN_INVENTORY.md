# SeedSafe UI Screen Inventory

This document provides a comprehensive inventory of all screens and modal flows in the SeedSafe Android application as of UI baseline v0.1.

---

## Screen Directory Overview

| Destination Enum | Display / Common Name | Source File | Primary ViewModel Dependencies |
|---|---|---|---|
| `ScreenDestination.Splash` | Splash Screen | `ui/screens/AuthAndOnboardingScreens.kt` | `isFirstLaunch`, `isVaultLocked` |
| `ScreenDestination.Onboarding` | Onboarding Carousel | `ui/screens/AuthAndOnboardingScreens.kt` | `isFirstLaunch` |
| `ScreenDestination.CreateVault` | 12-Word Recovery Phrase Generation | `ui/screens/AuthAndOnboardingScreens.kt` | `MockVaultData.mockRecoveryPhrase` |
| `ScreenDestination.RecoveryPhraseQuiz` | Recovery Confirmation Quiz | `ui/screens/AuthAndOnboardingScreens.kt` | `completeOnboarding()` |
| `ScreenDestination.Unlock` | Vault Unlock (PIN + Biometrics) | `ui/screens/UnlockScreen.kt` | `pinAttemptsRemaining`, `isLockoutActive`, `unlockPin` |
| `ScreenDestination.Home` | Main Vault Dashboard | `ui/screens/MainVaultScreen.kt` | `items`, `favoriteItems`, `recentlyAccessedItems`, `securityScore` |
| `ScreenDestination.Search` | Vault Search & Category Filter | `ui/screens/SearchScreen.kt` | `searchQuery`, `selectedCategoryFilter`, `recentSearches`, `filteredItems` |
| `ScreenDestination.Favorites` | Favorite Items List | `ui/screens/FavoritesAndRecentsScreens.kt` | `favoriteItems` |
| `ScreenDestination.RecentlyUsed` | Recently Accessed Entries | `ui/screens/FavoritesAndRecentsScreens.kt` | `recentlyAccessedItems` |
| `ScreenDestination.CategoryList(category)` | Category Vault Entries | `ui/screens/CategoryListScreen.kt` | `items` filtered by category |
| `ScreenDestination.ItemDetail(itemId)` | Entry Detail & Credential Viewer | `ui/screens/ItemDetailScreen.kt` | `getItemById(itemId)`, `deleteItem()`, `toggleFavorite()` |
| `ScreenDestination.AddEditItem(category, itemId)` | Add / Edit Entry Form | `ui/screens/AddEditItemScreen.kt` | `addItem()`, `updateItem()`, `getItemById()` |
| `ScreenDestination.SecurityCenter` | Vault Health & Weak Password Audit | `ui/screens/SecurityAndTrustScreens.kt` | `securityScore`, `getWeakPasswordItems()`, `getReusedPasswordItems()` |
| `ScreenDestination.BackupManagement` | Encrypted Backup Management | `ui/screens/BackupAndRestoreScreens.kt` | `backupStatus`, `lastBackupTime`, `googleAccount`, `isBackupConfigured` |
| `ScreenDestination.RestoreVault` | Restore Vault from Recovery Phrase | `ui/screens/BackupAndRestoreScreens.kt` | `unlockWithBiometrics()`, `navigateTo(Home)` |
| `ScreenDestination.PrivacyTrust` | Zero-Knowledge Architecture & Privacy | `ui/screens/SecurityAndTrustScreens.kt` | Static architectural guidance |
| `ScreenDestination.Settings` | Vault Security Settings & Preferences | `ui/screens/SettingsScreen.kt` | `themeSetting`, `biometricsEnabled`, `autoLockDuration`, `screenshotProtection`, `clipboardTimeout`, `wipeOnFailedAttempts`, `unlockPin` |

---

## Detailed Screen Specifications

### 1. Splash Screen
* **Destination**: `ScreenDestination.Splash`
* **File**: `app/src/main/java/com/example/ui/screens/AuthAndOnboardingScreens.kt`
* **Purpose**: Branded entry point displayed on app cold start. Displays the SeedSafe shield logo and triggers initialization check.
* **Key Components**:
  * Animated pulsing `SeedSafeLogo` (size 72dp) with glowing gradient border.
  * Tagline: *"Offline-First Zero-Knowledge Password Vault"*.
  * Architectural badge: *"Hardware Isolated Architecture"*.
* **User Flows**:
  * On splash delay complete: Navigates to `Onboarding` if first launch, otherwise to `Unlock`.
* **State Dependencies**: `VaultViewModel.isFirstLaunch`, `VaultViewModel.isVaultLocked`.
* **Test Tags**: None (auto-advances after 1.2s delay).
* **Mock vs. Production**:
  * *Mock*: Timer-based coroutine delay (`delay(1200)`).
  * *Production*: Validates presence of encrypted Master Key material in Android Keystore / SQLCipher. If keys exist, proceed to `Unlock`; if uninitialized, proceed to `Onboarding`.

---

### 2. Onboarding Carousel Screen
* **Destination**: `ScreenDestination.Onboarding`
* **File**: `app/src/main/java/com/example/ui/screens/AuthAndOnboardingScreens.kt`
* **Purpose**: Educates new users on zero-knowledge architecture, offline-first isolation, 12-word recovery phrases, and optional client-side encrypted cloud backups.
* **Key Components**:
  * 4-step interactive card carousel with indicators:
    1. Zero-Knowledge Encryption (Argon2id + AES-256-GCM architecture).
    2. True Offline Storage (Never phones home, local hardware isolation).
    3. 12-Word Recovery Phrase (Decentralized master seed architecture).
    4. Personal Cloud Backup (User-owned Google Drive client-side encrypted envelope).
  * Top bar with SeedSafe branding and "Skip" button.
  * Carousel action buttons: "Continue" (advances step), "Create My Vault" (primary CTA), and "Restore an Existing Vault" (secondary CTA).
* **User Flows**:
  * "Create My Vault" -> Navigates to `ScreenDestination.CreateVault`.
  * "Restore an Existing Vault" -> Navigates to `ScreenDestination.RestoreVault`.
* **State Dependencies**: None (local step index state).
* **Test Tags**:
  * `onboarding_next_btn`: Advance carousel step.
  * `create_vault_cta_btn`: Primary button to begin vault generation.
  * `restore_vault_cta_btn`: Secondary button to restore existing vault.
* **Mock vs. Production**:
  * *Mock*: Stateless Compose carousel.
  * *Production*: Same UI presentation; records onboarding completion flag in encrypted preferences upon vault generation.

---

### 3. Create Vault (12-Word Recovery Phrase) Screen
* **Destination**: `ScreenDestination.CreateVault`
* **File**: `app/src/main/java/com/example/ui/screens/AuthAndOnboardingScreens.kt`
* **Purpose**: Generates and displays the user's master 12-word recovery phrase. Enforces backup verification before allowing vault entry.
* **Key Components**:
  * Top navigation bar with back arrow, title, and "Generate New" phrase action.
  * 3x4 grid displaying 12 recovery phrase cards with word index numbers.
  * Obfuscation overlay with "Tap to Reveal Recovery Phrase" button.
  * "Copy Phrase to Clipboard" button with temporary clipboard auto-clear warning.
  * Warning callout banner regarding offline recovery and absolute zero-knowledge policy.
  * Verification acknowledgement checkbox: *"I have written down or backed up my recovery phrase in a safe place."*
  * Continue button enabled only after checkbox confirmation.
* **User Flows**:
  * Back -> Returns to previous screen (`Onboarding` or `Settings`).
  * "Verify Recovery Phrase" -> Navigates to `ScreenDestination.RecoveryPhraseQuiz`.
* **State Dependencies**: `MockVaultData.mockRecoveryPhrase`.
* **Test Tags**:
  * `create_vault_back_btn`: Navigation back button.
  * `create_vault_refresh_btn`: Generate alternative phrase button.
  * `reveal_seed_btn`: Toggle phrase visibility overlay.
  * `copy_seed_btn`: Copy phrase to system clipboard.
  * `verify_recovery_phrase_btn`: CTA leading to confirmation quiz.
* **Mock vs. Production**:
  * *Mock*: Uses static BIP-39 word list from `MockVaultData`.
  * *Production*: Uses cryptographically secure PRNG (`SecureRandom`) to sample 128 bits of entropy and derive a valid BIP-39 mnemonic phrase. Requires FLAG_SECURE window protection.

---

### 4. Recovery Phrase Confirmation Quiz Screen
* **Destination**: `ScreenDestination.RecoveryPhraseQuiz`
* **File**: `app/src/main/java/com/example/ui/screens/AuthAndOnboardingScreens.kt`
* **Purpose**: Enforces that the user has physically recorded their recovery phrase by challenging them to select 3 specific words in sequence.
* **Key Components**:
  * Stage progress indicator showing verification 1 of 3, 2 of 3, 3 of 3.
  * Challenge prompt (e.g., *"What is Word #4 of your recovery phrase?"*).
  * 4 selectable word option cards per challenge.
  * Error indicator on wrong word selection.
  * Vault Initialization Success modal card upon passing quiz.
  * "Enter Vault" CTA button.
* **User Flows**:
  * "Enter Vault" -> Calls `viewModel.completeOnboarding()` and transitions root state to `ScreenDestination.Home`.
* **State Dependencies**: `VaultViewModel.completeOnboarding()`.
* **Test Tags**:
  * `quiz_confirm_selection_btn`: Confirm selected word choice.
  * `enter_vault_btn`: Final CTA to enter initialized vault.
* **Mock vs. Production**:
  * *Mock*: Validates against mock words at indices 3, 6, 9.
  * *Production*: Validates against runtime-generated mnemonic entropy and commits derived encryption key to Android Keystore.

---

### 5. Vault Unlock Screen
* **Destination**: `ScreenDestination.Unlock`
* **File**: `app/src/main/java/com/example/ui/screens/UnlockScreen.kt`
* **Purpose**: Authentication gate for locked vault. Supports 4-digit PIN entry and Biometric authentication fallback.
* **Key Components**:
  * Shield logo and locked status indicator.
  * 4 circular PIN dot indicators showing entry progress.
  * Error feedback banner and remaining attempts counter.
  * Full 3x4 numeric keypad (digits 1-9, 0, biometric button, and backspace).
  * Biometric prompt dialog trigger (`MockBiometricDialog`).
  * "Forgot PIN? Restore using recovery phrase" recovery link.
  * Lockout state UI when attempts are exhausted (5 failed attempts).
* **User Flows**:
  * Successful PIN / Biometric -> Unlocks vault, navigates to `Home`.
  * "Forgot PIN?" -> Navigates to `ScreenDestination.RestoreVault`.
* **State Dependencies**: `VaultViewModel.pinAttemptsRemaining`, `isLockoutActive`, `attemptUnlockWithPin()`, `unlockWithBiometrics()`.
* **Test Tags**:
  * `pin_key_0` .. `pin_key_9`: Numeric keypad buttons.
  * `unlock_key_backspace`: Backspace key.
  * `unlock_biometric_btn`: Trigger biometric authentication prompt.
  * `unlock_forgot_pin_btn`: Recovery phrase navigation link.
  * `simulate_biometric_success`: Button in biometric dialog to simulate fingerprint match.
  * `biometric_fallback_pin_btn`: Fallback to keypad button in biometric dialog.
  * `biometric_cancel_btn`: Cancel biometric prompt.
* **Mock vs. Production**:
  * *Mock*: Default PIN is `1234`. Keypad validates in-memory string.
  * *Production*: PIN derives vault key via Argon2id. Biometric authentication uses `androidx.biometric.BiometricPrompt` with `CryptoObject` unlocked by Android Keystore.

---

### 6. Main Vault Dashboard Screen
* **Destination**: `ScreenDestination.Home`
* **File**: `app/src/main/java/com/example/ui/screens/MainVaultScreen.kt`
* **Purpose**: Primary vault hub displaying health status, search bar CTA, quick category filters, favorite entries, and recent credentials.
* **Key Components**:
  * Top bar with SeedSafe logo, "Offline" security badge, and instant "Lock Vault" button.
  * Search bar CTA card directing to dedicated Search screen.
  * Security Center Health card with overall score circular indicator (e.g., 92/100).
  * Category horizontal scroll carousel: Logins, Secure Notes, ID Cards, Payment Cards, Documents.
  * Favorites section with "See All" header and `VaultItemCard` rows.
  * Recently Accessed section with "See All" header.
  * Quick-Add Floating Action Button (FAB) displaying modal bottom sheet with 5 entry type options.
* **User Flows**:
  * Lock icon -> Locks vault immediately, navigates to `Unlock`.
  * Search card -> Navigates to `ScreenDestination.Search`.
  * Security card -> Navigates to `ScreenDestination.SecurityCenter`.
  * Category card -> Navigates to `ScreenDestination.CategoryList(category)`.
  * "See All Favorites" -> Navigates to `ScreenDestination.Favorites`.
  * "See All Recents" -> Navigates to `ScreenDestination.RecentlyUsed`.
  * Vault item click -> Records access time and navigates to `ScreenDestination.ItemDetail(id)`.
  * Quick-Add FAB -> Opens bottom sheet; selecting option navigates to `ScreenDestination.AddEditItem(category)`.
* **State Dependencies**: `VaultViewModel.items`, `favoriteItems`, `recentlyAccessedItems`, `securityScore`.
* **Test Tags**:
  * `top_bar_lock_vault_btn`: Lock vault button.
  * `main_quick_add_fab`: Floating action button.
  * `home_search_bar_cta`: Search bar card.
  * `home_security_center_widget`: Security score card.
  * `cat_card_LOGIN`, `cat_card_NOTE`, etc.: Category carousel items.
  * `quick_add_option_LOGIN`, etc.: Modal sheet options.
  * `vault_item_<id>`: Item row cards.
  * `fav_btn_<id>`: Item row favorite toggle button.
* **Mock vs. Production**:
  * *Mock*: Reads in-memory StateFlow from `MockVaultData`.
  * *Production*: Reads reactive SQLCipher Room query flow through Repository pattern.

---

### 7. Search Screen
* **Destination**: `ScreenDestination.Search`
* **File**: `app/src/main/java/com/example/ui/screens/SearchScreen.kt`
* **Purpose**: Real-time filtering across titles, usernames, URLs, notes, and metadata, with category chip filters and search history.
* **Key Components**:
  * Search text field with search icon, clear button, and back arrow.
  * Horizontal filter chips: All, Logins, Notes, IDs, Cards, Documents.
  * Recent searches chip cloud with "Clear All" action.
  * Live search results list rendering matching `VaultItemCard` entries.
  * Empty search query state and "No results found" empty state.
* **User Flows**:
  * Back -> Returns to previous screen.
  * Item click -> Records access time and navigates to `ScreenDestination.ItemDetail(id)`.
* **State Dependencies**: `VaultViewModel.searchQuery`, `selectedCategoryFilter`, `recentSearches`, `filteredItems`.
* **Test Tags**:
  * `search_input_field`: Text input field.
  * `clear_search_btn`: Clear query icon button.
  * `search_filter_chip_ALL`, `search_filter_chip_LOGIN`, etc.: Category filter chips.
  * `clear_recent_searches_btn`: Clear search history button.
  * `recent_search_chip_<query>`: History chip.
* **Mock vs. Production**:
  * *Mock*: Kotlin string filtering across in-memory lists.
  * *Production*: SQLite FTS (Full-Text Search) virtual table queries over decrypted in-memory entities.

---

### 8. Favorites Screen
* **Destination**: `ScreenDestination.Favorites`
* **File**: `app/src/main/java/com/example/ui/screens/FavoritesAndRecentsScreens.kt`
* **Purpose**: Dedicated view listing all starred credentials across all categories.
* **Key Components**:
  * Standardized header with back arrow, bold title ("Favorites"), subtitle ("Quick access to your starred credentials"), and item count badge.
  * LazyColumn displaying `VaultItemCard` rows.
  * Empty state when no favorites exist.
* **User Flows**:
  * Back -> Returns to previous screen.
  * Item click -> Navigates to `ScreenDestination.ItemDetail(id)`.
* **State Dependencies**: `VaultViewModel.favoriteItems`, `toggleFavorite()`.
* **Test Tags**:
  * `favorites_back_btn`: Navigation back button.
  * `vault_item_<id>`: Item cards.
  * `fav_btn_<id>`: Star toggle button.
* **Mock vs. Production**:
  * *Mock*: StateFlow filtered by `isFavorite == true`.
  * *Production*: Room database query `SELECT * FROM vault_items WHERE is_favorite = 1`.

---

### 9. Recently Used Screen
* **Destination**: `ScreenDestination.RecentlyUsed`
* **File**: `app/src/main/java/com/example/ui/screens/FavoritesAndRecentsScreens.kt`
* **Purpose**: Chronological log of recently opened or copied vault items.
* **Key Components**:
  * Standardized header with back arrow, bold title ("Recently Used"), subtitle ("Audit log of your recent vault access"), and count badge.
  * Chronological item list with timestamp badges.
* **User Flows**:
  * Back -> Returns to previous screen.
  * Item click -> Navigates to `ScreenDestination.ItemDetail(id)`.
* **State Dependencies**: `VaultViewModel.recentlyAccessedItems`.
* **Test Tags**:
  * `recents_back_btn`: Navigation back button.
  * `vault_item_<id>`: Item cards.
* **Mock vs. Production**:
  * *Mock*: In-memory LRU tracking list.
  * *Production*: Local access log table with encrypted timestamps.

---

### 10. Category List Screen
* **Destination**: `ScreenDestination.CategoryList(category)`
* **File**: `app/src/main/java/com/example/ui/screens/CategoryListScreen.kt`
* **Purpose**: Displays all entries belonging to a specific category (e.g. all Logins or all Payment Cards).
* **Key Components**:
  * Standardized header with back arrow, category icon/color badge, bold category title, explanatory subtitle, and item count.
  * "Add New" category-specific action button in header.
  * Filtered LazyColumn of `VaultItemCard` entries.
  * Empty category state with dedicated "Add your first entry" CTA.
* **User Flows**:
  * Back -> Returns to previous screen.
  * Add button -> Navigates to `ScreenDestination.AddEditItem(category = category)`.
  * Item click -> Navigates to `ScreenDestination.ItemDetail(id)`.
* **State Dependencies**: `VaultViewModel.items` filtered by category.
* **Test Tags**:
  * `category_list_back_btn`: Navigation back button.
  * `category_list_add_btn`: Add entry for this category button.
  * `vault_item_<id>`: Item cards.
* **Mock vs. Production**:
  * *Mock*: Filters in-memory item list by enum value.
  * *Production*: Reactive Room query `SELECT * FROM vault_items WHERE category = :category`.

---

### 11. Item Detail Screen
* **Destination**: `ScreenDestination.ItemDetail(itemId)`
* **File**: `app/src/main/java/com/example/ui/screens/ItemDetailScreen.kt`
* **Purpose**: Secure credential viewer displaying decrypted metadata, credentials, and actions.
* **Key Components**:
  * Top bar with back button, favorite toggle, edit button, and delete button.
  * Category badge, entry title, and last-updated timestamp.
  * Category-specific fields:
    * *Login*: Username row with copy button; Password row with toggle reveal (eye icon), copy button, and strength indicator; Website URL row with launch action.
    * *Secure Note*: Full-text note card with monospace font and copy button.
    * *ID Card*: ID Type, ID Number (masked with reveal toggle and copy button), Issuing Authority, Expiration Date.
    * *Payment Card*: Visual gradient payment card representation with brand logo, masked card number (reveal toggle + copy), expiration date, cardholder name, and CVV (masked with reveal toggle).
    * *Document*: File name, file size, MIME type badge, date added, and encrypted file placeholder.
  * Secure Notes / Memo card.
  * Metadata card: Created date, last updated date, item UUID.
  * Password generator launcher (for logins).
  * Permanent delete confirmation dialog (`DeleteConfirmationDialog`).
* **User Flows**:
  * Back -> Returns to previous screen.
  * Edit -> Navigates to `ScreenDestination.AddEditItem(category, itemId)`.
  * Delete -> Confirms via modal, deletes item, and pops back.
* **State Dependencies**: `VaultViewModel.getItemById(itemId)`, `deleteItem()`, `toggleFavorite()`.
* **Test Tags**:
  * `item_detail_back_btn`: Top bar back button.
  * `item_detail_favorite_btn`: Top bar favorite toggle button.
  * `edit_item_top_btn`: Top bar edit button.
  * `delete_item_top_btn`: Top bar delete button.
  * `confirm_delete_btn`: Confirmation button in deletion dialog.
  * `copy_btn_<hash>`: Copy credential buttons.
* **Mock vs. Production**:
  * *Mock*: Obfuscation via local boolean state (`isPasswordRevealed`).
  * *Production*: Zero-knowledge decryption into temporary char arrays. Automatically wipes decrypted memory buffers upon `onDispose`. Enforces `FLAG_SECURE` window flags to prevent screenshots/screen recordings.

---

### 12. Add / Edit Item Screen
* **Destination**: `ScreenDestination.AddEditItem(category, itemId)`
* **File**: `app/src/main/java/com/example/ui/screens/AddEditItemScreen.kt`
* **Purpose**: Unified form for creating new credentials or editing existing entries across all 5 vault types.
* **Key Components**:
  * Top bar with Cancel button, dynamic title ("New Login" / "Edit Login"), and Save action button.
  * Common Fields: Title (required, with validation error state), "Add to Favorites" toggle switch, Notes / Additional Information multiline input.
  * Category-Specific Fields:
    * *Login*: Username / Email input, Password input with reveal toggle and embedded Password Generator trigger, Password strength bar, Website URL.
    * *Secure Note*: Large multiline note content area.
    * *ID Card*: ID Type dropdown selector, ID Number, Issuing Authority, Expiry Date picker.
    * *Payment Card*: Cardholder Name, Card Number (with card brand detection), Expiry Date (MM/YY), CVV (3-4 digits), Card Type dropdown.
    * *Document*: File Name, File Type / Extension, File Size label.
  * Embedded Password Generator dialog (`PasswordGeneratorDialog`) with length slider (8-32 chars) and character set toggles (uppercase, numbers, symbols).
* **User Flows**:
  * Cancel / Back -> Discards changes and returns.
  * Save -> Validates input, saves item to ViewModel, and pops back to caller.
* **State Dependencies**: `VaultViewModel.addItem()`, `updateItem()`, `getItemById()`.
* **Test Tags**:
  * `add_edit_cancel_btn`: Cancel / back button.
  * `save_vault_item_btn`: Save button.
  * `add_item_title_input`: Title field.
  * `add_item_username_input`: Username field.
  * `add_item_password_input`: Password field.
  * `add_item_note_input`: Note body field.
  * `generate_password_btn`: Password generator dice/casino icon.
* **Mock vs. Production**:
  * *Mock*: Updates in-memory mutable list.
  * *Production*: Serializes entity into encrypted SQLCipher Room database with AES-256-GCM authenticated payload.

---

### 13. Security Center Screen
* **Destination**: `ScreenDestination.SecurityCenter`
* **File**: `app/src/main/java/com/example/ui/screens/SecurityAndTrustScreens.kt`
* **Purpose**: Security dashboard auditing password strength, identifying weak or reused passwords, and verifying device security hygiene.
* **Key Components**:
  * Top bar with back arrow and title.
  * Security Health Overview card with animated score indicator (e.g. 92/100) and status breakdown (Compromised: 0, Weak: 1, Reused: 2).
  * Weak Passwords list with "Fix" button leading directly to item editor.
  * Reused Passwords list with "Fix" button.
  * Device Security Checklist:
    * Biometric Protection (active/configured).
    * Screen Recording Protection (`FLAG_SECURE`).
    * Zero-Knowledge Encryption (local derivation).
    * Clipboard Auto-Clear status.
* **User Flows**:
  * Back -> Returns to previous screen.
  * "Fix" button -> Navigates to `ScreenDestination.AddEditItem(category, itemId)` for the target credential.
* **State Dependencies**: `VaultViewModel.securityScore`, `getWeakPasswordItems()`, `getReusedPasswordItems()`.
* **Test Tags**:
  * `security_center_back_btn`: Navigation back button.
  * `security_fix_item_<id>`: Action button to resolve flagged credential.
* **Mock vs. Production**:
  * *Mock*: Audits in-memory passwords using regex length checks and hash comparison.
  * *Production*: Client-side cryptographic zxcvbn password entropy evaluation, offline k-anonymity HaveIBeenPwned hash checks, and real device KeyguardManager security checks.

---

### 14. Backup Management Screen
* **Destination**: `ScreenDestination.BackupManagement`
* **File**: `app/src/main/java/com/example/ui/screens/BackupAndRestoreScreens.kt`
* **Purpose**: Controls user-directed, client-side encrypted backups to the user's personal Google Drive AppData folder.
* **Key Components**:
  * Standardized header with back arrow, bold title ("Encrypted Cloud Backup"), and subtitle.
  * Cloud Provider Card (Google Drive status, connected account name, authorization state).
  * Backup Configuration Toggle switch.
  * Synchronization Status Banner (Success, In-Progress, Failed, or Unconfigured).
  * Manual "Back Up Vault Now" CTA button.
  * Failure Simulation button (for UI prototype verification).
  * Architectural Client-Side Encryption Guarantee callout explaining that encryption occurs locally before upload.
* **User Flows**:
  * Back -> Returns to Settings.
  * Toggle backup -> Updates backup configured state.
  * Manual backup -> Triggers simulated or production backup routine.
* **State Dependencies**: `VaultViewModel.backupStatus`, `lastBackupTime`, `googleAccount`, `isBackupConfigured`.
* **Test Tags**:
  * `backup_screen_back_btn`: Navigation back button.
  * `backup_toggle_switch`: Switch enabling backup sync.
  * `manual_backup_now_btn`: Trigger backup sync button.
  * `simulate_backup_failure_btn`: Prototype test button.
* **Mock vs. Production**:
  * *Mock*: Simulates asynchronous upload delay with coroutine state updates.
  * *Production*: Uses Google Drive REST API / Google Identity Services to upload an AES-256-GCM encrypted file to the hidden AppData folder (`drive.appdata` scope).

---

### 15. Restore Vault Screen
* **Destination**: `ScreenDestination.RestoreVault`
* **File**: `app/src/main/java/com/example/ui/screens/BackupAndRestoreScreens.kt`
* **Purpose**: Enables existing users to reconstruct and decrypt their vault using their 12-word recovery phrase or cloud backup file.
* **Key Components**:
  * Standardized header with back arrow, bold title ("Restore Your Vault"), and subtitle.
  * Method selector tab row: "Recovery Phrase" (12 words) vs "Google Drive Backup".
  * Recovery Phrase tab: 12 individual text fields for entering mnemonic words, validation warning, and "Restore Vault" button.
  * Google Drive Backup tab: Account sign-in button, file selector, and recovery passphrase prompt.
  * Restore in-progress modal indicator.
* **User Flows**:
  * Back -> Returns to previous screen (`Onboarding`, `Unlock`, or `Settings`).
  * Restore Success -> Unlocks vault and navigates to `ScreenDestination.Home`.
* **State Dependencies**: `VaultViewModel.unlockWithBiometrics()`, `navigateTo(Home)`.
* **Test Tags**:
  * `restore_screen_back_btn`: Navigation back button.
  * `restore_submit_btn`: Primary button to trigger restoration.
  * `restore_phrase_input_<index>`: Mnemonic input fields.
* **Mock vs. Production**:
  * *Mock*: Simulates restoration delay and unblocks entry.
  * *Production*: Derives master seed via PBKDF2/Argon2 from the 12 words, downloads or accesses encrypted payload, validates auth tag, and initializes Room SQLCipher database.

---

### 16. Privacy & Trust Screen
* **Destination**: `ScreenDestination.PrivacyTrust`
* **File**: `app/src/main/java/com/example/ui/screens/SecurityAndTrustScreens.kt`
* **Purpose**: Clear, transparent explanation of SeedSafe's security model, cryptographic architecture, and zero-knowledge guarantee.
* **Key Components**:
  * Top bar with back arrow and title ("Architecture & Trust").
  * 4 detailed architectural pillars:
    1. Zero-Knowledge Architecture (Local key derivation, no external servers).
    2. Local Key Isolation (Android Keystore hardware-backed keys).
    3. User-Owned Cloud Sync (Google Drive AppData client-side encryption).
    4. Open Source & Verifiable (Auditable code, no proprietary tracking SDKs).
  * Technical specifications card: AES-256-GCM, Argon2id, BIP-39 standard, SQLCipher local database.
* **User Flows**:
  * Back -> Returns to Settings.
* **State Dependencies**: None (static educational content).
* **Test Tags**:
  * `privacy_trust_back_btn`: Navigation back button.
* **Mock vs. Production**: Pure documentation and user reassurance UI; identical in mock and production.

---

### 17. Settings Screen
* **Destination**: `ScreenDestination.Settings`
* **File**: `app/src/main/java/com/example/ui/screens/SettingsScreen.kt`
* **Purpose**: Vault preferences, security configuration, theme options, PIN management, and prototype reset tools.
* **Key Components**:
  * Profile & Vault Health summary card with quick link to Security Center.
  * Security Settings group:
    * Biometric Unlock toggle switch.
    * Master PIN change dialog trigger.
    * Auto-Lock Duration selector (Immediately, 1 min, 5 min, 15 min, 30 min).
    * Screen Recording & Screenshot Protection (`FLAG_SECURE`) toggle switch.
    * Clipboard Auto-Clear timeout selector (30s, 60s, 120s, Never).
    * Wipe on Failed Attempts (10 attempts) toggle switch.
  * Backup & Sync group:
    * Google Drive Cloud Backup row displaying last backup time and status badge.
    * Restore from Backup / Phrase navigation row.
  * Display & Theme group:
    * Theme mode segmented radio selector (System Default, Dark Mode, Light Mode).
  * Trust & Information group:
    * Cryptographic Architecture & Trust navigation row.
    * App Version & Build Information (`v0.1.0-alpha`).
  * Development & Prototype Utilities:
    * Reset to Onboarding button (resets all states and returns to initial launch).
* **User Flows**:
  * Back / Tab switch -> Bottom bar navigation.
  * Change PIN -> Opens PIN change modal dialog.
  * Navigate to Backup -> `ScreenDestination.BackupManagement`.
  * Navigate to Restore -> `ScreenDestination.RestoreVault`.
  * Navigate to Security Center -> `ScreenDestination.SecurityCenter`.
  * Navigate to Privacy Trust -> `ScreenDestination.PrivacyTrust`.
  * Reset to Onboarding -> Calls `viewModel.resetToOnboarding()` and navigates to `ScreenDestination.Splash`.
* **State Dependencies**: `VaultViewModel.themeSetting`, `biometricsEnabled`, `autoLockDuration`, `screenshotProtection`, `clipboardTimeout`, `wipeOnFailedAttempts`, `unlockPin`.
* **Test Tags**:
  * `settings_biometrics_switch`: Toggle biometrics.
  * `settings_change_pin_row`: Trigger PIN change dialog.
  * `settings_screenshot_protection_switch`: Toggle screenshot protection.
  * `settings_wipe_failed_switch`: Toggle wipe on failed attempts.
  * `settings_backup_row`: Navigate to backup management.
  * `settings_restore_row`: Navigate to restore screen.
  * `settings_security_center_row`: Navigate to security center.
  * `settings_privacy_trust_row`: Navigate to privacy & trust.
  * `settings_recovery_phrase_row`: View recovery phrase.
  * `settings_reset_onboarding_btn`: Reset prototype to onboarding state.
* **Mock vs. Production**:
  * *Mock*: StateFlow in-memory updates.
  * *Production*: Persists settings in `EncryptedSharedPreferences` / DataStore. Toggles live Android window flags (`FLAG_SECURE`) and schedules background auto-lock alarms.
