package com.example.ui.components

import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.os.Build
import android.os.PersistableBundle
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Key
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Note
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.CardItem
import com.example.model.DocumentItem
import com.example.model.IdItem
import com.example.model.LoginItem
import com.example.model.NoteItem
import com.example.model.VaultCategory
import com.example.model.VaultItem
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.BlueShield
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldContainerDark
import com.example.ui.theme.EmeraldOnPrimary
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GlassBorderDark
import com.example.ui.theme.GlassBorderEmerald
import com.example.ui.theme.GlassOverlayScrim
import com.example.ui.theme.GlassSurfaceDark
import com.example.ui.theme.PurpleKey
import com.example.ui.theme.RoseError
import com.example.ui.theme.TextDarkPrimary
import com.example.ui.theme.TextDarkSecondary
import com.example.ui.theme.VaultDarkBorder
import kotlin.random.Random

@Composable
fun SeedSafeLogo(
    modifier: Modifier = Modifier,
    size: Int = 56,
    showText: Boolean = false,
) {
  Row(
      modifier = modifier,
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center,
  ) {
    Box(
        modifier =
            Modifier.size(size.dp)
                .clip(RoundedCornerShape((size / 4).dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFF0F2027), Color(0xFF203A43), Color(0xFF004D40))
                    )
                )
                .border(
                    width = 1.5.dp,
                    brush = Brush.linearGradient(listOf(EmeraldPrimary, CyanAccent)),
                    shape = RoundedCornerShape((size / 4).dp)
                ),
        contentAlignment = Alignment.Center,
    ) {
      Icon(
          imageVector = androidx.compose.material.icons.Icons.Filled.Shield,
          contentDescription = "SeedSafe Shield",
          tint = EmeraldPrimary,
          modifier = Modifier.size((size * 0.58).dp),
      )
      Icon(
          imageVector = androidx.compose.material.icons.Icons.Filled.Lock,
          contentDescription = null,
          tint = Color(0xFF0A0F1D),
          modifier = Modifier.size((size * 0.28).dp),
      )
    }

    if (showText) {
      Spacer(modifier = Modifier.width(12.dp))
      Column {
        Text(
            text = "SeedSafe",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                letterSpacing = 0.5.sp,
            ),
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = "ZERO-KNOWLEDGE VAULT",
            style = MaterialTheme.typography.labelSmall.copy(
                letterSpacing = 1.5.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
            ),
            color = EmeraldPrimary,
        )
      }
    }
  }
}

fun getCategoryIcon(category: VaultCategory): ImageVector =
    when (category) {
      VaultCategory.ALL -> androidx.compose.material.icons.Icons.Default.Shield
      VaultCategory.LOGIN -> androidx.compose.material.icons.Icons.Outlined.Key
      VaultCategory.NOTE -> androidx.compose.material.icons.Icons.Outlined.Note
      VaultCategory.ID_CARD -> androidx.compose.material.icons.Icons.Outlined.Badge
      VaultCategory.PAYMENT_CARD -> androidx.compose.material.icons.Icons.Outlined.CreditCard
      VaultCategory.DOCUMENT -> androidx.compose.material.icons.Icons.Outlined.Description
    }

fun getCategoryColor(category: VaultCategory): Color =
    when (category) {
      VaultCategory.ALL -> EmeraldPrimary
      VaultCategory.LOGIN -> EmeraldPrimary
      VaultCategory.NOTE -> AmberWarning
      VaultCategory.ID_CARD -> BlueShield
      VaultCategory.PAYMENT_CARD -> PurpleKey
      VaultCategory.DOCUMENT -> CyanAccent
    }

@Composable
fun VaultItemCard(
    item: VaultItem,
    onClick: () -> Unit,
    onFavoriteToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
  val catColor = getCategoryColor(item.category)
  val catIcon = getCategoryIcon(item.category)

  val subtitle =
      when (item) {
        is LoginItem -> item.username
        is NoteItem -> item.content.lines().firstOrNull().orEmpty()
        is IdItem -> "${item.idType} • ${item.idNumber.take(4)}••••"
        is CardItem -> "•••• ${item.cardNumber.takeLast(4)} • Exp ${item.expiry}"
        is DocumentItem -> "${item.fileType} • ${item.fileSize}"
      }

  androidx.compose.material3.Card(
      modifier =
          modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(20.dp))
              .shadow(6.dp, RoundedCornerShape(20.dp), ambientColor = catColor.copy(alpha = 0.15f), spotColor = catColor.copy(alpha = 0.25f))
              .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(20.dp))
              .clickable(onClick = onClick)
              .testTag("vault_item_${item.id}"),
      colors =
          androidx.compose.material3.CardDefaults.cardColors(
              containerColor = MaterialTheme.colorScheme.surfaceVariant
          ),
      shape = RoundedCornerShape(20.dp),
  ) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
      Box(
          modifier =
              Modifier.size(44.dp)
                  .clip(RoundedCornerShape(12.dp))
                  .background(catColor.copy(alpha = 0.15f))
                  .border(1.dp, catColor.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
          contentAlignment = Alignment.Center,
      ) {
        Icon(
            imageVector = catIcon,
            contentDescription = item.category.title,
            tint = catColor,
            modifier = Modifier.size(22.dp),
        )
      }

      Spacer(modifier = Modifier.width(14.dp))

      Column(modifier = Modifier.weight(1f)) {
        Text(
            text = item.title,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = item.updatedAt,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
        )
      }

      androidx.compose.material3.IconButton(
          onClick = onFavoriteToggle,
          modifier = Modifier.testTag("fav_btn_${item.id}"),
      ) {
        Icon(
            imageVector = if (item.isFavorite) androidx.compose.material.icons.Icons.Filled.Star else androidx.compose.material.icons.Icons.Outlined.StarBorder,
            contentDescription = if (item.isFavorite) "Remove from favorites" else "Add to favorites",
            tint = if (item.isFavorite) AmberWarning else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(22.dp),
        )
      }
    }
  }
}

@Composable
fun CopyButton(
    textToCopy: String,
    label: String = "Copied to clipboard",
    modifier: Modifier = Modifier,
) {
  val clipboardManager = LocalClipboardManager.current
  val context = LocalContext.current

  androidx.compose.material3.IconButton(
      onClick = {
        val sysClipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? android.content.ClipboardManager
        if (sysClipboard != null) {
          val clip = ClipData.newPlainText("SeedSafe Item", textToCopy)
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            clip.description.extras = PersistableBundle().apply {
              putBoolean(ClipDescription.EXTRA_IS_SENSITIVE, true)
            }
          }
          sysClipboard.setPrimaryClip(clip)
        } else {
          clipboardManager.setText(AnnotatedString(textToCopy))
        }
        Toast.makeText(context, label, Toast.LENGTH_SHORT).show()
      },
      modifier = modifier.testTag("copy_btn_${textToCopy.hashCode()}"),
  ) {
    Icon(
        imageVector = androidx.compose.material.icons.Icons.Default.ContentCopy,
        contentDescription = "Copy to clipboard",
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier.size(20.dp),
    )
  }
}

@Composable
fun SecurityBadge(
    text: String,
    icon: ImageVector = androidx.compose.material.icons.Icons.Default.Shield,
    color: Color = EmeraldPrimary,
    modifier: Modifier = Modifier,
) {
  Row(
      modifier =
          modifier
              .clip(RoundedCornerShape(8.dp))
              .background(color.copy(alpha = 0.12f))
              .border(0.8.dp, color.copy(alpha = 0.25f), RoundedCornerShape(8.dp))
              .padding(horizontal = 8.dp, vertical = 4.dp),
      verticalAlignment = Alignment.CenterVertically,
  ) {
    Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(13.dp))
    Spacer(modifier = Modifier.width(5.dp))
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Medium),
        color = color,
    )
  }
}

@Composable
fun PasswordStrengthBar(
    password: String,
    modifier: Modifier = Modifier,
) {
  val (score, strengthText, color) = remember(password) {
    when {
      password.isEmpty() -> Triple(0f, "Enter password", Color.Gray)
      password.length < 8 -> Triple(0.25f, "Weak", RoseError)
      password.length < 12 -> Triple(0.55f, "Fair", AmberWarning)
      password.any { it.isDigit() } && password.any { !it.isLetterOrDigit() } && password.length >= 14 ->
          Triple(1.0f, "Excellent", EmeraldPrimary)
      else -> Triple(0.75f, "Strong", CyanAccent)
    }
  }

  val animatedProgress by animateFloatAsState(targetValue = score, animationSpec = tween(300), label = "p_bar")

  Column(modifier = modifier.fillMaxWidth()) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
      Text(
          text = "Security Strength",
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
      Text(
          text = strengthText,
          style = MaterialTheme.typography.labelSmall.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold),
          color = color,
      )
    }
    Spacer(modifier = Modifier.height(4.dp))
    LinearProgressIndicator(
        progress = { animatedProgress },
        modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
        color = color,
        trackColor = MaterialTheme.colorScheme.surfaceVariant,
    )
  }
}

@Composable
fun PasswordGeneratorDialog(
    onDismiss: () -> Unit,
    onPasswordGenerated: (String) -> Unit,
) {
  var length by remember { mutableFloatStateOf(16f) }
  var includeUpper by remember { mutableStateOf(true) }
  var includeNumbers by remember { mutableStateOf(true) }
  var includeSymbols by remember { mutableStateOf(true) }

  fun generate(): String {
    val lower = "abcdefghijklmnopqrstuvwxyz"
    val upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    val digits = "0123456789"
    val syms = "!@#\$%^&*()-_=+[]{}<>?"

    var pool = lower
    if (includeUpper) pool += upper
    if (includeNumbers) pool += digits
    if (includeSymbols) pool += syms

    return (1..length.toInt())
        .map { pool[Random.nextInt(pool.length)] }
        .joinToString("")
  }

  var currentGenerated by remember { mutableStateOf(generate()) }

  AlertDialog(
      onDismissRequest = onDismiss,
      title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(androidx.compose.material.icons.Icons.Default.Key, contentDescription = null, tint = EmeraldPrimary)
          Spacer(modifier = Modifier.width(8.dp))
          Text("Password Generator")
        }
      },
      text = {
        Column {
          androidx.compose.material3.Card(
              colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.fillMaxWidth(),
          ) {
            Row(
                modifier = Modifier.padding(14.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
              Text(
                  text = currentGenerated,
                  fontFamily = FontFamily.Monospace,
                  style = MaterialTheme.typography.bodyLarge.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold),
                  color = EmeraldPrimary,
                  modifier = Modifier.weight(1f),
              )
              androidx.compose.material3.IconButton(onClick = { currentGenerated = generate() }) {
                Icon(androidx.compose.material.icons.Icons.Default.Refresh, contentDescription = "Regenerate")
              }
            }
          }

          Spacer(modifier = Modifier.height(16.dp))
          Text(
              text = "Length: ${length.toInt()} characters",
              style = MaterialTheme.typography.bodyMedium.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Medium),
          )
          Slider(
              value = length,
              onValueChange = {
                length = it
                currentGenerated = generate()
              },
              valueRange = 8f..32f,
              steps = 23,
              colors = SliderDefaults.colors(thumbColor = EmeraldPrimary, activeTrackColor = EmeraldPrimary),
          )

          Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = includeUpper,
                onCheckedChange = {
                  includeUpper = it
                  currentGenerated = generate()
                },
            )
            Text("Include Uppercase (A-Z)")
          }
          Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = includeNumbers,
                onCheckedChange = {
                  includeNumbers = it
                  currentGenerated = generate()
                },
            )
            Text("Include Numbers (0-9)")
          }
          Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = includeSymbols,
                onCheckedChange = {
                  includeSymbols = it
                  currentGenerated = generate()
                },
            )
            Text("Include Symbols (!@#$)")
          }
        }
      },
      confirmButton = {
        Button(
            onClick = {
              onPasswordGenerated(currentGenerated)
              onDismiss()
            },
            colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
        ) {
          Text("Use Password", color = Color(0xFF003324))
        }
      },
      dismissButton = {
        TextButton(onClick = onDismiss) {
          Text("Cancel")
        }
      },
  )
}

@Composable
fun DeleteConfirmationDialog(
    itemTitle: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
  AlertDialog(
      onDismissRequest = onDismiss,
      title = { Text("Delete Vault Item?") },
      text = {
        Text(
            "Are you sure you want to permanently delete '$itemTitle'? This action cannot be undone because SeedSafe is zero-knowledge and stores no central copies."
        )
      },
      confirmButton = {
        Button(
            onClick = {
              onConfirm()
              onDismiss()
            },
            colors = ButtonDefaults.buttonColors(containerColor = RoseError),
            modifier = Modifier.testTag("confirm_delete_btn"),
        ) {
          Text("Delete Permanently", color = Color.White)
        }
      },
      dismissButton = {
        TextButton(onClick = onDismiss) {
          Text("Cancel")
        }
      },
  )
}

@Composable
fun MockBiometricDialog(
    onSuccess: () -> Unit,
    onDismiss: () -> Unit,
    onPinFallback: () -> Unit,
) {
  Dialog(
      onDismissRequest = onDismiss,
      properties = DialogProperties(usePlatformDefaultWidth = false),
  ) {
    Box(
        modifier =
            Modifier.fillMaxSize()
                .background(GlassOverlayScrim)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onDismiss,
                )
                .padding(20.dp),
        contentAlignment = Alignment.Center,
    ) {
      Surface(
          shape = RoundedCornerShape(28.dp),
          color = GlassSurfaceDark,
          tonalElevation = 8.dp,
          shadowElevation = 24.dp,
          modifier =
              Modifier.fillMaxWidth()
                  .clip(RoundedCornerShape(28.dp))
                  .border(
                      width = 1.dp,
                      brush =
                          Brush.verticalGradient(
                              listOf(
                                  GlassBorderDark,
                                  GlassBorderEmerald.copy(alpha = 0.3f),
                                  Color.Transparent,
                              )
                          ),
                      shape = RoundedCornerShape(28.dp),
                  )
                  .clickable(
                      interactionSource = remember { MutableInteractionSource() },
                      indication = null,
                      onClick = {},
                  ),
      ) {
        Column(
            modifier = Modifier.padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
          // Oversized Biometric Sensor Touch Target (prevents misclicks)
          Box(
              modifier =
                  Modifier.size(88.dp)
                      .clip(CircleShape)
                      .background(
                          Brush.radialGradient(
                              listOf(
                                  EmeraldPrimary.copy(alpha = 0.22f),
                                  EmeraldPrimary.copy(alpha = 0.04f),
                                  Color.Transparent,
                              )
                          )
                      )
                      .border(1.5.dp, EmeraldPrimary.copy(alpha = 0.6f), CircleShape)
                      .clickable(onClick = {
                        onSuccess()
                        onDismiss()
                      })
                      .testTag("biometric_sensor_target"),
              contentAlignment = Alignment.Center,
          ) {
            Icon(
                imageVector = androidx.compose.material.icons.Icons.Default.Fingerprint,
                contentDescription = "Biometric Sensor",
                tint = EmeraldPrimary,
                modifier = Modifier.size(52.dp),
            )
          }

          Spacer(modifier = Modifier.height(20.dp))

          Text(
              text = "Biometric Verification",
              style = MaterialTheme.typography.titleLarge.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold),
              color = TextDarkPrimary,
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
              text = "Touch the fingerprint sensor or verify your face to access your zero-knowledge vault.",
              style = MaterialTheme.typography.bodyMedium,
              color = TextDarkSecondary,
              textAlign = androidx.compose.ui.text.style.TextAlign.Center,
          )

          Spacer(modifier = Modifier.height(28.dp))

          Button(
              onClick = {
                onSuccess()
                onDismiss()
              },
              colors =
                  ButtonDefaults.buttonColors(
                      containerColor = EmeraldPrimary,
                      contentColor = EmeraldOnPrimary,
                  ),
              shape = RoundedCornerShape(14.dp),
              modifier = Modifier.fillMaxWidth().height(52.dp).testTag("simulate_biometric_success"),
          ) {
            Text(
                text = "Simulate Biometric Match",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold),
            )
          }

          Spacer(modifier = Modifier.height(12.dp))

          OutlinedButton(
              onClick = {
                onDismiss()
                onPinFallback()
              },
              shape = RoundedCornerShape(14.dp),
              border = androidx.compose.foundation.BorderStroke(1.dp, VaultDarkBorder),
              modifier = Modifier.fillMaxWidth().height(52.dp).testTag("biometric_fallback_pin_btn"),
          ) {
            Text(
                text = "Use PIN Fallback",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold),
                color = TextDarkPrimary,
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          TextButton(
              onClick = onDismiss,
              modifier = Modifier.testTag("biometric_cancel_btn"),
          ) {
            Text(
                text = "Cancel",
                color = TextDarkSecondary,
                style = MaterialTheme.typography.bodyMedium,
            )
          }
        }
      }
    }
  }
}

@Composable
fun EmptyVaultState(
    title: String,
    subtitle: String,
    icon: ImageVector = androidx.compose.material.icons.Icons.Outlined.Lock,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
  Column(
      modifier = modifier.fillMaxWidth().padding(32.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
  ) {
    Box(
        modifier =
            Modifier.size(80.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), CircleShape),
        contentAlignment = Alignment.Center,
    ) {
      Icon(
          imageVector = icon,
          contentDescription = null,
          tint = EmeraldPrimary,
          modifier = Modifier.size(36.dp),
      )
    }
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold),
        color = MaterialTheme.colorScheme.onSurface,
        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
    )
    Spacer(modifier = Modifier.height(6.dp))
    Text(
        text = subtitle,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
    )
    if (actionLabel != null && onAction != null) {
      Spacer(modifier = Modifier.height(20.dp))
      Button(
          onClick = onAction,
          colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
      ) {
        Text(actionLabel, color = Color(0xFF003324))
      }
    }
  }
}

@Composable
fun SynthwaveMaskedPasswordRow(
    label: String,
    secretValue: String,
    modifier: Modifier = Modifier,
) {
  var isRevealed by remember { mutableStateOf(false) }
  val clipboardManager = androidx.compose.ui.platform.LocalClipboardManager.current
  val context = androidx.compose.ui.platform.LocalContext.current

  androidx.compose.material3.Card(
      shape = RoundedCornerShape(20.dp),
      colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = com.example.ui.theme.VaultDarkSurfaceVariant),
      modifier = modifier
          .fillMaxWidth()
          .shadow(elevation = 12.dp, shape = RoundedCornerShape(20.dp), ambientColor = com.example.ui.theme.EmeraldPrimary.copy(alpha = 0.2f), spotColor = com.example.ui.theme.EmeraldPrimary.copy(alpha = 0.35f))
          .border(1.dp, com.example.ui.theme.VaultDarkBorder, RoundedCornerShape(20.dp)),
  ) {
    Column(modifier = Modifier.padding(18.dp)) {
      Text(
          text = label.uppercase(),
          style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
              letterSpacing = 1.2.sp,
          ),
          color = com.example.ui.theme.TextDarkSecondary,
      )

      Spacer(modifier = Modifier.height(10.dp))

      Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically,
      ) {
        // Value Text (Masked Dots vs Monospace Decrypted)
        Text(
            text = if (isRevealed) secretValue else "••••••••••••••••",
            style = if (isRevealed) {
              MaterialTheme.typography.titleMedium.copy(
                  fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                  fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
                  letterSpacing = 1.sp,
              )
            } else {
              MaterialTheme.typography.titleLarge.copy(
                  fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                  letterSpacing = 2.sp,
              )
            },
            color = if (isRevealed) com.example.ui.theme.EmeraldPrimary else com.example.ui.theme.TextDarkPrimary,
            modifier = Modifier.weight(1f),
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          // Reveal / Hide Action
          androidx.compose.material3.IconButton(
              onClick = { isRevealed = !isRevealed },
              modifier = Modifier
                  .size(44.dp)
                  .clip(CircleShape)
                  .background(com.example.ui.theme.VaultDarkSurface),
          ) {
            Icon(
                imageVector = if (isRevealed) androidx.compose.material.icons.Icons.Default.VisibilityOff else androidx.compose.material.icons.Icons.Default.Visibility,
                contentDescription = if (isRevealed) "Hide Secret" else "Reveal Secret",
                tint = com.example.ui.theme.TextDarkSecondary,
                modifier = Modifier.size(20.dp),
            )
          }

          // Glowing Quick Copy Button (Oversized 44dp Touch Zone)
          Box(
              modifier = Modifier
                  .size(44.dp)
                  .clip(CircleShape)
                  .background(com.example.ui.theme.EmeraldPrimary)
                  .clickable {
                    clipboardManager.setText(androidx.compose.ui.text.AnnotatedString(secretValue))
                    Toast.makeText(context, "$label copied to clipboard", Toast.LENGTH_SHORT).show()
                  },
              contentAlignment = Alignment.Center,
          ) {
            Icon(
                imageVector = androidx.compose.material.icons.Icons.Default.ContentCopy,
                contentDescription = "Quick Copy",
                tint = Color.White,
                modifier = Modifier.size(20.dp),
            )
          }
        }
      }
    }
  }
}
