# SeedSafe Security-Sensitive UI Policy & Guidelines

This document details the policies, constraints, and guidelines for handling sensitive security UI elements in SeedSafe.

---

## 1. Zero-Leakage Directive

> **MANDATORY SECURITY RULE:**
> Under no circumstances may passwords, master PINs, recovery seed phrases, cryptographic keys, card CVVs, or decrypted database entries be written to Android logcat, crash analytics, persistent unencrypted preferences, or debug dumps.

---

## 2. Window & Display Protection (`FLAG_SECURE`)

### Policy
When active, `WindowManager.LayoutParams.FLAG_SECURE` prevents the Android operating system and third-party applications from capturing screenshots, screen recordings, or displaying readable contents in the Android Recent Apps / Overview switcher.

### Enforcement Rules
1. **Always-On for Sensitive Destinations**:
   * `ScreenDestination.CreateVault` (Displays 12-word master recovery phrase).
   * `ScreenDestination.RecoveryPhraseQuiz` (Confirms master seed).
   * `ScreenDestination.Unlock` (PIN entry keypad).
   * `ScreenDestination.ItemDetail` (Decrypted passwords, CVVs, and notes).
2. **User-Configurable for General Vault**:
   * In `SettingsScreen`, the user can toggle "Screenshot & Recording Protection".
   * If disabled by user preference, `FLAG_SECURE` may be lifted on `Home`, `Search`, and `Settings`, but MUST remain strictly enforced whenever a raw password or recovery seed phrase is revealed.

---

## 3. Credential Obfuscation & Reveal Mechanics

### Default Masking State
* **Passwords**: Masked as `••••••••••••`. Must never display plaintext by default.
* **Payment Card Numbers**: Masked as `•••• •••• •••• 1234` (showing only last 4 digits).
* **Payment CVV**: Masked as `•••` or `••••`.
* **ID Numbers**: Masked with leading digits and trailing asterisks (e.g. `P123••••`).
* **Recovery Phrase Grid**: Rendered with an obfuscation overlay requiring explicit tap to reveal (`reveal_seed_btn`).

### Reveal Interaction Constraints
* Reveal toggles (Eye icon buttons) must require deliberate user taps.
* Toggled plaintext visibility must automatically reset to masked state whenever:
  * The screen loses focus (`onPause` / `onStop`).
  * The user navigates away from the item.
  * The vault auto-locks due to inactivity.

---

## 4. Secure Clipboard Management

### Android 13+ Sensitive Content Flagging
Whenever copying passwords, recovery phrases, or credit card numbers:
1. Attach `ClipDescription.EXTRA_IS_SENSITIVE = true` to `ClipData`. This prevents the Android OS from displaying sensitive clipboard preview overlays on the bottom edge of the screen.
2. Schedule a high-priority background coroutine or `WorkManager` job to clear the primary clip once the timeout expires (default: 30 seconds).
3. If the clipboard contents at timeout still match the copied secret, overwrite the clipboard with an empty string or generic non-sensitive string.

---

## 5. PIN Keypad & Lockout Safeguards

### Layout & Entry Security
* PIN indicators render filled dots rather than numbers.
* The keypad uses custom Compose touch targets rather than an Android system IME (keyboard) to prevent third-party keyboard keylogger interception.

### Failed Attempt & Lockout Logic
* **Counter**: Starts with 5 allowed PIN attempts (`_pinAttemptsRemaining = 5`).
* **Decrement**: Each incorrect PIN decrements the counter by 1 and renders a warning banner.
* **Lockout Enforcement**: When attempts reach 0, `isLockoutActive` turns `true`:
  * The numeric keypad is disabled.
  * An alert informs the user that the vault is locked.
  * The only recovery option is using the master 12-word recovery phrase via `ScreenDestination.RestoreVault`.
* **Prototype Demo Exception**: A dedicated reset button exists in the prototype to allow developer testing without losing state; in production, this is replaced with a cryptographic delay timer and wipe policy.

---

## 6. Truth in Advertising & Architectural Copy Standards

The application must never make unsupported absolute security claims (such as "unhackable" or "military-grade"). Instead, UI text must accurately describe the intended architecture:

* **DO NOT SAY**: *"Our military-grade AES-256 cipher makes your vault impenetrable."*
* **DO SAY**: *"Architected to encrypt your data locally on your device before synchronization."*
* **DO NOT SAY**: *"Google cannot ever see your vault."*
* **DO SAY**: *"Your vault is designed to be encrypted using your recovery phrase directly on your device before synchronization. Neither Google nor third parties can inspect your secrets."*
