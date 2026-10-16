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
// --------------------
function Logica() {
}

// --------------------
// obtenerMedicion() --> medicion: Medicion
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
