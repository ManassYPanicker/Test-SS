package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.outlined.CloudUpload
import androidx.compose.material.icons.outlined.DeleteForever
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.FileUpload
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.RestartAlt
import androidx.compose.material.icons.outlined.Screenshot
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.BackupStatus
import com.example.ui.components.SecurityBadge
import com.example.ui.theme.AppThemeSetting
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.RoseError

@Composable
fun SettingsScreen(
    themeSetting: AppThemeSetting,
    onThemeSettingChange: (AppThemeSetting) -> Unit,
    biometricsEnabled: Boolean,
    onBiometricsEnabledChange: (Boolean) -> Unit,
    autoLockDuration: String,
    onAutoLockDurationChange: (String) -> Unit,
    screenshotProtection: Boolean,
    onScreenshotProtectionChange: (Boolean) -> Unit,
    clipboardTimeout: String,
    onClipboardTimeoutChange: (String) -> Unit,
    wipeOnFailedAttempts: Boolean,
    onWipeOnFailedAttemptsChange: (Boolean) -> Unit,
    backupStatus: BackupStatus,
    lastBackupTime: String,
    onNavigateToBackup: () -> Unit,
    onNavigateToRestore: () -> Unit,
    onNavigateToSecurityCenter: () -> Unit,
    onNavigateToPrivacyTrust: () -> Unit,
    onNavigateToRecoveryPhrase: () -> Unit,
    onResetToOnboarding: () -> Unit,
    currentPin: String,
    onChangePin: (String) -> Unit,
) {
  val context = LocalContext.current
  var showThemeDialog by remember { mutableStateOf(false) }
  var showAutoLockDialog by remember { mutableStateOf(false) }
  var showClipboardDialog by remember { mutableStateOf(false) }
  var showChangePinDialog by remember { mutableStateOf(false) }
  var showLicenseDialog by remember { mutableStateOf(false) }
  var showResetConfirmDialog by remember { mutableStateOf(false) }

  // Theme Dialog
  if (showThemeDialog) {
    AlertDialog(
        onDismissRequest = { showThemeDialog = false },
        title = { Text("Select App Theme") },
        text = {
          Column {
            AppThemeSetting.entries.forEach { option ->
              val tag = when (option) {
                AppThemeSetting.SYSTEM -> "theme_radio_system"
                AppThemeSetting.DARK -> "theme_radio_dark"
                AppThemeSetting.LIGHT -> "theme_radio_light"
              }
              Row(
                  modifier =
                      Modifier.fillMaxWidth()
                          .testTag(tag)
                          .clickable {
                            onThemeSettingChange(option)
                            showThemeDialog = false
                          }
                          .padding(vertical = 8.dp),
                  verticalAlignment = Alignment.CenterVertically,
              ) {
                RadioButton(
                    selected = themeSetting == option,
                    onClick = {
                      onThemeSettingChange(option)
                      showThemeDialog = false
                    },
                    colors = RadioButtonDefaults.colors(selectedColor = EmeraldPrimary),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    when (option) {
                      AppThemeSetting.SYSTEM -> "System Default (Follows Android OS)"
                      AppThemeSetting.DARK -> "Dark Theme (Security Slate)"
                      AppThemeSetting.LIGHT -> "Light Theme"
                    }
                )
              }
            }
          }
        },
        confirmButton = {
          TextButton(onClick = { showThemeDialog = false }) { Text("Close") }
        },
    )
  }

  // Auto-lock Dialog
  if (showAutoLockDialog) {
    val durations = listOf("Immediate", "1 minute", "5 minutes", "15 minutes")
    AlertDialog(
        onDismissRequest = { showAutoLockDialog = false },
        title = { Text("Auto-Lock Duration") },
        text = {
          Column {
            durations.forEach { duration ->
              Row(
                  modifier =
                      Modifier.fillMaxWidth()
                          .clickable {
                            onAutoLockDurationChange(duration)
                            showAutoLockDialog = false
                          }
                          .padding(vertical = 8.dp),
                  verticalAlignment = Alignment.CenterVertically,
              ) {
                RadioButton(
                    selected = autoLockDuration == duration,
                    onClick = {
                      onAutoLockDurationChange(duration)
                      showAutoLockDialog = false
                    },
                    colors = RadioButtonDefaults.colors(selectedColor = EmeraldPrimary),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(duration)
              }
            }
          }
        },
        confirmButton = {
          TextButton(onClick = { showAutoLockDialog = false }) { Text("Close") }
        },
    )
  }

  // Clipboard Timeout Dialog
  if (showClipboardDialog) {
    val timeouts = listOf("30 seconds", "60 seconds", "Never")
    AlertDialog(
        onDismissRequest = { showClipboardDialog = false },
        title = { Text("Clipboard Auto-Clear") },
        text = {
          Column {
            timeouts.forEach { timeout ->
              Row(
                  modifier =
                      Modifier.fillMaxWidth()
                          .clickable {
                            onClipboardTimeoutChange(timeout)
                            showClipboardDialog = false
                          }
                          .padding(vertical = 8.dp),
                  verticalAlignment = Alignment.CenterVertically,
              ) {
                RadioButton(
                    selected = clipboardTimeout == timeout,
                    onClick = {
                      onClipboardTimeoutChange(timeout)
                      showClipboardDialog = false
                    },
                    colors = RadioButtonDefaults.colors(selectedColor = EmeraldPrimary),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(timeout)
              }
            }
          }
        },
        confirmButton = {
          TextButton(onClick = { showClipboardDialog = false }) { Text("Close") }
        },
    )
  }

  // Change PIN Dialog
  if (showChangePinDialog) {
    var newPinInput by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = { showChangePinDialog = false },
        title = { Text("Change Master PIN") },
        text = {
          Column {
            Text("Current PIN: $currentPin", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = newPinInput,
                onValueChange = { if (it.length <= 4) newPinInput = it },
                label = { Text("New 4-digit PIN") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
          }
        },
        confirmButton = {
          TextButton(
              onClick = {
                if (newPinInput.length == 4) {
                  onChangePin(newPinInput)
                  showChangePinDialog = false
                  Toast.makeText(context, "Master PIN updated to $newPinInput", Toast.LENGTH_SHORT).show()
                } else {
                  Toast.makeText(context, "PIN must be 4 digits", Toast.LENGTH_SHORT).show()
                }
              },
          ) {
            Text("Save PIN", color = EmeraldPrimary)
          }
        },
        dismissButton = {
          TextButton(onClick = { showChangePinDialog = false }) { Text("Cancel") }
        },
    )
  }

  // Open-source licenses dialog
  if (showLicenseDialog) {
    AlertDialog(
        onDismissRequest = { showLicenseDialog = false },
        title = { Text("Open Source & Architecture") },
        text = {
          Column {
            Text(
                "SeedSafe uses open cryptographic primitives: AES-256-GCM, Argon2id, and BIP39 mnemonics. Designed for zero-knowledge offline client sovereignty.",
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("Android Jetpack Compose • Material 3 • Kotlin Coroutines", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        },
        confirmButton = {
          TextButton(onClick = { showLicenseDialog = false }) { Text("Done") }
        },
    )
  }

  // Reset Onboarding Confirmation Dialog
  if (showResetConfirmDialog) {
    AlertDialog(
        onDismissRequest = { showResetConfirmDialog = false },
        icon = { Icon(Icons.Outlined.Warning, contentDescription = null, tint = RoseError) },
        title = { Text("Replay First-Run Onboarding?") },
        text = {
          Text(
              "This will reset your local setup flags and return to the first-run security walkthrough, seed phrase quiz, and PIN setup. Your demo vault state will be re-initialized.",
              style = MaterialTheme.typography.bodyMedium,
          )
        },
        confirmButton = {
          TextButton(
              onClick = {
                showResetConfirmDialog = false
                onResetToOnboarding()
              },
          ) {
            Text("Reset & Replay", color = RoseError, fontWeight = FontWeight.Bold)
          }
        },
        dismissButton = {
          TextButton(onClick = { showResetConfirmDialog = false }) {
            Text("Cancel")
          }
        },
    )
  }

  Scaffold(
      topBar = {
        Row(
            modifier =
                Modifier.fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = "Vault security, user backup, and application preferences",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
          }

          IconButton(
              onClick = {
                val nextSetting =
                    when (themeSetting) {
                      AppThemeSetting.LIGHT -> AppThemeSetting.DARK
                      AppThemeSetting.DARK -> AppThemeSetting.LIGHT
                      AppThemeSetting.SYSTEM -> AppThemeSetting.DARK
                    }
                onThemeSettingChange(nextSetting)
              },
              modifier = Modifier.testTag("theme_toggle_top_bar_btn"),
          ) {
            Icon(
                imageVector =
                    when (themeSetting) {
                      AppThemeSetting.LIGHT -> Icons.Default.DarkMode
                      AppThemeSetting.DARK -> Icons.Default.LightMode
                      AppThemeSetting.SYSTEM -> Icons.Default.BrightnessAuto
                    },
                contentDescription = "Quick Toggle Light/Dark Mode",
                tint = EmeraldPrimary,
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
      // SECTION: Security
      SettingsSectionHeader(title = "Security & Access")

      SettingsCard {
        SettingsToggleRow(
            icon = Icons.Default.Fingerprint,
            title = "Biometric Unlock",
            subtitle = "Use fingerprint or face unlock",
            checked = biometricsEnabled,
            modifier = Modifier.testTag("biometrics_toggle"),
            onCheckedChange = onBiometricsEnabledChange,
        )

        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

        SettingsClickableRow(
            icon = Icons.Default.Lock,
            title = "Master PIN",
            value = "••••",
            modifier = Modifier.testTag("master_pin_setting_row"),
            onClick = { showChangePinDialog = true },
        )

        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

        SettingsClickableRow(
            icon = Icons.Default.Timer,
            title = "Auto-Lock Duration",
            value = autoLockDuration,
            modifier = Modifier.testTag("autolock_setting_row"),
            onClick = { showAutoLockDialog = true },
        )

        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

        SettingsToggleRow(
            icon = Icons.Outlined.Screenshot,
            title = "Screenshot Protection",
            subtitle = "Block screenshots and app previews in recents",
            checked = screenshotProtection,
            modifier = Modifier.testTag("screenshot_protection_toggle"),
            onCheckedChange = onScreenshotProtectionChange,
        )

        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

        SettingsClickableRow(
            icon = Icons.Default.Security,
            title = "Clipboard Auto-Clear",
            value = clipboardTimeout,
            modifier = Modifier.testTag("clipboard_timeout_setting_row"),
            onClick = { showClipboardDialog = true },
        )

        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

        SettingsToggleRow(
            icon = Icons.Default.Shield,
            title = "Self-Destruct on 10 Fails",
            subtitle = "Wipe local encrypted keys after 10 failed unlock attempts",
            checked = wipeOnFailedAttempts,
            modifier = Modifier.testTag("wipe_failed_attempts_toggle"),
            onCheckedChange = onWipeOnFailedAttemptsChange,
        )
      }

      Spacer(modifier = Modifier.height(20.dp))

      // SECTION: Backup & Cloud
      SettingsSectionHeader(title = "Encrypted Backup")

      SettingsCard {
        SettingsClickableRow(
            icon = Icons.Default.CloudDone,
            title = "Google Drive Backup",
            value = if (backupStatus == BackupStatus.SUCCESS) "Connected" else "Not connected",
            modifier = Modifier.testTag("backup_setting_row"),
            onClick = onNavigateToBackup,
        )

        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

        SettingsClickableRow(
            icon = Icons.Outlined.CloudUpload,
            title = "Backup & Restore Center",
            value = "Last: $lastBackupTime",
            modifier = Modifier.testTag("backup_center_setting_row"),
            onClick = onNavigateToBackup,
        )

        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

        SettingsClickableRow(
            icon = Icons.Outlined.FileDownload,
            title = "Export Encrypted Vault (.seedsafe)",
            value = "Local file",
            modifier = Modifier.testTag("export_backup_setting_row"),
            onClick = {
              Toast.makeText(context, "Exported encrypted vault backup to Downloads/SeedSafe_Vault_Backup.seedsafe", Toast.LENGTH_LONG).show()
            },
        )

        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

        SettingsClickableRow(
            icon = Icons.Outlined.FileUpload,
            title = "Restore Vault from Backup",
            value = "Phrase / Cloud",
            modifier = Modifier.testTag("restore_setting_row"),
            onClick = onNavigateToRestore,
        )
      }

      Spacer(modifier = Modifier.height(20.dp))

      // SECTION: Vault Keys & Trust
      SettingsSectionHeader(title = "Vault Cryptography & Audits")

      SettingsCard {
        SettingsClickableRow(
            icon = Icons.Default.Key,
            title = "View 12-Word Recovery Phrase",
            value = "Secret words",
            modifier = Modifier.testTag("recovery_phrase_setting_row"),
            onClick = onNavigateToRecoveryPhrase,
        )

        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

        SettingsClickableRow(
            icon = Icons.Default.Shield,
            title = "Security Center Dashboard",
            value = "Audits & health",
            modifier = Modifier.testTag("security_center_setting_row"),
            onClick = onNavigateToSecurityCenter,
        )

        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

        SettingsClickableRow(
            icon = Icons.Default.PrivacyTip,
            title = "Zero-Knowledge Architecture",
            value = "Privacy & Trust",
            modifier = Modifier.testTag("privacy_trust_setting_row"),
            onClick = onNavigateToPrivacyTrust,
        )
      }

      Spacer(modifier = Modifier.height(20.dp))

      // SECTION: Appearance
      SettingsSectionHeader(title = "Appearance & Theme")

      SettingsCard {
        // Quick Segmented Theme Selector
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)) {
          Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically,
          ) {
            Text(
                text = "Theme Preference",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text =
                    when (themeSetting) {
                      AppThemeSetting.SYSTEM -> "System"
                      AppThemeSetting.DARK -> "Dark"
                      AppThemeSetting.LIGHT -> "Light"
                    },
                style = MaterialTheme.typography.bodySmall,
                color = EmeraldPrimary,
                fontWeight = FontWeight.SemiBold,
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          Row(
              modifier =
                  Modifier.fillMaxWidth()
                      .background(
                          color = MaterialTheme.colorScheme.background,
                          shape = RoundedCornerShape(12.dp),
                      )
                      .padding(4.dp),
              horizontalArrangement = Arrangement.spacedBy(6.dp),
          ) {
            val themeOptions =
                listOf(
                    Triple(AppThemeSetting.SYSTEM, "System", Icons.Default.BrightnessAuto),
                    Triple(AppThemeSetting.LIGHT, "Light", Icons.Default.LightMode),
                    Triple(AppThemeSetting.DARK, "Dark", Icons.Default.DarkMode),
                )

            themeOptions.forEach { (option, label, icon) ->
              val isSelected = themeSetting == option
              Row(
                  modifier =
                      Modifier.weight(1f)
                          .clip(RoundedCornerShape(8.dp))
                          .background(
                              if (isSelected) EmeraldPrimary.copy(alpha = 0.2f) else Color.Transparent
                          )
                          .border(
                              width = if (isSelected) 1.5.dp else 0.dp,
                              color = if (isSelected) EmeraldPrimary else Color.Transparent,
                              shape = RoundedCornerShape(8.dp),
                          )
                          .clickable { onThemeSettingChange(option) }
                          .padding(vertical = 10.dp)
                          .testTag("theme_option_${label.lowercase()}"),
                  horizontalArrangement = Arrangement.Center,
                  verticalAlignment = Alignment.CenterVertically,
              ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = if (isSelected) EmeraldPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp),
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = label,
                    style =
                        MaterialTheme.typography.labelMedium.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        ),
                    color = if (isSelected) EmeraldPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                )
              }
            }
          }
        }

        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

        SettingsClickableRow(
            icon = Icons.Default.Palette,
            title = "Theme Chooser Dialog",
            value =
                when (themeSetting) {
                  AppThemeSetting.SYSTEM -> "System Default"
                  AppThemeSetting.DARK -> "Dark (Security Slate)"
                  AppThemeSetting.LIGHT -> "Light"
                },
            modifier = Modifier.testTag("theme_setting_row"),
            onClick = { showThemeDialog = true },
        )

        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

        SettingsClickableRow(
            icon = Icons.Outlined.Info,
            title = "Language",
            value = "English (US)",
            modifier = Modifier.testTag("language_setting_row"),
            onClick = { Toast.makeText(context, "English is active", Toast.LENGTH_SHORT).show() },
        )
      }

      Spacer(modifier = Modifier.height(20.dp))

      // SECTION: About & Demo Testing Controls
      SettingsSectionHeader(title = "About & Licenses")

      SettingsCard {
        SettingsClickableRow(
            icon = Icons.Outlined.Info,
            title = "Version",
            value = "1.0.0-prototype",
            modifier = Modifier.testTag("version_setting_row"),
            onClick = { Toast.makeText(context, "SeedSafe UI Prototype v1.0.0", Toast.LENGTH_SHORT).show() },
        )

        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

        SettingsClickableRow(
            icon = Icons.Default.Security,
            title = "Open Source & Architecture",
            value = "View details",
            modifier = Modifier.testTag("license_setting_row"),
            onClick = { showLicenseDialog = true },
        )
      }

      Spacer(modifier = Modifier.height(20.dp))

      // SECTION: Danger Zone
      Text(
          text = "Danger Zone",
          style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
          color = RoseError,
          modifier = Modifier.padding(bottom = 8.dp, start = 4.dp),
      )

      Card(
          colors = CardDefaults.cardColors(containerColor = RoseError.copy(alpha = 0.08f)),
          border = androidx.compose.foundation.BorderStroke(1.dp, RoseError.copy(alpha = 0.25f)),
          shape = RoundedCornerShape(16.dp),
          modifier = Modifier.fillMaxWidth().testTag("danger_zone_card"),
      ) {
        Row(
            modifier =
                Modifier.fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { showResetConfirmDialog = true }
                    .padding(horizontal = 16.dp, vertical = 14.dp)
                    .testTag("reset_onboarding_btn"),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
          Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Icon(
                imageVector = Icons.Outlined.DeleteForever,
                contentDescription = null,
                tint = RoseError,
                modifier = Modifier.size(22.dp),
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column {
              Text(
                  text = "Reset Prototype & Replay Onboarding",
                  style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                  color = RoseError,
              )
              Text(
                  text = "Clears unlocked session, restarts security quiz",
                  style = MaterialTheme.typography.labelSmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
              )
            }
          }
          Icon(
              Icons.Default.ChevronRight,
              contentDescription = null,
              tint = RoseError.copy(alpha = 0.7f),
              modifier = Modifier.size(18.dp),
          )
        }
      }

      Spacer(modifier = Modifier.height(72.dp))
    }
  }
}

@Composable
fun SettingsSectionHeader(title: String) {
  Text(
      text = title,
      style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
      color = EmeraldPrimary,
      modifier = Modifier.padding(bottom = 8.dp, start = 4.dp),
  )
}

@Composable
fun SettingsCard(content: @Composable () -> Unit) {
  Card(
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
      shape = RoundedCornerShape(16.dp),
      modifier = Modifier.fillMaxWidth(),
  ) {
    Column { content() }
  }
}

@Composable
fun SettingsClickableRow(
    icon: ImageVector,
    title: String,
    value: String? = null,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
  Row(
      modifier =
          modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(12.dp))
              .clickable(onClick = onClick)
              .padding(horizontal = 16.dp, vertical = 14.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween,
  ) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
      Icon(imageVector = icon, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(20.dp))
      Spacer(modifier = Modifier.width(14.dp))
      Text(text = title, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium), color = MaterialTheme.colorScheme.onSurface)
    }
    Row(verticalAlignment = Alignment.CenterVertically) {
      if (value != null) {
        Text(text = value, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.width(4.dp))
      }
      Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f), modifier = Modifier.size(18.dp))
    }
  }
}

@Composable
fun SettingsToggleRow(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    checked: Boolean,
    modifier: Modifier = Modifier,
    onCheckedChange: (Boolean) -> Unit,
) {
  Row(
      modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween,
  ) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
      Icon(imageVector = icon, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(20.dp))
      Spacer(modifier = Modifier.width(14.dp))
      Column {
        Text(text = title, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium), color = MaterialTheme.colorScheme.onSurface)
        if (subtitle != null) {
          Text(text = subtitle, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
      }
    }
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        colors = SwitchDefaults.colors(checkedThumbColor = EmeraldPrimary),
    )
  }
}
