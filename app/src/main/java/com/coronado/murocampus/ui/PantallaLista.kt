package com.coronado.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.coronado.murocampus.viewmodel.PostsViewModel
import com.coronado.murocampus.viewmodel.UiState

@Composable
fun PantallaLista(vm: PostsViewModel, onAbrir: (Int) -> Unit, modifier: Modifier = Modifier) {
    val estado by vm.listaFiltrada.collectAsStateWithLifecycle()
    val filtro by vm.usuarioFiltro.collectAsStateWithLifecycle()
    val query by vm.busquedaQuery.collectAsStateWithLifecycle()

    Column(modifier) {
        OutlinedTextField(
            value = query,
            onValueChange = { vm.buscar(it) },
            label = { Text("Buscar por título") },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
            singleLine = true
        )

        // PASO 8: fila de chips (Todos, Usuario 1..10)
        LazyRow(
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(listOf<Int?>(null) + (1..10).toList()) { id ->
                FilterChip(
                    selected = filtro == id,
                    onClick = { vm.filtrar(id) },
                    label = { Text(if (id == null) "Todos" else "Usuario $id") }
                )
            }
        }

        when (val e = estado) {
            UiState.Cargando -> Cargando("Cargando publicaciones…")
            is UiState.Error -> ErrorConReintento(e.mensaje) { vm.cargar() }
            is UiState.Exito ->
                if (e.datos.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(if (query.isNotBlank()) "No se encontraron publicaciones" else "Sin publicaciones")
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(e.datos, key = { it.id }) { post ->
                            TarjetaPost(post) { onAbrir(post.id) }
                        }
                    }
                }
        }
    }
}