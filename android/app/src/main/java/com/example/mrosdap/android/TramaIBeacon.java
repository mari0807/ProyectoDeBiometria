package com.example.mrosdap.android;

import java.util.Arrays;

// ----------------------------------------------------- TramaIBeacon -----------------------------------------------------
// |
// | prefijo: [ N ]_9
// | uuid: [ N ]_16
// | major: [ N ]_2
// | minor: [ N ]_2
// | tx_power: Z
// | los_bytes: [ N ]
// | adv_flags: [ N ]_3
// | adv_header: [ N ]_2
// | company_id: [ N ]_2
// | ibeacon_type: N
// | ibeacon_length: N
// |
// |
// los_bytes: [ N ] --> TramaIBeacon() -->
// |
// |
//           [ N ]_9 <-- getPrefijo() <--
// |
// |
//          [ N ]_16 <-- getUUID() <--
// |
// |
//           [ N ]_2 <-- getMajor() <--
// |
// |
//           [ N ]_2 <-- getMinor() <--
// |
// |
//                 Z <-- getTxPower() <--
// |
// |
//             [ N ] <-- getLosBytes() <--
// |
// |
//           [ N ]_3 <-- getAdvFlags() <--
// |
// |
//           [ N ]_2 <-- getAdvHeader() <--
// |
// |
//           [ N ]_2 <-- getCompanyID() <--
// |
// |
//                 N <-- getiBeaconType() <--
// |
// |
//                 N <-- getiBeaconLength() <--
// |
// ------------------------------------------------------------------------------------------------------------------------
public class TramaIBeacon {
    private byte[] prefijo = null;
    private byte[] uuid = null;
    private byte[] major = null;
    private byte[] minor = null;
    private byte txPower = 0;

    private byte[] losBytes;

    private byte[] advFlags = null;
    private byte[] advHeader = null;
    private byte[] companyID = new byte[2];
    private byte iBeaconType = 0;
    private byte iBeaconLength = 0;

    // --------------------
    // getPrefijo() --> [ N ]_9
    // --------------------
    public byte[] getPrefijo() {
        return prefijo;
    }

    // --------------------
    // getUUID() --> [ N ]_16
    // --------------------
    public byte[] getUUID() {
        return uuid;
    }

    // --------------------
    // getMajor() --> [ N ]_2
    // --------------------
    public byte[] getMajor() {
        return major;
    }

    // --------------------
    // getMinor() --> [ N ]_2
    // --------------------
    public byte[] getMinor() {
        return minor;
    }

    // --------------------
    // getTxPower() --> Z
    // --------------------
    public byte getTxPower() {
        return txPower;
    }

    // --------------------
    // getLosBytes() --> [ N ]
    // --------------------
    public byte[] getLosBytes() {
        return losBytes;
    }

    // --------------------
    // getAdvFlags() --> [ N ]_3
    // --------------------
    public byte[] getAdvFlags() {
        return advFlags;
    }

    // --------------------
    // getAdvHeader() --> [ N ]_2
    // --------------------
    public byte[] getAdvHeader() {
        return advHeader;
    }

    // --------------------
    // getCompanyID() --> [ N ]_2
    // --------------------
    public byte[] getCompanyID() {
        return companyID;
    }

    // --------------------
    // getiBeaconType() --> N
    // --------------------
    public byte getiBeaconType() {
        return iBeaconType;
    }

    // --------------------
    // getiBeaconLength() --> N
    // --------------------
    public byte getiBeaconLength() {
        return iBeaconLength;
    }

    // --------------------
    // los_bytes: [ N ] --> TramaIBeacon() -->
    // --------------------
    public TramaIBeacon(byte[] bytes) {
        this.losBytes = bytes;

        prefijo = Arrays.copyOfRange(losBytes, 0, 8 + 1);
        uuid = Arrays.copyOfRange(losBytes, 9, 24 + 1);
        major = Arrays.copyOfRange(losBytes, 25, 26 + 1);
        minor = Arrays.copyOfRange(losBytes, 27, 28 + 1);
        txPower = losBytes[29];

        advFlags = Arrays.copyOfRange(prefijo, 0, 2 + 1);
        advHeader = Arrays.copyOfRange(prefijo, 3, 4 + 1);
        companyID = Arrays.copyOfRange(prefijo, 5, 6 + 1);
        iBeaconType = prefijo[7];
        iBeaconLength = prefijo[8];
    }
}
