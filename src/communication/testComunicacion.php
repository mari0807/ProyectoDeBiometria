<?php
// Nombre: testComunicacion.php
// Descripción: Test automático del POST, del GET y de un POST incompleto.
// Copyright: (c) 2026 Mari Rosenova Dapcheva
// Fecha: 16/10/2026
// Autor: Mari Rosenova Dapcheva
// Aportación: criterio de aceptación del API REST.
include __DIR__ . '/../business_logic/Logica.php';
include __DIR__ . '/ReglasREST.php';

$conn = new mysqli("localhost", "root", "", "datosMediciones");
if ($conn->connect_error) {
    echo "MySQL no está arrancado: " . $conn->connect_error . "\n";
    exit(1);
}

$laLogica = new Logica($conn);
$fallos = 0;

$respuestaPost = atenderPOST($laLogica, [
    "nombre" => "Mari-Biometria",
    "uuid" => "EPSG-GTI-PROY-3A",
    "major" => 2817,
    "minor" => 123
]);
if (empty($respuestaPost['success']) || empty($respuestaPost['id'])) {
    echo "Fallo POST guardarMedicion\n";
    $fallos++;
} else {
    echo "POST id=" . $respuestaPost['id'] . "\n";
}

$respuestaGet = atenderGET($laLogica);
if (empty($respuestaGet['success']) || (int) $respuestaGet['medicion']['minor'] !== 123) {
    echo "Fallo GET obtenerMedicion\n";
    $fallos++;
} else {
    echo "GET minor=" . $respuestaGet['medicion']['minor'] . "\n";
}

$incompleto = atenderPOST($laLogica, ["nombre" => "Mari-Biometria"]);
if (!empty($incompleto['success'])) {
    echo "Fallo POST sin datos\n";
    $fallos++;
} else {
    echo "POST sin datos ok\n";
}

if (!empty($respuestaPost['id'])) {
    $conn->query("DELETE FROM mediciones WHERE id_medicion = " . (int) $respuestaPost['id']);
}
$conn->close();

if ($fallos > 0) {
    echo "Pruebas de comunicación: $fallos fallos\n";
    exit(1);
}
echo "Pruebas de comunicación: ok\n";
exit(0);
?>
