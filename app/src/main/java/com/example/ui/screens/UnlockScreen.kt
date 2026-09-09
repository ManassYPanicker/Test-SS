package com.example.ui.screens

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.MockBiometricDialog
import com.example.ui.components.SeedSafeLogo
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.RoseError

@Composable
fun UnlockScreen(
    onUnlockSuccess: () -> Unit,
    onRestoreWithRecoveryPhrase: () -> Unit,
    pinAttemptsRemaining: Int,
    isLockoutActive: Boolean,
    onAttemptPin: (String) -> Boolean,
    onResetLockout: () -> Unit,
) {
  var enteredPin by remember { mutableStateOf("") }
  var showBiometricModal by remember { mutableStateOf(false) }
  var errorMessage by remember { mutableStateOf<String?>(null) }
  var showPinKeypad by remember { mutableStateOf(true) }

  if (showBiometricModal) {
    MockBiometricDialog(
        onSuccess = {
          showBiometricModal = false
          onUnlockSuccess()
        },
        onDismiss = { showBiometricModal = false },
        onPinFallback = {
          showBiometricModal = false
          showPinKeypad = true
        },
    )
  }

  Scaffold(
      containerColor = MaterialTheme.colorScheme.background,
  ) { padding ->
    Column(
        modifier =
            Modifier.fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
      // Top Vault Status Header
      Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier.padding(top = 28.dp),
      ) {
        SeedSafeLogo(size = 54, showText = false)
        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = "SeedSafe Vault Locked",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Enter master PIN or touch biometric sensor to decrypt",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(24.dp))

        // PIN Dot indicators (4 digits)
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
          repeat(4) { index ->
            val isFilled = index < enteredPin.length
            Box(
                modifier =
                    Modifier.size(16.dp)
                        .clip(CircleShape)
                        .background(
                            if (isFilled) EmeraldPrimary else MaterialTheme.colorScheme.surfaceVariant
                        )
                        .border(
                            1.5.dp,
                            if (isFilled) EmeraldPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                            CircleShape,
                        )
            )
          }
        }

        // Error message / Lockout warning
        AnimatedVisibility(visible = errorMessage != null || isLockoutActive) {
          val text =
              if (isLockoutActive) "Too many failed attempts. Vault locked for security."
              else errorMessage.orEmpty()
          Row(
              modifier = Modifier.padding(top = 14.dp),
              verticalAlignment = Alignment.CenterVertically,
          ) {
            Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = RoseError, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                color = RoseError,
            )
          }
        }

        if (isLockoutActive) {
          Spacer(modifier = Modifier.height(8.dp))
          TextButton(onClick = onResetLockout) {
            Text("Reset Lockout (Prototype Demo)", color = EmeraldPrimary, style = MaterialTheme.typography.labelSmall)
          }
        }
      }

      // Numeric Keypad
      Column(
          modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
          horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        val keypadRows =
            listOf(
                listOf("1", "2", "3"),
                listOf("4", "5", "6"),
                listOf("7", "8", "9"),
                listOf("biometric", "0", "backspace"),
            )

        keypadRows.forEach { row ->
          Row(
              modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
              horizontalArrangement = Arrangement.SpaceEvenly,
          ) {
            row.forEach { key ->
              when (key) {
                "biometric" -> {
                  Box(
                      modifier =
                          Modifier.size(76.dp)
                              .clip(CircleShape)
                              .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f))
                              .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f), CircleShape)
                              .clickable { showBiometricModal = true }
                              .testTag("unlock_biometric_btn"),
                      contentAlignment = Alignment.Center,
                  ) {
                    Icon(
                        imageVector = Icons.Default.Fingerprint,
                        contentDescription = "Unlock with Biometrics",
                        tint = EmeraldPrimary,
                        modifier = Modifier.size(36.dp),
                    )
                  }
                }
                "backspace" -> {
                  Box(
                      modifier =
                          Modifier.size(76.dp)
                              .clip(CircleShape)
                              .clickable {
                                if (enteredPin.isNotEmpty()) {
                                  enteredPin = enteredPin.dropLast(1)
                                  errorMessage = null
                                }
                              }
                              .testTag("unlock_key_backspace"),
                      contentAlignment = Alignment.Center,
                  ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Backspace,
                        contentDescription = "Backspace",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(26.dp),
                    )
                  }
                }
                else -> {
                  Box(
                      modifier =
                          Modifier.size(76.dp)
                              .clip(CircleShape)
                              .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f))
                              .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), CircleShape)
                              .clickable(enabled = !isLockoutActive) {
                                if (enteredPin.length < 4) {
                                  val newPin = enteredPin + key
                                  enteredPin = newPin
                                  if (newPin.length == 4) {
                                    val success = onAttemptPin(newPin)
                                    if (success) {
                                      onUnlockSuccess()
                                    } else {
                                      enteredPin = ""
                                      val left = pinAttemptsRemaining - 1
                                      errorMessage = "Incorrect PIN. $left attempts remaining (Demo PIN: 1234)"
                                    }
                                  }
                                }
                              }
                              .testTag("pin_key_$key"),
                      contentAlignment = Alignment.Center,
                  ) {
                    Text(
                        text = key,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold, fontSize = 24.sp),
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                  }
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Security Footnote & Recovery phrase CTA
        TextButton(
            onClick = onRestoreWithRecoveryPhrase,
            modifier = Modifier.testTag("unlock_forgot_pin_btn"),
        ) {
          Text("Forgot PIN? Restore using recovery phrase", color = EmeraldPrimary)
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 4.dp),
        ) {
          Icon(Icons.Default.Security, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(12.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text(
              "Zero-knowledge encrypted locally. Default PIN: 1234",
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
          )
        }
      }
    }
  }
}
