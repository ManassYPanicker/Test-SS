# SeedSafe Design System Specification

This document details the visual design system, color palette, typography hierarchy, component library, and spacing rules for SeedSafe v0.1.

---

## 1. Visual Identity & Brand Philosophy

SeedSafe is designed to convey **absolute cryptographic sovereignty, clarity, and precision**. 
The visual language pairs deep vault slate tones with a vivid, high-trust emerald primary accent (`#00D2A0`), supported by cyan, amber, and rose status accents.

---

## 2. Color System

### Brand & Semantic Accents

| Token | Hex | Dark Mode Role | Light Mode Role |
|---|---|---|---|
| `EmeraldPrimary` | `#00D2A0` | Primary brand CTA, active tabs, strong security indicators | High-contrast accent |
| `EmeraldPrimaryDark` | `#00A880` | Accent hover/press state | Primary button and header accent |
| `EmeraldOnPrimary` | `#003324` | Text/icon color on primary emerald button containers | Same |
| `EmeraldContainerDark` | `#00382B` | Low-opacity background container tint | Secondary badge tint |
| `EmeraldContainerLight` | `#E6FBF5` | Light mode badge fill | Soft background badge container |
| `CyanAccent` | `#0EA5E9` | Technical identifiers, documents, secondary links | Secondary accent |
| `AmberWarning` | `#F59E0B` | Security warnings, fair password score, notes icon | Warnings & starred favorites |
| `RoseError` | `#F43F5E` | Critical alerts, weak passwords, destructive actions | Errors & deletions |
| `BlueShield` | `#3B82F6` | Identity items, ID cards, security center badge | ID card category tint |
| `PurpleKey` | `#A855F7` | Payment cards, financial secrets | Payment card category tint |

### Surface & Canvas Tokens

#### Dark Vault Palette (Default Theme)
* `VaultDarkBg`: `#0A0F1D` (Deep oceanic obsidian canvas)
* `VaultDarkSurface`: `#111827` (Card containers, bottom sheets, navigation surfaces)
* `VaultDarkSurfaceVariant`: `#1F2937` (Interactive item card containers, text fields)
* `VaultDarkSurfaceHighlight`: `#283548` (Selected states, elevated surfaces)
* `VaultDarkBorder`: `#2E3D52` (Subtle 1dp card borders, dividers)
* `TextDarkPrimary`: `#F9FAFB` (High-contrast titles, values, headlines)
* `TextDarkSecondary`: `#9CA3AF` (Subtitles, metadata, timestamps)
* `TextDarkMuted`: `#6B7280` (Footnotes, disabled states, placeholders)

#### Light Modern Slate Palette
* `VaultLightBg`: `#F8FAFC` (Clean slate off-white canvas)
* `VaultLightSurface`: `#FFFFFF` (Elevated card containers)
* `VaultLightSurfaceVariant`: `#F1F5F9` (Group containers, input fields)
* `VaultLightBorder`: `#E2E8F0` (Divider lines, outlines)
* `TextLightPrimary`: `#0F172A` (Headings, primary values)
* `TextLightSecondary`: `#475569` (Body descriptions, secondary labels)
* `TextLightMuted`: `#94A3B8` (Hints, timestamps)

---

## 3. Typography Scale

Typography is mapped to Android Material Design 3 type scales:

| Style Role | Font Weight | Letter Spacing | Typical Application |
|---|---|---|---|
| `displayLarge` / `headlineSmall` | Bold (700) | Normal | Onboarding hero headlines, lock screen title |
| `titleLarge` | Bold (700) | `0.5.sp` | Modal bottom sheet titles, dialog titles |
| `titleMedium` | Bold / SemiBold (600) | Normal | Screen headers, category titles, card headers |
| `titleSmall` | SemiBold (600) | Normal | Section subtitles, alert card titles |
| `bodyLarge` | Medium / SemiBold | Normal | Primary vault item titles, input field values |
| `bodyMedium` | Normal (400) | Normal | Descriptive copy, card subtitles, dialog bodies |
| `bodySmall` | Normal (400) | Normal | Timestamps, supporting hints, secondary details |
| `labelSmall` | SemiBold (600) | `1.5.sp` (Uppercase) | "ZERO-KNOWLEDGE VAULT" brand subtitles, badges |
| `Monospace` | Medium / Bold | Normal | Recovery seed words, passwords, PIN digits, CVV |

---

## 4. Reusable Component Catalog

All shared UI components are located in `app/src/main/java/com/example/ui/components/CommonComponents.kt`.

### 1. `SeedSafeLogo`
* **Parameters**: `modifier`, `size: Int = 56`, `showText: Boolean = false`
* **Visuals**: Rounded rectangular badge with multi-stop linear gradient (`#0F2027` -> `#203A43` -> `#004D40`) framed by a glowing emerald/cyan gradient border. Centers a dual-layer Shield and Lock glyph.
* **Usage**: Splash, Onboarding, Lock screen, Top App Bar.

### 2. `VaultItemCard`
* **Parameters**: `item: VaultItem`, `onClick: () -> Unit`, `onFavoriteToggle: () -> Unit`
* **Visuals**: 16dp rounded card with subtle container background. Left: category-tinted icon badge (44dp). Center: title, formatted category-specific subtitle (e.g. masked card number or username), and last updated timestamp. Right: star favorite toggle button.

### 3. `CopyButton`
* **Parameters**: `textToCopy: String`, `label: String = "Copied to clipboard"`
* **Visuals**: IconButton displaying `Icons.Default.ContentCopy`. Upon click, commits text to `LocalClipboardManager` and triggers an Android Toast notification.

### 4. `SecurityBadge`
* **Parameters**: `text: String`, `icon: ImageVector`, `color: Color`
* **Visuals**: Compact rounded chip (8dp) with 12% alpha tinted background and 25% alpha outline. Displays mini icon (13dp) and uppercase label text.

### 5. `PasswordStrengthBar`
* **Parameters**: `password: String`
* **Visuals**: Evaluates password length and composition in real time (Weak: Rose, Fair: Amber, Strong: Cyan, Excellent: Emerald). Renders a smooth animated progress bar (height 6dp) with percentage fill and strength label.

### 6. `PasswordGeneratorDialog`
* **Parameters**: `onDismiss: () -> Unit`, `onPasswordGenerated: (String) -> Unit`
* **Visuals**: Material AlertDialog displaying monospace generated password, refresh icon button, length slider (8 to 32 characters), and toggle checkboxes for uppercase, numbers, and symbols.

### 7. `DeleteConfirmationDialog`
* **Parameters**: `itemTitle: String`, `onConfirm: () -> Unit`, `onDismiss: () -> Unit`
* **Visuals**: Security dialog explicitly reminding the user that deletion is irreversible because SeedSafe does not keep central backups. Confirms via a `RoseError` destructive button.

### 8. `MockBiometricDialog`
* **Parameters**: `onSuccess: () -> Unit`, `onDismiss: () -> Unit`, `onPinFallback: () -> Unit`
* **Visuals**: Centered modal card featuring biometric fingerprint glyph, explanatory prompt, "Simulate Biometric Match" CTA, and "Use PIN Fallback" option.

### 9. `EmptyVaultState`
* **Parameters**: `title: String`, `subtitle: String`, `icon: ImageVector`, `actionLabel: String?`, `onAction: (() -> Unit)?`
* **Visuals**: Centered circular 80dp glyph container, title, subtitle, and optional primary action button for empty lists and search queries.

---

## 5. Spacing, Layout & Accessibility Grid

* **Base Grid**: Strict 8.dp / 4.dp spatial cadence.
* **Screen Padding**:
  * Outer margin: `20.dp` (horizontal) on handhelds.
  * Screen-edge list spacing: `verticalArrangement = Arrangement.spacedBy(16.dp)`.
* **Touch Targets**: All interactive icons (`IconButton`), chips, and buttons enforce a minimum touch target of `48.dp x 48.dp` (`minimumInteractiveComponentSize`).
* **Card Padding**:
  * Internal padding: `14.dp` to `16.dp`.
  * Corner radius: Standard `16.dp` (`RoundedCornerShape(16.dp)`).
* **Dynamic Sizing**: Content uses `fillMaxWidth()` and is constrained for larger displays using adaptive wrappers to prevent disproportionate element stretching.
