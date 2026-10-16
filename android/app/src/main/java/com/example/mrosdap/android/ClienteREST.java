package com.example.mrosdap.android;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

// ---------------------------------------------- ClienteREST ----------------------------------------------
// |
// nombre: Text, uuid: Text, major: N, minor: N --> enviarMedicion() --x
// |
// ----------------------------------------------------------------------------------------------------------
public class ClienteREST {

    public interface Callback {
        void onResultado(boolean ok, String mensaje);
    }

    // --------------------
    // nombre: Text, uuid: Text, major: N, minor: N --> enviarMedicion() --x
    // --------------------
    public static void enviarMedicion(
            String nombre,
            String uuid,
            int major,
            int minor,
            Callback callback
    ) {
        new Thread(() -> {
            HttpURLConnection conn = null;
            try {
                JSONObject json = new JSONObject();
                json.put("nombre", nombre);
                json.put("uuid", uuid);
                json.put("major", major);
                json.put("minor", minor);

                byte[] body = json.toString().getBytes(StandardCharsets.UTF_8);

                URL url = new URL("http://172.20.10.4/biometria/src/communication/ReglasREST.php");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setDoOutput(true);
                conn.setConnectTimeout(8000);
                conn.setReadTimeout(8000);

                try (OutputStream os = conn.getOutputStream()) {
                    os.write(body);
                }

                int code = conn.getResponseCode();
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        code >= 400 ? conn.getErrorStream() : conn.getInputStream(),
                        StandardCharsets.UTF_8
                ));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                reader.close();

                String respuesta = sb.toString();
                Log.d(">>>>", "POST servidor code=" + code + " body=" + respuesta);

                boolean ok = false;
                String mensaje = respuesta;
                try {
                    JSONObject res = new JSONObject(respuesta);
                    ok = res.optBoolean("success", false);
                    mensaje = res.optString("mensaje", respuesta);
                    if (ok && res.has("id")) {
                        mensaje = mensaje + " (id=" + res.get("id") + ")";
                    }
                } catch (Exception ignored) {
                    ok = (code >= 200 && code < 300);
                }

                final boolean okFinal = ok;
                final String msgFinal = mensaje;
                new Handler(Looper.getMainLooper()).post(() -> {
                    if (callback != null) {
                        callback.onResultado(okFinal, msgFinal);
                    }
                });

            } catch (Exception e) {
                Log.e(">>>>", "Error POST servidor: " + e.getMessage(), e);
                new Handler(Looper.getMainLooper()).post(() -> {
                    if (callback != null) {
                        callback.onResultado(false, "Error de red: " + e.getMessage());
                    }
                });
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }).start();
    }
}
