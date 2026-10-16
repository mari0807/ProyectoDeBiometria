// -*- mode: c++ -*-
// Nombre: Publicador.h
// Descripción: Publica el CO2 como iBeacon usando la emisora BLE.
// Copyright: (c) 2026 Mari Rosenova Dapcheva
// Fecha: 16/10/2026
// Autor: Mari Rosenova Dapcheva
// Aportación: paso de la medida al anuncio.

// MedicionesID = { CO2, TEMPERATURA, RUIDO }
//
// ----------------------------------------- Publicador -----------------------------------------
// |
// | beacon_uuid: [ N ]_16
// | emisora: EmisoraBLE
// | rssi: Z
// |
// |
//                                                                                Publicador() -->
// |
// |
//                                                                            encenderEmisora() -->
// |
// |
// valor_co2: Z, contador: N, tiempo_espera: Z --> publicarCO2() -->
// |
// ----------------------------------------------------------------------------------------------

#ifndef PUBLICADOR_H_INCLUIDO
#define PUBLICADOR_H_INCLUIDO

class Publicador {

private:

  uint8_t beaconUUID[16] = {
	'E', 'P', 'S', 'G', '-', 'G', 'T', 'I',
	'-', 'P', 'R', 'O', 'Y', '-', '3', 'A'
	};

public:
  EmisoraBLE laEmisora {
	"Mari-Biometria",
	  0x004c,
	  4
	  };

  const int RSSI = -53;

public:

  enum MedicionesID  {
	CO2 = 11,
	TEMPERATURA = 12,
	RUIDO = 13
  };

  // --------------------
  // Publicador() -->
  // Crea el publicador con su emisora.
  // --------------------
  Publicador( ) {
  }

  // --------------------
  // encenderEmisora() -->
  // Pide a la emisora que se encienda.
  // --------------------
  void encenderEmisora() {
	(*this).laEmisora.encenderEmisora();
  }

  // --------------------
  // valor_co2: Z, contador: N, tiempo_espera: Z --> publicarCO2() -->
  // Emite el iBeacon con el CO2 en el minor.
  // --------------------
  void publicarCO2( int16_t valorCO2, uint8_t contador,
					long tiempoEspera ) {

	uint16_t major = (MedicionesID::CO2 << 8) + contador;
	char carga[21];
	memcpy( &carga[0], (*this).beaconUUID, 16 );
	carga[16] = (char) ((major >> 8) & 0xFF);
	carga[17] = (char) (major & 0xFF);
	carga[18] = (char) ((valorCO2 >> 8) & 0xFF);
	carga[19] = (char) (valorCO2 & 0xFF);
	carga[20] = (char) (*this).RSSI;

	(*this).laEmisora.emitirAnuncioIBeaconLibre( carga, 21 );

	esperar( tiempoEspera );
  }

};

#endif
