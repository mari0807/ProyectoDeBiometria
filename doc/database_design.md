# Component Design

====================================================================================
TABLE: mediciones

DESCRIPTION: Guarda cada medición recibida del beacon. La web lee solo la más reciente.

COLUMNS:

+ id_medicion | INT | NOT NULL | Auto-Increment
+ nombre | VARCHAR(50) | NOT NULL
+ uuid | VARCHAR(255) | NOT NULL
+ major | INT UNSIGNED | NOT NULL
+ minor | INT UNSIGNED | NOT NULL
+ timestamp | DATETIME | NULL | current_timestamp

PRIMARY KEY: id_medicion

FOREIGN KEYS:

CONSTRAINTS:

====================================================================================

# Design Clarifications

- La base se llama datosMediciones.
- El minor es el CO2. En la demo vale 123.
- Solo está la tabla mediciones.

# General Rules

- Lenguaje: SQL.
- Sin comentarios de más.
- Hay que probar guardar una fila y leer la última.
