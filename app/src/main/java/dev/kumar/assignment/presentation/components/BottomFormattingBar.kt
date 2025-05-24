package dev.kumar.assignment.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatAlignCenter
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BottomFormattingBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        IconButton(onClick = { /* Bold */ }) {
            Icon(Icons.Default.FormatBold, contentDescription = "Bold")
        }
        IconButton(onClick = { /* Italic */ }) {
            Icon(Icons.Default.FormatItalic, contentDescription = "Italic")
        }
        IconButton(onClick = { /* Center Align */ }) {
            Icon(Icons.Default.FormatAlignCenter, contentDescription = "Center Align")
        }
        IconButton(onClick = { /* Increase Font Size */ }) {
            Icon(Icons.Default.FormatSize, contentDescription = "Font Size")
        }
    }
}
