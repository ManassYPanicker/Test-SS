# SeedSafe Test Tag Reference Specification

This document provides a comprehensive directory of all Compose `Modifier.testTag` identifiers used throughout SeedSafe v0.1 for automated UI testing (Robolectric, Roborazzi, and Espresso).

---

## 1. Test Tag Naming Convention

SeedSafe enforces a strict `snake_case` semantic prefix convention:

| Prefix | Semantic Target | Example |
|---|---|---|
| `nav_tab_` | Bottom navigation bar items | `nav_tab_vault`, `nav_tab_settings` |
| `pin_key_` | Numeric PIN keypad digits | `pin_key_1`, `pin_key_0` |
| `unlock_` | Vault unlock actions | `unlock_biometric_btn`, `unlock_key_backspace` |
| `cat_card_` | Category selector cards | `cat_card_LOGIN`, `cat_card_NOTE` |
| `quick_add_option_` | Entry type modal sheet options | `quick_add_option_LOGIN` |
| `vault_item_` | Vault item cards | `vault_item_login-1` |
| `fav_btn_` | Item row favorite star buttons | `fav_btn_login-1` |
| `copy_btn_` | Clipboard copy buttons | `copy_btn_123456` |
| `search_` | Search input & filter chips | `search_input_field`, `search_filter_chip_LOGIN` |
| `settings_` | Settings controls & switches | `settings_biometrics_switch` |
| `add_item_` | Add/Edit input fields | `add_item_title_input`, `add_item_password_input` |

---

## 2. Complete Test Tag Inventory

### Navigation & Shell
| Test Tag | Screen / Location | Element Description |
|---|---|---|
| `nav_tab_vault` | `MainActivity` | Bottom navigation "Vault" tab |
| `nav_tab_search` | `MainActivity` | Bottom navigation "Search" tab |
| `nav_tab_favorites` | `MainActivity` | Bottom navigation "Favorites" tab |
| `nav_tab_settings` | `MainActivity` | Bottom navigation "Settings" tab |

### Onboarding & Vault Initialization
| Test Tag | Screen / Location | Element Description |
|---|---|---|
| `onboarding_next_btn` | `OnboardingScreen` | Advance to next onboarding carousel step |
| `create_vault_cta_btn` | `OnboardingScreen` | Primary CTA to create a new vault |
| `restore_vault_cta_btn` | `OnboardingScreen` | Secondary CTA to restore an existing vault |
| `create_vault_back_btn` | `CreateVaultScreen` | Top bar back button |
| `create_vault_refresh_btn` | `CreateVaultScreen` | Generate alternative recovery phrase |
| `reveal_seed_btn` | `CreateVaultScreen` | Tap to reveal 12-word seed phrase overlay |
| `copy_seed_btn` | `CreateVaultScreen` | Copy entire 12-word recovery phrase |
| `verify_recovery_phrase_btn` | `CreateVaultScreen` | Advance to recovery phrase confirmation quiz |
| `quiz_confirm_selection_btn` | `RecoveryPhraseConfirmationScreen` | Confirm chosen word in quiz |
| `enter_vault_btn` | `RecoveryPhraseConfirmationScreen` | Final CTA upon completing quiz |

### Vault Authentication & Unlock
| Test Tag | Screen / Location | Element Description |
|---|---|---|
| `pin_key_0` .. `pin_key_9` | `UnlockScreen` | Keypad numeric digit buttons |
| `unlock_key_backspace` | `UnlockScreen` | Keypad backspace button |
| `unlock_biometric_btn` | `UnlockScreen` | Keypad biometric trigger button |
| `unlock_forgot_pin_btn` | `UnlockScreen` | "Forgot PIN?" recovery link |
| `simulate_biometric_success` | `MockBiometricDialog` | Simulate biometric fingerprint match |
| `biometric_fallback_pin_btn` | `MockBiometricDialog` | Switch from biometric to PIN keypad |
| `biometric_cancel_btn` | `MockBiometricDialog` | Dismiss biometric prompt |

### Main Vault Dashboard
| Test Tag | Screen / Location | Element Description |
|---|---|---|
| `top_bar_lock_vault_btn` | `MainVaultScreen` | Top bar button to lock vault immediately |
| `home_search_bar_cta` | `MainVaultScreen` | Search bar card opening Search screen |
| `home_security_center_widget` | `MainVaultScreen` | Security health card opening Security Center |
| `cat_card_LOGIN` | `MainVaultScreen` | Category card: Logins |
| `cat_card_NOTE` | `MainVaultScreen` | Category card: Secure Notes |
| `cat_card_ID_CARD` | `MainVaultScreen` | Category card: ID Cards |
| `cat_card_PAYMENT_CARD` | `MainVaultScreen` | Category card: Payment Cards |
| `cat_card_DOCUMENT` | `MainVaultScreen` | Category card: Documents |
| `main_quick_add_fab` | `MainVaultScreen` | Floating Action Button opening Quick-Add sheet |
| `quick_add_option_LOGIN` | Quick-Add Sheet | New Login entry option |
| `quick_add_option_NOTE` | Quick-Add Sheet | New Note entry option |
| `quick_add_option_ID_CARD` | Quick-Add Sheet | New ID Card entry option |
| `quick_add_option_PAYMENT_CARD` | Quick-Add Sheet | New Payment Card entry option |
| `quick_add_option_DOCUMENT` | Quick-Add Sheet | New Document entry option |
| `vault_item_<id>` | Any list | Dynamic vault item card row (e.g. `vault_item_login-1`) |
| `fav_btn_<id>` | Any list item | Dynamic star button to toggle favorite status |

### Search Screen
| Test Tag | Screen / Location | Element Description |
|---|---|---|
| `search_input_field` | `SearchScreen` | Text search input field |
| `clear_search_btn` | `SearchScreen` | Clear current search query button |
| `search_filter_chip_ALL` | `SearchScreen` | Filter chip: All |
| `search_filter_chip_LOGIN` | `SearchScreen` | Filter chip: Logins |
| `search_filter_chip_NOTE` | `SearchScreen` | Filter chip: Secure Notes |
| `search_filter_chip_ID_CARD` | `SearchScreen` | Filter chip: ID Cards |
| `search_filter_chip_PAYMENT_CARD` | `SearchScreen` | Filter chip: Payment Cards |
| `search_filter_chip_DOCUMENT` | `SearchScreen` | Filter chip: Documents |
| `clear_recent_searches_btn` | `SearchScreen` | Clear all recent search queries |

### Category & Favorites Screens
| Test Tag | Screen / Location | Element Description |
|---|---|---|
| `category_list_back_btn` | `CategoryListScreen` | Navigation back button |
| `category_list_add_btn` | `CategoryListScreen` | Add entry within current category |
| `favorites_back_btn` | `FavoritesScreen` | Navigation back button |
| `recents_back_btn` | `RecentlyUsedScreen` | Navigation back button |

### Item Detail Screen
| Test Tag | Screen / Location | Element Description |
|---|---|---|
| `item_detail_back_btn` | `ItemDetailScreen` | Top bar back button |
| `item_detail_favorite_btn` | `ItemDetailScreen` | Top bar favorite toggle button |
| `edit_item_top_btn` | `ItemDetailScreen` | Top bar edit entry button |
| `delete_item_top_btn` | `ItemDetailScreen` | Top bar delete entry button |
| `confirm_delete_btn` | `DeleteConfirmationDialog` | Confirm permanent deletion button |

### Add / Edit Item Screen
| Test Tag | Screen / Location | Element Description |
|---|---|---|
| `add_edit_cancel_btn` | `AddEditItemScreen` | Top bar cancel/back button |
| `save_vault_item_btn` | `AddEditItemScreen` | Top bar save button |
| `add_item_title_input` | `AddEditItemScreen` | Entry title text field |
| `add_item_username_input` | `AddEditItemScreen` | Username / email text field |
| `add_item_password_input` | `AddEditItemScreen` | Password text field |
| `add_item_note_input` | `AddEditItemScreen` | Secure note multiline body field |
| `generate_password_btn` | `AddEditItemScreen` | Launch password generator dialog |
| `toggle_password_visibility_btn` | `AddEditItemScreen` | Toggle password plaintext visibility |

### Security Center & Trust
| Test Tag | Screen / Location | Element Description |
|---|---|---|
| `security_center_back_btn` | `SecurityCenterScreen` | Navigation back button |
| `security_fix_item_<id>` | `SecurityCenterScreen` | Button to edit/fix flagged weak credential |
| `privacy_trust_back_btn` | `PrivacyTrustScreen` | Navigation back button |

### Backup & Restore Screens
| Test Tag | Screen / Location | Element Description |
|---|---|---|
| `backup_screen_back_btn` | `BackupScreen` | Navigation back button |
| `backup_toggle_switch` | `BackupScreen` | Switch enabling cloud backup synchronization |
| `manual_backup_now_btn` | `BackupScreen` | Button triggering immediate backup sync |
| `simulate_backup_failure_btn` | `BackupScreen` | Prototype simulation of backup error |
| `restore_screen_back_btn` | `RestoreScreen` | Navigation back button |
| `restore_submit_btn` | `RestoreScreen` | Primary button triggering vault restoration |

### Settings Screen
| Test Tag | Screen / Location | Element Description |
|---|---|---|
| `settings_biometrics_switch` | `SettingsScreen` | Biometric authentication toggle switch |
| `settings_change_pin_row` | `SettingsScreen` | Row launching master PIN change dialog |
| `settings_screenshot_protection_switch` | `SettingsScreen` | Screen recording & screenshot protection switch |
| `settings_wipe_failed_switch` | `SettingsScreen` | Wipe on failed attempts switch |
| `settings_backup_row` | `SettingsScreen` | Navigate to Cloud Backup Management |
| `settings_restore_row` | `SettingsScreen` | Navigate to Restore Vault screen |
| `settings_security_center_row` | `SettingsScreen` | Navigate to Security Center |
| `settings_privacy_trust_row` | `SettingsScreen` | Navigate to Privacy & Trust |
| `settings_recovery_phrase_row` | `SettingsScreen` | View recovery phrase screen |
| `settings_reset_onboarding_btn` | `SettingsScreen` | Reset prototype to first launch state |
