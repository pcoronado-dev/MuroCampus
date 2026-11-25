package com.coronado.murocampus.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.coronado.murocampus.viewmodel.PostsViewModel
import com.coronado.murocampus.viewmodel.UiState

@Composable
fun PantallaDetalle(vm: PostsViewModel, id: Int, onVolver: () -> Unit = {}, modifier: Modifier = Modifier) {
    val estado by vm.detalle.collectAsStateWithLifecycle()
    val estadoEliminar by vm.eliminar.collectAsStateWithLifecycle()

    LaunchedEffect(id) {
        vm.abrir(id)
        vm.limpiarEliminar()
    }

    Box(modifier) {
        when (val e = estado) {
            UiState.Cargando -> Cargando("Cargando publicación…")
            is UiState.Error -> ErrorConReintento(e.mensaje) { vm.abrir(id) }
            is UiState.Exito -> LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Text(e.datos.post.title, style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    Text(e.datos.post.body)
                    Spacer(Modifier.height(16.dp))

                    Button(
                        onClick = { vm.eliminar(id) },
                        enabled = estadoEliminar !is UiState.Cargando,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Eliminar")
                    }

                    Spacer(Modifier.height(8.dp))
                    when (val elState = estadoEliminar) {
                        UiState.Cargando -> LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                        is UiState.Error -> Text(elState.mensaje, color = MaterialTheme.colorScheme.error)
                        is UiState.Exito -> {
                            Text("Publicación eliminada correctamente", color = MaterialTheme.colorScheme.primary)
                            LaunchedEffect(Unit) {
                                vm.limpiarEliminar()
                                onVolver()
                            }
                        }
                        null -> {}
                    }

                    Spacer(Modifier.height(16.dp))
                    Text("Comentarios (${e.datos.comentarios.size})",
                        style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }
                items(e.datos.comentarios, key = { it.id }) { c ->
                    HorizontalDivider()
                    Spacer(Modifier.height(4.dp))
                    Text(c.email, color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelLarge)
                    Text(c.body, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
