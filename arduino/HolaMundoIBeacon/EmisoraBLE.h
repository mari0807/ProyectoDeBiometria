// -*- mode: c++ -*-

// ---------------------------------------------- EmisoraBLE ----------------------------------------------
// |
// | nombre_emisora: Text
// | fabricante_id: N
// | tx_power: Z
// |
// |
// nombre_emisora: Text, fabricante_id: N, tx_power: Z --> EmisoraBLE() -->
// |
// |
//                                                                      encenderEmisora() -->
// |
// |
//                                                                       detenerAnuncio() -->
// |
// |
//                                                            B <-- estaAnunciando() <--
// |
// |
// beacon_uuid: [ N ]_16, major: Z, minor: Z, rssi: N --> emitirAnuncioIBeacon() -->
// |
// |
// carga: Text, tamanyo_carga: N --> emitirAnuncioIBeaconLibre() -->
// |
// |
//                                                     instalarCallbackConexionEstablecida() -->
// |
// |
//                                                        instalarCallbackConexionTerminada() -->
// |
// |
// conn_handle: N --> getConexion() <--
// |
// -------------------------------------------------------------------------------------------------------

#ifndef EMISORA_H_INCLUIDO
#define EMISORA_H_INCLUIDO

class EmisoraBLE {
private:

  const char * nombreEmisora;
  const uint16_t fabricanteID;
  const int8_t txPower;

public:

  using CallbackConexionEstablecida = void ( uint16_t connHandle );
  using CallbackConexionTerminada = void ( uint16_t connHandle, uint8_t reason);

  // --------------------
  // nombre_emisora: Text, fabricante_id: N, tx_power: Z --> EmisoraBLE() -->
  // --------------------
  EmisoraBLE( const char * nombreEmisora_, const uint16_t fabricanteID_,
			  const int8_t txPower_ )
	:
	nombreEmisora( nombreEmisora_ ) ,
	fabricanteID( fabricanteID_ ) ,
	txPower( txPower_ )
  {
  }

  // --------------------
  // encenderEmisora() -->
  // --------------------
  void encenderEmisora() {
	 Bluefruit.begin();

	 (*this).detenerAnuncio();
  }

  // --------------------
  // encenderEmisora() -->
  // --------------------
  void encenderEmisora( CallbackConexionEstablecida cbce,
						CallbackConexionTerminada cbct ) {

	encenderEmisora();

	instalarCallbackConexionEstablecida( cbce );
	instalarCallbackConexionTerminada( cbct );

  }

  // --------------------
  // detenerAnuncio() -->
  // --------------------
  void detenerAnuncio() {

	if ( (*this).estaAnunciando() ) {
	  Bluefruit.Advertising.stop();
	}

  }

  // --------------------
  // estaAnunciando() --> B
  // --------------------
  bool estaAnunciando() {
	return Bluefruit.Advertising.isRunning();
  }

  // --------------------
  // beacon_uuid: [ N ]_16, major: Z, minor: Z, rssi: N --> emitirAnuncioIBeacon() -->
  // --------------------
  void emitirAnuncioIBeacon(uint8_t *beaconUUID,
                            int16_t major,
                            int16_t minor,
                            uint8_t rssi) {
    detenerAnuncio();
    Bluefruit.Advertising.clearData();
    Bluefruit.ScanResponse.clearData();

    Bluefruit.setTxPower(txPower);
    Bluefruit.setName(nombreEmisora);

    Bluefruit.Advertising.addFlags(
        BLE_GAP_ADV_FLAGS_LE_ONLY_GENERAL_DISC_MODE
    );

    BLEBeacon beacon(beaconUUID, major, minor, (int8_t) rssi);
    beacon.setManufacturer(fabricanteID);
    Bluefruit.Advertising.setBeacon(beacon);

    Bluefruit.ScanResponse.addName();

    Bluefruit.Advertising.restartOnDisconnect(true);
    Bluefruit.Advertising.setInterval(160, 160);
    Bluefruit.Advertising.start(0);
  }

  // --------------------
  // carga: Text, tamanyo_carga: N --> emitirAnuncioIBeaconLibre() -->
  // --------------------
  void emitirAnuncioIBeaconLibre( const char * carga, const uint8_t tamanyoCarga ) {

	(*this).detenerAnuncio();

	Bluefruit.Advertising.clearData();
	Bluefruit.ScanResponse.clearData();

	Bluefruit.setName( (*this).nombreEmisora );
	Bluefruit.ScanResponse.addName();

	Bluefruit.Advertising.addFlags(BLE_GAP_ADV_FLAGS_LE_ONLY_GENERAL_DISC_MODE);

	uint8_t restoPrefijoYCarga[4+21] = {
	  0x4c, 0x00,
	  0x02,
	  21,
	  '-', '-', '-', '-',
	  '-', '-', '-', '-',
	  '-', '-', '-', '-',
	  '-', '-', '-', '-',
	  '-'
	};

	memcpy( &restoPrefijoYCarga[4], &carga[0], ( tamanyoCarga > 21 ? 21 : tamanyoCarga ) );

	Bluefruit.Advertising.addData( BLE_GAP_AD_TYPE_MANUFACTURER_SPECIFIC_DATA,
								   &restoPrefijoYCarga[0],
								   4+21 );

	Bluefruit.Advertising.restartOnDisconnect(true);
	Bluefruit.Advertising.setInterval(100, 100);

	Bluefruit.Advertising.setFastTimeout( 1 );

	Bluefruit.Advertising.start( 0 );

	Globales::elPuerto.escribir( "emitiriBeacon libre  Bluefruit.Advertising.start( 0 );  \n");
  }

  // --------------------
  // instalarCallbackConexionEstablecida() -->
  // --------------------
  void instalarCallbackConexionEstablecida( CallbackConexionEstablecida cb ) {
	Bluefruit.Periph.setConnectCallback( cb );
  }

  // --------------------
  // instalarCallbackConexionTerminada() -->
  // --------------------
  void instalarCallbackConexionTerminada( CallbackConexionTerminada cb ) {
	Bluefruit.Periph.setDisconnectCallback( cb );
  }

  // --------------------
  // conn_handle: N --> getConexion() <--
  // --------------------
  BLEConnection * getConexion( uint16_t connHandle ) {
	return Bluefruit.Connection( connHandle );
  }

};

#endif
