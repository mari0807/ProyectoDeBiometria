<?php
// Nombre: Logica.php
// Descripción: Lógica de negocio. Guarda una medición y devuelve la última. Solo habla con MySQL.
// Copyright: (c) 2026 Mari Rosenova Dapcheva
// Fecha: 16/10/2026
// Autor: Mari Rosenova Dapcheva
// Aportación: clase independiente del API REST.
class Logica {

    private $conn;

    // --------------------
    // Logica() -->
    // Construye el objeto de lógica.
    // --------------------
    public function __construct($conn) {
        $this->conn = $conn;
    }

    // --------------------
    // nombre: Text, uuid: Text, major: N, minor: N --> guardarMedicion() --> id: Z
    // Guarda los cuatro campos y devuelve el id de la fila.
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
    // Lee la última fila de la tabla mediciones.
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
