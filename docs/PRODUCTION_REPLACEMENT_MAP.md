# SeedSafe Production Replacement Map

This document provides an explicit, module-by-module mapping from the current SeedSafe v0.1 UI/Mock baseline to the intended production implementation for the Antigravity engineering phase.

---

## 1. Architectural Replacement Matrix

| Subsystem | Baseline Mock Location | Production Implementation Target | Dependencies & Libraries |
|---|---|---|---|
| **Data Persistence** | `data/MockVaultData.kt`, `_items` StateFlow in `VaultViewModel.kt` | Room Database backed by SQLCipher, Repository Pattern, Room DAO | `androidx.room:room-runtime`, `androidx.room:room-ktx`, `net.zetetic:sqlcipher-android` |
| **Recovery Phrase Generation** | `MockVaultData.mockRecoveryPhrase` (hardcoded 12 words) | BIP-39 Mnemonic Generator using `SecureRandom` 128-bit entropy | `cash.z.ecc.android:bip39` or native BIP-39 Kotlin library |
| **Key Derivation (KDF)** | None / plain string matching in `attemptUnlockWithPin()` | Argon2id Key Derivation Function (or PBKDF2 with 600,000 iterations) | `de.mkammerer:argon2-jvm` or libsodium / Tink |
| **Payload Encryption** | Plaintext field storage in `VaultItem` objects | AES-256-GCM authenticated encryption envelope with unique 96-bit IVs | `com.google.crypto.tink:tink-android` or AndroidX Security Crypto |
| **Hardware Key Security** | In-memory `_unlockPin` string | Android Keystore System (`MasterKey` backed by StrongBox / TEE) | `androidx.security:security-crypto` |
| **Biometric Auth** | `MockBiometricDialog` simulated click in `CommonComponents.kt` | AndroidX `BiometricPrompt` with `BiometricPrompt.CryptoObject` | `androidx.biometric:biometric` |
| **Cloud Backup** | Coroutine delay simulation in `triggerManualBackup()` | Google Drive REST API targeting the hidden AppData folder (`drive.appdata`) | `com.google.android.gms:play-services-auth`, Google API Client Drive v3 |
| **Clipboard Security** | Plain `LocalClipboardManager` copy with Toast | Android 13+ Sensitive Content Flag (`EXTRA_IS_SENSITIVE`) + auto-clearing background coroutine | `android.content.ClipData`, `ClipDescription` |
| **Screen Protection** | Mock boolean toggle `_screenshotProtection` in Settings | Runtime window flag enforcement: `window.setFlags(FLAG_SECURE, FLAG_SECURE)` | Android Window Manager API |
| **System Autofill** | Not implemented (placeholder in UI) | Android `AutofillService` parsing dataset structures and heuristics | `android.service.autofill.AutofillService` |

---

## 2. Detailed Migration Blueprints

### Step 1: Replace Mock Data with Room + SQLCipher

* **Current Files**:
  * `app/src/main/java/com/example/data/MockVaultData.kt`
  * `app/src/main/java/com/example/state/VaultViewModel.kt` (lines 83-102)
* **Production Action**:
  1. Define Room Entity `VaultItemEntity` matching the relational schema in `UI_DATA_CONTRACT.md`.
  2. Create `VaultDao` with reactive `@Query` methods returning `Flow<List<VaultItemEntity>>`.
  3. Instantiate `Room.databaseBuilder` configured with `SupportOpenHelperFactory(passphrase)` using SQLCipher.
  4. Introduce `VaultRepositoryImpl` that converts encrypted entities into `VaultItem` domain models using the unlocked session key.
  5. Delete `MockVaultData.kt` completely.

### Step 2: Implement True BIP-39 & Argon2id Key Derivation

* **Current Files**:
  * `app/src/main/java/com/example/ui/screens/AuthAndOnboardingScreens.kt` (`CreateVaultScreen`)
  * `app/src/main/java/com/example/ui/screens/BackupAndRestoreScreens.kt` (`RestoreScreen`)
* **Production Action**:
  1. In `CreateVaultScreen`, replace the static list with a call to `MnemonicGenerator.generate(strength = 128)`.
  2. Implement BIP-39 checksum validation during phrase entry on `RestoreScreen`.
  3. Derive the vault Master Encryption Key (MEK) using Argon2id:
     * Memory cost: 64MB
     * Time cost: 3 iterations
     * Parallelism: 4 threads
     * Salt: Unique random 128-bit hardware-stored salt.

### Step 3: Implement Android Keystore & BiometricPrompt

* **Current Files**:
  * `app/src/main/java/com/example/ui/screens/UnlockScreen.kt`
  * `app/src/main/java/com/example/ui/components/CommonComponents.kt` (`MockBiometricDialog`)
* **Production Action**:
  1. Replace `MockBiometricDialog` with `BiometricPrompt.authenticate(promptInfo, CryptoObject(cipher))`.
  2. The `Cipher` must be initialized with an AES key generated in Android Keystore with `.setUserAuthenticationRequired(true)`.
  3. When biometrics succeed, Keystore permits cipher operation to decrypt the MEK stored in encrypted storage.
  4. The unlocked MEK is stored exclusively in ephemeral process memory (`ByteArray`) and zeroized on vault lock.

### Step 4: Implement Google Drive AppData Sync

* **Current Files**:
  * `app/src/main/java/com/example/ui/screens/BackupAndRestoreScreens.kt` (`BackupScreen`)
  * `app/src/main/java/com/example/state/VaultViewModel.kt` (`triggerManualBackup`)
* **Production Action**:
  1. Integrate Google Identity Services (`GoogleSignInOptions.DEFAULT_SIGN_IN` with scope `Scopes.DRIVE_APPFOLDER`).
  2. To create a backup:
     * Export the encrypted database file.
     * Encrypt with a key derived from the 12-word seed phrase + backup salt.
     * Upload the binary file to Google Drive's hidden `appDataFolder`.
  3. To restore:
     * Download the latest backup envelope from `appDataFolder`.
     * Decrypt using the derived seed phrase key.
     * Overwrite/initialize the local database.

### Step 5: Enforce Window `FLAG_SECURE` & Clipboard Auto-Clear

* **Current Files**:
  * `app/src/main/java/com/example/MainActivity.kt`
  * `app/src/main/java/com/example/ui/screens/SettingsScreen.kt`
* **Production Action**:
  1. In `MainActivity.onCreate()` and reactive to `viewModel.screenshotProtection`:
     ```kotlin
     if (screenshotProtection) {
       window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
     } else {
       window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
     }
     ```
  2. When copying credentials in `CopyButton`:
     ```kotlin
     val clipData = ClipData.newPlainText("password", textToCopy)
     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
       clipData.description.extras = PersistableBundle().apply {
         putBoolean(ClipDescription.EXTRA_IS_SENSITIVE, true)
       }
     }
     clipboardManager.setPrimaryClip(clipData)
     // Launch coroutine to clear clipboard after configured timeout (e.g. 30 seconds)
     ```
