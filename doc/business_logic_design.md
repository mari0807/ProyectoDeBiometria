# Component Design

Medicion = ( id_medicion: Z, nombre: Text, uuid: Text, major: N, minor: N, timestamp: Text )

----------------------------------------- Logica -----------------------------------------
|
|
|
                                                                               Logica() -->
|
|
nombre: Text, uuid: Text, major: N, minor: N --> guardarMedicion() -->
                                            id: Z <--
|
|
                                medicion: Medicion <-- obtenerMedicion() <--
|
------------------------------------------------------------------------------------------

# Design Clarifications

- guardarMedicion mete una fila en mediciones. La hora la pone la base de datos.
- obtenerMedicion lee la fila más reciente. Si no hay filas, no devuelve nada.
- No sabe de HTTP ni de la web.

# General Rules

- Lenguaje: PHP.
- Cada método lleva su diseño entre rayas (--------------------).
- Sin comentarios de más.
- Hay que probar guardarMedicion y obtenerMedicion.
