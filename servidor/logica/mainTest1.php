<?php
// ------------------------------------------------------------------
// Fichero: mainTest1.php
// Autora: Mari Dapcheva
// ------------------------------------------------------------------
// Descripción:
//   1. Inserta una medición
//   2. Recupera mediciones
//   3. Muestra JSON
//   4. Limpia la medición de prueba
// ------------------------------------------------------------------

include '../servidorREST/conexion.php';
include 'Logica.php';

$laLogica = new Logica($conn);

echo "Test 1: Guardar medición --> \t";

try {
    $id = $laLogica->guardarMedicion(
        "Mari-Biometria",
        "EPSG-GTI-PROY-3A",
        -53,
        2817,
        123,
        0.0,
        0.0,
        123,
        1
    );
    echo "Medición insertada correctamente. ID: $id \n\n";
} catch (Exception $e) {
    echo "Error al insertar la medición: " . $e->getMessage() . "\n\n";
}

echo "Test 2: Recoger mediciones --> \t";
$mediciones = $laLogica->recogerMediciones(1);
if (count($mediciones) > 0) {
    echo "Se han recuperado " . count($mediciones) . " mediciones. \n\n";
} else {
    echo "No se han recuperado mediciones. \n\n";
}

echo "Test 3: Mostrar datos --> \n";
echo json_encode($mediciones, JSON_PRETTY_PRINT);

if (!empty($id)) {
    $conn->query("DELETE FROM mediciones WHERE id_medicion = $id");
    echo "\n\nMedición de prueba eliminada.";
}

$conn->close();
?>
