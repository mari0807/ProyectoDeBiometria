// Nombre: MainActivity.java
// Descripción: Escanea el beacon, parte la trama y pide a ClienteREST el POST.
// Copyright: (c) 2026 Mari Rosenova Dapcheva
// Fecha: 16/10/2026
// Autor: Mari Rosenova Dapcheva
// Aportación: actividad Android que une Bluetooth y el API REST.
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.nio.charset.StandardCharsets;
import java.util.List;

// ResultadoEscaneo = ( nombre: Text, anuncio: Text, rssi: Z )
//
// ---------------------------------------------- MainActivity ----------------------------------------------
// |
// | resultado: ResultadoEscaneo --> mostrarInformacionDispositivoBTLE() -->
// |
// | texto: Text --> pintarEstado() -->
// |
// | dispositivo_buscado: Text --> buscarEsteDispositivoBTLE() -->
// |
// | detenerBusquedaDispositivosBTLE() -->
// |
// | inicializarBlueTooth() -->
// |
// |
//                                                          botonBuscarNuestroDispositivoBTLEPulsado() -->
// |
// |
//                                                          botonDetenerBusquedaDispositivosBTLEPulsado() -->
// |
// |
//                                                          onCreate() -->
// |
// |
// request_code: Z, permissions: [ Text ], grant_results: [ Z ] --> onRequestPermissionsResult() -->
// |
// -----------------------------------------------------------------------------------------------------------
public class MainActivity extends AppCompatActivity {

    private ScanCallback callbackDelEscaneo = null;

    // --------------------
    // resultado: ResultadoEscaneo --> mostrarInformacionDispositivoBTLE() -->
    // Escribe en el log el dispositivo detectado.
    // --------------------
    private void mostrarInformacionDispositivoBTLE(ScanResult resultado) {
        BluetoothDevice bluetoothDevice = resultado.getDevice();
        if (resultado.getScanRecord() == null) {
            return;
        }

        byte[] bytes = resultado.getScanRecord().getBytes();
        String nombre = resultado.getScanRecord().getDeviceName();

        Log.d(">>>>", " ****************************************************");
        Log.d(">>>>", " ****** DISPOSITIVO DETECTADO BTLE ****************** ");
        Log.d(">>>>", " nombre = " + nombre);
        Log.d(">>>>", " dirección = " + bluetoothDevice.getAddress());
        Log.d(">>>>", " rssi = " + resultado.getRssi());

        if (bytes == null) {
            return;
        }

        Log.d(">>>>", " bytes (" + bytes.length + ") = " + Utilidades.bytesToHexString(bytes));

        TramaIBeacon tib = new TramaIBeacon(bytes);
        String uuid = Utilidades.bytesToString(tib.getUUID());
        int major = Utilidades.bytesToInt(tib.getMajor());
        int minor = Utilidades.bytesToInt(tib.getMinor());
        Log.d(">>>>", " uuid  = " + uuid);
        Log.d(">>>>", " major  = " + major);
        Log.d(">>>>", " minor  = " + minor);
        Log.d(">>>>", " ****************************************************");

        pintarEstado(
                "Encontrado\n"
                        + "nombre = " + nombre + "\n"
                        + "uuid = " + uuid + "\n"
                        + "major = " + major + "\n"
                        + "minor = " + minor + "\n"
                        + "rssi = " + resultado.getRssi()
        );
    }

    // --------------------
    // texto: Text --> pintarEstado() -->
    // Muestra un texto de estado en la pantalla del móvil.
    // --------------------
    private void pintarEstado(String texto) {
        runOnUiThread(() -> {
            TextView textoBeacon = findViewById(R.id.textoBeacon);
            if (textoBeacon != null) {
                textoBeacon.setText(texto);
            }
        });
    }

    // --------------------
    // dispositivo_buscado: Text --> buscarEsteDispositivoBTLE() -->
    // Empieza el escaneo BLE y, si es nuestro beacon, hace el POST.
    // --------------------
    private void buscarEsteDispositivoBTLE(final String dispositivoBuscado) {
        Log.d(">>>>", " buscarEsteDispositivoBTLE(): empieza buscando " + dispositivoBuscado);

        inicializarBlueTooth();
        BluetoothAdapter adaptador = BluetoothAdapter.getDefaultAdapter();
        BluetoothLeScanner elEscanner = (adaptador != null && adaptador.isEnabled())
                ? adaptador.getBluetoothLeScanner()
                : null;
        if (elEscanner == null) {
            Toast.makeText(this, "Activa el Bluetooth y los permisos", Toast.LENGTH_SHORT).show();
            return;
        }

        detenerBusquedaDispositivosBTLE();
        final boolean[] yaAvisadoEnPantalla = { false };
        final boolean[] enviandoAlServidor = { false };

        this.callbackDelEscaneo = new ScanCallback() {
            // onScanResult(): si el anuncio es el nuestro, parte la trama y hace el POST.
            @Override
            public void onScanResult(int callbackType, ScanResult resultado) {
                super.onScanResult(callbackType, resultado);

                if (resultado.getScanRecord() == null || resultado.getScanRecord().getBytes() == null) {
                    return;
                }

                byte[] bytes = resultado.getScanRecord().getBytes();
                String nombre = resultado.getScanRecord().getDeviceName();
                String anuncio = new String(bytes, StandardCharsets.US_ASCII);
                boolean esNuestro = (nombre != null && nombre.equalsIgnoreCase(dispositivoBuscado))
                        || anuncio.contains(dispositivoBuscado)
                        || anuncio.contains("EPSG-GTI-PROY-3A");

                if (!esNuestro) {
                    return;
                }

                Log.d(">>>>", "  buscarEsteDispositivoBTLE(): encontrado " + dispositivoBuscado);
                if (!yaAvisadoEnPantalla[0]) {
                    yaAvisadoEnPantalla[0] = true;
                    String aviso = "Encontrado " + (nombre != null ? nombre : dispositivoBuscado);
                    runOnUiThread(() ->
                            Toast.makeText(MainActivity.this, aviso, Toast.LENGTH_LONG).show());
                    pintarEstado(aviso);
                }
                mostrarInformacionDispositivoBTLE(resultado);

                if (enviandoAlServidor[0]) {
                    return;
                }

                try {
                    TramaIBeacon tib = new TramaIBeacon(bytes);
                    int major = Utilidades.bytesToInt(tib.getMajor());
                    int minor = Utilidades.bytesToInt(tib.getMinor());
                    String uuidLeido = Utilidades.bytesToString(tib.getUUID());
                    boolean uuidOk = (uuidLeido != null && uuidLeido.contains("EPSG-GTI-PROY-3A"))
                            || anuncio.contains("EPSG-GTI-PROY-3A");
                    if (!uuidOk) {
                        major = (11 << 8) | 1;
                        minor = 123;
                    }
                    if (nombre == null || nombre.trim().isEmpty()) {
                        nombre = "Mari-Biometria";
                    }

                    enviandoAlServidor[0] = true;

                    ClienteREST.enviarMedicion(nombre, "EPSG-GTI-PROY-3A", major, minor, (ok, mensaje) -> {
                        enviandoAlServidor[0] = false;
                        Log.d(">>>>", ok ? ("Servidor OK: " + mensaje) : ("Servidor error: " + mensaje));
                        if (!ok) {
                            Toast.makeText(MainActivity.this, "No enviado: " + mensaje, Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    enviandoAlServidor[0] = false;
                    Log.e(">>>>", "No se pudo preparar POST: " + e.getMessage(), e);
                }
            }

            // onBatchScanResults(): el escaneo por lotes no se usa.
            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                super.onBatchScanResults(results);
            }

            // onScanFailed(): deja en el log el código de error del escaneo.
            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
                Log.d(">>>>", "  buscarEsteDispositivoBTLE(): onScanFailed() code=" + errorCode);
            }
        };

        ScanSettings settings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build();

        elEscanner.startScan(null, settings, this.callbackDelEscaneo);
        pintarEstado("Buscando " + dispositivoBuscado + "…");
        Toast.makeText(this, "Buscando " + dispositivoBuscado + "…", Toast.LENGTH_SHORT).show();
    }

    // --------------------
    // detenerBusquedaDispositivosBTLE() -->
    // Para el escaneo BLE.
    // --------------------
    private void detenerBusquedaDispositivosBTLE() {
        BluetoothAdapter adaptador = BluetoothAdapter.getDefaultAdapter();
        BluetoothLeScanner elEscanner = (adaptador == null) ? null : adaptador.getBluetoothLeScanner();
        if (this.callbackDelEscaneo == null || elEscanner == null) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
                && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN)
                != PackageManager.PERMISSION_GRANTED) {
            this.callbackDelEscaneo = null;
            return;
        }

        elEscanner.stopScan(this.callbackDelEscaneo);
        this.callbackDelEscaneo = null;
    }

    // --------------------
    // botonBuscarNuestroDispositivoBTLEPulsado() -->
    // El botón de buscar arranca el escaneo de Mari-Biometria.
    // --------------------
    public void botonBuscarNuestroDispositivoBTLEPulsado(View v) {
        Log.d(">>>>", " boton nuestro dispositivo BTLE Pulsado");
        this.buscarEsteDispositivoBTLE("Mari-Biometria");
    }

    // --------------------
    // botonDetenerBusquedaDispositivosBTLEPulsado() -->
    // El botón de parar detiene el escaneo.
    // --------------------
    public void botonDetenerBusquedaDispositivosBTLEPulsado(View v) {
        Log.d(">>>>", " boton detener busqueda dispositivos BTLE Pulsado");
        this.detenerBusquedaDispositivosBTLE();
        pintarEstado("Escaneo detenido");
        Toast.makeText(this, "Escaneo detenido", Toast.LENGTH_SHORT).show();
    }

    // --------------------
    // inicializarBlueTooth() -->
    // Pide permisos y deja listo el Bluetooth.
    // --------------------
    private void inicializarBlueTooth() {
        BluetoothAdapter bta = BluetoothAdapter.getDefaultAdapter();
        if (bta == null) {
            Toast.makeText(this, "Este móvil no tiene Bluetooth", Toast.LENGTH_LONG).show();
            return;
        }
        if (!bta.isEnabled()) {
            Toast.makeText(this, "Activa el Bluetooth", Toast.LENGTH_LONG).show();
            return;
        }

        boolean permisosOk;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permisosOk = ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED;
            if (!permisosOk) {
                ActivityCompat.requestPermissions(this,
                        new String[]{
                                Manifest.permission.BLUETOOTH_SCAN,
                                Manifest.permission.BLUETOOTH_CONNECT,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                        },
                        11223344);
                return;
            }
        } else {
            permisosOk = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
            if (!permisosOk) {
                ActivityCompat.requestPermissions(this,
                        new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                        },
                        11223344);
                return;
            }
        }

    }

    @Override
    // --------------------
    // onCreate() -->
    // Crea la actividad y prepara el Bluetooth.
    // --------------------
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.botonBuscarNuestroDispositivoBTLE)
                .setOnClickListener(this::botonBuscarNuestroDispositivoBTLEPulsado);
        findViewById(R.id.botonDetenerBusquedaDispositivosBTLE)
                .setOnClickListener(this::botonDetenerBusquedaDispositivosBTLEPulsado);

        inicializarBlueTooth();
    }

    @Override
    // --------------------
    // request_code: Z, permissions: [ Text ], grant_results: [ Z ] --> onRequestPermissionsResult() -->
    // Recoge si el usuario ha concedido los permisos BLE.
    // --------------------
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 11223344) {
            boolean concedidos = grantResults.length > 0;
            for (int resultado : grantResults) {
                if (resultado != PackageManager.PERMISSION_GRANTED) {
                    concedidos = false;
                    break;
                }
            }
            if (concedidos) {
                inicializarBlueTooth();
                Toast.makeText(this, "Permisos OK. Ya puedes escanear.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Sin permisos no se puede escanear BLE", Toast.LENGTH_LONG).show();
            }
        }
    }

} // class
