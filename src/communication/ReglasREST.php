<?php

// --------------------
// obtenerMedicion() --> medicion: Medicion
// --------------------
function atenderGET($laLogica) {
    return [
        "success" => true,
        "medicion" => $laLogica->obtenerMedicion()
    ];
}

// --------------------
// nombre: Text, uuid: Text, major: N, minor: N --> guardarMedicion() --> id: Z
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
        echo json_encode(atenderGET($laLogica));
    }

    else if ($metodo === 'POST') {
        $input = json_decode(file_get_contents("php://input"), true);
        echo json_encode(atenderPOST($laLogica, $input));
    }

    else {
        echo json_encode(["success" => false, "mensaje" => "Método no permitido"]);
    }

} catch (Exception $e) {
    echo json_encode([
        "success" => false,
        "mensaje" => $e->getMessage()
    ]);
}

$conn->close();
?>
