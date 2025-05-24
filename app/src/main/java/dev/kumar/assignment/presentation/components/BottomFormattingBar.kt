package dev.kumar.assignment.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatAlignCenter
import androidx.compose.material.icons.filled.FormatAlignLeft
import androidx.compose.material.icons.filled.FormatAlignRight
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.FormatUnderlined
import androidx.compose.material.icons.filled.TextDecrease
import androidx.compose.material.icons.filled.TextIncrease
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.kumar.assignment.data.TextFormatting


@Composable
fun BottomFormattingBar(
    currentFormatting: TextFormatting,
    onFormattingChange: (TextFormatting) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // Bold Button
        FormattingIconButton(
            icon = Icons.Default.FormatBold,
            contentDescription = "Bold",
            isActive = currentFormatting.isBold,
            onClick = {
                onFormattingChange(currentFormatting.copy(isBold = !currentFormatting.isBold))
            }
        )

        // Italic Button
        FormattingIconButton(
            icon = Icons.Default.FormatItalic,
            contentDescription = "Italic",
            isActive = currentFormatting.isItalic,
            onClick = {
                onFormattingChange(currentFormatting.copy(isItalic = !currentFormatting.isItalic))
            }
        )

        // Underline Button
        FormattingIconButton(
            icon = Icons.Default.FormatUnderlined,
            contentDescription = "Underline",
            isActive = currentFormatting.isUnderlined,
            onClick = {
                onFormattingChange(currentFormatting.copy(isUnderlined = !currentFormatting.isUnderlined))
            }
        )

        // Decrease Font Size
        IconButton(
            onClick = {
                if (currentFormatting.fontSize > 10) {
                    onFormattingChange(currentFormatting.copy(fontSize = currentFormatting.fontSize - 2))
                }
            }
        ) {
            Icon(
                Icons.Default.TextDecrease,
                contentDescription = "Decrease Font Size",
                tint = if (currentFormatting.fontSize > 10) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(
                    alpha = 0.5f
                )
            )
        }

        // Increase Font Size
        IconButton(
            onClick = {
                if (currentFormatting.fontSize < 28) {
                    onFormattingChange(currentFormatting.copy(fontSize = currentFormatting.fontSize + 2))
                }
            }
        ) {
            Icon(
                Icons.Default.TextIncrease,
                contentDescription = "Increase Font Size",
                tint = if (currentFormatting.fontSize < 28) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(
                    alpha = 0.5f
                )
            )
        }

        // Left Align
        FormattingIconButton(
            icon = Icons.Default.FormatAlignLeft,
            contentDescription = "Align Left",
            isActive = currentFormatting.textAlign == TextAlign.Start,
            onClick = {
                onFormattingChange(currentFormatting.copy(textAlign = TextAlign.Start))
            }
        )

        // Center Align
        FormattingIconButton(
            icon = Icons.Default.FormatAlignCenter,
            contentDescription = "Center Align",
            isActive = currentFormatting.textAlign == TextAlign.Center,
            onClick = {
                onFormattingChange(currentFormatting.copy(textAlign = TextAlign.Center))
            }
        )

        // Right Align
        FormattingIconButton(
            icon = Icons.Default.FormatAlignRight,
            contentDescription = "Align Right",
            isActive = currentFormatting.textAlign == TextAlign.End,
            onClick = {
                onFormattingChange(currentFormatting.copy(textAlign = TextAlign.End))
            }
        )
    }
}

@Composable
private fun FormattingIconButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    isActive: Boolean,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(
            icon,
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.onSurface.copy(if (!isActive) 1f else 0.5f)
        )
    }
}