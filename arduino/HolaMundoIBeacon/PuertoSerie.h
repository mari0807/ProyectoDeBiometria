// -*- mode: c++ -*-

// ------------------------------------------ PuertoSerie ------------------------------------------
// |
// |
// |
// baudios: Z --> PuertoSerie() -->
// |
// |
//                    esperarDisponible() -->
// |
// |
// mensaje: Text --> escribir() -->
// |
// -----------------------------------------------------------------------------------------------

#ifndef PUERTO_SERIE_H_INCLUIDO
#define PUERTO_SERIE_H_INCLUIDO

class PuertoSerie  {

private:
  long baudios;

public:
  // --------------------
  // baudios: Z --> PuertoSerie() -->
  // --------------------
  PuertoSerie (long baudios_) {
	baudios = baudios_;
  }

  // --------------------
  // esperarDisponible() -->
  // --------------------
  void esperarDisponible() {
	Serial.begin( baudios );
	delay( 200 );
  }

  // --------------------
  // mensaje: Text --> escribir() -->
  // --------------------
  template<typename T>
  void escribir (T mensaje) {
	Serial.print( mensaje );
  }

};

#endif
