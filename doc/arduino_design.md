# Component Design

MedicionesID = { CO2, TEMPERATURA, RUIDO }

MedicionBeacon = ( uuid: Text, major: N, minor: N )

------------------------------------ HolaMundoIBeacon ------------------------------------
|
|
                                                                   inicializarPlaquita()
|
|
                                                                                 setup() -->
|
|
                                                                                  loop() -->
|
-------------------------------------------------------------------------------------------

----------------------------------------- Publicador -----------------------------------------
|
| beacon_uuid: [ N ]_16
| rssi: Z
|
|
                                                                               Publicador() -->
|
|
                                                                           encenderEmisora() -->
|
|
valor_co2: Z, contador: N, tiempo_espera: Z --> publicarCO2() -->
|
----------------------------------------------------------------------------------------------

---------------------------------------------- EmisoraBLE ----------------------------------------------
|
| nombre_emisora: Text
| fabricante_id: N
| tx_power: Z
|
|
nombre_emisora: Text, fabricante_id: N, tx_power: Z --> EmisoraBLE() -->
|
|
                                                                     encenderEmisora() -->
|
|
                                                                      detenerAnuncio() -->
|
|
                                                           B <-- estaAnunciando() <--
|
|
beacon_uuid: [ N ]_16, major: Z, minor: Z, rssi: N --> emitirAnuncioIBeacon() -->
|
|
carga: Text, tamanyo_carga: N --> emitirAnuncioIBeaconLibre() -->
|
|
                                                    instalarCallbackConexionEstablecida() -->
|
|
                                                       instalarCallbackConexionTerminada() -->
|
|
conn_handle: N --> getConexion() <--
|
-------------------------------------------------------------------------------------------------------

tiempo: Z --> esperar()

---------------------------------------------- LED ----------------------------------------------
|
| numero_led: Z
| encendido: B
|
|
numero: Z --> LED() -->
|
|
              encender() -->
|
|
                apagar() -->
|
|
              alternar() -->
|
|
tiempo: Z --> brillar() -->
|
-----------------------------------------------------------------------------------------------

-------------------------------------------- Medidor --------------------------------------------
|
|
|
                                                                       Medidor() -->
|
|
                                                                iniciarMedidor() -->
|
|
                                                          Z <-- medirCO2() <--
|
-----------------------------------------------------------------------------------------------

------------------------------------------ PuertoSerie ------------------------------------------
|
|
|
baudios: Z --> PuertoSerie() -->
|
|
                   esperarDisponible() -->
|
|
mensaje: Text --> escribir() -->
|
-----------------------------------------------------------------------------------------------

# Design Clarifications

- setup enciende la emisora. loop llama a publicarCO2.
- Publica 123 una sola vez, con contador 1. Luego el anuncio sigue hasta el siguiente RESET.
- Nombre: Mari-Biometria. UUID: EPSG-GTI-PROY-3A.
- major lleva el tipo CO2 y el contador. minor lleva el CO2.
- El medidor y el led están en el código. El anuncio de CO2 no los usa.

# General Rules

- Lenguaje: C++.
- Cada función lleva su diseño entre rayas (--------------------).
- Sin comentarios de más.
- Hay que probar que el 123 se publica una sola vez.
