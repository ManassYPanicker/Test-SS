package com.example.ui.screens

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
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.SecurityScore
import com.example.model.VaultItem
import com.example.ui.components.SecurityBadge
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.RoseError

@Composable
fun SecurityCenterScreen(
    securityScore: SecurityScore,
    weakItems: List<VaultItem>,
    reusedItems: List<VaultItem>,
    onFixItem: (VaultItem) -> Unit,
    onBack: () -> Unit,
) {
  Scaffold(
      topBar = {
        Row(
            modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(horizontal = 8.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
          IconButton(onClick = onBack, modifier = Modifier.testTag("security_center_back_btn")) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
          }
          Spacer(modifier = Modifier.width(4.dp))
          Column {
            Text(
                text = "Security Center",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = "Vault audits and vulnerability detection",
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

      // Score Dial Card
      Card(
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
          border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.25f)),
          shape = RoundedCornerShape(20.dp),
          modifier = Modifier.fillMaxWidth(),
      ) {
        Column(
            modifier = Modifier.padding(24.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
          Box(
              modifier =
                  Modifier.size(110.dp)
                      .clip(CircleShape)
                      .background(EmeraldPrimary.copy(alpha = 0.15f))
                      .border(3.dp, EmeraldPrimary, CircleShape),
              contentAlignment = Alignment.Center,
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text(
                  text = "${securityScore.score}",
                  style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                  color = EmeraldPrimary,
              )
              Text("OF 100", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold), color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          Text(
              text = if (securityScore.score >= 80) "Vault Protection: Strong" else "Attention Recommended",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onSurface,
          )

          Spacer(modifier = Modifier.height(6.dp))

          Text(
              text = "No compromised credentials detected. Designed for local hardware-backed isolation and zero-knowledge storage.",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              textAlign = TextAlign.Center,
              lineHeight = 18.sp,
          )
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Metrics Row
      Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp),
      ) {
        MetricCard(
            title = "Weak",
            count = securityScore.weakCount,
            color = if (securityScore.weakCount > 0) AmberWarning else EmeraldPrimary,
            modifier = Modifier.weight(1f),
        )
        MetricCard(
            title = "Reused",
            count = securityScore.reusedCount,
            color = if (securityScore.reusedCount > 0) RoseError else EmeraldPrimary,
            modifier = Modifier.weight(1f),
        )
        MetricCard(
            title = "Old (>90d)",
            count = securityScore.oldCount,
            color = CyanAccent,
            modifier = Modifier.weight(1f),
        )
      }

      Spacer(modifier = Modifier.height(24.dp))

      // Recommendations list
      Text(
          text = "Security Recommendations",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.onBackground,
      )

      Spacer(modifier = Modifier.height(12.dp))

      if (weakItems.isNotEmpty()) {
        weakItems.forEach { item ->
          RecommendationItemCard(
              title = "Update weak password for ${item.title}",
              subtitle = "Consider using a 16+ character passphrase with symbols",
              badgeColor = AmberWarning,
              onFix = { onFixItem(item) },
          )
          Spacer(modifier = Modifier.height(10.dp))
        }
      }

      RecommendationItemCard(
          title = "Secret recovery phrase backed up offline",
          subtitle = "Ensure your 12 words are stored on physical paper in a secure location",
          badgeColor = EmeraldPrimary,
          isCompleted = true,
      )

      Spacer(modifier = Modifier.height(10.dp))

      RecommendationItemCard(
          title = "Personal Google Drive sync verified",
          subtitle = "Zero-knowledge encrypted cloud snapshot is active",
          badgeColor = EmeraldPrimary,
          isCompleted = true,
      )

      Spacer(modifier = Modifier.height(32.dp))
    }
  }
}

@Composable
fun MetricCard(title: String, count: Int, color: Color, modifier: Modifier = Modifier) {
  Card(
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
      border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.2f)),
      shape = RoundedCornerShape(14.dp),
      modifier = modifier,
  ) {
    Column(modifier = Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
      Text(text = "$count", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), color = color)
      Spacer(modifier = Modifier.height(2.dp))
      Text(text = title, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
  }
}

@Composable
fun RecommendationItemCard(
    title: String,
    subtitle: String,
    badgeColor: Color,
    isCompleted: Boolean = false,
    onFix: (() -> Unit)? = null,
) {
  Card(
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
      border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)),
      shape = RoundedCornerShape(14.dp),
      modifier = Modifier.fillMaxWidth(),
  ) {
    Row(
        modifier = Modifier.padding(14.dp).fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
      Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
        Icon(
            imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Default.WarningAmber,
            contentDescription = null,
            tint = badgeColor,
            modifier = Modifier.size(24.dp),
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
          Text(text = title, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold), color = MaterialTheme.colorScheme.onSurface)
          Spacer(modifier = Modifier.height(2.dp))
          Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
      }
      if (onFix != null) {
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = onFix,
            colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary.copy(alpha = 0.2f)),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.testTag("fix_recommendation_btn"),
        ) {
          Text("Fix", color = EmeraldPrimary, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
        }
      }
    }
  }
}

@Composable
fun PrivacyTrustScreen(onBack: () -> Unit) {
  Scaffold(
      topBar = {
        Row(
            modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(horizontal = 8.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
          IconButton(onClick = onBack, modifier = Modifier.testTag("privacy_trust_back_btn")) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
          }
          Spacer(modifier = Modifier.width(4.dp))
          Column {
            Text(
                text = "Privacy & Trust Architecture",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = "Zero-knowledge security principles",
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

      Text(
          text = "Your Vault. Your Recovery Phrase. Your Data Never Belongs To Us.",
          style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.onBackground,
      )

      Spacer(modifier = Modifier.height(8.dp))

      Text(
          text = "SeedSafe was engineered with a strict zero-knowledge philosophy: we cannot access, view, or restore your passwords even under legal subpoena.",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          lineHeight = 22.sp,
      )

      Spacer(modifier = Modifier.height(20.dp))

      TrustPillarCard(
          icon = Icons.Default.CloudOff,
          title = "Zero Central Company Servers",
          description = "Unlike traditional cloud password managers, SeedSafe has no master authentication server or database holding user vaults.",
      )

      Spacer(modifier = Modifier.height(12.dp))

      TrustPillarCard(
          icon = Icons.Default.Lock,
          title = "Client-Side AES-256 GCM",
          description = "Architected to encrypt every password, note, ID number, and card directly on your device using client-side AES-256 in Galois/Counter Mode.",
      )

      Spacer(modifier = Modifier.height(12.dp))

      TrustPillarCard(
          icon = Icons.Default.Key,
          title = "BIP39 Master Mnemonic Key",
          description = "Your 12-word recovery phrase generates the root cryptographic master key. You own the keys, ensuring portability and perpetual access.",
      )

      Spacer(modifier = Modifier.height(12.dp))

      TrustPillarCard(
          icon = Icons.Default.Security,
          title = "User-Controlled Cloud Backup",
          description = "Cloud backups only go to your personal Google Drive account in end-to-end encrypted format. Neither Google nor SeedSafe has decryption ability.",
      )

      Spacer(modifier = Modifier.height(32.dp))
    }
  }
}

@Composable
fun TrustPillarCard(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, description: String) {
  Card(
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
      border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)),
      shape = RoundedCornerShape(16.dp),
      modifier = Modifier.fillMaxWidth(),
  ) {
    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
      Box(
          modifier =
              Modifier.size(42.dp)
                  .clip(RoundedCornerShape(10.dp))
                  .background(EmeraldPrimary.copy(alpha = 0.15f)),
          contentAlignment = Alignment.Center,
      ) {
        Icon(icon, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(22.dp))
      }
      Spacer(modifier = Modifier.width(14.dp))
      Column {
        Text(text = title, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.onSurface)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 20.sp)
      }
    }
  }
}
