package dev.kumar.assignment.presentation.components

import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import dev.kumar.assignment.data.TextFormatting

@Composable
fun RichTextField(
    value: String,
    onValueChange: (String) -> Unit,
    formatting: TextFormatting,
    placeholder: String = "",
    modifier: Modifier,
    singleLine: Boolean = false,
    minLines: Int = 1,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    val customTextSelectionColors = TextSelectionColors(
        handleColor = MaterialTheme.colorScheme.onSurface,
        backgroundColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
    )

    val textStyle = TextStyle(
        fontWeight = if (formatting.isBold) FontWeight.Bold else FontWeight.Normal,
        fontStyle = if (formatting.isItalic) FontStyle.Italic else FontStyle.Normal,
        textDecoration = if (formatting.isUnderlined) TextDecoration.Underline else TextDecoration.None,
        fontSize = formatting.fontSize.sp,
        textAlign = formatting.textAlign
    )

    CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
        SelectionContainer {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = { Text(placeholder) },
                modifier = modifier,
                singleLine = singleLine,
                minLines = minLines,
                textStyle = textStyle,
                trailingIcon = trailingIcon,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    cursorColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }
}