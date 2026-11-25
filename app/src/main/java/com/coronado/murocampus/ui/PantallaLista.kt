package com.coronado.murocampus.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.coronado.murocampus.viewmodel.PostsViewModel
import com.coronado.murocampus.viewmodel.UiState

@Composable
fun PantallaLista(vm: PostsViewModel, modifier: Modifier = Modifier) {
    val estado by vm.lista.collectAsStateWithLifecycle()

    when (val e = estado) {
        UiState.Cargando -> Cargando("Cargando publicaciones…")
        is UiState.Error -> ErrorConReintento(e.mensaje) { vm.cargar() }
        is UiState.Exito -> LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(e.datos, key = { it.id }) { post ->
                TarjetaPost(post) { /* el detalle se agrega en el Paso 7 */ }
            }
        }
    }
}
