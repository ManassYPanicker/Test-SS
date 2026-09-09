# SeedSafe Navigation Specification

This document details the complete navigation architecture, destination models, backstack behavior, modal and dialog transitions, and desired handling for unsaved form state for SeedSafe.

---

## 1. Destination Architecture

All application destinations are strongly typed and managed via the `ScreenDestination` sealed interface in `app/src/main/java/com/example/state/VaultViewModel.kt`:

```kotlin
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
```

---

## 2. Navigation State & Backstack Management

Navigation state is encapsulated inside `VaultViewModel`:

* `_currentDestination`: `MutableStateFlow<ScreenDestination>` exposed as an immutable `StateFlow`.
* `navigationStack`: Internal `mutableListOf<ScreenDestination>` operating as a LIFO backstack history.
* **Stack Transition Methods**:
  * `navigateTo(destination: ScreenDestination)`: If `destination` differs from the current destination, appends the current screen to `navigationStack` and emits `destination`.
  * `navigateBack() / popBackStack()`: Removes and returns the top entry from `navigationStack`, transitioning to the previous destination. If the stack is empty, defaults to `ScreenDestination.Home`.
  * `resetToHome()`: Clears the entire stack and sets `Home` as the sole root destination (used upon successful unlock, vault creation, or restore).
  * `resetToOnboarding()`: Clears the stack and navigates to `Onboarding` with `hasCompletedOnboarding = false` (used when resetting or purging the vault).

### System Back-Button & Gesture Navigation Interception

Hardware back buttons and edge-swipe gestures are intercepted in `MainActivity.kt` using AndroidX's Compose `BackHandler`:

```kotlin
BackHandler(
    enabled = currentDestination !is ScreenDestination.Home &&
              currentDestination !is ScreenDestination.Splash
) {
  // Dispatches popBackStack() unless intercepted by an active form / modal
  viewModel.popBackStack()
}
```

---

## 3. Global Navigation Graph

```
                    [ App Launch ]
                          │
                          ▼
                 ScreenDestination.Splash
                          │
          ┌───────────────┴───────────────┐
          │ (First Launch)                │ (Returning User)
          ▼                               ▼
ScreenDestination.Onboarding     ScreenDestination.Unlock
          │                               │ (Auth Success)
          ├──► ScreenDestination.Restore  │
          │                               ▼
          ▼                      ScreenDestination.Home ◄───┐
ScreenDestination.CreateVault             │                 │
          │                               ├──► Search       │
          ▼                               ├──► Favorites    │
ScreenDestination.RecoveryPhraseQuiz      ├──► Settings ────┤
          │ (Quiz Passed)                 │                 │
          └───────────────────────────────┴─────────────────┘
```

### Main Hub Routes (from `Home`)

| Source | Trigger | Destination | Passing Arguments |
|---|---|---|---|
| `Home` | Search Bar Card | `Search` | None |
| `Home` | Category Card | `CategoryList` | `category: VaultCategory` |
| `Home` | Vault Item Card | `ItemDetail` | `itemId: String` |
| `Home` | Security Center Widget | `SecurityCenter` | None |
| `Home` | "See All" Favorites | `Favorites` | None |
| `Home` | "See All" Recents | `RecentlyUsed` | None |
| `Home` | Quick-Add FAB -> Option | `AddEditItem` | `category: VaultCategory` |
| `Home` | Top Bar Lock Button | `Unlock` | None (Locks vault, clears session key) |

### Detail & Edit Routes

| Source | Trigger | Destination | Back Target |
|---|---|---|---|
| `ItemDetail` | Top Bar Edit Button | `AddEditItem` | Pops back to `ItemDetail` |
| `ItemDetail` | Delete Confirmation | Pops back | Returns to parent list (`Home` / `CategoryList` / `Favorites`) |
| `AddEditItem` | Top Bar Back / Cancel | Pops back | Returns to caller (prompts if dirty) |
| `AddEditItem` | Save Button | Pops back | Commits changes and returns to caller |

### Settings Sub-Routes

| Source | Trigger | Destination | Notes |
|---|---|---|---|
| `Settings` | Cloud Backup Row | `BackupManagement` | Configures Google Drive sync & local export |
| `Settings` | Restore Vault Row | `RestoreVault` | Restores vault via phrase or cloud backup |
| `Settings` | Security Audit Row | `SecurityCenter` | Weak & reused password audit |
| `Settings` | Cryptographic Trust Row | `PrivacyTrust` | Zero-knowledge architecture & proofs |
| `Settings` | View Recovery Phrase | `CreateVault` | Shows master recovery seed |
| `Settings` | Reset to Onboarding | `Onboarding` | Wipes local keys & returns to initial setup |

---

## 4. Back-Navigation Behavior Rules

1. **Top-Level Destinations (`Home`, `Search`, `Favorites`, `Settings`)**:
   - Tapping the active bottom navigation tab scrolls to the top of that destination.
   - Pressing the hardware/gesture back button while on `Search`, `Favorites`, or `Settings` transitions back to `Home`.
   - Pressing back while on `Home` minimizes/exits the application to the Android launcher (`BackHandler` is disabled on `Home`).
2. **Authentication Guard (`UnlockScreen`)**:
   - The user cannot navigate backward out of `UnlockScreen` into the vault. Pressing back while locked exits the application.
3. **Onboarding & Vault Initialization**:
   - During `CreateVault` and `RecoveryPhraseQuiz`, back navigation returns to the preceding step (`CreateVault` -> `Onboarding`, `RecoveryPhraseQuiz` -> `CreateVault`).
   - Upon completing the quiz, `resetToHome()` is invoked, purging onboarding steps from the backstack so pressing back cannot re-open the unencrypted seed phrase.
4. **Detail & Category Sub-Screens**:
   - Navigating to `CategoryList` or `ItemDetail` preserves the previous destination in `navigationStack`. Calling `popBackStack()` returns to the exact prior view with scroll state preserved.
   - Deleting an item from `ItemDetail` automatically pops the backstack back to the containing list.

---

## 5. Bottom Navigation Bar Policy

The bottom navigation bar is hosted inside `MainActivity.kt`'s root `Scaffold`.

### Visibility Matrix

The bottom navigation bar is visible **only** when authenticated and on top-level hub screens:

| Destination | Bottom Bar Visible? |
|---|---|
| `Splash` | ❌ Hidden |
| `Onboarding` | ❌ Hidden |
| `CreateVault` | ❌ Hidden |
| `RecoveryPhraseQuiz` | ❌ Hidden |
| `Unlock` | ❌ Hidden |
| `Home` | ✅ **Visible** (Tab: "Vault") |
| `Search` | ✅ **Visible** (Tab: "Search") |
| `Favorites` | ✅ **Visible** (Tab: "Favorites") |
| `Settings` | ✅ **Visible** (Tab: "Settings") |
| `CategoryList` | ❌ Hidden (Dedicated back-arrow top app bar) |
| `ItemDetail` | ❌ Hidden (Dedicated action bar) |
| `AddEditItem` | ❌ Hidden (Modal full-screen editor) |
| `SecurityCenter` | ❌ Hidden (Dedicated sub-screen) |
| `BackupManagement` | ❌ Hidden (Dedicated sub-screen) |
| `RestoreVault` | ❌ Hidden (Dedicated sub-screen) |
| `PrivacyTrust` | ❌ Hidden (Dedicated sub-screen) |
| `RecentlyUsed` | ❌ Hidden (Dedicated sub-screen) |

Implementation check in `MainActivity.kt`:
```kotlin
val showBottomBar = currentDestination is ScreenDestination.Home ||
                    currentDestination is ScreenDestination.Search ||
                    currentDestination is ScreenDestination.Favorites ||
                    currentDestination is ScreenDestination.Settings
```

---

## 6. Modal Sheets & Dialog Transitions

Overlay dialogs and bottom sheets exist as composable state layers rendered on top of the host screen without creating new routes in `navigationStack`:

1. **Quick-Add Modal Bottom Sheet** (`MainVaultScreen`):
   - **Trigger**: `main_quick_add_fab` floating action button.
   - **Transition**: Slides up from screen bottom with spring physics and semi-transparent scrim (`Color.Black.copy(alpha = 0.5f)`).
   - **Dismissal**: Tapping scrim, dragging down, or selecting one of the 5 entry types (`LOGIN`, `NOTE`, `ID_CARD`, `PAYMENT_CARD`, `DOCUMENT`).
   - **Action**: Selecting a type dismisses the sheet and initiates `navigateTo(ScreenDestination.AddEditItem(category))`.
2. **Password Generator Modal Dialog** (`AddEditItemScreen` / `CommonComponents`):
   - **Trigger**: Casino/dice icon button (`generate_password_btn`).
   - **Transition**: Scaled pop-in dialog with background dimming.
   - **Controls**: Length slider (8–32 chars), toggles for uppercase, lowercase, numbers, and symbols, plus regenerator button.
   - **Action**: Clicking "Use Password" fills the active password field and dismisses the dialog.
3. **Destructive Delete Confirmation Dialog** (`ItemDetailScreen`):
   - **Trigger**: Top bar trash icon button (`delete_item_top_btn`) or bottom actions.
   - **Transition**: Material 3 Alert Dialog with red warning accents (`RoseError`).
   - **Actions**: "Cancel" dismisses without side effects; "Delete Permanently" (`confirm_delete_btn`) removes the item from the repository, clears cached clipboard data, and triggers `popBackStack()`.
4. **Biometric Simulator Dialog** (`UnlockScreen`):
   - **Trigger**: Keypad biometric button (`unlock_biometric_btn`).
   - **Transition**: Material 3 Alert Dialog displaying fingerprint icon and simulator controls.
   - **Actions**: "Simulate Biometric Match" authenticates directly; "Use PIN Fallback" cancels the dialog and refocuses PIN dots.
5. **Change Master PIN Dialog** (`SettingsScreen`):
   - **Trigger**: Master PIN settings row (`master_pin_setting_row`).
   - **Transition**: Modal dialog requesting confirmation of new 4-digit PIN with validation against simple sequential digits (e.g., `1234`).
6. **Auto-Lock & Clipboard Timeout Selection Dialogs** (`SettingsScreen`):
   - **Trigger**: Auto-lock and clipboard timeout settings rows.
   - **Transition**: Single-choice radio list dialog with immediate selection feedback.

---

## 7. Desired Behavior for Unsaved Form State

In security-focused applications like SeedSafe, managing unsaved form state requires balancing data preservation with sensitive credential hygiene.

### 1. Dirty Form Tracking (`AddEditItemScreen`)
A form is marked **dirty** (`isDirty = true`) when any input field deviates from its initial state:
* Title, Username, Password, URL, Notes, or custom category-specific attributes (Card Number, CVV, Expiry, ID Number, Document Name).
* When creating a new item: any non-empty field marks the form dirty.
* When editing an existing item: any field differing from `initialItem` marks the form dirty.

### 2. Back Navigation Interception
When the user attempts to exit an active `AddEditItemScreen` (via the TopAppBar back arrow, cancel button, or system back gesture):
* **If `!isDirty`**: The screen dismisses immediately via `popBackStack()` with no prompt.
* **If `isDirty`**: The exit attempt is intercepted by `BackHandler(enabled = isDirty)`. A confirmation dialog is presented:
  - **Title**: "Discard Unsaved Changes?"
  - **Message**: "You have unsaved changes in this vault entry. If you leave now, your changes will be discarded."
  - **Actions**:
    - **"Keep Editing"** (`dismiss`): Closes dialog; user remains on the form with all input preserved.
    - **"Discard Changes"** (`confirm`): Wipes temporary input buffers from memory and executes `popBackStack()`.

### 3. Memory & Security Sanitization on Exit
* Upon form dismissal or cancellation, all state strings holding sensitive secrets (passwords, PINs, card numbers) are cleared to prevent lingering plaintext in heap memory.
* If the app is sent to the background while editing, `FLAG_SECURE` window protection remains actively enforced, shielding uncommitted passwords from recents screenshots.
* If an auto-lock event triggers while a form has uncommitted changes, the app immediately locks and transitions to `UnlockScreen`. Unsaved credential buffers are wiped to ensure zero unauthenticated exposure.

### 4. Successful Commit
* Tapping **Save** (`save_item_top_btn` / `bottom_save_item_btn`):
  - Validates required fields (e.g. non-empty title).
  - Encrypts and writes the entry to the vault repository.
  - Updates category counts and security scores.
  - Sets `isDirty = false` and pops back to the previous screen without confirmation.

