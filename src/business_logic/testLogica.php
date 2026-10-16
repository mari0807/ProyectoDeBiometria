<?php
// Nombre: testLogica.php
// Descripción: Test automático de guardarMedicion y obtenerMedicion con la medida ficticia 123.
// Copyright: (c) 2026 Mari Rosenova Dapcheva
// Fecha: 16/10/2026
// Autor: Mari Rosenova Dapcheva
// Aportación: criterio de aceptación de la lógica de negocio.
include __DIR__ . '/Logica.php';

$conn = new mysqli("localhost", "root", "", "datosMediciones");
if ($conn->connect_error) {
    echo "MySQL no está arrancado: " . $conn->connect_error . "\n";
    exit(1);
}

$laLogica = new Logica($conn);
$fallos = 0;

$id = $laLogica->guardarMedicion("Mari-Biometria", "EPSG-GTI-PROY-3A", 2817, 123);
if (!$id) {
    echo "Fallo guardarMedicion\n";
    $fallos++;
} else {
    echo "guardarMedicion id=$id\n";
}

$ultima = $laLogica->obtenerMedicion();
if (!$ultima || (int) $ultima['id_medicion'] !== (int) $id || (int) $ultima['minor'] !== 123) {
    echo "Fallo obtenerMedicion\n";
    $fallos++;
} else {
    echo "obtenerMedicion minor=" . $ultima['minor'] . "\n";
}

$conn->query("DELETE FROM mediciones WHERE id_medicion = " . (int) $id);
$conn->close();

if ($fallos > 0) {
    echo "Pruebas de lógica: $fallos fallos\n";
    exit(1);
}
echo "Pruebas de lógica: ok\n";
exit(0);
?>
