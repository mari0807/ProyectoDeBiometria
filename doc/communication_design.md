# Component Design

Medicion = ( id_medicion: Z, nombre: Text, uuid: Text, major: N, minor: N, timestamp: Text )

GET --> obtenerMedicion() --> medicion: Medicion

nombre: Text, uuid: Text, major: N, minor: N --> POST --> guardarMedicion() --> id: Z

# Design Clarifications

- GET pide la última medición a la lógica.
- POST guarda nombre, uuid, major y minor, y devuelve el id.
- Si falta el nombre, se usa Mari-Biometria. Si falta el major, se usa 0.
- uuid y minor tienen que venir en el POST.

# General Rules

- Lenguaje: PHP.
- Cada entrada lleva su diseño entre rayas (--------------------).
- Sin comentarios de más.
- Solo llama a la lógica. No escribe SQL.
- Hay que probar el GET y el POST.
