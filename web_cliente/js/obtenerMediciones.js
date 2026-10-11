// ------------------------------------------------------------------
// Fichero: obtenerMediciones.js
// Autora: Mari Dapcheva
// ------------------------------------------------------------------
// Descripción:
//   Carga las mediciones con GET a ReglasREST.php y las muestra
//   en la tabla de webMari.html
// ------------------------------------------------------------------

document.addEventListener("DOMContentLoaded", () => {
    const tableBody = document.querySelector("#medicionesTable tbody");

    // Misma carpeta biometria en htdocs: web_cliente + servidor
    const apiURL = "../servidor/servidorREST/ReglasREST.php";

    fetch(apiURL)
        .then(response => response.json())
        .then(data => {
            if (data.success && data.mediciones && data.mediciones.length > 0) {
                data.mediciones.forEach(medida => {
                    const row = document.createElement("tr");
                    row.innerHTML = `
                        <td data-label="ID_medicion">${medida.id_medicion}</td>
                        <td data-label="ID_sensor">${medida.id_sensor}</td>
                        <td data-label="Nombre">${medida.nombre}</td>
                        <td data-label="UUID">${medida.uuid}</td>
                        <td data-label="RSSI">${medida.rssi}</td>
                        <td data-label="CO2">${medida.medicionCo2}</td>
                        <td data-label="Fecha">${medida.timestamp}</td>
                    `;
                    tableBody.appendChild(row);
                });
            } else {
                tableBody.innerHTML = `<tr><td colspan="7">No hay datos disponibles</td></tr>`;
            }
        })
        .catch(err => {
            console.error("Error al cargar los datos:", err);
            tableBody.innerHTML = `<tr><td colspan="7">Error al cargar los datos</td></tr>`;
        });
});
