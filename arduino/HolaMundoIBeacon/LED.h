// -*- mode: c++ -*-
// Nombre: LED.h
// Descripción: Control del LED de la placa y espera bloqueante.
// Copyright: (c) 2026 Mari Rosenova Dapcheva
// Fecha: 16/10/2026
// Autor: Mari Rosenova Dapcheva
// Aportación: apoyo del sketch.

// tiempo: Z --> esperar()

#ifndef LED_H_INCLUIDO
#define LED_H_INCLUIDO

// --------------------
// tiempo: Z --> esperar()
// Espera los milisegundos indicados.
// --------------------
void esperar (long tiempo) {
  delay (tiempo);
}

// ---------------------------------------------- LED ----------------------------------------------
// |
// | numero_led: Z
// | encendido: B
// |
// |
// numero: Z --> LED() -->
// |
// |
//               encender() -->
// |
// |
//                 apagar() -->
// |
// |
//               alternar() -->
// |
// |
// tiempo: Z --> brillar() -->
// |
// -----------------------------------------------------------------------------------------------
class LED {
private:
  int numeroLED;
  bool encendido;
public:

  // --------------------
  // numero: Z --> LED() -->
  // Crea el LED en el pin indicado.
  // --------------------
  LED (int numero)
	: numeroLED (numero), encendido(false)
  {
	pinMode(numeroLED, OUTPUT);
	apagar ();
  }

  // --------------------
  // encender() -->
  // Enciende el LED.
  // --------------------
  void encender () {
	digitalWrite(numeroLED, HIGH);
	encendido = true;
  }

  // --------------------
  // apagar() -->
  // Apaga el LED.
  // --------------------
  void apagar () {
	  digitalWrite(numeroLED, LOW);
	  encendido = false;
  }

  // --------------------
  // alternar() -->
  // Cambia el LED de encendido a apagado o al revés.
  // --------------------
  void alternar () {
	if (encendido) {
	  apagar();
	} else {
	  encender ();
	}
  }

  // --------------------
  // tiempo: Z --> brillar() -->
  // Enciende el LED, espera y lo apaga.
  // --------------------
  void brillar (long tiempo) {
	encender ();
	esperar(tiempo);
	apagar ();
  }
};

#endif
