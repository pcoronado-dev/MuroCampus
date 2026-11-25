package com.coronado.murocampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.coronado.murocampus.ui.PantallaDetalle
import com.coronado.murocampus.ui.PantallaLista
import com.coronado.murocampus.ui.theme.MuroCampusTheme
import com.coronado.murocampus.viewmodel.PostsViewModel
import com.coronado.murocampus.viewmodel.PostsViewModelFactory

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MuroCampusTheme {
                val vm: PostsViewModel = viewModel(factory = PostsViewModelFactory())
                var idSeleccionado by rememberSaveable { mutableStateOf<Int?>(null) }

                // El botón Atrás del sistema vuelve a la lista
                BackHandler(enabled = idSeleccionado != null) {
                    idSeleccionado = null
                }

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("MuroCampus") },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                ) { padding ->
                    Box(Modifier.padding(padding)) {
                        val id = idSeleccionado
                        if (id == null) {
                            PantallaLista(vm, onAbrir = { idSeleccionado = it })
                        } else {
                            PantallaDetalle(vm, id)
                        }
                    }
                }
            }
        }
    }
}