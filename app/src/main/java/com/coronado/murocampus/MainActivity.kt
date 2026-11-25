package com.coronado.murocampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.coronado.murocampus.ui.PantallaCrear
import com.coronado.murocampus.ui.PantallaDetalle
import com.coronado.murocampus.ui.PantallaLista
import com.coronado.murocampus.ui.theme.MuroCampusTheme
import com.coronado.murocampus.viewmodel.PostsViewModel
import com.coronado.murocampus.viewmodel.PostsViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MuroCampusTheme { MuroCampusApp() } }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MuroCampusApp() {
    val vm: PostsViewModel = viewModel(factory = PostsViewModelFactory())
    var idSeleccionado by rememberSaveable { mutableStateOf<Int?>(null) }
    var creando by rememberSaveable { mutableStateOf(false) }

    BackHandler(enabled = idSeleccionado != null || creando) {
        idSeleccionado = null
        creando = false
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
        },
        floatingActionButton = {
            if (idSeleccionado == null && !creando) {
                FloatingActionButton(onClick = { creando = true }) { Text("+") }
            }
        }
    ) { padding ->
        val mod = Modifier.padding(padding)
        val id = idSeleccionado
        when {
            creando -> PantallaCrear(vm, mod)
            id != null -> PantallaDetalle(vm, id, onVolver = { idSeleccionado = null }, modifier = mod)
            else -> PantallaLista(vm, onAbrir = { idSeleccionado = it }, modifier = mod)
        }
    }
}
