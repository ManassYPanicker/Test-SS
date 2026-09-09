package com.example.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush

// ========================================================
// Cyberpunk / Synthwave Security Palette
// Dominant: Midnight Purple & Deep Charcoal Void
// Highlights: High-Intensity Neon Magenta & Electric Violet
// ========================================================

// Canvas & Elevated Surfaces (Dark Mode First)
val VaultDarkBg = Color(0xFF120C1F)                  // Deepest Midnight Purple Void
val VaultDarkSurface = Color(0xFF1B142B)             // Elevated Container Background
val VaultDarkSurfaceVariant = Color(0xFF251C3A)      // Card / Input Field Background
val VaultDarkSurfaceHighlight = Color(0xFF32274E)    // Active / Hover Container State
val VaultDarkBorder = Color(0xFF3F3061)              // Soft Purple Ambient Border
val VaultDarkBorderSubtle = Color(0xFF251C3A)        // Background Hairline Divider

// Modern Crisp Slate Colors (Light Theme Fallback - keeping for compatibility but darkening slightly)
val VaultLightBg = Color(0xFFE2DDF0)                 
val VaultLightSurface = Color(0xFFFFFFFF)            
val VaultLightSurfaceVariant = Color(0xFFF3F0F9)     
val VaultLightBorder = Color(0xFFD4CBE5)             
val VaultLightBorderSubtle = Color(0xFFE8E4F2)

// Primary Interactive Accents (Neon Highlights)
val EmeraldPrimary = Color(0xFFFF007F)               // Neon Magenta (#FF007F)
val EmeraldPrimaryDark = Color(0xFFC70062)           // Pressed / Tonal Accent
val EmeraldOnPrimary = Color(0xFFFFFFFF)             // High Contrast Label
val EmeraldContainerDark = Color(0xFF3E0A24)         // Tonal Magenta Container (Dark)
val EmeraldContainerLight = Color(0xFFFFB3D9)        // Tonal Magenta Container (Light)

// Contextual Glassmorphic Layer Tokens
val GlassOverlayScrim = Color(0xCC0D0817)            // Deep Modal Backdrop Scrim
val GlassSurfaceDark = Color(0xD91B142B)             // Frosted Purple Glass Surface (85% opacity)
val GlassBorderDark = Color(0x66FF007F)              // Neon Magenta Edge Reflection
val GlassBorderEmerald = Color(0x4DFF007F)           // Subtle Specular Neon Frost Border

// Semantic Status & Security Indicators (Minimalist)
val CyanAccent = Color(0xFF00F0FF)                   // Secondary Telemetry / Safe Info
val CyanAccentDark = Color(0xFF00B3BF)
val AmberWarning = Color(0xFFFFB800)                 // Retro Amber Warning
val RoseError = Color(0xFFFF2A6D)                    // Cyber Red Destructive
val BlueShield = Color(0xFF00F0FF)                   // Cryptographic Shield (Cyan)
val PurpleKey = Color(0xFFA855F7)                    // Key Pair / Encryption

// Typography Colors
val TextDarkPrimary = Color(0xFFF9F7FD)              // Crisp High-Contrast Off-White
val TextDarkSecondary = Color(0xFFB5A8D3)            // Soft Lavender-Grey
val TextDarkMuted = Color(0xFF7B6E96)                // Muted Meta Text

val TextLightPrimary = Color(0xFF1B142B)             
val TextLightSecondary = Color(0xFF5E5473)           
val TextLightMuted = Color(0xFF8B80A1)               
