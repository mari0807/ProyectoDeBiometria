
// -*- mode: c++ -*-

// ----------------------------------------------------------
// Mari Dapcheva
// 2019-07-07
// ----------------------------------------------------------

#ifndef PUERTO_SERIE_H_INCLUIDO
#define PUERTO_SERIE_H_INCLUIDO

// ----------------------------------------------------------
// ----------------------------------------------------------
class PuertoSerie  {

public:
  // .........................................................
  // .........................................................
  PuertoSerie (long baudios) {
	Serial.begin( baudios );
	// mejor no poner esto aquí: while ( !Serial ) delay(10);   
  } // ()

  // .........................................................
  // .........................................................
  // Espera el Monitor Serie un máximo de ~3 s.
  // Si no se abre, sigue igual: así el BLE no se queda sin arrancar.
  void esperarDisponible() {
	const unsigned long timeoutMs = 3000;
	const unsigned long t0 = millis();

	while ( !Serial && (millis() - t0) < timeoutMs ) {
	  delay(10);
	}

  } // ()

  // .........................................................
  // .........................................................
  template<typename T>
  void escribir (T mensaje) {
	Serial.print( mensaje );
  } // ()
  
}; // class PuertoSerie

// ----------------------------------------------------------
// ----------------------------------------------------------
// ----------------------------------------------------------
// ----------------------------------------------------------
#endif
