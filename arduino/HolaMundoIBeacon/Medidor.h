// -*- mode: c++ -*-

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
  // --------------------
  Medidor(  ) {
  }

  // --------------------
  // iniciarMedidor() -->
  // --------------------
  void iniciarMedidor() {
  }

  // --------------------
  // medirCO2() --> Z
  // --------------------
  int medirCO2() {
	return 235;
  }

  // --------------------
  // medirTemperatura() --> Z
  // --------------------
  int medirTemperatura() {
	return -12;
  }

};

#endif
