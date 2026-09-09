package com.example.data

import com.example.model.CardItem
import com.example.model.DocumentItem
import com.example.model.IdItem
import com.example.model.LoginItem
import com.example.model.NoteItem
import com.example.model.VaultItem

object MockVaultData {
  val mockRecoveryPhrase: List<String> =
      listOf(
          "abandon",
          "canvas",
          "desert",
          "foster",
          "galaxy",
          "harbor",
          "island",
          "jungle",
          "matrix",
          "nebula",
          "oxygen",
          "puzzle",
      )

  fun getInitialItems(): List<VaultItem> =
      listOf(
          // 5 Logins
          LoginItem(
              id = "login-1",
              title = "Google Workspace",
              username = "alex.miller@fictional-domain.org",
              password = "k8#Np!9vLz\$2mQe",
              website = "https://accounts.google.com",
              notes = "Primary professional Google identity. 2FA configured via hardware token.",
              isFavorite = true,
              updatedAt = "Today, 09:30 AM",
              serviceBadge = "Google",
          ),
          LoginItem(
              id = "login-2",
              title = "GitHub",
              username = "alex-m-dev",
              password = "ghp_X99FictionalTokenSample9831",
              website = "https://github.com",
              notes = "Developer organization credentials and SSH passphrase backup.",
              isFavorite = true,
              updatedAt = "Yesterday, 4:12 PM",
              serviceBadge = "GitHub",
          ),
          LoginItem(
              id = "login-3",
              title = "Microsoft Azure",
              username = "alex.m@cloud-mock-corp.com",
              password = "Msft#AzureVault2026!",
              website = "https://portal.azure.com",
              notes = "Corporate tenant administrative login. Rotated quarterly.",
              isFavorite = false,
              updatedAt = "2 days ago",
              serviceBadge = "Microsoft",
          ),
          LoginItem(
              id = "login-4",
              title = "Amazon Prime",
              username = "alex.prime.shopper@mockmail.net",
              password = "Pass1234MockWeak", // Intentionally weak mock password for security audit demonstration
              website = "https://amazon.com",
              notes = "Shared household orders and Prime video subscription.",
              isFavorite = false,
              updatedAt = "3 weeks ago",
              serviceBadge = "Amazon",
          ),
          LoginItem(
              id = "login-5",
              title = "First National Bank",
              username = "nb_alex_9921",
              password = "b@nk!Saf3#99Secure2026",
              website = "https://online.firstnationalmock.com",
              notes = "Savings and checking checking account. Requires security questions: Mother's pet -> Apollo.",
              isFavorite = true,
              updatedAt = "1 month ago",
              serviceBadge = "Bank",
          ),

          // 2 Secure Notes
          NoteItem(
              id = "note-1",
              title = "Home Wi-Fi & Router Config",
              content =
                  "SSID Primary: OrbitGuard-5G\nPassword: Orion#Starlight%98\nAdmin Portal: 192.168.1.1\nAdmin Pass: HyperLock-992-Vault\nDNS: 1.1.1.1 (Cloudflare Private)",
              notes = "Router located in living room cabinet. Reset pin is behind the right antenna.",
              isFavorite = true,
              updatedAt = "Yesterday, 6:45 PM",
          ),
          NoteItem(
              id = "note-2",
              title = "Emergency Safe Combination",
              content =
                  "Master Physical Safe (Office Wardrobe):\n1. Turn right 3 times to 34\n2. Turn left past 34 once to 78\n3. Turn right directly to 12\n4. Turn handle downward firmly.\nBackup key hidden in study bookshelf (behind Encyclopedia Vol 4).",
              notes = "Only share with trusted executor in emergency situations.",
              isFavorite = false,
              updatedAt = "2 weeks ago",
          ),

          // 3 ID Numbers
          IdItem(
              id = "id-1",
              title = "Passport (United States)",
              idType = "Passport",
              idNumber = "E84920194",
              expiryDate = "2032-11-18",
              issuingCountry = "United States of America",
              notes = "Biometric e-Passport. Issued in Washington DC office.",
              isFavorite = true,
              updatedAt = "1 month ago",
          ),
          IdItem(
              id = "id-2",
              title = "State Driver License",
              idType = "Driver's License",
              idNumber = "DL-CA-92841029",
              expiryDate = "2028-06-24",
              issuingCountry = "California, USA",
              notes = "REAL ID compliant driver license. Endorsements: Class C.",
              isFavorite = false,
              updatedAt = "3 months ago",
          ),
          IdItem(
              id = "id-3",
              title = "Social Security Card",
              idType = "National ID / SSN",
              idNumber = "XXX-42-8910",
              expiryDate = "Does not expire",
              issuingCountry = "United States Social Security Admin",
              notes = "Keep physical card in safe. NEVER share over insecure email.",
              isFavorite = false,
              updatedAt = "6 months ago",
          ),

          // 2 Payment Cards
          CardItem(
              id = "card-1",
              title = "Sapphire Reserve Rewards",
              cardholderName = "ALEXANDER MILLER",
              cardNumber = "4111 2222 3333 4291",
              expiry = "08/29",
              cvv = "842",
              cardType = "Visa",
              notes = "Primary travel and dining card. Airport lounge access registered.",
              isFavorite = true,
              updatedAt = "1 week ago",
          ),
          CardItem(
              id = "card-2",
              title = "Apple Card Titanium",
              cardholderName = "ALEXANDER MILLER",
              cardNumber = "5412 7512 3412 8820",
              expiry = "11/27",
              cvv = "319",
              cardType = "Mastercard",
              notes = "Zero foreign transaction fees. 2% cashback via Apple Pay.",
              isFavorite = false,
              updatedAt = "3 weeks ago",
          ),

          // 3 Documents
          DocumentItem(
              id = "doc-1",
              title = "Tax Return Fiscal 2025",
              fileName = "Tax_Return_Federal_2025_Final.pdf",
              fileType = "PDF",
              fileSize = "2.4 MB",
              dateAdded = "Apr 12, 2026",
              notes = "Signed federal and state joint filing confirmation copy.",
              isFavorite = true,
              updatedAt = "Apr 12, 2026",
          ),
          DocumentItem(
              id = "doc-2",
              title = "Property Title & Deed",
              fileName = "Residence_Deed_Certificate.enc",
              fileType = "ENC",
              fileSize = "8.1 MB",
              dateAdded = "Jan 20, 2026",
              notes = "Scanned notary deed and escrow settlement papers.",
              isFavorite = false,
              updatedAt = "Jan 20, 2026",
          ),
          DocumentItem(
              id = "doc-3",
              title = "Passport Scan & Visa Stamp",
              fileName = "Passport_PhotoPage_SchengenVisa.pdf",
              fileType = "PDF",
              fileSize = "1.8 MB",
              dateAdded = "May 04, 2026",
              notes = "High-resolution color scan for international travel emergencies.",
              isFavorite = false,
              updatedAt = "May 04, 2026",
          ),
      )
}
