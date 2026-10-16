package com.example.mrosdap.android;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EnvioBeaconTest {

    @Test
    public void alEncontrarElBeaconEnviaNombreUuidMajorYMinor() {
        byte[] anuncio = new byte[30];
        String uuid = "EPSG-GTI-PROY-3A";
        byte[] uuidBytes = uuid.getBytes();
        System.arraycopy(uuidBytes, 0, anuncio, 9, uuidBytes.length);
        anuncio[25] = 0x0B;
        anuncio[26] = 0x01;
        anuncio[27] = 0x00;
        anuncio[28] = 0x7B;

        TramaIBeacon trama = new TramaIBeacon(anuncio);
        String nombre = "Mari-Biometria";
        String uuidLeido = Utilidades.bytesToString(trama.getUUID());
        int major = Utilidades.bytesToInt(trama.getMajor());
        int minor = Utilidades.bytesToInt(trama.getMinor());

        assertEquals("Mari-Biometria", nombre);
        assertEquals("EPSG-GTI-PROY-3A", uuidLeido);
        assertEquals(2817, major);
        assertEquals(123, minor);
    }
}
