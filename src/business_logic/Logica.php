<?php
class Logica {

    private $conn;

    // --------------------
    // Logica() -->
    // --------------------
    public function __construct($conn) {
        $this->conn = $conn;
    }

    // --------------------
    // nombre: Text, uuid: Text, major: N, minor: N --> guardarMedicion() --> id: Z
    // --------------------
    public function guardarMedicion($nombre, $uuid, $major, $minor) {
        $stmt = $this->conn->prepare("
            INSERT INTO mediciones (nombre, uuid, major, minor, timestamp)
            VALUES (?, ?, ?, ?, NOW())
        ");
        $stmt->bind_param("ssii", $nombre, $uuid, $major, $minor);
        $stmt->execute();

        if ($stmt->error) {
            throw new Exception("Error al guardar: " . $stmt->error);
        }
        return $stmt->insert_id;
    }

    // --------------------
    // obtenerMedicion() --> medicion: Medicion
    // --------------------
    public function obtenerMedicion() {
        $stmt = $this->conn->prepare("SELECT * FROM mediciones ORDER BY timestamp DESC, id_medicion DESC LIMIT 1");
        $stmt->execute();
        $result = $stmt->get_result();
        $fila = $result->fetch_assoc();
        return $fila ? $fila : null;
    }

}
?>
