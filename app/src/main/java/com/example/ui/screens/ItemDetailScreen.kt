package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CardItem
import com.example.model.DocumentItem
import com.example.model.IdItem
import com.example.model.LoginItem
import com.example.model.NoteItem
import com.example.model.VaultItem
import com.example.ui.components.CopyButton
import com.example.ui.components.DeleteConfirmationDialog
import com.example.ui.components.PasswordStrengthBar
import com.example.ui.components.SecurityBadge
import com.example.ui.components.getCategoryColor
import com.example.ui.components.getCategoryIcon
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.RoseError

@Composable
fun ItemDetailScreen(
    item: VaultItem,
    onEditClick: () -> Unit,
    onDeleteConfirm: () -> Unit,
    onFavoriteToggle: () -> Unit,
    onBack: () -> Unit,
) {
  var showDeleteDialog by remember { mutableStateOf(false) }
  var isPasswordRevealed by remember { mutableStateOf(false) }
  var isCvvRevealed by remember { mutableStateOf(false) }
  var isIdRevealed by remember { mutableStateOf(false) }
  val context = LocalContext.current

  if (showDeleteDialog) {
    DeleteConfirmationDialog(
        itemTitle = item.title,
        onConfirm = {
          showDeleteDialog = false
          onDeleteConfirm()
        },
        onDismiss = { showDeleteDialog = false },
    )
  }

  Scaffold(
      topBar = {
        Row(
            modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
          IconButton(onClick = onBack, modifier = Modifier.testTag("item_detail_back_btn")) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
          }
          Row {
            IconButton(onClick = onFavoriteToggle, modifier = Modifier.testTag("item_detail_favorite_btn")) {
              Icon(
                  imageVector = if (item.isFavorite) Icons.Filled.Star else Icons.Outlined.StarBorder,
                  contentDescription = "Favorite",
                  tint = if (item.isFavorite) AmberWarning else MaterialTheme.colorScheme.onSurfaceVariant,
              )
            }
            IconButton(onClick = onEditClick, modifier = Modifier.testTag("edit_item_top_btn")) {
              Icon(Icons.Default.Edit, contentDescription = "Edit entry")
            }
            IconButton(onClick = { showDeleteDialog = true }, modifier = Modifier.testTag("delete_item_top_btn")) {
              Icon(Icons.Default.Delete, contentDescription = "Delete entry", tint = RoseError)
            }
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
      // Header Title & Category Icon
      val catColor = getCategoryColor(item.category)
      val catIcon = getCategoryIcon(item.category)

      Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
      ) {
        Box(
            modifier =
                Modifier.size(54.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(catColor.copy(alpha = 0.15f))
                    .border(1.dp, catColor.copy(alpha = 0.3f), RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center,
        ) {
          Icon(catIcon, contentDescription = null, tint = catColor, modifier = Modifier.size(28.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
          Text(
              text = item.title,
              style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onBackground,
          )
          Spacer(modifier = Modifier.height(2.dp))
          Row(verticalAlignment = Alignment.CenterVertically) {
            SecurityBadge(text = "AES-256 GCM", color = EmeraldPrimary)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Updated: ${item.updatedAt}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Category Specific Fields
      when (item) {
        is LoginItem -> {
          // Username Card
          DetailFieldCard(
              label = "Username / Email",
              value = item.username,
              trailingAction = { CopyButton(textToCopy = item.username, label = "Username copied") },
          )

          Spacer(modifier = Modifier.height(12.dp))

          // Password Card
          Card(
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
              shape = RoundedCornerShape(14.dp),
              modifier = Modifier.fillMaxWidth(),
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Text(
                  text = "Password",
                  style = MaterialTheme.typography.labelMedium,
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
              )
              Spacer(modifier = Modifier.height(4.dp))
              Row(
                  modifier = Modifier.fillMaxWidth(),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceBetween,
              ) {
                Text(
                    text = if (isPasswordRevealed) item.password else "••••••••••••••••",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.SemiBold,
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f),
                )
                Row {
                  IconButton(
                      onClick = { isPasswordRevealed = !isPasswordRevealed },
                      modifier = Modifier.testTag("item_detail_toggle_password_btn"),
                  ) {
                    Icon(
                        imageVector = if (isPasswordRevealed) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = "Toggle password visibility",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                  }
                  CopyButton(textToCopy = item.password, label = "Password copied")
                }
              }

              Spacer(modifier = Modifier.height(8.dp))
              PasswordStrengthBar(password = item.password)
            }
          }

          if (item.website.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            DetailFieldCard(
                label = "Website",
                value = item.website,
                trailingAction = {
                  IconButton(onClick = {
                    Toast.makeText(context, "Opening ${item.website} in browser", Toast.LENGTH_SHORT).show()
                  }) {
                    Icon(Icons.Default.OpenInNew, contentDescription = "Open link", tint = EmeraldPrimary)
                  }
                },
            )
          }
        }

        is NoteItem -> {
          Card(
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
              shape = RoundedCornerShape(14.dp),
              modifier = Modifier.fillMaxWidth(),
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically,
              ) {
                Text(
                    text = "Encrypted Memo Content",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                CopyButton(textToCopy = item.content, label = "Note content copied")
              }
              Spacer(modifier = Modifier.height(8.dp))
              Text(
                  text = item.content,
                  style = MaterialTheme.typography.bodyMedium.copy(
                      fontFamily = FontFamily.Monospace,
                      lineHeight = 22.sp,
                  ),
                  color = MaterialTheme.colorScheme.onSurface,
              )
            }
          }
        }

        is IdItem -> {
          DetailFieldCard(label = "Document Type", value = item.idType)
          Spacer(modifier = Modifier.height(12.dp))

          DetailFieldCard(
              label = "ID / Document Number",
              value = if (isIdRevealed) item.idNumber else "•••• •••• •••• ${item.idNumber.takeLast(4)}",
              trailingAction = {
                Row {
                  IconButton(
                      onClick = { isIdRevealed = !isIdRevealed },
                      modifier = Modifier.testTag("item_detail_toggle_id_btn"),
                  ) {
                    Icon(
                        imageVector = if (isIdRevealed) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = "Toggle ID visibility",
                    )
                  }
                  CopyButton(textToCopy = item.idNumber, label = "ID number copied")
                }
              },
          )

          if (item.expiryDate.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            DetailFieldCard(label = "Expiration Date", value = item.expiryDate)
          }

          if (item.issuingCountry.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            DetailFieldCard(label = "Issuing Country / Authority", value = item.issuingCountry)
          }
        }

        is CardItem -> {
          // Visual Credit Card Component
          Box(
              modifier =
                  Modifier.fillMaxWidth()
                      .height(200.dp)
                      .clip(RoundedCornerShape(20.dp))
                      .background(
                          Brush.linearGradient(
                              colors = listOf(Color(0xFF1E293B), Color(0xFF0F172A), Color(0xFF064E3B))
                          )
                      )
                      .border(1.dp, EmeraldPrimary.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                      .padding(20.dp),
          ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
              Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically,
              ) {
                Text(
                    text = item.cardType.uppercase(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                    ),
                    color = EmeraldPrimary,
                )
                Icon(Icons.Default.CreditCard, contentDescription = null, tint = Color.White)
              }

              // Card Number
              Text(
                  text = item.cardNumber,
                  style = MaterialTheme.typography.titleLarge.copy(
                      fontFamily = FontFamily.Monospace,
                      fontWeight = FontWeight.SemiBold,
                      letterSpacing = 2.sp,
                  ),
                  color = Color.White,
              )

              Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.Bottom,
              ) {
                Column {
                  Text("CARDHOLDER", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                  Text(
                      item.cardholderName,
                      style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                      color = Color.White,
                  )
                }
                Column(horizontalAlignment = Alignment.End) {
                  Text("EXPIRES", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                  Text(
                      item.expiry,
                      style = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace),
                      color = Color.White,
                  )
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          DetailFieldCard(
              label = "Card Number",
              value = item.cardNumber,
              trailingAction = { CopyButton(textToCopy = item.cardNumber.replace(" ", ""), label = "Card number copied") },
          )

          Spacer(modifier = Modifier.height(12.dp))

          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(modifier = Modifier.weight(1f)) {
              DetailFieldCard(label = "Expires", value = item.expiry)
            }
            Box(modifier = Modifier.weight(1f)) {
              DetailFieldCard(
                  label = "CVV / Security Code",
                  value = if (isCvvRevealed) item.cvv else "•••",
                  trailingAction = {
                    Row {
                      IconButton(
                          onClick = { isCvvRevealed = !isCvvRevealed },
                          modifier = Modifier.testTag("item_detail_toggle_cvv_btn"),
                      ) {
                        Icon(
                            imageVector = if (isCvvRevealed) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = "Toggle CVV",
                        )
                      }
                      CopyButton(textToCopy = item.cvv, label = "CVV copied")
                    }
                  },
              )
            }
          }
        }

        is DocumentItem -> {
          Card(
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
              shape = RoundedCornerShape(16.dp),
              modifier = Modifier.fillMaxWidth(),
          ) {
            Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
              Box(
                  modifier =
                      Modifier.size(64.dp)
                          .clip(CircleShape)
                          .background(EmeraldPrimary.copy(alpha = 0.15f)),
                  contentAlignment = Alignment.Center,
              ) {
                Icon(Icons.Default.Description, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(32.dp))
              }
              Spacer(modifier = Modifier.height(12.dp))
              Text(
                  text = item.fileName,
                  style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                  color = MaterialTheme.colorScheme.onSurface,
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                  text = "${item.fileType} format • ${item.fileSize} • Added ${item.dateAdded}",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
              )

              Spacer(modifier = Modifier.height(20.dp))

              Button(
                  onClick = {
                    Toast.makeText(context, "Decrypted ${item.fileName} into secure memory viewer", Toast.LENGTH_SHORT).show()
                  },
                  colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                  modifier = Modifier.fillMaxWidth().testTag("view_decrypted_document_btn"),
              ) {
                Icon(Icons.Default.FileDownload, contentDescription = null, tint = Color(0xFF003324))
                Spacer(modifier = Modifier.width(8.dp))
                Text("View Decrypted Document", color = Color(0xFF003324), fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      }

      // Notes section (applicable to all items)
      val notesContent =
          when (item) {
            is LoginItem -> item.notes
            is NoteItem -> item.notes
            is IdItem -> item.notes
            is CardItem -> item.notes
            is DocumentItem -> item.notes
          }

      if (notesContent.isNotEmpty()) {
        Spacer(modifier = Modifier.height(16.dp))
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text("Encrypted Private Notes", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(6.dp))
            Text(notesContent, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
          }
        }
      }

      Spacer(modifier = Modifier.height(28.dp))

      // Action Buttons Row
      Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp),
      ) {
        OutlinedButton(
            onClick = onEditClick,
            modifier = Modifier.weight(1f).height(48.dp).testTag("bottom_edit_item_btn"),
            shape = RoundedCornerShape(12.dp),
        ) {
          Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text("Edit Entry", fontWeight = FontWeight.SemiBold)
        }

        Button(
            onClick = { showDeleteDialog = true },
            colors = ButtonDefaults.buttonColors(containerColor = RoseError.copy(alpha = 0.15f), contentColor = RoseError),
            modifier = Modifier.weight(1f).height(48.dp).testTag("bottom_delete_item_btn"),
            shape = RoundedCornerShape(12.dp),
        ) {
          Icon(Icons.Outlined.Delete, contentDescription = null, tint = RoseError, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text("Delete", color = RoseError, fontWeight = FontWeight.SemiBold)
        }
      }

      Spacer(modifier = Modifier.height(32.dp))
    }
  }
}

@Composable
fun DetailFieldCard(
    label: String,
    value: String,
    trailingAction: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
  Card(
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
      shape = RoundedCornerShape(14.dp),
      modifier = modifier.fillMaxWidth(),
  ) {
    Row(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp).fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onSurface,
        )
      }
      trailingAction?.invoke()
    }
  }
}
