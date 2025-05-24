package dev.kumar.assignment.data

import androidx.compose.ui.text.style.TextAlign

data class TextFormatting(
    val isBold: Boolean = false,
    val isItalic: Boolean = false,
    val isUnderlined: Boolean = false,
    val fontSize: Int = 14,
    val textAlign: TextAlign = TextAlign.Start
)
