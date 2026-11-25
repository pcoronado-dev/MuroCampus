package com.coronado.murocampus.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.coronado.murocampus.viewmodel.PostsViewModel
import com.coronado.murocampus.viewmodel.UiState

@Composable
fun PantallaCrear(vm: PostsViewModel, modifier: Modifier = Modifier) {
    val estado by vm.crear.collectAsStateWithLifecycle()
    var titulo by rememberSaveable { mutableStateOf("") }
    var texto by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(Unit) { vm.limpiarCrear() }
    LaunchedEffect(estado) {
        if (estado is UiState.Exito) { titulo = ""; texto = "" }
    }

    Column(modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedTextField(titulo, { titulo = it }, label = { Text("Título") },
            modifier = Modifier.fillMaxWidth())
        OutlinedTextField(texto, { texto = it }, label = { Text("Contenido") },
            modifier = Modifier.fillMaxWidth(), minLines = 4)
        Button(
            onClick = { vm.publicar(titulo, texto) },
            enabled = titulo.isNotBlank() && texto.isNotBlank() && estado !is UiState.Cargando,
            modifier = Modifier.fillMaxWidth()
        ) { Text("Publicar") }

        when (val e = estado) {
            null -> {}
            UiState.Cargando -> CircularProgressIndicator()
            is UiState.Exito -> Text("Publicado con id ${e.datos.id}",
                color = MaterialTheme.colorScheme.primary)
            is UiState.Error -> Text(e.mensaje, color = MaterialTheme.colorScheme.error)
        }
    }
}
