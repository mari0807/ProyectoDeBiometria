<?php
// ------------------------------------------------------------------
// Fichero: ReglasREST.php
// Plantilla profe (ReglasREST.js) adaptada a PHP + Apache
// ------------------------------------------------------------------
// Descripción:
//   Reglas REST:
//     GET  --> recoger mediciones (JSON)
//     POST --> guardar una medición (JSON desde Android)
// ------------------------------------------------------------------

header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, POST');

include 'conexion.php';
include '../logica/Logica.php';

$metodo = $_SERVER['REQUEST_METHOD'];
$laLogica = new Logica($conn);

try {

    // .......................................................
    // GET --> recogerMediciones()
    // .......................................................
    if ($metodo === 'GET') {
        $id_sensor = isset($_GET['id_sensor']) ? intval($_GET['id_sensor']) : null;
        $mediciones = $laLogica->recogerMediciones($id_sensor);

        echo json_encode([
            "success" => true,
            "mediciones" => $mediciones
        ]);
    }

    // .......................................................
    // POST --> guardarMedicion()
    // .......................................................
    else if ($metodo === 'POST') {
        $input = json_decode(file_get_contents("php://input"), true);

        if (!isset($input['uuid']) || !isset($input['rssi']) || !isset($input['medicionCo2'])) {
            echo json_encode(["success" => false, "mensaje" => "Faltan datos"]);
            exit;
        }

        $id = $laLogica->guardarMedicion(
            isset($input['nombre']) ? $input['nombre'] : "Mari-Biometria",
            $input['uuid'],
            intval($input['rssi']),
            intval(isset($input['major']) ? $input['major'] : 0),
            intval(isset($input['minor']) ? $input['minor'] : 0),
            floatval(isset($input['latitud']) ? $input['latitud'] : 0.0),
            floatval(isset($input['longitud']) ? $input['longitud'] : 0.0),
            intval($input['medicionCo2']),
            intval(isset($input['id_sensor']) ? $input['id_sensor'] : 1)
        );

        echo json_encode([
            "success" => true,
            "mensaje" => "Medida guardada correctamente",
            "id" => $id
        ]);
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
