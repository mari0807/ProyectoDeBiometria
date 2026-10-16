# ProyectoBiometria

Autora: Mari Rosenova Dapcheva

La placa Arduino emite una vez un iBeacon con una medida ficticia de CO2: minor `123`, major `2817`, uuid `EPSG-GTI-PROY-3A`, nombre `Mari-Biometria`. El móvil Android la lee y hace un POST JSON al API REST. La lógica de negocio la guarda en MySQL. La página web pide la última medición y la muestra.

## Arquitectura

Cada capa solo llama a la de debajo.

- Arduino: `HolaMundoIBeacon` publica el beacon con `Publicador` y `EmisoraBLE`.
- Android: `MainActivity` escanea, `TramaIBeacon` y `Utilidades` parten la trama, `ClienteREST` hace el POST.
- Servidor: `ReglasREST.php` es la puerta HTTP. `Logica.php` es la lógica de negocio y es la única que escribe SQL.
- Base de datos: tabla `mediciones` en `datosMediciones`.
- Web: `interfaz.js` llama a la lógica fake `LogicaProxy.js`. Esa proxy hace el GET. No tiene conexión MySQL.

Los diseños, en la notación de la asignatura, están en `doc/` y en `doc/Documento de diseño.pdf`.

## Ramas

El trabajo está en `master` y en `develop`. Los commits se han ido haciendo sobre el historial del proyecto (Arduino, Android, servidor y web).

## Despliegue

1. Arrancar en XAMPP Apache y MySQL.
2. Importar `src/database/datosMediciones.sql`.
3. Copiar esta carpeta a `C:\xampp\htdocs\biometria`.
4. API: `http://localhost/biometria/src/communication/ReglasREST.php`
5. Página: `http://localhost/biometria/src/gui/webMari.html`
6. Abrir en Android Studio la carpeta `android/` (no la raíz).
7. El sketch está en `arduino/HolaMundoIBeacon/`.

`ClienteREST.java` tiene la IP del ordenador en la red Wi-Fi. Si cambia la red, hay que sustituir esa IP por la IPv4 que salga en `ipconfig` y volver a instalar la app. La página, abierta en el mismo ordenador, no usa esa IP.

## Criterios de aceptación y tests

La medida de la demo es siempre nombre `Mari-Biometria`, uuid `EPSG-GTI-PROY-3A`, major `2817` y minor `123`. Si un test falla, el programa termina con error. El detalle está en `doc/tests.md`.

Desde la raíz del proyecto, con MySQL arrancado:

```
php src/business_logic/testLogica.php
php src/communication/testComunicacion.php
node src/frontend_business_logic/testProxy.js
g++ arduino/test/testEmisionUnaVez.cpp -o prueba-arduino
.\prueba-arduino.exe
```

El test de Android, desde la carpeta `android`, con el JDK de Android Studio:

```
gradlew :app:testDebugUnitTest --tests com.example.mrosdap.android.EnvioBeaconTest
```

Qué tiene que cumplirse:

- La lógica guarda la medida y `obtenerMedicion` devuelve esa misma fila, con minor 123.
- El POST del API guarda la medida y el GET siguiente la devuelve. Un POST sin uuid o sin minor no tiene éxito.
- La lógica fake hace un GET y entrega la medición con minor 123.
- El anuncio de la placa se publica una sola vez, con valor 123 y contador 1.
- Al leer la trama, el móvil obtiene el nombre, el uuid, el major 2817 y el minor 123.

## Uso de la IA

El documento de uso está en `doc/Uso_de_la_IA.pdf`. Los prompts con los que se pidió implementar cada parte, y la revisión contra el diseño, están en `doc/prompts_ia.md`.
