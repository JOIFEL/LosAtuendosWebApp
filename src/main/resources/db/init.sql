-- Crea la base de datos solo si no existe
CREATE DATABASE IF NOT EXISTS los_atuendos_db;
-- Selecciona la base de datos para usarla
USE los_atuendos_db;

-- Crea las tablas solo si no existen
CREATE TABLE IF NOT EXISTS clientes (
  id VARCHAR(20) PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  direccion VARCHAR(255),
  telefono VARCHAR(20),
  mail VARCHAR(100) UNIQUE
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

-- Creación de la tabla de usuarios
CREATE TABLE IF NOT EXISTS usuarios (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(100) NOT NULL UNIQUE,
  password VARCHAR(100) NOT NULL,
  rol VARCHAR(20) NOT NULL,
  cliente_id VARCHAR(20),
  FOREIGN KEY (cliente_id) REFERENCES clientes(id) ON DELETE CASCADE
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

-- Datos de prueba solo si las tablas están vacías
INSERT INTO empleados (id, nombre, cargo) SELECT 'E01', 'Ana García', 'Vendedora' WHERE NOT EXISTS (SELECT 1 FROM empleados);
INSERT INTO clientes (id, nombre, mail) SELECT '123', 'Carlos Perez', 'carlos@mail.com' WHERE NOT EXISTS (SELECT 1 FROM clientes);
INSERT INTO prendas (ref, tipo, descripcion, talla, valor_alquiler) SELECT 'V01', 'vestido', 'Vestido de Dama (Ref: V01, Talla: M)', 'M', 150000 WHERE NOT EXISTS (SELECT 1 FROM prendas);

-- Usuarios de prueba
INSERT INTO usuarios (username, password, rol) SELECT 'admin', 'admin123', 'ADMIN' WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE username = 'admin');
INSERT INTO usuarios (username, password, rol, cliente_id) SELECT 'carlos@mail.com', '123', 'CLIENTE', '123' WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE username = 'carlos@mail.com');
