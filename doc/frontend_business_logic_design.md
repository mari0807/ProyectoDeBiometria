# Component Design

Medicion = ( id_medicion: Z, nombre: Text, uuid: Text, major: N, minor: N, timestamp: Text )

----------------------------------------- Logica -----------------------------------------
|
|
|
                                                                               Logica() -->
|
|
                                medicion: Medicion <-- obtenerMedicion() <--
|
------------------------------------------------------------------------------------------

# Design Clarifications

- obtenerMedicion se llama igual que en el servidor.
- La página hace new Logica() y llama a obtenerMedicion.
- Por debajo pide los datos al PHP. Eso no se dibuja.

# General Rules

- Lenguaje: JavaScript.
- Cada método lleva su diseño entre rayas (--------------------).
- Sin comentarios de más.
- La página no llama al PHP. Llama a Logica.
- Hay que probar obtenerMedicion.
