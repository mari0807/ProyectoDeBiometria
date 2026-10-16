# Pruebas

Cada prueba comprueba una pieza del proyecto. Si algo falla, el programa termina con un error. Si todo va bien, escribe que está ok.

Los valores de la demo son siempre los mismos: nombre `Mari-Biometria`, uuid `EPSG-GTI-PROY-3A`, major `2817` y minor `123`. El 123 es el CO2 ficticio. El 2817 es el major de la demo: identificador de CO2 (11) y contador 1.

## Lógica

Fichero: `src/business_logic/testLogica.php`

Comprueba la clase `Logica`, la que habla con MySQL.

1. Abre la base `datosMediciones` en localhost, con usuario root y sin contraseña. Si MySQL está apagado, la prueba para y lo dice.
2. `guardarMedicion` inserta una fila con los cuatro datos de la demo y debe devolver un id.
3. `obtenerMedicion` lee la última fila. Ese id y el minor 123 tienen que coincidir con lo que se acaba de guardar.
4. Borra esa fila para no dejar basura en la tabla.

Cómo lanzarla, con XAMPP y MySQL arrancados:

```
php src/business_logic/testLogica.php
```

## Comunicación

Fichero: `src/communication/testComunicacion.php`

Comprueba `atenderPOST` y `atenderGET`, que están dentro de `ReglasREST.php`. No abre el navegador: llama a esas funciones directamente. Ellas, a su vez, llaman a la lógica.

1. Un POST completo, con nombre, uuid, major y minor, debe guardar la medición y devolver un id.
2. El GET de después debe devolver esa medición, con minor 123.
3. Un POST al que le faltan uuid y minor no debe tener éxito.
4. Borra la fila creada.

También necesita MySQL arrancado:

```
php src/communication/testComunicacion.php
```

## Proxy de la página

Fichero: `src/frontend_business_logic/testProxy.js`

Comprueba `LogicaProxy.js`, la función que usa la página. No llama al PHP de verdad. Sustituye `fetch` por una respuesta falsa, para ver que la proxy pide lo correcto y entiende la respuesta.

`obtenerMedicion` debe hacer un GET y debe entregar la medición con minor 123.

Hace falta Node:

```
node src/frontend_business_logic/testProxy.js
```

## Arduino

Fichero: `arduino/test/testEmisionUnaVez.cpp`

Comprueba la regla de `loop`: el anuncio sale una sola vez.

Repite tres veces el mismo criterio que el sketch. La primera vez todavía no se ha emitido, así que publica el valor 123 y el contador 1. Las dos siguientes ya ven que se emitió y no publican otra vez. Al final tiene que haber una sola publicación, con valor 123 y contador 1.

Se puede compilar con un compilador de C++, por ejemplo:

```
g++ arduino/test/testEmisionUnaVez.cpp -o prueba-arduino
```

## Android

Fichero: `android/app/src/test/java/com/example/mrosdap/android/EnvioBeaconTest.java`

Comprueba los cuatro datos que el móvil prepara cuando encuentra el beacon, antes de mandarlos al servidor.

Monta un anuncio de 30 bytes, el mismo formato que lee `TramaIBeacon`: el uuid `EPSG-GTI-PROY-3A` en los bytes del uuid, el major 2817 y el minor 123. Luego lee esos bytes con `TramaIBeacon` y `Utilidades`.

Tiene que salir:

- nombre `Mari-Biometria`
- uuid `EPSG-GTI-PROY-3A`
- major `2817`
- minor `123`

Se lanza desde la carpeta `android`, con el JDK de Android Studio:

```
gradlew :app:testDebugUnitTest --tests com.example.mrosdap.android.EnvioBeaconTest
```

`ExampleUnitTest` y `ExampleInstrumentedTest` los creó Android Studio. El primero solo comprueba que 2 + 2 es 4. El segundo solo comprueba el nombre del paquete de la app. No prueban el beacon.
