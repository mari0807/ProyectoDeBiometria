// -*- mode: c++ -*-
// Nombre: PuertoSerie.h
// Descripción: Escribe mensajes de seguimiento por el puerto serie.
// Copyright: (c) 2026 Mari Rosenova Dapcheva
// Fecha: 16/10/2026
// Autor: Mari Rosenova Dapcheva
// Aportación: traza de la emisión.

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
  // Crea el puerto con esos baudios.
  // --------------------
  PuertoSerie (long baudios_) {
	baudios = baudios_;
  }

  // --------------------
  // esperarDisponible() -->
  // Abre el puerto serie.
  // --------------------
  void esperarDisponible() {
	Serial.begin( baudios );
	delay( 200 );
  }

  // --------------------
  // mensaje: Text --> escribir() -->
  // Escribe un mensaje por el puerto serie.
  // --------------------
  template<typename T>
  void escribir (T mensaje) {
	Serial.print( mensaje );
  }

};

#endif
