package com.example.mrosdap.android;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.UUID;

// ------------------------------------------------------ Utilidades ------------------------------------------------------
// |
// |
// |
//                    texto: Text --> stringToBytes() --x
//                            [ N ] <--
// |
// |
//                     uuid: Text --> stringToUUID() --x
//                            Text <--
// |
// |
//                     uuid: Text --> uuidToString() --x
//                            Text <--
// |
// |
//                     uuid: Text --> uuidToHexString() --x
//                            Text <--
// |
// |
//                     bytes: [ N ] --> bytesToString() --x
//                              Text <--
// |
// |
// mas_significativos: Z, menos_significativos: Z --> dosLongToBytes() --x
//                                              [ N ]_16 <--
// |
// |
//                     bytes: [ N ] --> bytesToInt() --x
//                                 Z <--
// |
// |
//                     bytes: [ N ] --> bytesToLong() --x
//                                 Z <--
// |
// |
//                     bytes: [ N ] --> bytesToIntOK() --x
//                                 Z <--
// |
// |
//                     bytes: [ N ] --> bytesToHexString() --x
//                              Text <--
// |
// ------------------------------------------------------------------------------------------------------------------------
public class Utilidades {

    // --------------------
    // texto: Text --> stringToBytes() --x
    // [ N ] <--
    // --------------------
    public static byte[] stringToBytes(String texto) {
        return texto.getBytes();
    }

    // --------------------
    // uuid: Text --> stringToUUID() --x
    // Text <--
    // --------------------
    public static UUID stringToUUID(String uuid) {
        if (uuid.length() != 16) {
            throw new Error("stringUUID: string no tiene 16 caracteres ");
        }
        String masSignificativo = uuid.substring(0, 8);
        String menosSignificativo = uuid.substring(8, 16);
        UUID res = new UUID(Utilidades.bytesToLong(masSignificativo.getBytes()), Utilidades.bytesToLong(menosSignificativo.getBytes()));

        return res;
    }

    // --------------------
    // uuid: Text --> uuidToString() --x
    // Text <--
    // --------------------
    public static String uuidToString(UUID uuid) {
        return bytesToString(dosLongToBytes(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits()));
    }

    // --------------------
    // uuid: Text --> uuidToHexString() --x
    // Text <--
    // --------------------
    public static String uuidToHexString(UUID uuid) {
        return bytesToHexString(dosLongToBytes(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits()));
    }

    // --------------------
    // bytes: [ N ] --> bytesToString() --x
    // Text <--
    // --------------------
    public static String bytesToString(byte[] bytes) {
        if (bytes == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append((char) b);
        }
        return sb.toString();
    }

    // --------------------
    // mas_significativos: Z, menos_significativos: Z --> dosLongToBytes() --x
    // [ N ]_16 <--
    // --------------------
    public static byte[] dosLongToBytes(long masSignificativos, long menosSignificativos) {
        ByteBuffer buffer = ByteBuffer.allocate(2 * Long.BYTES);
        buffer.putLong(masSignificativos);
        buffer.putLong(menosSignificativos);
        return buffer.array();
    }

    // --------------------
    // bytes: [ N ] --> bytesToInt() --x
    // Z <--
    // --------------------
    public static int bytesToInt(byte[] bytes) {
        return new BigInteger(bytes).intValue();
    }

    // --------------------
    // bytes: [ N ] --> bytesToLong() --x
    // Z <--
    // --------------------
    public static long bytesToLong(byte[] bytes) {
        return new BigInteger(bytes).longValue();
    }

    // --------------------
    // bytes: [ N ] --> bytesToIntOK() --x
    // Z <--
    // --------------------
    public static int bytesToIntOK(byte[] bytes) {
        if (bytes == null) {
            return 0;
        }

        if (bytes.length > 4) {
            throw new Error("demasiados bytes para pasar a int ");
        }
        int res = 0;

        for (byte b : bytes) {
            res = (res << 8)
                    + (b & 0xFF);
        }

        if ((bytes[0] & 0x8) != 0) {
            res = -(~(byte) res) - 1;
        }

        return res;
    }

    // --------------------
    // bytes: [ N ] --> bytesToHexString() --x
    // Text <--
    // --------------------
    public static String bytesToHexString(byte[] bytes) {

        if (bytes == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
            sb.append(':');
        }
        return sb.toString();
    }
}
