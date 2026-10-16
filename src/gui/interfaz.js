// Nombre: interfaz.js
// Descripción: Pinta la última medición y la vuelve a pedir cada 10 segundos.
// Copyright: (c) 2026 Mari Rosenova Dapcheva
// Fecha: 16/10/2026
// Autor: Mari Rosenova Dapcheva
// Aportación: pantalla de la web. Llama solo a la lógica fake.
document.addEventListener("DOMContentLoaded", function () {

    // --------------------
    // medicion: Medicion --> pintarMedicion()
    // Rellena la tabla con una medición o con el aviso de que no hay.
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
    // Pide la última medición a la lógica fake y actualiza la tabla.
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
