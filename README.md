## Estructura de paquetes

```
com.coronado.murocampus/
├── MainActivity.kt
├── model/
│   └── Modelos.kt              Usuario, Post, Comentario, NuevoPost
├── data/
│   ├── remote/
│   │   ├── PostApi.kt          interface de Retrofit
│   │   └── RetrofitClient.kt   cliente Retrofit + OkHttp
│   └── PostRepository.kt       interface + PostRepositoryRemote (Result)
├── viewmodel/
│   ├── UiState.kt              Cargando / Exito / Error
│   ├── PostsViewModel.kt       lista, detalle, filtro y crear
│   └── PostsViewModelFactory.kt
└── ui/
    ├── Componentes.kt          TarjetaPost, Cargando, ErrorConReintento
    ├── PantallaLista.kt
    ├── PantallaDetalle.kt
    ├── PantallaCrear.kt
    └── theme/
```


<img width="540" height="1230" alt="WhatsApp Image 2026-11-25 at 12 49 06 AM" src="https://github.com/user-attachments/assets/7adb1f53-7685-4c48-b1eb-f047ec68c061" />
<img width="540" height="1230" alt="WhatsApp Image 2026-11-25 at 12 49 10 AM" src="https://github.com/user-attachments/assets/d587b9d3-0c2c-4896-bdfd-75d18f897a3c" />



1. ¿Qué diferencia hay entre GET y POST? ¿Por qué crear lleva @Body y porUsuario lleva @Query?
Respuesta: GET obtiene datos y POST envía datos para crear nuevos registros. @Body envía información en formato JSON y @Query permite filtrar datos mediante la URL.
2. ¿Por qué la ruta es "posts" y no "/posts"?
Respuesta: Porque Retrofit une la ruta con la baseUrl. Usar posts evita reemplazar la ruta base y permite construir correctamente la URL.
3. ¿Por qué el Repository devuelve Result en vez de dejar que la excepción llegue al ViewModel?
Respuesta: Porque Result permite controlar los errores sin cerrar la aplicación y evita que el ViewModel tenga que manejar directamente los errores de conexión.
4. ¿Qué pasa si olvidas el permiso INTERNET? ¿Y si olvidas ignoreUnknownKeys y el servidor agrega un campo nuevo?
Respuesta: Sin el permiso INTERNET, la aplicación no podrá conectarse. Sin ignoreUnknownKeys, puede producirse un error al recibir campos nuevos que no están definidos en el modelo.
5. ¿Por qué la pantalla de lista usa un when sobre UiState y no un if con tres variables?
Respuesta: Porque UiState permite controlar los estados de carga, éxito y error de manera ordenada, evitando que existan varios estados al mismo tiempo.
6. JSONPlaceholder no guarda los POST. ¿Cómo cambiaría tu app si usaras un servicio real?
Respuesta: Los registros se guardarían en el servidor y necesitaríamos autenticación, manejo de errores y operaciones CRUD. La arquitectura MVVM permitiría realizar cambios principalmente en la capa Repository y Data.
