# Prompts usados con la IA

Autora: Mari Rosenova Dapcheva

Agente: Cursor, modelo Grok.

Los diseños son míos. Están en `doc/Documento de diseño.pdf` y en los markdown de `doc/`. Casi todo el código de partida sale de la plantilla de Poliformat (carpeta DSIC), no de un proyecto empezado en cero. 
## Prompt 1. Diseños en markdown

A partir de mi PDF de diseños, genera los markdown de `doc/` siguiendo `Logical_Design_RE_Rules_v3`. No inventes partes que el PDF no tenga. El diseño de la base de datos, el de la lógica de negocio, el de la lógica fake del cliente y el del API REST tienen que quedar escritos en la notación de la asignatura.

Revisión: los markdown coinciden con el PDF. `database_design.md`, `business_logic_design.md`, `frontend_business_logic_design.md` y `communication_design.md` cubren la base, la lógica, la lógica fake y el REST.

## Prompt 2. Arduino, solo lo que uso

Quita del sketch y de las cabeceras lo que no uso: `lucecitas`, `publicarTemperatura`, `medirTemperatura` y el servicio GATT. `yaEmitido` es un `static bool` local de `loop()`, no un atributo. `loop()` publica el CO2 ficticio 123 una sola vez. El código tiene que coincidir con `doc/arduino_design.md`.

Revisión: el sketch publica una vez con `publicarCO2`. El diseño de `Publicador`, `EmisoraBLE`, `Medidor`, `LED` y `PuertoSerie` coincide con las cabeceras. No queda el servicio GATT.

## Prompt 3. Lógica de negocio y API REST

Implementa `Logica.php` para que sea la única clase que accede a MySQL: `guardarMedicion(nombre, uuid, major, minor)` devuelve el id e `obtenerMedicion()` devuelve la última fila. `ReglasREST.php` solo llama a esa lógica. El POST llega en un JSON `{"nombre","uuid","major","minor"}`. El GET devuelve la última medición. Las respuestas JSON usan `JSON_UNESCAPED_UNICODE`. Si falta el nombre se usa `Mari-Biometria`. Si falta el major se usa 0. uuid y minor son obligatorios.

Revisión: `Logica.php` hace el INSERT y el SELECT. `ReglasREST.php` no escribe SQL. El JSON de salida conserva los acentos.

## Prompt 4. Lógica fake y página

La página no puede usar mysqli. Implementa `LogicaProxy.js` como lógica fake: `obtenerMedicion` hace un GET a `ReglasREST.php` y devuelve la medición del JSON. `interfaz.js` crea esa lógica, pinta la tabla (ID, nombre, uuid, major, minor, hora) y se actualiza cada 10 segundos. Si no hay filas, la tabla lo dice.

Revisión: la proxy no tiene conexión a la base. La página pide los datos con `obtenerMedicion` y muestra la última fila que devuelve el GET.

## Prompt 5. Tests automáticos

Escribe un test automático por pieza, con la medida de la demo (nombre `Mari-Biometria`, uuid `EPSG-GTI-PROY-3A`, major 2817, minor 123):

- `testLogica.php`: guardar y leer la última fila.
- `testComunicacion.php`: POST válido, GET siguiente y POST incompleto.
- `testProxy.js`: el GET de la proxy y el minor 123, sin llamar al PHP real.
- `testEmisionUnaVez.cpp`: el anuncio sale una sola vez.
- `EnvioBeaconTest.java`: la trama se parte en esos cuatro datos.

Revisión: cada test comprueba el criterio de su pieza y falla si el resultado no es el de la demo. Los comandos están en `README.md` y en `doc/tests.md`.
