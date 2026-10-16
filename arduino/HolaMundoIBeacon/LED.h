// -*- mode: c++ -*-

// tiempo: Z --> esperar()

#ifndef LED_H_INCLUIDO
#define LED_H_INCLUIDO

// --------------------
// tiempo: Z --> esperar()
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
  // --------------------
  LED (int numero)
	: numeroLED (numero), encendido(false)
  {
	pinMode(numeroLED, OUTPUT);
	apagar ();
  }

  // --------------------
  // encender() -->
  // --------------------
  void encender () {
	digitalWrite(numeroLED, HIGH);
	encendido = true;
  }

  // --------------------
  // apagar() -->
  // --------------------
  void apagar () {
	  digitalWrite(numeroLED, LOW);
	  encendido = false;
  }

  // --------------------
  // alternar() -->
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
  // --------------------
  void brillar (long tiempo) {
	encender ();
	esperar(tiempo);
	apagar ();
  }
};

#endif
