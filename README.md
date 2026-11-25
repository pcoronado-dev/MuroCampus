PROMPT 1: Quiero implementar la funcionalidad **Borrar publicación** en mi proyecto Android.

Requisitos:
- Agrega un botón llamado **Eliminar** en la pantalla de detalle de cada publicación.
- Al presionar el botón, realiza una petición HTTP DELETE al endpoint `@DELETE("posts/{id}")`.
- Envía el ID de la publicación que se desea eliminar.
- Implementa la operación siguiendo la arquitectura actual del proyecto, utilizando las capas API, Repository, ViewModel y View.
- El Repository debe ejecutar la petición DELETE.
- El ViewModel debe gestionar la operación y su estado (cargando, éxito o error).
- La View debe mostrar el resultado de la eliminación y actualizar la interfaz para que la publicación eliminada ya no aparezca.
- Utiliza `Response<Unit>` para manejar la respuesta del servidor.

Antes de realizar cambios, revisa la estructura y los nombres de archivos existentes. No crees clases innecesarias ni modifiques funcionalidades que ya funcionan.

Implementa los cambios directamente en los archivos correspondientes y asegúrate de que el proyecto compile correctamente.

PROMPT 2: Quiero implementar la funcionalidad **Editar publicación** en mi proyecto Android.

Requisitos:
- Agrega una opción para editar una publicación desde su pantalla de detalle.
- Al ingresar a editar, muestra una pantalla o formulario que permita modificar el título de la publicación.
- Utiliza una petición HTTP `@PUT` o `@PATCH` para actualizar la publicación en el servidor.
- Implementa el endpoint correspondiente en la interfaz API.
- Implementa el método de actualización en el Repository.
- Utiliza el ViewModel para gestionar los datos, ejecutar la actualización y controlar los estados de carga, éxito y error.
- En la View, muestra el formulario con el título actual y un botón para guardar los cambios.
- Después de actualizar correctamente, muestra la respuesta y refleja el nuevo título en la interfaz.

Trabaja con la arquitectura existente: API + Repository + ViewModel + View.

Primero revisa los archivos y las clases existentes para adaptar la implementación al proyecto. No dupliques código ni crees estructuras innecesarias. No modifiques las funcionalidades que ya están implementadas.

Realiza los cambios directamente y verifica que el proyecto compile.

PROMPT 3: Quiero implementar una funcionalidad de **Búsqueda de publicaciones** en mi proyecto Android.

Requisitos:
- Agrega un campo de búsqueda en la pantalla donde se muestran las publicaciones.
- El campo debe permitir escribir el título de una publicación.
- La búsqueda debe filtrar las publicaciones que ya fueron descargadas desde la API.
- El filtrado debe realizarse localmente, sin enviar nuevas peticiones al servidor.
- La búsqueda debe comparar el texto ingresado con el título de cada publicación.
- Debe actualizar los resultados automáticamente mientras el usuario escribe.
- Si el campo está vacío, deben mostrarse nuevamente todas las publicaciones descargadas.
- Si no existen coincidencias, muestra un mensaje indicando que no se encontraron publicaciones.

Implementa esta funcionalidad utilizando la arquitectura actual del proyecto: Repository + ViewModel + View.

El ViewModel debe gestionar el texto de búsqueda y proporcionar la lista filtrada a la View.

Antes de modificar el código, revisa cómo se obtienen y muestran actualmente las publicaciones. Reutiliza las clases y estructuras existentes, evita duplicar código y no alteres las funcionalidades que ya funcionan.

Realiza los cambios directamente en los archivos correspondientes y verifica que el proyecto compile correctamente.



<img width="540" height="1230" alt="WhatsApp Image 2026-11-25 at 1 50 50 AM (2)" src="https://github.com/user-attachments/assets/d8f338af-9979-48f4-b165-bd3013a1163a" />
<img width="540" height="1230" alt="WhatsApp Image 2026-11-25 at 1 50 50 AM (1)" src="https://github.com/user-attachments/assets/8b0b81b1-fc59-4e8d-89b1-a61b1ffebf76" />
<img width="540" height="1230" alt="WhatsApp Image 2026-11-25 at 1 50 50 AM" src="https://github.com/user-attachments/assets/c2e07fa8-71b5-46d2-8856-afb54315ed71" />

