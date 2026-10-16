-- Medicion = ( id_medicion: Z, nombre: Text, uuid: Text, major: N, minor: N, timestamp: Text )

CREATE DATABASE IF NOT EXISTS datosMediciones
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;

USE datosMediciones;

CREATE TABLE IF NOT EXISTS mediciones (
  id_medicion int(11) NOT NULL AUTO_INCREMENT,
  nombre varchar(50) NOT NULL,
  uuid varchar(255) NOT NULL,
  major int(5) UNSIGNED NOT NULL,
  minor int(5) UNSIGNED NOT NULL,
  timestamp datetime DEFAULT current_timestamp(),
  PRIMARY KEY (id_medicion)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
