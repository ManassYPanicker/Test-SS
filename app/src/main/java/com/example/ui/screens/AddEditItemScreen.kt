package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.CardItem
import com.example.model.DocumentItem
import com.example.model.IdItem
import com.example.model.LoginItem
import com.example.model.NoteItem
import com.example.model.VaultCategory
import com.example.model.VaultItem
import com.example.ui.components.PasswordGeneratorDialog
import com.example.ui.components.PasswordStrengthBar
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.EmeraldPrimary
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditItemScreen(
    category: VaultCategory,
    existingItem: VaultItem?,
    onSaveItem: (VaultItem) -> Unit,
    onBack: () -> Unit,
) {
  val isEditing = existingItem != null

  var title by remember { mutableStateOf(existingItem?.title.orEmpty()) }
  var isFavorite by remember { mutableStateOf(existingItem?.isFavorite ?: false) }
  var notes by remember {
    mutableStateOf(
        when (existingItem) {
          is LoginItem -> existingItem.notes
          is NoteItem -> existingItem.notes
          is IdItem -> existingItem.notes
          is CardItem -> existingItem.notes
          is DocumentItem -> existingItem.notes
          else -> ""
        }
    )
  }

  // Login fields
  var username by remember { mutableStateOf((existingItem as? LoginItem)?.username.orEmpty()) }
  var password by remember { mutableStateOf((existingItem as? LoginItem)?.password.orEmpty()) }
  var website by remember { mutableStateOf((existingItem as? LoginItem)?.website.orEmpty()) }
  var isPasswordRevealed by remember { mutableStateOf(false) }
  var showGeneratorDialog by remember { mutableStateOf(false) }

  // Note fields
  var noteContent by remember { mutableStateOf((existingItem as? NoteItem)?.content.orEmpty()) }

  // ID fields
  var idType by remember { mutableStateOf((existingItem as? IdItem)?.idType ?: "Passport") }
  var idNumber by remember { mutableStateOf((existingItem as? IdItem)?.idNumber.orEmpty()) }
  var expiryDate by remember { mutableStateOf((existingItem as? IdItem)?.expiryDate.orEmpty()) }
  var issuingCountry by remember { mutableStateOf((existingItem as? IdItem)?.issuingCountry.orEmpty()) }

  // Card fields
  var cardholderName by remember { mutableStateOf((existingItem as? CardItem)?.cardholderName.orEmpty()) }
  var cardNumber by remember { mutableStateOf((existingItem as? CardItem)?.cardNumber.orEmpty()) }
  var cardExpiry by remember { mutableStateOf((existingItem as? CardItem)?.expiry.orEmpty()) }
  var cardCvv by remember { mutableStateOf((existingItem as? CardItem)?.cvv.orEmpty()) }
  var cardType by remember { mutableStateOf((existingItem as? CardItem)?.cardType ?: "Visa") }

  // Document fields
  var fileName by remember { mutableStateOf((existingItem as? DocumentItem)?.fileName ?: "Document.pdf") }
  var fileType by remember { mutableStateOf((existingItem as? DocumentItem)?.fileType ?: "PDF") }
  var fileSize by remember { mutableStateOf((existingItem as? DocumentItem)?.fileSize ?: "2.1 MB") }

  var titleError by remember { mutableStateOf(false) }

  if (showGeneratorDialog) {
    PasswordGeneratorDialog(
        onDismiss = { showGeneratorDialog = false },
        onPasswordGenerated = { generated -> password = generated },
    )
  }

  val itemNoun =
      when (category) {
        VaultCategory.LOGIN -> "Login"
        VaultCategory.NOTE -> "Secure Note"
        VaultCategory.ID_CARD -> "ID Card"
        VaultCategory.PAYMENT_CARD -> "Payment Card"
        VaultCategory.DOCUMENT -> "Document"
        VaultCategory.ALL -> "Item"
      }

  Scaffold(
      topBar = {
        Row(
            modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack, modifier = Modifier.testTag("add_edit_cancel_btn")) {
              Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Cancel")
            }
            Text(
                text = if (isEditing) "Edit $itemNoun" else "New $itemNoun",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            )
          }

          TextButton(
              onClick = {
                if (title.isBlank()) {
                  titleError = true
                  return@TextButton
                }

                val itemId = existingItem?.id ?: UUID.randomUUID().toString()
                val updatedDate = "Just now"

                val newItem: VaultItem =
                    when (category) {
                      VaultCategory.ALL, VaultCategory.LOGIN ->
                          LoginItem(
                              id = itemId,
                              title = title.trim(),
                              username = username.trim(),
                              password = password,
                              website = website.trim(),
                              notes = notes.trim(),
                              isFavorite = isFavorite,
                              updatedAt = updatedDate,
                          )
                      VaultCategory.NOTE ->
                          NoteItem(
                              id = itemId,
                              title = title.trim(),
                              content = noteContent.trim(),
                              notes = notes.trim(),
                              isFavorite = isFavorite,
                              updatedAt = updatedDate,
                          )
                      VaultCategory.ID_CARD ->
                          IdItem(
                              id = itemId,
                              title = title.trim(),
                              idType = idType,
                              idNumber = idNumber.trim(),
                              expiryDate = expiryDate.trim(),
                              issuingCountry = issuingCountry.trim(),
                              notes = notes.trim(),
                              isFavorite = isFavorite,
                              updatedAt = updatedDate,
                          )
                      VaultCategory.PAYMENT_CARD ->
                          CardItem(
                              id = itemId,
                              title = title.trim(),
                              cardholderName = cardholderName.trim().uppercase(),
                              cardNumber = cardNumber.trim(),
                              expiry = cardExpiry.trim(),
                              cvv = cardCvv.trim(),
                              cardType = cardType,
                              notes = notes.trim(),
                              isFavorite = isFavorite,
                              updatedAt = updatedDate,
                          )
                      VaultCategory.DOCUMENT ->
                          DocumentItem(
                              id = itemId,
                              title = title.trim(),
                              fileName = fileName.trim(),
                              fileType = fileType.trim().uppercase(),
                              fileSize = fileSize,
                              dateAdded = "Today",
                              notes = notes.trim(),
                              isFavorite = isFavorite,
                              updatedAt = updatedDate,
                          )
                    }

                onSaveItem(newItem)
                onBack()
              },
              modifier = Modifier.testTag("save_vault_item_btn"),
          ) {
            Text("Save", color = EmeraldPrimary, fontWeight = FontWeight.Bold)
          }
        }
      },
      containerColor = MaterialTheme.colorScheme.background,
  ) { padding ->
    Column(
        modifier =
            Modifier.fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
    ) {
      Spacer(modifier = Modifier.height(12.dp))

      // Title field
      OutlinedTextField(
          value = title,
          onValueChange = {
            title = it
            if (it.isNotBlank()) titleError = false
          },
          label = { Text("Title / Service Name *") },
          isError = titleError,
          supportingText = if (titleError) { { Text("Title is required") } } else null,
          modifier = Modifier.fillMaxWidth().testTag("add_item_title_input"),
          singleLine = true,
          shape = RoundedCornerShape(12.dp),
      )

      Spacer(modifier = Modifier.height(12.dp))

      // Favorite Switch Row
      Row(
          modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically,
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
              imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.StarBorder,
              contentDescription = null,
              tint = if (isFavorite) AmberWarning else MaterialTheme.colorScheme.onSurfaceVariant,
          )
          Spacer(modifier = Modifier.width(10.dp))
          Text("Add to Favorites", style = MaterialTheme.typography.bodyMedium)
        }
        Switch(
            checked = isFavorite,
            onCheckedChange = { isFavorite = it },
            colors = SwitchDefaults.colors(checkedThumbColor = EmeraldPrimary),
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Category Specific Form Fields
      when (category) {
        VaultCategory.ALL, VaultCategory.LOGIN -> {
          OutlinedTextField(
              value = username,
              onValueChange = { username = it },
              label = { Text("Username or Email") },
              modifier = Modifier.fillMaxWidth().testTag("add_item_username_input"),
              singleLine = true,
              shape = RoundedCornerShape(12.dp),
          )

          Spacer(modifier = Modifier.height(14.dp))

          OutlinedTextField(
              value = password,
              onValueChange = { password = it },
              label = { Text("Password") },
              modifier = Modifier.fillMaxWidth().testTag("add_item_password_input"),
              singleLine = true,
              shape = RoundedCornerShape(12.dp),
              trailingIcon = {
                Row {
                  IconButton(
                      onClick = { showGeneratorDialog = true },
                      modifier = Modifier.testTag("generate_password_btn"),
                  ) {
                    Icon(Icons.Default.Casino, contentDescription = "Generate Password", tint = EmeraldPrimary)
                  }
                  IconButton(
                      onClick = { isPasswordRevealed = !isPasswordRevealed },
                      modifier = Modifier.testTag("add_item_toggle_password_btn"),
                  ) {
                    Icon(
                        imageVector = if (isPasswordRevealed) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = "Toggle visibility",
                    )
                  }
                }
              },
          )

          if (password.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            PasswordStrengthBar(password = password)
          }

          Spacer(modifier = Modifier.height(14.dp))

          OutlinedTextField(
              value = website,
              onValueChange = { website = it },
              label = { Text("Website URL (e.g. https://domain.com)") },
              modifier = Modifier.fillMaxWidth().testTag("add_item_website_input"),
              singleLine = true,
              shape = RoundedCornerShape(12.dp),
          )
        }

        VaultCategory.NOTE -> {
          OutlinedTextField(
              value = noteContent,
              onValueChange = { noteContent = it },
              label = { Text("Secure Note Body") },
              modifier = Modifier.fillMaxWidth().height(200.dp).testTag("add_item_note_input"),
              shape = RoundedCornerShape(12.dp),
          )
        }

        VaultCategory.ID_CARD -> {
          var dropdownExpanded by remember { mutableStateOf(false) }
          val idTypes = listOf("Passport", "Driver's License", "National ID", "Social Security / SSN", "Tax ID")

          ExposedDropdownMenuBox(
              expanded = dropdownExpanded,
              onExpandedChange = { dropdownExpanded = !dropdownExpanded },
              modifier = Modifier.fillMaxWidth(),
          ) {
            OutlinedTextField(
                value = idType,
                onValueChange = {},
                readOnly = true,
                label = { Text("Document Type") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded) },
                modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable),
                shape = RoundedCornerShape(12.dp),
            )
            ExposedDropdownMenu(
                expanded = dropdownExpanded,
                onDismissRequest = { dropdownExpanded = false },
            ) {
              idTypes.forEach { type ->
                DropdownMenuItem(
                    text = { Text(type) },
                    onClick = {
                      idType = type
                      dropdownExpanded = false
                    },
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          OutlinedTextField(
              value = idNumber,
              onValueChange = { idNumber = it },
              label = { Text("ID / Number") },
              modifier = Modifier.fillMaxWidth().testTag("add_item_id_number_input"),
              singleLine = true,
              shape = RoundedCornerShape(12.dp),
          )

          Spacer(modifier = Modifier.height(14.dp))

          OutlinedTextField(
              value = expiryDate,
              onValueChange = { expiryDate = it },
              label = { Text("Expiration Date (YYYY-MM-DD)") },
              modifier = Modifier.fillMaxWidth(),
              singleLine = true,
              shape = RoundedCornerShape(12.dp),
          )

          Spacer(modifier = Modifier.height(14.dp))

          OutlinedTextField(
              value = issuingCountry,
              onValueChange = { issuingCountry = it },
              label = { Text("Issuing Country or State") },
              modifier = Modifier.fillMaxWidth(),
              singleLine = true,
              shape = RoundedCornerShape(12.dp),
          )
        }

        VaultCategory.PAYMENT_CARD -> {
          OutlinedTextField(
              value = cardholderName,
              onValueChange = { cardholderName = it },
              label = { Text("Cardholder Name") },
              modifier = Modifier.fillMaxWidth(),
              singleLine = true,
              shape = RoundedCornerShape(12.dp),
          )

          Spacer(modifier = Modifier.height(14.dp))

          OutlinedTextField(
              value = cardNumber,
              onValueChange = { cardNumber = it },
              label = { Text("Card Number (16 digits)") },
              placeholder = { Text("4111 2222 3333 4444") },
              modifier = Modifier.fillMaxWidth().testTag("add_item_card_number_input"),
              singleLine = true,
              shape = RoundedCornerShape(12.dp),
          )

          Spacer(modifier = Modifier.height(14.dp))

          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(
                value = cardExpiry,
                onValueChange = { cardExpiry = it },
                label = { Text("Expiry (MM/YY)") },
                modifier = Modifier.weight(1f).testTag("add_item_card_expiry_input"),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
            )
            OutlinedTextField(
                value = cardCvv,
                onValueChange = { cardCvv = it },
                label = { Text("CVV") },
                modifier = Modifier.weight(1f).testTag("add_item_card_cvv_input"),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
            )
          }

          Spacer(modifier = Modifier.height(14.dp))

          OutlinedTextField(
              value = cardType,
              onValueChange = { cardType = it },
              label = { Text("Card Brand (Visa, Mastercard, Amex)") },
              modifier = Modifier.fillMaxWidth(),
              singleLine = true,
              shape = RoundedCornerShape(12.dp),
          )
        }

        VaultCategory.DOCUMENT -> {
          OutlinedTextField(
              value = fileName,
              onValueChange = { fileName = it },
              label = { Text("Attached File Name") },
              modifier = Modifier.fillMaxWidth().testTag("add_item_doc_filename_input"),
              singleLine = true,
              shape = RoundedCornerShape(12.dp),
          )

          Spacer(modifier = Modifier.height(14.dp))

          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(
                value = fileType,
                onValueChange = { fileType = it },
                label = { Text("File Type (PDF, ENC)") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
            )
            OutlinedTextField(
                value = fileSize,
                onValueChange = { fileSize = it },
                label = { Text("File Size") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Notes
      OutlinedTextField(
          value = notes,
          onValueChange = { notes = it },
          label = { Text("Private Encrypted Notes") },
          modifier = Modifier.fillMaxWidth().height(110.dp).testTag("add_item_notes_input"),
          shape = RoundedCornerShape(12.dp),
      )

      Spacer(modifier = Modifier.height(28.dp))
    }
  }
}
