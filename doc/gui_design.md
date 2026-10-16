# Component Design

Pantalla: Última medición

Muestra una tabla con una sola fila, la medición más reciente.

Columnas: ID, Nombre, UUID, Major, Minor, Hora.

Si no hay mediciones, la tabla dice que no hay mediciones.

La pantalla se actualiza sola cada 10 segundos.

# Design Clarifications

- Pide los datos con obtenerMedicion.
- No llama al PHP.

# General Rules

- Lenguaje: HTML y JavaScript.
- Sin comentarios de más.
