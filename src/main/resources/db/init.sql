CREATE DATABASE IF NOT EXISTS los_atuendos_db;
USE los_atuendos_db;

CREATE TABLE IF NOT EXISTS clientes (
  id VARCHAR(20) PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  direccion VARCHAR(255),
  telefono VARCHAR(20),
  mail VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS empleados (
  id VARCHAR(20) PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  direccion VARCHAR(255),
  telefono VARCHAR(20),
  cargo VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS prendas (
  ref VARCHAR(20) PRIMARY KEY,
  tipo VARCHAR(50) NOT NULL,
  descripcion VARCHAR(255) NOT NULL,
  talla VARCHAR(10),
  valor_alquiler DOUBLE NOT NULL
);

CREATE TABLE IF NOT EXISTS alquileres (
  numero INT AUTO_INCREMENT PRIMARY KEY,
  fecha_solicitud DATETIME NOT NULL,
  fecha_alquiler DATE NOT NULL,
  cliente_id VARCHAR(20),
  empleado_id VARCHAR(20),
  prenda_ref VARCHAR(20),
  FOREIGN KEY (cliente_id) REFERENCES clientes(id),
  FOREIGN KEY (empleado_id) REFERENCES empleados(id),
  FOREIGN KEY (prenda_ref) REFERENCES prendas(ref)
);

-- Insertamos datos solo si las tablas están vacías
INSERT INTO empleados (id, nombre, cargo) SELECT 'E01', 'Ana García', 'Vendedora' WHERE NOT EXISTS (SELECT 1 FROM empleados);
INSERT INTO clientes (id, nombre, mail) SELECT '123', 'Carlos Perez', 'carlos@mail.com' WHERE NOT EXISTS (SELECT 1 FROM clientes);
INSERT INTO prendas (ref, tipo, descripcion, talla, valor_alquiler) SELECT 'V01', 'vestido', 'Vestido de Dama (Ref: V01, Talla: M)', 'M', 150000 WHERE NOT EXISTS (SELECT 1 FROM prendas);
INSERT INTO prendas (ref, tipo, descripcion, talla, valor_alquiler) SELECT 'T01', 'traje', 'Traje de Caballero (Ref: T01, Talla: L, Aderezo: Corbata)', 'L', 200000 WHERE NOT EXISTS (SELECT 1 FROM prendas);
INSERT INTO prendas (ref, tipo, descripcion, talla, valor_alquiler) SELECT 'D01', 'disfraz', 'Disfraz: Superhéroe (Ref: D01, Talla: S)', 'S', 100000 WHERE NOT EXISTS (SELECT 1 FROM prendas);
