package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MockVaultData
import com.example.ui.components.SeedSafeLogo
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.RoseError
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit,
) {
  LaunchedEffect(Unit) {
    delay(1400)
    onSplashFinished()
  }

  Box(
      modifier =
          Modifier.fillMaxSize()
              .background(
                  Brush.verticalGradient(
                      listOf(
                          Color(0xFF0A0F1D),
                          Color(0xFF081C18),
                          Color(0xFF0A0F1D),
                      )
                  )
              ),
      contentAlignment = Alignment.Center,
  ) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
      SeedSafeLogo(size = 80)
      Spacer(modifier = Modifier.height(20.dp))
      Text(
          text = "SeedSafe",
          style = MaterialTheme.typography.headlineLarge.copy(
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.sp,
          ),
          color = Color.White,
      )
      Spacer(modifier = Modifier.height(6.dp))
      Text(
          text = "Your vault. Your recovery phrase. Your data never belongs to us.",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          textAlign = TextAlign.Center,
          modifier = Modifier.padding(horizontal = 32.dp),
      )
      Spacer(modifier = Modifier.height(32.dp))
      LinearProgressIndicator(
          modifier = Modifier.width(120.dp).height(3.dp).clip(RoundedCornerShape(2.dp)),
          color = EmeraldPrimary,
          trackColor = Color(0xFF1E293B),
      )
    }
  }
}

data class OnboardingStep(
    val title: String,
    val subtitle: String,
    val description: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val iconColor: Color,
)

@Composable
fun OnboardingScreen(
    onCreateVaultClick: () -> Unit,
    onRestoreVaultClick: () -> Unit,
) {
  val steps =
      remember {
        listOf(
            OnboardingStep(
                title = "Total Data Sovereignty",
                subtitle = "Your vault stays on your device",
                description =
                    "SeedSafe is designed from the ground up with zero-knowledge architecture. There is no central company server holding master keys or tracking activity.",
                icon = Icons.Default.Security,
                iconColor = EmeraldPrimary,
            ),
            OnboardingStep(
                title = "100% Offline by Default",
                subtitle = "Uncompromised local protection",
                description =
                    "Your logins, notes, cards, and identity docs are encrypted directly on this hardware. No internet connection is ever required to unlock or access your data.",
                icon = Icons.Default.Storage,
                iconColor = CyanAccent,
            ),
            OnboardingStep(
                title = "Secret Recovery Phrase",
                subtitle = "You hold the master cryptographic key",
                description =
                    "During setup, you will receive a unique 12-word recovery phrase. This phrase is the ONLY key that can rebuild your vault. Keep it safe and offline.",
                icon = Icons.Default.Key,
                iconColor = AmberWarning,
            ),
            OnboardingStep(
                title = "Personal Cloud Backup",
                subtitle = "User-controlled, encrypted backup",
                description =
                    "Optionally store an encrypted backup file in your personal Google Drive. The file remains end-to-end encrypted; neither Google nor SeedSafe can read it.",
                icon = Icons.Default.CloudDone,
                iconColor = EmeraldPrimary,
            ),
        )
      }

  var currentStep by remember { mutableIntStateOf(0) }

  Scaffold(
      containerColor = MaterialTheme.colorScheme.background,
  ) { padding ->
    Column(
        modifier =
            Modifier.fillMaxSize()
                .padding(padding)
                .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Row(
          modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically,
      ) {
        SeedSafeLogo(size = 36, showText = true)
        if (currentStep < steps.size - 1) {
          TextButton(onClick = { currentStep = steps.size - 1 }) {
            Text("Skip", color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        }
      }

      Spacer(modifier = Modifier.height(32.dp))

      // Content Card
      val step = steps[currentStep]
      Column(
          modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center,
      ) {
        Box(
            modifier =
                Modifier.size(96.dp)
                    .clip(CircleShape)
                    .background(step.iconColor.copy(alpha = 0.12f))
                    .border(1.5.dp, step.iconColor.copy(alpha = 0.35f), CircleShape),
            contentAlignment = Alignment.Center,
        ) {
          Icon(
              imageVector = step.icon,
              contentDescription = null,
              tint = step.iconColor,
              modifier = Modifier.size(48.dp),
          )
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = step.title,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = step.subtitle,
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
            color = step.iconColor,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = step.description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp,
        )
      }

      // Step indicators
      Row(
          horizontalArrangement = Arrangement.Center,
          modifier = Modifier.padding(vertical = 20.dp),
      ) {
        steps.forEachIndexed { index, _ ->
          Box(
              modifier =
                  Modifier.padding(horizontal = 4.dp)
                      .size(width = if (index == currentStep) 24.dp else 8.dp, height = 8.dp)
                      .clip(CircleShape)
                      .background(
                          if (index == currentStep) EmeraldPrimary
                          else MaterialTheme.colorScheme.surfaceVariant
                      )
          )
        }
      }

      // Buttons
      if (currentStep < steps.size - 1) {
        Button(
            onClick = { currentStep++ },
            colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
            modifier = Modifier.fillMaxWidth().height(52.dp).testTag("onboarding_next_btn"),
            shape = RoundedCornerShape(14.dp),
        ) {
          Text("Continue", color = Color(0xFF003324), fontWeight = FontWeight.SemiBold)
          Spacer(modifier = Modifier.width(8.dp))
          Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color(0xFF003324))
        }
      } else {
        Column(modifier = Modifier.fillMaxWidth()) {
          Button(
              onClick = onCreateVaultClick,
              colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
              modifier = Modifier.fillMaxWidth().height(52.dp).testTag("create_vault_cta_btn"),
              shape = RoundedCornerShape(14.dp),
          ) {
            Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF003324))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Create My Vault", color = Color(0xFF003324), fontWeight = FontWeight.Bold)
          }

          Spacer(modifier = Modifier.height(10.dp))

          OutlinedButton(
              onClick = onRestoreVaultClick,
              modifier = Modifier.fillMaxWidth().height(52.dp).testTag("restore_vault_cta_btn"),
              shape = RoundedCornerShape(14.dp),
          ) {
            Text("Restore an Existing Vault", color = MaterialTheme.colorScheme.onSurface)
          }
        }
      }
    }
  }
}

@Composable
fun CreateVaultScreen(
    onContinueToQuiz: () -> Unit,
    onBack: () -> Unit,
) {
  val words = MockVaultData.mockRecoveryPhrase
  val clipboardManager = LocalClipboardManager.current
  val context = LocalContext.current
  var wordsRevealed by remember { mutableStateOf(false) }
  var hasConfirmedWarning by remember { mutableStateOf(false) }

  Scaffold(
      topBar = {
        Row(
            modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(horizontal = 8.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
          IconButton(onClick = onBack, modifier = Modifier.testTag("create_vault_back_btn")) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
          }
          Spacer(modifier = Modifier.width(4.dp))
          Column {
            Text(
                text = "Secret Recovery Phrase",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = "Your master cryptographic seed",
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
      Text(
          text = "Write Down Your 12 Words",
          style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.onBackground,
      )
      Spacer(modifier = Modifier.height(6.dp))
      Text(
          text =
              "These 12 words form the master cryptographic key to your vault. If you lose access to this device, this phrase is the only way to recover your data.",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
      )

      Spacer(modifier = Modifier.height(20.dp))

      // Recovery Words Grid Card
      Card(
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
          shape = RoundedCornerShape(18.dp),
          modifier = Modifier.fillMaxWidth(),
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically,
          ) {
            Text(
                text = "12-WORD SEED PHRASE",
                style = MaterialTheme.typography.labelMedium.copy(letterSpacing = 1.sp),
                color = EmeraldPrimary,
                fontWeight = FontWeight.Bold,
            )
            Row {
              IconButton(onClick = { wordsRevealed = !wordsRevealed }) {
                Icon(
                    imageVector = if (wordsRevealed) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                    contentDescription = "Toggle visibility",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
              }
              IconButton(
                  onClick = {
                    clipboardManager.setText(AnnotatedString(words.joinToString(" ")))
                    Toast.makeText(context, "Recovery phrase copied to clipboard", Toast.LENGTH_SHORT).show()
                  },
              ) {
                Icon(Icons.Default.ContentCopy, contentDescription = "Copy words", tint = MaterialTheme.colorScheme.onSurfaceVariant)
              }
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          LazyVerticalGrid(
              columns = GridCells.Fixed(2),
              modifier = Modifier.height(250.dp),
              verticalArrangement = Arrangement.spacedBy(8.dp),
              horizontalArrangement = Arrangement.spacedBy(8.dp),
          ) {
            itemsIndexed(words) { index, word ->
              Row(
                  modifier =
                      Modifier.clip(RoundedCornerShape(10.dp))
                          .background(MaterialTheme.colorScheme.surface)
                          .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
                          .padding(horizontal = 12.dp, vertical = 10.dp),
                  verticalAlignment = Alignment.CenterVertically,
              ) {
                Text(
                    text = "${index + 1}.",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = EmeraldPrimary,
                    modifier = Modifier.width(26.dp),
                )
                Text(
                    text = if (wordsRevealed) word else "••••••",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.SemiBold,
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                )
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Warning Card
      Card(
          colors = CardDefaults.cardColors(containerColor = AmberWarning.copy(alpha = 0.1f)),
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier.fillMaxWidth().border(1.dp, AmberWarning.copy(alpha = 0.3f), RoundedCornerShape(14.dp)),
      ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.Top) {
          Icon(Icons.Default.WarningAmber, contentDescription = null, tint = AmberWarning, modifier = Modifier.size(24.dp))
          Spacer(modifier = Modifier.width(12.dp))
          Column {
            Text("Never Share Your Secret Phrase", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = AmberWarning)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Anyone who gains access to these 12 words can decrypt everything. SeedSafe support will NEVER ask for your secret recovery phrase.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      Row(
          modifier = Modifier.fillMaxWidth().clickable { hasConfirmedWarning = !hasConfirmedWarning }.padding(vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically,
      ) {
        Checkbox(checked = hasConfirmedWarning, onCheckedChange = { hasConfirmedWarning = it })
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "I have written down or backed up my recovery phrase in a safe place.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
      }

      Spacer(modifier = Modifier.height(24.dp))

      Button(
          onClick = onContinueToQuiz,
          enabled = hasConfirmedWarning,
          colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
          modifier = Modifier.fillMaxWidth().height(52.dp).testTag("verify_recovery_phrase_btn"),
          shape = RoundedCornerShape(14.dp),
      ) {
        Text("Verify Recovery Phrase", color = Color(0xFF003324), fontWeight = FontWeight.Bold)
      }

      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}

@Composable
fun RecoveryPhraseConfirmationScreen(
    onQuizPassed: () -> Unit,
    onBack: () -> Unit,
) {
  val words = MockVaultData.mockRecoveryPhrase
  // Quiz: Verify 3 random words, e.g. Word 4 ("foster"), Word 7 ("island"), Word 10 ("nebula")
  val quizStages =
      remember {
        listOf(
            Triple(3, words[3], listOf("foster", "galaxy", "island", "puzzle")),
            Triple(6, words[6], listOf("canvas", "island", "matrix", "desert")),
            Triple(9, words[9], listOf("oxygen", "nebula", "harbor", "abandon")),
        )
      }

  var currentStageIndex by remember { mutableIntStateOf(0) }
  var selectedWord by remember { mutableStateOf<String?>(null) }
  var hasError by remember { mutableStateOf(false) }
  var isCompleted by remember { mutableStateOf(false) }

  val (targetIndex, correctWord, options) = quizStages[currentStageIndex]

  Scaffold(
      topBar = {
        Row(
            modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(horizontal = 8.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
          IconButton(onClick = onBack, modifier = Modifier.testTag("quiz_back_btn")) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
          }
          Spacer(modifier = Modifier.width(4.dp))
          Column {
            Text(
                text = "Confirm Recovery Phrase",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = "Verification test to ensure phrase was saved",
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
                .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      if (!isCompleted) {
        // Progress indicator
        LinearProgressIndicator(
            progress = { (currentStageIndex + 1) / 3f },
            modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
            color = EmeraldPrimary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Step ${currentStageIndex + 1} of 3",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Select Word #${targetIndex + 1}",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Choose the correct word that matches position #${targetIndex + 1} from your written recovery phrase.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Options Grid
        options.forEach { option ->
          val isSelected = selectedWord == option
          Card(
              modifier =
                  Modifier.fillMaxWidth()
                      .padding(vertical = 6.dp)
                      .clip(RoundedCornerShape(14.dp))
                      .clickable {
                        selectedWord = option
                        hasError = false
                      },
              colors =
                  CardDefaults.cardColors(
                      containerColor =
                          if (isSelected) EmeraldPrimary.copy(alpha = 0.15f)
                          else MaterialTheme.colorScheme.surfaceVariant
                  ),
              shape = RoundedCornerShape(14.dp),
              border =
                  if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, EmeraldPrimary)
                  else null,
          ) {
            Row(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
              Text(
                  text = option,
                  style = MaterialTheme.typography.titleMedium.copy(
                      fontFamily = FontFamily.Monospace,
                      fontWeight = FontWeight.Bold,
                  ),
                  color = if (isSelected) EmeraldPrimary else MaterialTheme.colorScheme.onSurface,
              )
              if (isSelected) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldPrimary)
              }
            }
          }
        }

        AnimatedVisibility(visible = hasError) {
          Row(
              modifier = Modifier.padding(top = 16.dp),
              verticalAlignment = Alignment.CenterVertically,
          ) {
            Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = RoseError, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Incorrect word chosen. Please verify with your backup.", color = RoseError, style = MaterialTheme.typography.bodySmall)
          }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
              if (selectedWord == correctWord) {
                if (currentStageIndex < 2) {
                  currentStageIndex++
                  selectedWord = null
                  hasError = false
                } else {
                  isCompleted = true
                }
              } else {
                hasError = true
              }
            },
            enabled = selectedWord != null,
            colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
            modifier = Modifier.fillMaxWidth().height(52.dp).testTag("quiz_confirm_selection_btn"),
            shape = RoundedCornerShape(14.dp),
        ) {
          Text("Confirm Selection", color = Color(0xFF003324), fontWeight = FontWeight.Bold)
        }
      } else {
        // Vault Successfully Created State
        Spacer(modifier = Modifier.height(40.dp))
        Box(
            modifier =
                Modifier.size(88.dp)
                    .clip(CircleShape)
                    .background(EmeraldPrimary.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center,
        ) {
          Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(48.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Vault Verified & Initialized!",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Your 12-word recovery phrase has been confirmed. Your offline-first personal vault is now ready to store passwords, notes, IDs, and cards securely.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp,
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onQuizPassed,
            colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
            modifier = Modifier.fillMaxWidth().height(52.dp).testTag("enter_vault_btn"),
            shape = RoundedCornerShape(14.dp),
        ) {
          Text("Enter Vault", color = Color(0xFF003324), fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}
