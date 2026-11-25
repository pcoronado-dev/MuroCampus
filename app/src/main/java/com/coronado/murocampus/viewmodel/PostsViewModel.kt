package com.coronado.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coronado.murocampus.data.PostRepository
import com.coronado.murocampus.model.Comentario
import com.coronado.murocampus.model.NuevoPost
import com.coronado.murocampus.model.Post
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DetalleUi(val post: Post, val comentarios: List<Comentario>)

class PostsViewModel(private val repo: PostRepository) : ViewModel() {

    // PASO 6: lista
    private val _lista = MutableStateFlow<UiState<List<Post>>>(UiState.Cargando)
    val lista: StateFlow<UiState<List<Post>>> = _lista.asStateFlow()

    // PASO 8: filtro (null = Todos)
    private val _usuarioFiltro = MutableStateFlow<Int?>(null)
    val usuarioFiltro: StateFlow<Int?> = _usuarioFiltro.asStateFlow()

    // PASO 7: detalle
    private val _detalle = MutableStateFlow<UiState<DetalleUi>>(UiState.Cargando)
    val detalle: StateFlow<UiState<DetalleUi>> = _detalle.asStateFlow()

    // PASO 9: crear (null = aún no se envió)
    private val _crear = MutableStateFlow<UiState<Post>?>(null)
    val crear: StateFlow<UiState<Post>?> = _crear.asStateFlow()

    // Estado para eliminar (null = aún no se intentó eliminar)
    private val _eliminar = MutableStateFlow<UiState<Unit>?>(null)
    val eliminar: StateFlow<UiState<Unit>?> = _eliminar.asStateFlow()

    // Estado para actualizar / editar (null = aún no se envió)
    private val _actualizar = MutableStateFlow<UiState<Post>?>(null)
    val actualizar: StateFlow<UiState<Post>?> = _actualizar.asStateFlow()

    init { cargar() }

    fun cargar() {
        viewModelScope.launch {
            _lista.value = UiState.Cargando
            val userId = _usuarioFiltro.value
            val resultado = if (userId == null) repo.listar() else repo.porUsuario(userId)
            resultado
                .onSuccess { _lista.value = UiState.Exito(it) }
                .onFailure { _lista.value = UiState.Error(mensajeDe(it)) }
        }
    }

    fun filtrar(userId: Int?) {
        _usuarioFiltro.value = userId
        cargar()
    }

    fun abrir(id: Int) {
        viewModelScope.launch {
            _detalle.value = UiState.Cargando
            val (rPost, rComs) = coroutineScope {
                val p = async { repo.detalle(id) }
                val c = async { repo.comentarios(id) }
                p.await() to c.await()
            }
            val post = rPost.getOrNull()
            val coms = rComs.getOrNull()
            _detalle.value = if (post != null && coms != null) {
                UiState.Exito(DetalleUi(post, coms))
            } else {
                val causa = rPost.exceptionOrNull() ?: rComs.exceptionOrNull() ?: Exception()
                UiState.Error(mensajeDe(causa))
            }
        }
    }

    fun publicar(titulo: String, texto: String) {
        viewModelScope.launch {
            _crear.value = UiState.Cargando
            repo.crear(NuevoPost(userId = 1, title = titulo, body = texto))
                .onSuccess { _crear.value = UiState.Exito(it) }
                .onFailure { _crear.value = UiState.Error(mensajeDe(it)) }
        }
    }

    fun limpiarCrear() { _crear.value = null }

    fun eliminar(id: Int) {
        viewModelScope.launch {
            _eliminar.value = UiState.Cargando
            repo.eliminar(id)
                .onSuccess {
                    _eliminar.value = UiState.Exito(Unit)
                    val listaActual = _lista.value
                    if (listaActual is UiState.Exito) {
                        _lista.value = UiState.Exito(listaActual.datos.filter { it.id != id })
                    }
                }
                .onFailure {
                    _eliminar.value = UiState.Error(mensajeDe(it))
                }
        }
    }

    fun limpiarEliminar() { _eliminar.value = null }

    fun actualizar(post: Post) {
        viewModelScope.launch {
            _actualizar.value = UiState.Cargando
            repo.actualizar(post.id, post)
                .onSuccess { postActualizado ->
                    _actualizar.value = UiState.Exito(postActualizado)
                    val detalleActual = _detalle.value
                    if (detalleActual is UiState.Exito) {
                        _detalle.value = UiState.Exito(detalleActual.datos.copy(post = postActualizado))
                    }
                    val listaActual = _lista.value
                    if (listaActual is UiState.Exito) {
                        _lista.value = UiState.Exito(
                            listaActual.datos.map { if (it.id == postActualizado.id) postActualizado else it }
                        )
                    }
                }
                .onFailure {
                    _actualizar.value = UiState.Error(mensajeDe(it))
                }
        }
    }

    fun limpiarActualizar() { _actualizar.value = null }
}
