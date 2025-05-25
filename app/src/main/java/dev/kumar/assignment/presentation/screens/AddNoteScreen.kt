package dev.kumar.assignment.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.kumar.assignment.data.GeminiHelper
import dev.kumar.assignment.data.TextFormatting
import dev.kumar.assignment.presentation.components.BottomFormattingBar
import dev.kumar.assignment.presentation.components.RichTextField
import dev.kumar.assignment.presentation.vm.AddEditNoteViewModel
import dev.kumar.assignment.utils.Constants
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteScreen(
    viewModel: AddEditNoteViewModel,
    onSaveClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val noteTitle by viewModel.noteTitle.collectAsState()
    val noteText by viewModel.noteText.collectAsState()

    var isSummaryLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var summary by remember { mutableStateOf("") }
    var isTitleLoading by remember { mutableStateOf(false) }

    val geminiApiKey = Constants.GEMINI_API_KEY
    val geminiHelper = remember { GeminiHelper(geminiApiKey) }

    var titleFormatting by remember { mutableStateOf(TextFormatting()) }
    var textFormatting by remember { mutableStateOf(TextFormatting()) }
    var isTextFieldFocused by remember { mutableStateOf(false) }

    val customTextSelectionColors = TextSelectionColors(
        handleColor = MaterialTheme.colorScheme.onSurface,
        backgroundColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Add Note",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.saveNote(onSaveClick)
                    }) {
                        Icon(Icons.Default.Save, contentDescription = "Save")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (noteText.isNotBlank()) {
                        isSummaryLoading = true
                        scope.launch {
                            summary = geminiHelper.summarizeNote(noteText)
                            isSummaryLoading = false
                        }
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .padding(bottom = 80.dp)
                    .border(
                        BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.onSurface),
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                if (isSummaryLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Generate AI Summary",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
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
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Title Input
                CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
                    RichTextField(
                        value = noteTitle,
                        onValueChange = viewModel::updateTitle,
                        formatting = titleFormatting,
                        placeholder = "Note title...",
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        trailingIcon = @Composable {
                            IconButton(onClick = {
                                if (noteTitle.isNotBlank()) {
                                    isTitleLoading = true
                                    scope.launch {
                                        val newNoteTitle =
                                            geminiHelper.suggestTitle(noteTitle, noteText)
                                        viewModel.updateTitle(newNoteTitle)
                                        isTitleLoading = false
                                    }
                                }
                            }) {
                                if (isTitleLoading) CircularProgressIndicator(
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(24.dp),
                                    strokeWidth = 2.dp
                                )
                                else {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = "Suggest a title",
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    )

                }
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
                        .height(300.dp),
                    minLines = 12
                )

                Spacer(modifier = Modifier.height(24.dp))

                if (summary.isNotBlank()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp)),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                                Text(
                                    "AI Summary",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
                                SelectionContainer {
                                    Text(
                                        text = summary,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.2
                                        ),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(80.dp))
            }

            BottomFormattingBar(
                currentFormatting = textFormatting,
                onFormattingChange = { textFormatting = it },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}