// Nombre: LogicaProxy.js
// Descripción: Lógica fake del cliente web. obtenerMedicion hace un GET al API REST.
// Copyright: (c) 2026 Mari Rosenova Dapcheva
// Fecha: 16/10/2026
// Autor: Mari Rosenova Dapcheva
// Aportación: la página no accede a MySQL.
// ----------------------------------------- Logica -----------------------------------------
// |
// |
// |
//                                                                                Logica() -->
// |
// |
//                                 medicion: Medicion <-- obtenerMedicion() <--
// |
// ------------------------------------------------------------------------------------------

// --------------------
// Logica() -->
// Construye el objeto de lógica.
// --------------------
function Logica() {
}

// --------------------
// obtenerMedicion() --> medicion: Medicion
// Devuelve la última medición. En la web, el resultado llega por callback.
// --------------------
Logica.prototype.obtenerMedicion = function (callback) {
    var url = "../communication/ReglasREST.php?t=" + Date.now();

    fetch(url, {
        method: "GET",
        cache: "no-store",
        headers: { "Cache-Control": "no-cache" }
    })
        .then(function (response) {
            if (!response.ok) {
                throw new Error("HTTP " + response.status);
            }
            return response.json();
        })
        .then(function (data) {
            if (data && data.success) {
                callback(null, data.medicion || null);
            } else {
                callback(new Error(data && data.mensaje ? data.mensaje : "Sin datos"));
            }
        })
        .catch(function (err) {
            callback(err);
        });
};
