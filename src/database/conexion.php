<?php
// Nombre: conexion.php
// Descripción: Abre la conexión mysqli a la base datosMediciones y la deja lista para la lógica.
// Copyright: (c) 2026 Mari Rosenova Dapcheva
// Fecha: 16/10/2026
// Autor: Mari Rosenova Dapcheva
// Aportación: conexión que usa Logica. No guarda ni lee filas.
header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');

$servername = "localhost";
$username = "root";
$password = "";
$dbname = "datosMediciones";

$conn = new mysqli($servername, $username, $password, $dbname);

if ($conn->connect_error) {
    die(json_encode(["error" => "Conexión fallida: " . $conn->connect_error], JSON_UNESCAPED_UNICODE));
}

?>
