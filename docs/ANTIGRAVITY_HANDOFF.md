# SeedSafe Antigravity Engineering Handoff

This document serves as the primary handoff guide for senior Android engineers continuing development in Antigravity.

---

## 1. Executive Summary & Design Freeze Directive

The visual design, layout, interaction patterns, typography, and color palette of SeedSafe are **frozen at v0.1**.

> **CORE PRINCIPLE: PRESERVE THE APPROVED UI BASELINE**
> * Do NOT redesign the application or alter established component styles.
> * Do NOT introduce new top-level screens or unsolicited navigation sidebars/drawers.
> * Your objective is to replace the mock/prototype data layer with production-grade cryptography, hardware security, SQLCipher Room persistence, and Google Drive synchronization behind the existing UI interfaces.

---

## 2. Phased Antigravity Implementation Roadmap

```
┌──────────────────────────────────────────────────────────────────────────────────┐
│                             ANTIGRAVITY ROADMAP                                  │
├──────────────────────────────────────────────────────────────────────────────────┤
│ Phase 1: Local Storage Engine (Room Database + SQLCipher)                        │
│ Phase 2: Cryptographic Engine (BIP-39 Mnemonic + Argon2id KDF + AES-256-GCM)     │
│ Phase 3: Hardware Authentication (Android Keystore + BiometricPrompt)            │
│ Phase 4: Cloud Synchronization (Google Drive AppData Client-Side Encrypted Sync) │
│ Phase 5: System Credential Integration (Android AutofillService)                 │
│ Phase 6: Runtime Security Hardening (FLAG_SECURE, Ephemeral Memory Zeroization)  │
└──────────────────────────────────────────────────────────────────────────────────┘
```

### Phase 1: Local Storage Engine (Room + SQLCipher)
* **Objective**: Replace in-memory `_items` StateFlow with an encrypted local SQLite database.
* **Key Tasks**:
  1. Add dependencies: `androidx.room:room-runtime`, `androidx.room:room-ktx`, `net.zetetic:sqlcipher-android`.
  2. Implement `VaultItemEntity` and `VaultDao` following the schema defined in `UI_DATA_CONTRACT.md`.
  3. Create `VaultRepository` to bridge `VaultDao` and `VaultViewModel`.
  4. Ensure all CRUD operations emit reactive updates to the existing `items`, `favoriteItems`, and `recentlyAccessedItems` StateFlows.

### Phase 2: Cryptographic Engine
* **Objective**: Implement real master key derivation and authenticated encryption.
* **Key Tasks**:
  1. Replace static recovery phrase in `CreateVaultScreen` with a native BIP-39 mnemonic generator using `SecureRandom` 128-bit entropy.
  2. Implement BIP-39 phrase validation and checksum verification during `RestoreVault`.
  3. Derive Master Encryption Key (MEK) using Argon2id (or PBKDF2 with 600,000 rounds).
  4. Encrypt sensitive entry payloads using AES-256-GCM with unique 96-bit random initialization vectors (IVs) and 128-bit authentication tags.

### Phase 3: Hardware Authentication (Keystore & Biometrics)
* **Objective**: Hardware-backed key protection and seamless biometric unlock.
* **Key Tasks**:
  1. Generate an AES-256 key inside Android Keystore (`MasterKey` with StrongBox backing when available).
  2. Wrap the unlocked Master Encryption Key using the Keystore key.
  3. Implement `BiometricPrompt` with `BiometricPrompt.CryptoObject` in `UnlockScreen.kt`, eliminating `MockBiometricDialog`.
  4. Zeroize raw key byte arrays immediately upon vault lock or activity termination.

### Phase 4: Cloud Synchronization (Google Drive AppData)
* **Objective**: User-owned, client-side encrypted backups.
* **Key Tasks**:
  1. Configure Google Sign-In with `drive.appdata` scope (App Data folder is completely hidden from user's regular Google Drive).
  2. Implement backup export: create an AES-256-GCM encrypted envelope containing the database backup.
  3. Implement restoration flow: download file from AppData, verify auth tag with recovery phrase, and restore database.

### Phase 5: System Credential Integration (AutofillService)
* **Objective**: Auto-fill credentials in browsers and native Android apps.
* **Key Tasks**:
  1. Register an Android `AutofillService` in `AndroidManifest.xml`.
  2. Parse `AssistStructure` on `onFillRequest` to extract web domains and package names.
  3. Query decrypted vault credentials for domain matches.
  4. Return `Dataset` structures containing username and password representations.

### Phase 6: Runtime Security Hardening
* **Objective**: Protect against memory scraping, screen recording, and clipboard leaks.
* **Key Tasks**:
  1. Enforce `FLAG_SECURE` on sensitive windows (`CreateVault`, `RecoveryPhraseQuiz`, `Unlock`, `ItemDetail`).
  2. Tag sensitive clipboard copies with `ClipDescription.EXTRA_IS_SENSITIVE = true`.
  3. Schedule background worker to purge clipboard after timeout.
  4. Implement `ProcessLifecycleOwner` listener to lock vault after configured background duration.

---

## 3. Negative Constraints (What NOT to Do)

1. **NO Hardcoded Credentials or API Keys**:
   Never embed secret tokens in source code. Use AI Studio Secrets panel / `BuildConfig`.
2. **NO Plaintext Secrets in Storage or Logs**:
   Never log recovery words, PINs, passwords, or decrypted records.
3. **NO Unsolicited UI Expansions**:
   Keep the interface centered strictly on the 17 defined destinations. Do not introduce new sidebars, floating widgets, or third-party web views.
4. **NO Telemetry or Third-Party Analytics**:
   SeedSafe is an offline-first zero-knowledge vault. It must never embed tracking, ad SDKs, or analytics pings.
