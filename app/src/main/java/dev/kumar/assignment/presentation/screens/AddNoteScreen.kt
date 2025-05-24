package dev.kumar.assignment.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.kumar.assignment.data.TextFormatting
import dev.kumar.assignment.presentation.components.BottomFormattingBar
import dev.kumar.assignment.presentation.components.RichTextField
import dev.kumar.assignment.presentation.vm.AddEditNoteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteScreen(
    viewModel: AddEditNoteViewModel,
    onSaveClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val noteTitle by viewModel.noteTitle.collectAsState()
    val noteText by viewModel.noteText.collectAsState()

    var titleFormatting by remember { mutableStateOf(TextFormatting()) }
    var textFormatting by remember { mutableStateOf(TextFormatting()) }
    var isTextFieldFocused by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Note") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    FloatingActionButton(
                        onClick = {
                            viewModel.saveNote(onSaveClick)
                        },
                        containerColor = MaterialTheme.colorScheme.onPrimary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        elevation = FloatingActionButtonDefaults.elevation(
                            defaultElevation = 0.dp
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Save, contentDescription = "Save",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.ime)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            ) {
                RichTextField(
                    value = noteTitle,
                    onValueChange = viewModel::updateTitle,
                    formatting = titleFormatting,
                    placeholder = "Note title...",
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                RichTextField(
                    value = noteText,
                    onValueChange = {
                        viewModel.updateText(it)
                        isTextFieldFocused = true
                    },
                    formatting = textFormatting,
                    placeholder = "Write your note...",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    minLines = 10
                )
            }

            BottomFormattingBar(
                currentFormatting = textFormatting,
                onFormattingChange = { textFormatting = it },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}