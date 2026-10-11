-- ------------------------------------------------------------------
-- BBDD datosMediciones
-- Basado en el SQL de Ferran Sansaloni, adaptado a Mari
-- Importar en phpMyAdmin (XAMPP)
-- ------------------------------------------------------------------

CREATE DATABASE IF NOT EXISTS datosMediciones
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;

USE datosMediciones;

CREATE TABLE IF NOT EXISTS usuarios (
  id_usuario int(11) NOT NULL,
  nombre varchar(50) NOT NULL,
  apellidos varchar(50) NOT NULL,
  correo varchar(100) NOT NULL,
  telefono varchar(20) NOT NULL,
  PRIMARY KEY (id_usuario),
  UNIQUE KEY correo (correo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS sensores (
  id_sensor int(11) NOT NULL AUTO_INCREMENT,
  id_usuario int(11) NOT NULL,
  nombre_sensor varchar(100) NOT NULL,
  descripcion varchar(255) DEFAULT NULL,
  PRIMARY KEY (id_sensor),
  KEY sensores_ibfk_1 (id_usuario),
  CONSTRAINT sensores_ibfk_1 FOREIGN KEY (id_usuario)
    REFERENCES usuarios (id_usuario) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS mediciones (
  id_medicion int(11) NOT NULL AUTO_INCREMENT,
  id_sensor int(11) NOT NULL,
  nombre varchar(50) NOT NULL,
  uuid varchar(255) NOT NULL,
  rssi int(11) NOT NULL,
  major smallint(5) UNSIGNED NOT NULL,
  minor smallint(5) UNSIGNED NOT NULL,
  medicionCo2 smallint(6) NOT NULL,
  latitud decimal(9,6) NOT NULL,
  longitud decimal(9,6) NOT NULL,
  timestamp datetime DEFAULT current_timestamp(),
  PRIMARY KEY (id_medicion),
  KEY mediciones_ibfk_1 (id_sensor),
  CONSTRAINT mediciones_ibfk_1 FOREIGN KEY (id_sensor)
    REFERENCES sensores (id_sensor) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO usuarios (id_usuario, nombre, apellidos, correo, telefono) VALUES
(0, 'Mari', 'Ros', 'mari@example.com', '000000000')
ON DUPLICATE KEY UPDATE nombre=VALUES(nombre);

INSERT INTO sensores (id_sensor, id_usuario, nombre_sensor, descripcion) VALUES
(1, 0, 'Mari-Biometria', 'Beacon SparkFun nRF52840 de Mari')
ON DUPLICATE KEY UPDATE nombre_sensor=VALUES(nombre_sensor);
