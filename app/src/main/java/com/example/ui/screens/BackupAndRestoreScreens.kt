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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.CloudDownload
import androidx.compose.material.icons.outlined.CloudUpload
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BackupStatus
import com.example.ui.components.SecurityBadge
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.RoseError

@Composable
fun BackupScreen(
    backupStatus: BackupStatus,
    lastBackupTime: String,
    googleAccount: String,
    isBackupConfigured: Boolean,
    onToggleBackupConfigured: (Boolean) -> Unit,
    onTriggerBackup: (Boolean) -> Unit,
    onBack: () -> Unit,
) {
  var simulateFailure by remember { mutableStateOf(false) }
  val context = LocalContext.current

  Scaffold(
      topBar = {
        Row(
            modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(horizontal = 8.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
          IconButton(onClick = onBack, modifier = Modifier.testTag("backup_screen_back_btn")) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
          }
          Spacer(modifier = Modifier.width(4.dp))
          Column {
            Text(
                text = "Encrypted Cloud Backup",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = "Zero-knowledge synchronization with your Google Drive",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
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
      Spacer(modifier = Modifier.height(8.dp))

      // Status Banner Card
      Card(
          colors =
              CardDefaults.cardColors(
                  containerColor =
                      when (backupStatus) {
                        BackupStatus.SUCCESS -> EmeraldPrimary.copy(alpha = 0.12f)
                        BackupStatus.FAILED -> RoseError.copy(alpha = 0.12f)
                        BackupStatus.IN_PROGRESS -> CyanAccent.copy(alpha = 0.12f)
                        else -> MaterialTheme.colorScheme.surfaceVariant
                      }
              ),
          shape = RoundedCornerShape(18.dp),
          modifier =
              Modifier.fillMaxWidth().border(
                  1.dp,
                  when (backupStatus) {
                    BackupStatus.SUCCESS -> EmeraldPrimary.copy(alpha = 0.3f)
                    BackupStatus.FAILED -> RoseError.copy(alpha = 0.3f)
                    BackupStatus.IN_PROGRESS -> CyanAccent.copy(alpha = 0.3f)
                    else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                  },
                  RoundedCornerShape(18.dp),
              ),
      ) {
        Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
          Box(
              modifier =
                  Modifier.size(60.dp)
                      .clip(CircleShape)
                      .background(
                          when (backupStatus) {
                            BackupStatus.SUCCESS -> EmeraldPrimary.copy(alpha = 0.2f)
                            BackupStatus.FAILED -> RoseError.copy(alpha = 0.2f)
                            BackupStatus.IN_PROGRESS -> CyanAccent.copy(alpha = 0.2f)
                            else -> Color.Gray.copy(alpha = 0.2f)
                          }
                      ),
              contentAlignment = Alignment.Center,
          ) {
            Icon(
                imageVector =
                    when (backupStatus) {
                      BackupStatus.SUCCESS -> Icons.Default.CloudDone
                      BackupStatus.FAILED -> Icons.Default.CloudOff
                      BackupStatus.IN_PROGRESS -> Icons.Default.Refresh
                      else -> Icons.Default.CloudOff
                    },
                contentDescription = null,
                tint =
                    when (backupStatus) {
                      BackupStatus.SUCCESS -> EmeraldPrimary
                      BackupStatus.FAILED -> RoseError
                      BackupStatus.IN_PROGRESS -> CyanAccent
                      else -> Color.Gray
                    },
                modifier = Modifier.size(32.dp),
            )
          }

          Spacer(modifier = Modifier.height(14.dp))

          Text(
              text =
                  when (backupStatus) {
                    BackupStatus.SUCCESS -> "Encrypted Backup Active"
                    BackupStatus.FAILED -> "Backup Synchronization Failed"
                    BackupStatus.IN_PROGRESS -> "Encrypting & Uploading Vault..."
                    BackupStatus.DISABLED -> "Cloud Backup Disabled"
                    else -> "Backup Ready"
                  },
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onSurface,
          )

          Spacer(modifier = Modifier.height(4.dp))

          Text(
              text =
                  when (backupStatus) {
                    BackupStatus.SUCCESS -> "Last synchronized: $lastBackupTime"
                    BackupStatus.FAILED -> "Connection timed out or storage permission required"
                    BackupStatus.IN_PROGRESS -> "Encrypting and preparing vault backup..."
                    else -> "No active cloud destination configured"
                  },
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              textAlign = TextAlign.Center,
          )

          if (backupStatus == BackupStatus.IN_PROGRESS) {
            Spacer(modifier = Modifier.height(16.dp))
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp)),
                color = CyanAccent,
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Google Drive Account Configuration Card
      Card(
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
          shape = RoundedCornerShape(16.dp),
          modifier = Modifier.fillMaxWidth(),
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically,
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text("Google Drive Integration", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
              Text(
                  if (isBackupConfigured) googleAccount else "Not linked to a cloud account",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
              )
            }
            Switch(
                checked = isBackupConfigured,
                onCheckedChange = onToggleBackupConfigured,
                colors = SwitchDefaults.colors(checkedThumbColor = EmeraldPrimary),
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Zero-knowledge security guarantee notice
      Card(
          colors = CardDefaults.cardColors(containerColor = EmeraldPrimary.copy(alpha = 0.08f)),
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier.fillMaxWidth().border(1.dp, EmeraldPrimary.copy(alpha = 0.2f), RoundedCornerShape(14.dp)),
      ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.Top) {
          Icon(Icons.Default.Security, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(22.dp))
          Spacer(modifier = Modifier.width(12.dp))
          Column {
            Text("Client-Side Encryption Guarantee", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = EmeraldPrimary)
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                "Your vault is designed to be encrypted using your recovery phrase directly on your device before synchronization. Neither Google nor third parties can inspect your secrets.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 18.sp,
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      // Trigger Backup Button
      Button(
          onClick = { onTriggerBackup(simulateFailure) },
          enabled = isBackupConfigured && backupStatus != BackupStatus.IN_PROGRESS,
          colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
          modifier = Modifier.fillMaxWidth().height(52.dp).testTag("trigger_backup_btn"),
          shape = RoundedCornerShape(14.dp),
      ) {
        Icon(Icons.Outlined.CloudUpload, contentDescription = null, tint = Color(0xFF003324))
        Spacer(modifier = Modifier.width(8.dp))
        Text("Back Up Vault Now", color = Color(0xFF003324), fontWeight = FontWeight.Bold)
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Prototype Simulation Controls
      Card(
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier.fillMaxWidth(),
      ) {
        Row(
            modifier = Modifier.padding(14.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text("Simulate Backup Failure", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium))
            Text("Toggle to demonstrate error recovery UX", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
          Switch(
              checked = simulateFailure,
              onCheckedChange = { simulateFailure = it },
              colors = SwitchDefaults.colors(checkedThumbColor = RoseError),
          )
        }
      }

      Spacer(modifier = Modifier.height(32.dp))
    }
  }
}

@Composable
fun RestoreScreen(
    onRestoreSuccess: () -> Unit,
    onBack: () -> Unit,
) {
  var selectedTab by remember { mutableIntStateOf(0) } // 0: Recovery Phrase, 1: Google Drive, 2: File Import
  val tabs = listOf("Recovery Phrase", "Google Drive", "File (.seedsafe)")

  var phraseInput by remember { mutableStateOf("") }
  var isRestoring by remember { mutableStateOf(false) }
  var restoreStatusMessage by remember { mutableStateOf<String?>(null) }
  var isError by remember { mutableStateOf(false) }
  var showCorruptedDialog by remember { mutableStateOf(false) }

  if (showCorruptedDialog) {
    AlertDialog(
        onDismissRequest = { showCorruptedDialog = false },
        icon = { Icon(Icons.Default.Warning, contentDescription = null, tint = RoseError) },
        title = { Text("Corrupted Backup File") },
        text = {
          Text("The encrypted payload could not be verified by your master key. The file may be truncated or altered. Please choose a different backup snapshot.")
        },
        confirmButton = {
          TextButton(onClick = { showCorruptedDialog = false }) { Text("OK") }
        },
    )
  }

  Scaffold(
      topBar = {
        Row(
            modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(horizontal = 8.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
          IconButton(onClick = onBack, modifier = Modifier.testTag("restore_screen_back_btn")) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
          }
          Spacer(modifier = Modifier.width(4.dp))
          Column {
            Text(
                text = "Restore Existing Vault",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = "Recover credentials via seed words, cloud, or file",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
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
      TabRow(
          selectedTabIndex = selectedTab,
          containerColor = MaterialTheme.colorScheme.surfaceVariant,
          contentColor = EmeraldPrimary,
          modifier = Modifier.clip(RoundedCornerShape(12.dp)),
      ) {
        tabs.forEachIndexed { index, title ->
          Tab(
              selected = selectedTab == index,
              onClick = {
                selectedTab = index
                isError = false
                restoreStatusMessage = null
              },
              text = { Text(title, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold)) },
          )
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      when (selectedTab) {
        0 -> {
          // Tab 0: 12-word recovery phrase input
          Text(
              text = "Enter Your 12 Secret Words",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
              text = "Type or paste your 12 recovery words separated by spaces. SeedSafe will rebuild your master vault decryption key.",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
          )

          Spacer(modifier = Modifier.height(16.dp))

          OutlinedTextField(
              value = phraseInput,
              onValueChange = {
                phraseInput = it
                isError = false
                restoreStatusMessage = null
              },
              label = { Text("12-word recovery phrase") },
              placeholder = { Text("abandon canvas desert foster galaxy...") },
              modifier = Modifier.fillMaxWidth().height(140.dp).testTag("restore_phrase_input"),
              shape = RoundedCornerShape(14.dp),
              isError = isError,
          )

          Spacer(modifier = Modifier.height(8.dp))

          TextButton(
              onClick = {
                phraseInput = "abandon canvas desert foster galaxy harbor island jungle matrix nebula oxygen puzzle"
              },
          ) {
            Text("Insert Valid Demo Phrase", color = EmeraldPrimary, style = MaterialTheme.typography.labelSmall)
          }

          Spacer(modifier = Modifier.height(12.dp))

          Button(
              onClick = {
                val words = phraseInput.trim().split("\\s+".toRegex())
                if (words.size < 12) {
                  isError = true
                  restoreStatusMessage = "Expected 12 words, found ${words.size}. Please check your phrase."
                } else {
                  isRestoring = true
                  restoreStatusMessage = "Verifying cryptographic checksum & decrypting vault..."
                }
              },
              enabled = phraseInput.isNotBlank() && !isRestoring,
              colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
              modifier = Modifier.fillMaxWidth().height(52.dp).testTag("restore_from_phrase_btn"),
              shape = RoundedCornerShape(14.dp),
          ) {
            Icon(Icons.Default.Key, contentDescription = null, tint = Color(0xFF003324))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Decrypt & Restore Vault", color = Color(0xFF003324), fontWeight = FontWeight.Bold)
          }
        }

        1 -> {
          // Tab 1: Google Drive Cloud Restore
          Card(
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
              shape = RoundedCornerShape(16.dp),
              modifier = Modifier.fillMaxWidth(),
          ) {
            Column(modifier = Modifier.padding(18.dp)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CloudDone, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(28.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                  Text("Google Drive Vault Found", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                  Text("alex.miller@gmail.com", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
              }

              Spacer(modifier = Modifier.height(16.dp))

              Card(
                  colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                  shape = RoundedCornerShape(12.dp),
                  modifier = Modifier.fillMaxWidth(),
              ) {
                Column(modifier = Modifier.padding(14.dp)) {
                  Text("seedsafe_encrypted_snapshot_2026.enc", style = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold))
                  Text("Snapshot size: 48.2 KB • Contains 14 items", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                  Text("Created: Today at 08:30 AM", style = MaterialTheme.typography.labelSmall, color = EmeraldPrimary)
                }
              }

              Spacer(modifier = Modifier.height(16.dp))

              Button(
                  onClick = {
                    isRestoring = true
                    restoreStatusMessage = "Downloading encrypted cloud snapshot & decrypting..."
                  },
                  colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                  modifier = Modifier.fillMaxWidth().height(50.dp),
                  shape = RoundedCornerShape(12.dp),
              ) {
                Icon(Icons.Outlined.CloudDownload, contentDescription = null, tint = Color(0xFF003324))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Download & Restore Backup", color = Color(0xFF003324), fontWeight = FontWeight.Bold)
              }

              Spacer(modifier = Modifier.height(8.dp))

              OutlinedButton(
                  onClick = { showCorruptedDialog = true },
                  modifier = Modifier.fillMaxWidth(),
                  shape = RoundedCornerShape(12.dp),
              ) {
                Text("Simulate Corrupted Backup File", color = RoseError)
              }
            }
          }
        }

        2 -> {
          // Tab 2: File Import
          Card(
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
              shape = RoundedCornerShape(16.dp),
              modifier = Modifier.fillMaxWidth(),
          ) {
            Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
              Icon(Icons.Outlined.FolderOpen, contentDescription = null, tint = CyanAccent, modifier = Modifier.size(48.dp))
              Spacer(modifier = Modifier.height(12.dp))
              Text("Select .seedsafe Backup File", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                  "Choose an offline encrypted archive exported from SeedSafe on another device.",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                  textAlign = TextAlign.Center,
              )
              Spacer(modifier = Modifier.height(20.dp))

              Button(
                  onClick = {
                    isRestoring = true
                    restoreStatusMessage = "Importing local archive: SeedSafe_Vault_Backup.seedsafe"
                  },
                  colors = ButtonDefaults.buttonColors(containerColor = CyanAccent),
                  modifier = Modifier.fillMaxWidth().height(50.dp),
                  shape = RoundedCornerShape(12.dp),
              ) {
                Text("Browse Storage for .seedsafe", color = Color(0xFF00354E), fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      }

      // Restoring Progress or Result Feedback
      AnimatedVisibility(visible = restoreStatusMessage != null) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
          if (isRestoring) {
            CircularProgressIndicator(color = EmeraldPrimary, modifier = Modifier.size(36.dp))
            Spacer(modifier = Modifier.height(12.dp))
          } else if (isError) {
            Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = RoseError, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.height(8.dp))
          }

          Text(
              text = restoreStatusMessage.orEmpty(),
              style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
              color = if (isError) RoseError else MaterialTheme.colorScheme.onSurface,
              textAlign = TextAlign.Center,
          )

          if (isRestoring) {
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onRestoreSuccess,
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                shape = RoundedCornerShape(12.dp),
            ) {
              Text("Complete Restore & Open Vault", color = Color(0xFF003324), fontWeight = FontWeight.Bold)
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(32.dp))
    }
  }
}
