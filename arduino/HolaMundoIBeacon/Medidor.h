// -*- mode: c++ -*-
// Nombre: Medidor.h
// Descripción: Medidor de apoyo. En la demo el valor publicado es el 123 del sketch.
// Copyright: (c) 2026 Mari Rosenova Dapcheva
// Fecha: 16/10/2026
// Autor: Mari Rosenova Dapcheva
// Aportación: iniciar el medidor y medir CO2.

// -------------------------------------------- Medidor --------------------------------------------
// |
// |
// |
//                                                                        Medidor() -->
// |
// |
//                                                                 iniciarMedidor() -->
// |
// |
//                                                           Z <-- medirCO2() <--
// |
// -----------------------------------------------------------------------------------------------

#ifndef MEDIDOR_H_INCLUIDO
#define MEDIDOR_H_INCLUIDO

class Medidor {

private:

public:

  // --------------------
  // Medidor() -->
  // Crea el medidor.
  // --------------------
  Medidor(  ) {
  }

  // --------------------
  // iniciarMedidor() -->
  // Deja el medidor listo. En la demo no hay sensor real.
  // --------------------
  void iniciarMedidor() {
  }

  // --------------------
  // medirCO2() --> Z
  // Devuelve un CO2 de apoyo. El valor emitido es el 123 del sketch.
  // --------------------
  int medirCO2() {
	return 235;
  }


};

#endif
