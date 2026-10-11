package com.example.mrosdap.android;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String ETIQUETA_LOG = ">>>>";
    private static final int CODIGO_PETICION_PERMISOS = 11223344;

    // Debe coincidir con el nombre del Arduino (Publicador / EmisoraBLE)
    private static final String NOMBRE_BEACON = "Mari-Biometria";
    private static final String UUID_PROYECTO = "EPSG-GTI-PROY-3A";

    private BluetoothLeScanner elEscanner;
    private ScanCallback callbackDelEscaneo = null;

    private boolean tienePermisosBluetooth() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED;
        }
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void pedirPermisosBluetooth() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.BLUETOOTH_SCAN,
                            Manifest.permission.BLUETOOTH_CONNECT,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    CODIGO_PETICION_PERMISOS);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    CODIGO_PETICION_PERMISOS);
        }
    }

    private void prepararEscanner() {
        BluetoothAdapter bta = BluetoothAdapter.getDefaultAdapter();
        if (bta == null) {
            Toast.makeText(this, "Este móvil no tiene Bluetooth", Toast.LENGTH_LONG).show();
            return;
        }
        if (!bta.isEnabled()) {
            Toast.makeText(this, "Activa el Bluetooth", Toast.LENGTH_LONG).show();
            return;
        }
        this.elEscanner = bta.getBluetoothLeScanner();
    }

    private boolean prepararEscaneo(String accion) {
        if (!tienePermisosBluetooth()) {
            Log.d(ETIQUETA_LOG, accion + ": faltan permisos, los pido");
            Toast.makeText(this, "Concede permisos Bluetooth/ubicación", Toast.LENGTH_SHORT).show();
            pedirPermisosBluetooth();
            return false;
        }

        if (this.elEscanner == null) {
            prepararEscanner();
        }

        if (this.elEscanner == null) {
            Toast.makeText(this, "Activa el Bluetooth e inténtalo de nuevo", Toast.LENGTH_SHORT).show();
            return false;
        }

        detenerBusquedaDispositivosBTLE();
        return true;
    }

    private void buscarTodosLosDispositivosBTLE() {
        Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): empieza ");

        if (!prepararEscaneo("buscarTodosLosDispositivosBTLE")) {
            return;
        }

        this.callbackDelEscaneo = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult resultado) {
                super.onScanResult(callbackType, resultado);
                Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): onScanResult() ");
                mostrarInformacionDispositivoBTLE(resultado);
            }

            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                super.onBatchScanResults(results);
            }

            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
                Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): onScanFailed() code=" + errorCode);
            }
        };

        ScanSettings settings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build();

        this.elEscanner.startScan(null, settings, this.callbackDelEscaneo);
        Toast.makeText(this, "Escaneando todos los BTLE…", Toast.LENGTH_SHORT).show();
    }

    private String obtenerNombreDispositivo(ScanResult resultado) {
        if (resultado.getScanRecord() != null
                && resultado.getScanRecord().getDeviceName() != null) {
            return resultado.getScanRecord().getDeviceName();
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S
                || ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT)
                == PackageManager.PERMISSION_GRANTED) {
            try {
                return resultado.getDevice().getName();
            } catch (SecurityException ignored) {
            }
        }
        return null;
    }

    private boolean esNuestroBeacon(ScanResult resultado) {
        String nombre = obtenerNombreDispositivo(resultado);
        if (nombre != null && nombre.equalsIgnoreCase(NOMBRE_BEACON)) {
            return true;
        }

        if (resultado.getScanRecord() == null) {
            return false;
        }

        byte[] bytes = resultado.getScanRecord().getBytes();
        if (bytes == null || bytes.length < 25) {
            return false;
        }

        try {
            TramaIBeacon tib = new TramaIBeacon(bytes);
            String uuid = Utilidades.bytesToString(tib.getUUID());
            return uuid != null && uuid.contains(UUID_PROYECTO);
        } catch (Exception e) {
            return false;
        }
    }

    private void mostrarInformacionDispositivoBTLE(ScanResult resultado) {
        BluetoothDevice bluetoothDevice = resultado.getDevice();
        if (resultado.getScanRecord() == null) {
            return;
        }

        byte[] bytes = resultado.getScanRecord().getBytes();
        int rssi = resultado.getRssi();
        String nombre = obtenerNombreDispositivo(resultado);

        Log.d(ETIQUETA_LOG, " ****************************************************");
        Log.d(ETIQUETA_LOG, " ****** DISPOSITIVO DETECTADO BTLE ****************** ");
        Log.d(ETIQUETA_LOG, " nombre = " + nombre);
        Log.d(ETIQUETA_LOG, " dirección = " + bluetoothDevice.getAddress());
        Log.d(ETIQUETA_LOG, " rssi = " + rssi);

        if (bytes == null) {
            return;
        }

        Log.d(ETIQUETA_LOG, " bytes (" + bytes.length + ") = " + Utilidades.bytesToHexString(bytes));

        TramaIBeacon tib = new TramaIBeacon(bytes);
        Log.d(ETIQUETA_LOG, " uuid  = " + Utilidades.bytesToString(tib.getUUID()));
        Log.d(ETIQUETA_LOG, " major  = " + Utilidades.bytesToInt(tib.getMajor()));
        Log.d(ETIQUETA_LOG, " minor  = " + Utilidades.bytesToInt(tib.getMinor()));
        Log.d(ETIQUETA_LOG, " ****************************************************");
    }

    private void buscarEsteDispositivoBTLE(final String dispositivoBuscado) {
        Log.d(ETIQUETA_LOG, " buscarEsteDispositivoBTLE(): empieza buscando " + dispositivoBuscado);

        if (!prepararEscaneo("buscarEsteDispositivoBTLE")) {
            return;
        }

        this.callbackDelEscaneo = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult resultado) {
                super.onScanResult(callbackType, resultado);
                if (!esNuestroBeacon(resultado)) {
                    return;
                }
                Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): encontrado " + dispositivoBuscado);
                Toast.makeText(MainActivity.this, "Encontrado: " + dispositivoBuscado, Toast.LENGTH_SHORT).show();
                mostrarInformacionDispositivoBTLE(resultado);
            }

            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                super.onBatchScanResults(results);
            }

            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
                Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): onScanFailed() code=" + errorCode);
            }
        };

        ScanSettings settings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build();

        this.elEscanner.startScan(null, settings, this.callbackDelEscaneo);
        Toast.makeText(this, "Buscando " + dispositivoBuscado + "…", Toast.LENGTH_SHORT).show();
    }

    private void detenerBusquedaDispositivosBTLE() {
        if (this.callbackDelEscaneo == null || this.elEscanner == null) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
                && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN)
                != PackageManager.PERMISSION_GRANTED) {
            this.callbackDelEscaneo = null;
            return;
        }

        this.elEscanner.stopScan(this.callbackDelEscaneo);
        this.callbackDelEscaneo = null;
    }

    public void botonBuscarDispositivosBTLEPulsado(View v) {
        Log.d(ETIQUETA_LOG, " boton buscar dispositivos BTLE Pulsado");
        this.buscarTodosLosDispositivosBTLE();
    }

    public void botonBuscarNuestroDispositivoBTLEPulsado(View v) {
        Log.d(ETIQUETA_LOG, " boton nuestro dispositivo BTLE Pulsado");
        this.buscarEsteDispositivoBTLE(NOMBRE_BEACON);
    }

    public void botonDetenerBusquedaDispositivosBTLEPulsado(View v) {
        Log.d(ETIQUETA_LOG, " boton detener busqueda dispositivos BTLE Pulsado");
        this.detenerBusquedaDispositivosBTLE();
        Toast.makeText(this, "Escaneo detenido", Toast.LENGTH_SHORT).show();
    }

    private void inicializarBlueTooth() {
        prepararEscanner();
        if (!tienePermisosBluetooth()) {
            pedirPermisosBluetooth();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.botonBuscarDispositivosBTLE)
                .setOnClickListener(this::botonBuscarDispositivosBTLEPulsado);
        findViewById(R.id.botonBuscarNuestroDispositivoBTLE)
                .setOnClickListener(this::botonBuscarNuestroDispositivoBTLEPulsado);
        findViewById(R.id.botonDetenerBusquedaDispositivosBTLE)
                .setOnClickListener(this::botonDetenerBusquedaDispositivosBTLEPulsado);

        inicializarBlueTooth();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CODIGO_PETICION_PERMISOS) {
            if (tienePermisosBluetooth()) {
                prepararEscanner();
                Toast.makeText(this, "Permisos OK. Ya puedes escanear.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Sin permisos no se puede escanear BLE", Toast.LENGTH_LONG).show();
            }
        }
    }

} // class
