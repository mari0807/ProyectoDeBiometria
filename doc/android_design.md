# Component Design

ResultadoEscaneo = ( nombre: Text, anuncio: Text, rssi: Z )

Medicion = ( id_medicion: Z, nombre: Text, uuid: Text, major: N, minor: N, timestamp: Text )

---------------------------------------------- MainActivity ----------------------------------------------
|
| resultado: ResultadoEscaneo --> mostrarInformacionDispositivoBTLE() -->
|
| texto: Text --> pintarEstado() -->
|
| dispositivo_buscado: Text --> buscarEsteDispositivoBTLE() -->
|
| detenerBusquedaDispositivosBTLE() -->
|
| inicializarBlueTooth() -->
|
|
                                                         botonBuscarNuestroDispositivoBTLEPulsado() -->
|
|
                                                         botonDetenerBusquedaDispositivosBTLEPulsado() -->
|
|
                                                         onCreate() -->
|
|
request_code: Z, permissions: [ Text ], grant_results: [ Z ] --> onRequestPermissionsResult() -->
|
-----------------------------------------------------------------------------------------------------------

---------------------------------------------- ClienteREST ----------------------------------------------
|
nombre: Text, uuid: Text, major: N, minor: N --> enviarMedicion() --x
|
----------------------------------------------------------------------------------------------------------

----------------------------------------------------- TramaIBeacon -----------------------------------------------------
|
| prefijo: [ N ]_9
| uuid: [ N ]_16
| major: [ N ]_2
| minor: [ N ]_2
| tx_power: Z
| los_bytes: [ N ]
| adv_flags: [ N ]_3
| adv_header: [ N ]_2
| company_id: [ N ]_2
| ibeacon_type: N
| ibeacon_length: N
|
|
los_bytes: [ N ] --> TramaIBeacon() -->
|
|
          [ N ]_9 <-- getPrefijo() <--
|
|
         [ N ]_16 <-- getUUID() <--
|
|
          [ N ]_2 <-- getMajor() <--
|
|
          [ N ]_2 <-- getMinor() <--
|
|
                Z <-- getTxPower() <--
|
|
            [ N ] <-- getLosBytes() <--
|
|
          [ N ]_3 <-- getAdvFlags() <--
|
|
          [ N ]_2 <-- getAdvHeader() <--
|
|
          [ N ]_2 <-- getCompanyID() <--
|
|
                N <-- getiBeaconType() <--
|
|
                N <-- getiBeaconLength() <--
|
------------------------------------------------------------------------------------------------------------------------

------------------------------------------------------ Utilidades ------------------------------------------------------
|
|
|
                   texto: Text --> stringToBytes() --x
                           [ N ] <--
|
|
                    uuid: Text --> stringToUUID() --x
                           Text <--
|
|
                    uuid: Text --> uuidToString() --x
                           Text <--
|
|
                    uuid: Text --> uuidToHexString() --x
                           Text <--
|
|
                    bytes: [ N ] --> bytesToString() --x
                             Text <--
|
|
mas_significativos: Z, menos_significativos: Z --> dosLongToBytes() --x
                                             [ N ]_16 <--
|
|
                    bytes: [ N ] --> bytesToInt() --x
                                Z <--
|
|
                    bytes: [ N ] --> bytesToLong() --x
                                Z <--
|
|
                    bytes: [ N ] --> bytesToIntOK() --x
                                Z <--
|
|
                    bytes: [ N ] --> bytesToHexString() --x
                             Text <--
|
------------------------------------------------------------------------------------------------------------------------

# Design Clarifications

- Busca el anuncio Mari-Biometria, uuid EPSG-GTI-PROY-3A.
- Al encontrarlo manda nombre, uuid, major y minor a ClienteREST.
- pintarEstado escribe en pantalla: buscando, parado, o los datos del beacon.
- TramaIBeacon lee el anuncio. Utilidades pasa los bytes a texto y a número.
- Mientras un envío no ha terminado, no sale otro.

# General Rules

- Lenguaje: Java.
- Cada método lleva su diseño entre rayas (--------------------).
- Sin comentarios de más.
- MainActivity no abre la conexión. Llama a ClienteREST.
- Hay que probar que se envían nombre, uuid, major y minor.
