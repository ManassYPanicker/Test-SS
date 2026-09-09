package com.example.model

enum class VaultCategory(val title: String, val subtitle: String) {
  ALL("All Items", "Everything in your vault"),
  LOGIN("Logins", "Passwords & accounts"),
  NOTE("Secure Notes", "Encrypted private memos"),
  ID_CARD("ID Numbers", "Passports, licenses & SSNs"),
  PAYMENT_CARD("Cards", "Debit & credit payment cards"),
  DOCUMENT("Documents", "Important identity files"),
}

sealed interface VaultItem {
  val id: String
  val title: String
  val isFavorite: Boolean
  val updatedAt: String
  val category: VaultCategory

  fun copyWithFavorite(isFav: Boolean): VaultItem
}

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

data class IdItem(
    override val id: String,
    override val title: String,
    val idType: String, // "Passport", "Driver's License", "SSN", "National ID"
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

data class CardItem(
    override val id: String,
    override val title: String,
    val cardholderName: String,
    val cardNumber: String, // formatted with spaces
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

enum class BackupStatus {
  DISABLED,
  CONNECTING,
  IDLE,
  IN_PROGRESS,
  SUCCESS,
  FAILED,
}

data class SecurityScore(
    val score: Int = 92,
    val weakCount: Int = 1,
    val reusedCount: Int = 1,
    val oldCount: Int = 1,
    val compromisedCount: Int = 0,
    val lastAudit: String = "Checked today",
)
