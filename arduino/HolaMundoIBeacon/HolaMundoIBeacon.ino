// -*-c++-*-

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
// --------------------
void inicializarPlaquita () {

}

// --------------------
// setup()
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
