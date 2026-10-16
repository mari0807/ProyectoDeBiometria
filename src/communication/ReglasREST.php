<?php
// Nombre: ReglasREST.php
// Descripción: API REST. El GET y el POST llaman a la lógica y no escriben SQL.
// Copyright: (c) 2026 Mari Rosenova Dapcheva
// Fecha: 16/10/2026
// Autor: Mari Rosenova Dapcheva
// Aportación: puerta HTTP entre el móvil, la web y la lógica.

// --------------------
// obtenerMedicion() --> medicion: Medicion
// Devuelve la última medición. En la web, el resultado llega por callback.
// --------------------
function atenderGET($laLogica) {
    return [
        "success" => true,
        "medicion" => $laLogica->obtenerMedicion()
    ];
}

// --------------------
// nombre: Text, uuid: Text, major: N, minor: N --> guardarMedicion() --> id: Z
// Guarda los cuatro campos y devuelve el id de la fila.
// --------------------
function atenderPOST($laLogica, $input) {
    if (!is_array($input) || !isset($input['uuid']) || !isset($input['minor'])) {
        return ["success" => false, "mensaje" => "Faltan datos"];
    }

    $id = $laLogica->guardarMedicion(
        isset($input['nombre']) ? $input['nombre'] : "Mari-Biometria",
        $input['uuid'],
        intval(isset($input['major']) ? $input['major'] : 0),
        intval($input['minor'])
    );

    return [
        "success" => true,
        "mensaje" => "Medida guardada correctamente",
        "id" => $id
    ];
}

if (realpath(__FILE__) !== realpath($_SERVER['SCRIPT_FILENAME'])) {
    return;
}

header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, POST');
header('Cache-Control: no-store, no-cache, must-revalidate, max-age=0');
header('Pragma: no-cache');
header('Expires: 0');

include __DIR__ . '/../database/conexion.php';
include __DIR__ . '/../business_logic/Logica.php';

$metodo = $_SERVER['REQUEST_METHOD'];
$laLogica = new Logica($conn);

try {

    if ($metodo === 'GET') {
        echo json_encode(atenderGET($laLogica), JSON_UNESCAPED_UNICODE);
    }

    else if ($metodo === 'POST') {
        $input = json_decode(file_get_contents("php://input"), true);
        echo json_encode(atenderPOST($laLogica, $input), JSON_UNESCAPED_UNICODE);
    }

    else {
        echo json_encode(["success" => false, "mensaje" => "Método no permitido"], JSON_UNESCAPED_UNICODE);
    }

} catch (Exception $e) {
    echo json_encode([
        "success" => false,
        "mensaje" => $e->getMessage()
    ], JSON_UNESCAPED_UNICODE);
}

$conn->close();
?>
