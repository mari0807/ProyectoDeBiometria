document.addEventListener("DOMContentLoaded", function () {

    // --------------------
    // medicion: Medicion --> pintarMedicion()
    // --------------------
    function pintarMedicion(medida) {
        var tableBody = document.querySelector("#medicionesTable tbody");
        if (!tableBody) {
            return;
        }
        tableBody.innerHTML = "";

        if (!medida) {
            tableBody.innerHTML = "<tr><td colspan=\"6\">No hay mediciones</td></tr>";
            return;
        }

        var row = document.createElement("tr");
        row.innerHTML =
            "<td data-label=\"ID\">" + medida.id_medicion + "</td>" +
            "<td data-label=\"Nombre\">" + medida.nombre + "</td>" +
            "<td data-label=\"UUID\">" + medida.uuid + "</td>" +
            "<td data-label=\"Major\">" + medida.major + "</td>" +
            "<td data-label=\"Minor\">" + medida.minor + "</td>" +
            "<td data-label=\"Hora\">" + medida.timestamp + "</td>";
        tableBody.appendChild(row);
    }

    // --------------------
    // cargarMediciones()
    // --------------------
    var laLogica = new Logica();

    function cargarMediciones() {
        var estado = document.getElementById("estadoActualizacion");
        laLogica.obtenerMedicion(function (err, medicion) {
            if (err) {
                if (estado) {
                    estado.textContent = "Error al actualizar: " + err.message;
                }
                return;
            }
            pintarMedicion(medicion);
            if (estado) {
                estado.textContent = medicion
                    ? "Última medición, actualizada a las " + new Date().toLocaleTimeString()
                    : "No hay mediciones";
            }
        });
    }

    cargarMediciones();
    setInterval(cargarMediciones, 10000);
});
