<?php
// ------------------------------------------------------------------
// Fichero: Logica.php
// Plantilla profe (Logica.js) + Sprint 0 Ferran Sansaloni
// ------------------------------------------------------------------
// Descripción:
//   Clase de lógica de negocio (no proxy).
//   Inserta y recupera mediciones en la BBDD.
// ------------------------------------------------------------------
class Logica {

    private $conn;

    // --------------------------------------------------------------
    // Constructor
    // --------------------------------------------------------------
    public function __construct($conn) {
        $this->conn = $conn;
    }

    // --------------------------------------------------------------
    // Método: guardarMedicion
    // Diseño:
    //   string: nombre, string: uuid, int: rssi, int: major, int: minor,
    //   float: latitud, float: longitud, int: co2, int: id_sensor
    //     --> guardarMedicion() -->
    //                                    int: insert_id <--
    // --------------------------------------------------------------
    public function guardarMedicion($nombre, $uuid, $rssi, $major, $minor, $latitud, $longitud, $co2, $id_sensor = 1) {
        $stmt = $this->conn->prepare("
            INSERT INTO mediciones (id_sensor, nombre, uuid, rssi, major, minor, medicionCo2, latitud, longitud, timestamp)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())
        ");
        $stmt->bind_param("issiiiddd", $id_sensor, $nombre, $uuid, $rssi, $major, $minor, $co2, $latitud, $longitud);
        $stmt->execute();

        if ($stmt->error) {
            throw new Exception("Error al guardar: " . $stmt->error);
        }
        return $stmt->insert_id;
    } // ()

    // --------------------------------------------------------------
    // Método: recogerMediciones
    // Diseño:
    //   int|null: id_sensor --> recogerMediciones()
    //                                     <-- [mediciones]
    // --------------------------------------------------------------
    public function recogerMediciones($id_sensor = null) {
        if ($id_sensor) {
            $stmt = $this->conn->prepare("SELECT * FROM mediciones WHERE id_sensor = ? ORDER BY timestamp ASC");
            $stmt->bind_param("i", $id_sensor);
        } else {
            $stmt = $this->conn->prepare("SELECT * FROM mediciones ORDER BY timestamp ASC");
        }

        $stmt->execute();
        $result = $stmt->get_result();
        return $result->fetch_all(MYSQLI_ASSOC);
    } // ()

} // class
?>
