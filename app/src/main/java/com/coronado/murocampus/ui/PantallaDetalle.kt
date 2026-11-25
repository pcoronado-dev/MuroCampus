package com.coronado.murocampus.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
    val estadoActualizar by vm.actualizar.collectAsStateWithLifecycle()

    var editando by rememberSaveable { mutableStateOf(false) }
    var nuevoTitulo by rememberSaveable { mutableStateOf("") }
    var nuevoBody by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(id) {
        vm.abrir(id)
        vm.limpiarEliminar()
        vm.limpiarActualizar()
        editando = false
    }

    Box(modifier) {
        when (val e = estado) {
            UiState.Cargando -> Cargando("Cargando publicación…")
            is UiState.Error -> ErrorConReintento(e.mensaje) { vm.abrir(id) }
            is UiState.Exito -> {
                LaunchedEffect(e.datos.post) {
                    if (!editando) {
                        nuevoTitulo = e.datos.post.title
                        nuevoBody = e.datos.post.body
                    }
                }

                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        if (editando) {
                            Text("Editar Publicación", style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(8.dp))
                            OutlinedTextField(
                                value = nuevoTitulo,
                                onValueChange = { nuevoTitulo = it },
                                label = { Text("Título") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(8.dp))
                            OutlinedTextField(
                                value = nuevoBody,
                                onValueChange = { nuevoBody = it },
                                label = { Text("Contenido") },
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 3
                            )
                            Spacer(Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        val postActualizado = e.datos.post.copy(
                                            title = nuevoTitulo,
                                            body = nuevoBody
                                        )
                                        vm.actualizar(postActualizado)
                                    },
                                    enabled = nuevoTitulo.isNotBlank() && estadoActualizar !is UiState.Cargando,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Guardar")
                                }
                                OutlinedButton(
                                    onClick = {
                                        editando = false
                                        vm.limpiarActualizar()
                                    },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Cancelar")
                                }
                            }

                            Spacer(Modifier.height(8.dp))
                            when (val upState = estadoActualizar) {
                                UiState.Cargando -> LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                                is UiState.Error -> Text(upState.mensaje, color = MaterialTheme.colorScheme.error)
                                is UiState.Exito -> {
                                    Text("Actualizado con éxito: ${upState.datos.title}", color = MaterialTheme.colorScheme.primary)
                                    LaunchedEffect(Unit) {
                                        editando = false
                                        vm.limpiarActualizar()
                                    }
                                }
                                null -> {}
                            }
                        } else {
                            Text(e.datos.post.title, style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(8.dp))
                            Text(e.datos.post.body)
                            Spacer(Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        editando = true
                                        nuevoTitulo = e.datos.post.title
                                        nuevoBody = e.datos.post.body
                                        vm.limpiarActualizar()
                                    },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Editar")
                                }

                                Button(
                                    onClick = { vm.eliminar(id) },
                                    enabled = estadoEliminar !is UiState.Cargando,
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Eliminar")
                                }
                            }
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
}
