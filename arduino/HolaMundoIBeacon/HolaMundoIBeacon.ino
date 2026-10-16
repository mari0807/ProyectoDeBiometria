// -*-c++-*-
// Nombre: HolaMundoIBeacon.ino
// Descripción: Programa de la placa. Emite una vez la medida ficticia de CO2.
// Copyright: (c) 2026 Mari Rosenova Dapcheva
// Fecha: 16/10/2026
// Autor: Mari Rosenova Dapcheva
// Aportación: origen de la medición que acaba en la base de datos.

// inicializarPlaquita()
//
// setup()
//
// loop()

#include <bluefruit.h>

#undef min
#undef max

#include "LED.h"
#include "PuertoSerie.h"

namespace Globales {

  LED elLED ( 7 );

  PuertoSerie elPuerto ( 115200 );

};

#include "EmisoraBLE.h"
#include "Publicador.h"
#include "Medidor.h"

namespace Globales {

  Publicador elPublicador;

  Medidor elMedidor;

};

// --------------------
// inicializarPlaquita()
// Prepara LED, puerto serie, emisora, publicador y medidor.
// --------------------
void inicializarPlaquita () {

}

// --------------------
// setup()
// Arranca la placa llamando a inicializarPlaquita.
// --------------------
void setup() {

  Globales::elPuerto.esperarDisponible();

  inicializarPlaquita();

  Globales::elPublicador.encenderEmisora();

  Globales::elMedidor.iniciarMedidor();

  esperar( 1000 );

  Globales::elPuerto.escribir( "---- setup(): fin ---- \n " );

}

// --------------------
// loop()
// Publica el CO2 123 una sola vez y después solo espera.
// --------------------
void loop () {

  using namespace Globales;

  static bool yaEmitido = false;

  if ( yaEmitido ) {
    elPuerto.escribir( "---- anuncio sigue \n" );
    esperar( 10000 );
    return;
  }
  yaEmitido = true;

  int valorCO2 = 123;

  elPuerto.escribir( "\n---- emite CO2 una vez: " );
  elPuerto.escribir( valorCO2 );
  elPuerto.escribir( "\n" );

  elPublicador.publicarCO2( valorCO2, 1, 0 );
  elPuerto.escribir( "---- anuncio sigue \n" );

}
