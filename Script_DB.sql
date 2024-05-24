-- Crear la base de datos si no existe
CREATE DATABASE IF NOT EXISTS school_db;

-- Seleccionar la base de datos
USE school_db;

-- Crear la tabla de alumnos
CREATE TABLE IF NOT EXISTS alumnos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50),
    apellido VARCHAR(50),
    edad INT
);

-- Crear la tabla de cursos
CREATE TABLE IF NOT EXISTS cursos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_curso VARCHAR(50),
    profesor VARCHAR(50),
    descripcion TEXT
);

-- Crear la tabla intermedia para la relación entre alumnos y cursos
CREATE TABLE IF NOT EXISTS alumnos_cursos (
    alumno_id INT,
    curso_id INT,
    FOREIGN KEY (alumno_id) REFERENCES alumnos(id),
    FOREIGN KEY (curso_id) REFERENCES cursos(id)
);

-- Insertar datos de prueba en la tabla de alumnos
INSERT INTO alumnos (nombre, apellido, edad) VALUES 
('Juan', 'Perez', 20),
('Maria', 'Gomez', 22),
('Pedro', 'Martinez', 21);

-- Insertar datos de prueba en la tabla de cursos
INSERT INTO cursos (nombre_curso, profesor, descripcion) VALUES 
('Matemáticas', 'Prof. López', 'Curso de matemáticas básicas'),
('Historia', 'Prof. Sánchez', 'Curso de historia mundial'),
('Inglés', 'Prof. Smith', 'Curso de inglés avanzado');

-- Insertar relaciones entre alumnos y cursos
INSERT INTO alumnos_cursos (alumno_id, curso_id) VALUES 
(1, 1),
(1, 2),
(2, 1),
(2, 3),
(3, 2),
(3, 3);