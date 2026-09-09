# SeedSafe Entry Types Specification

This document details the 5 core credential entry types supported in SeedSafe v0.1, including their domain fields, validation rules, category branding, and specialized UI presentations.

---

## 1. Entry Type Summary Matrix

| Category Enum | Display Title | Theme Accent | Icon | Subtitle Display Pattern | Key Primary Fields |
|---|---|---|---|---|---|
| `LOGIN` | Logins | `EmeraldPrimary` (`#00D2A0`) | `Key` | Username or email | Username, Password, Website URL |
| `NOTE` | Secure Notes | `AmberWarning` (`#F59E0B`) | `Note` | First line of memo body | Content (multiline text) |
| `ID_CARD` | ID Numbers | `BlueShield` (`#3B82F6`) | `Badge` | Type & masked ID number | ID Type, ID Number, Issuing Country |
| `PAYMENT_CARD` | Cards | `PurpleKey` (`#A855F7`) | `CreditCard` | Masked digits & expiration | Cardholder, Number, Expiry, CVV |
| `DOCUMENT` | Documents | `CyanAccent` (`#0EA5E9`) | `Description` | File type & file size | File Name, File Type, File Size |

---

## 2. Detailed Field Specifications by Entry Type

### 1. Login Item (`VaultCategory.LOGIN`)

Used for website logins, mobile app credentials, and server accounts.

* **Common Fields**:
  * `id: String` (UUID v4)
  * `title: String` (Required; e.g. "Google Account", "GitHub", "AWS Console")
  * `isFavorite: Boolean` (Default `false`)
  * `updatedAt: String` (Timestamp)
  * `notes: String` (Optional supplementary notes/instructions)
* **Type-Specific Fields**:
  * `username: String` (Username, handle, or email address)
  * `password: String` (Encrypted secret password)
  * `website: String` (Optional domain or URL, e.g. `https://accounts.google.com`)
  * `serviceBadge: String` (Optional branding glyph/text)
* **Form & Validation Rules**:
  * `title` cannot be blank.
  * Embedded password generator available with length slider (8-32 chars) and character options.
  * Real-time password strength meter (`PasswordStrengthBar`).
* **Detail Presentation**:
  * Username row with single-tap copy button.
  * Password row with eye reveal toggle, copy button, and strength bar.
  * Website row with browser launch external link action.

---

### 2. Secure Note Item (`VaultCategory.NOTE`)

Used for encrypted private memos, software license keys, SSH keys, seed phrases, or private notes.

* **Common Fields**: `id`, `title`, `isFavorite`, `updatedAt`, `notes`.
* **Type-Specific Fields**:
  * `content: String` (Required; arbitrary multiline text)
* **Form & Validation Rules**:
  * `title` cannot be blank.
  * `content` text area provides 200dp default vertical space with auto-wrapping.
* **Detail Presentation**:
  * Monospace rendered text card with copy button in header.
  * Preserves line breaks, indentation, and formatting.

---

### 3. ID Card Item (`VaultCategory.ID_CARD`)

Used for sensitive personal identification documents.

* **Common Fields**: `id`, `title`, `isFavorite`, `updatedAt`, `notes`.
* **Type-Specific Fields**:
  * `idType: String` (Dropdown selection: "Passport", "Driver's License", "Social Security / SSN", "National ID", "Tax ID")
  * `idNumber: String` (Required; official identification number)
  * `expiryDate: String` (Optional expiration date, e.g. "2032-08-15")
  * `issuingCountry: String` (Optional jurisdiction/country, e.g. "United States", "Germany")
* **Form & Validation Rules**:
  * `title` and `idNumber` cannot be blank.
* **Detail Presentation**:
  * Card header showing ID Type and Country badge.
  * ID Number row masked by default with reveal toggle and copy button.
  * Expiration date and validity status badge.

---

### 4. Payment Card Item (`VaultCategory.PAYMENT_CARD`)

Used for physical and virtual payment instruments (credit cards, debit cards, corporate expense cards).

* **Common Fields**: `id`, `title`, `isFavorite`, `updatedAt`, `notes`.
* **Type-Specific Fields**:
  * `cardholderName: String` (Name printed on card)
  * `cardNumber: String` (15-16 digit card number formatted with space grouping)
  * `expiry: String` (Expiration date in `MM/YY` format)
  * `cvv: String` (3 or 4 digit security verification code)
  * `cardType: String` (Card network: "Visa", "Mastercard", "Amex", "Discover")
* **Form & Validation Rules**:
  * Auto-detects card network from leading digits (e.g. 4 -> Visa, 5 -> Mastercard, 37 -> Amex).
  * Enforces `MM/YY` format pattern.
* **Detail Presentation**:
  * Realistic visual gradient payment card mockup displaying card brand glyph, chip graphic, masked card number, cardholder name, and expiry date.
  * Dedicated CVV card row with reveal toggle button.

---

### 5. Document Item (`VaultCategory.DOCUMENT`)

Used for encrypted identity attachments, tax forms, emergency travel documents, and backup files.

* **Common Fields**: `id`, `title`, `isFavorite`, `updatedAt`, `notes`.
* **Type-Specific Fields**:
  * `fileName: String` (File name including extension, e.g. `passport_scan.pdf`)
  * `fileType: String` (MIME or file category: "PDF", "ENC", "SCAN", "KEY")
  * `fileSize: String` (Formatted size string, e.g. `2.4 MB`, `156 KB`)
  * `dateAdded: String` (Creation/import timestamp)
* **Form & Validation Rules**:
  * In mock baseline, allows entering file name and format label.
  * In production, integrates Android Storage Access Framework (`ActivityResultContracts.OpenDocument`).
* **Detail Presentation**:
  * File format icon badge (Cyan container).
  * File size and date added information card.
  * Secure viewer / export action placeholder.
