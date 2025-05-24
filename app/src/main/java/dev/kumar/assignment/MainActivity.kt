package dev.kumar.assignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import dev.kumar.assignment.data.NotesRepository
import dev.kumar.assignment.navigation.MainNavGraph
import dev.kumar.assignment.ui.theme.AIPoweredSmartNotesAppTheme
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var repository: NotesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AIPoweredSmartNotesAppTheme {
                MainNavGraph()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            repository.syncUnsyncedNotes()
        }
    }
}
