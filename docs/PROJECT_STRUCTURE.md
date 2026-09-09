# SeedSafe Project Structure & Codebase Map

This document outlines the directory structure, package layout, and file responsibilities for the SeedSafe Android codebase.

---

## 1. Directory Tree Overview

```
app/
├── build.gradle.kts                      # Module build configuration & dependencies
├── src/
│   ├── main/
│   │   ├── AndroidManifest.xml           # App declaration, permissions, activities
│   │   ├── java/com/example/
│   │   │   ├── MainActivity.kt           # Root activity, Scaffold, navigation dispatcher
│   │   │   ├── data/
│   │   │   │   └── MockVaultData.kt      # Mock entries, seed phrase, categories
│   │   │   ├── model/
│   │   │   │   └── VaultModels.kt        # Polymorphic VaultItem, categories, statuses
│   │   │   ├── state/
│   │   │   │   └── VaultViewModel.kt     # Single source of truth, StateFlows, actions
│   │   │   ├── ui/
│   │   │   │   ├── components/
│   │   │   │   │   └── CommonComponents.kt # Logo, cards, copy button, dialogs
│   │   │   │   ├── screens/
│   │   │   │   │   ├── AddEditItemScreen.kt
│   │   │   │   │   ├── AuthAndOnboardingScreens.kt
│   │   │   │   │   ├── BackupAndRestoreScreens.kt
│   │   │   │   │   ├── CategoryListScreen.kt
│   │   │   │   │   ├── FavoritesAndRecentsScreens.kt
│   │   │   │   │   ├── ItemDetailScreen.kt
│   │   │   │   │   ├── MainVaultScreen.kt
│   │   │   │   │   ├── SearchScreen.kt
│   │   │   │   │   ├── SecurityAndTrustScreens.kt
│   │   │   │   │   ├── SettingsScreen.kt
│   │   │   │   │   └── UnlockScreen.kt
│   │   │   │   └── theme/
│   │   │   │       ├── Color.kt          # Obsidian, slate, emerald, accent palettes
│   │   │   │       ├── Theme.kt          # SeedSafeTheme, dynamic color, ColorScheme
│   │   │   │       └── Type.kt           # Material 3 typography definitions
│   │   └── res/
│   │       ├── drawable/                 # Vector drawables & app launcher icons
│   │       ├── mipmap-*/                 # Adaptive launcher mipmap icons
│   │       └── values/
│   │           ├── colors.xml
│   │           └── strings.xml           # App name & localization resources
docs/                                     # Architectural & UI documentation suite
├── ANTIGRAVITY_HANDOFF.md
├── DESIGN_SYSTEM.md
├── ENTRY_TYPES.md
├── NAVIGATION_SPEC.md
├── PRODUCTION_REPLACEMENT_MAP.md
├── PROJECT_STRUCTURE.md
├── SECURITY_SENSITIVE_UI.md
├── TEST_TAGS.md
├── UI_DATA_CONTRACT.md
└── UI_SCREEN_INVENTORY.md
```

---

## 2. Package & Layer Responsibilities

### `com.example` (Root)
* **`MainActivity.kt`**: Main application lifecycle container. Hosts the root `SeedSafeTheme`, navigation dispatcher `when (val dest = currentDestination)`, the bottom navigation bar (`NavigationBar`), and system backstack integration (`BackHandler`).

### `com.example.model` (Data Contracts)
* **`VaultModels.kt`**: Contains the sealed interface `VaultItem` and its five concrete records (`LoginItem`, `NoteItem`, `IdItem`, `CardItem`, `DocumentItem`). Defines `VaultCategory`, `BackupStatus`, and `SecurityScore`.

### `com.example.data` (Mock Baseline)
* **`MockVaultData.kt`**: Provides the sample dataset (credentials, private notes, passports, credit cards, PDF files) and the initial 12-word BIP-39 recovery phrase used for UI demonstration.

### `com.example.state` (State Management)
* **`VaultViewModel.kt`**: The single source of truth for the entire application. Manages:
  * Active screen routing and navigation backstack history (`currentDestination`, `navigationStack`).
  * Vault entries list, favorites filtering, and recently accessed LRU tracking.
  * Live search query, category filtering, and full-text search results.
  * Master PIN validation, remaining attempts counter, and lockout enforcement.
  * User preferences (theme, biometrics, auto-lock timeout, screenshot protection, clipboard auto-clear, wipe policies).
  * Cloud backup simulation and status states.

### `com.example.ui.theme` (Design System)
* **`Color.kt`**: Defines the dark vault obsidian palette (`VaultDarkBg`, `VaultDarkSurface`), light modern slate palette (`VaultLightBg`), and brand semantic tokens (`EmeraldPrimary`, `CyanAccent`, `AmberWarning`, `RoseError`, `BlueShield`, `PurpleKey`).
* **`Theme.kt`**: Provides `SeedSafeTheme` composable with support for `AppThemeSetting` (`SYSTEM`, `DARK`, `LIGHT`).
* **`Type.kt`**: Configures Material 3 typography scales.

### `com.example.ui.components` (Shared Reusable UI)
* **`CommonComponents.kt`**: Centralizes UI components including `SeedSafeLogo`, `VaultItemCard`, `CopyButton`, `SecurityBadge`, `PasswordStrengthBar`, `PasswordGeneratorDialog`, `DeleteConfirmationDialog`, `MockBiometricDialog`, and `EmptyVaultState`.

### `com.example.ui.screens` (Screen Implementations)
* **`AuthAndOnboardingScreens.kt`**: `SplashScreen`, `OnboardingScreen`, `CreateVaultScreen`, `RecoveryPhraseConfirmationScreen`.
* **`UnlockScreen.kt`**: Custom 3x4 PIN keypad, biometric authentication trigger, forgot-PIN recovery link.
* **`MainVaultScreen.kt`**: Primary vault dashboard, quick category scroll, favorites, recent entries, Quick-Add FAB with bottom sheet.
* **`SearchScreen.kt`**: Full-text search field, category chips, recent search tags, live result list.
* **`FavoritesAndRecentsScreens.kt`**: `FavoritesScreen` and `RecentlyUsedScreen`.
* **`CategoryListScreen.kt`**: Category-specific list of credentials with header and entry action.
* **`ItemDetailScreen.kt`**: Comprehensive credential viewer with reveal toggles, copy actions, and deletion dialog.
* **`AddEditItemScreen.kt`**: Unified form for adding or modifying credentials across all 5 vault types.
* **`SecurityAndTrustScreens.kt`**: `SecurityCenterScreen` (password health audit) and `PrivacyTrustScreen` (architectural explanation).
* **`BackupAndRestoreScreens.kt`**: `BackupScreen` (Google Drive backup sync) and `RestoreScreen` (mnemonic phrase restore).
* **`SettingsScreen.kt`**: Complete vault configuration, security toggles, theme selector, and prototype reset.
