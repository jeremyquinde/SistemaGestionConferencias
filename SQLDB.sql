CREATE DATABASE ConferenciasDB;
go

USE ConferenciasDB;

-- Tabla de Usuarios
CREATE TABLE Usuario (
    id INT IDENTITY(1,1) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    contrasena_hash VARCHAR(255) NOT NULL,
    rol VARCHAR(20) NOT NULL CHECK (rol IN ('ADMIN', 'ORGANIZADOR', 'ASISTENTE')),
    fecha_registro DATE NOT NULL DEFAULT GETDATE()
);

-- Tabla de Ubicaciones
CREATE TABLE Ubicacion (
    id INT IDENTITY(1,1) PRIMARY KEY,
    nombre VARCHAR(100) UNIQUE NOT NULL,
    direccion VARCHAR(255) NOT NULL,
    capacidad INT NOT NULL CHECK (capacidad >= 10),
    tipo VARCHAR(20) NOT NULL CHECK (tipo IN ('FISICA', 'VIRTUAL'))
);

-- Tabla de Eventos
CREATE TABLE Evento (
    id INT IDENTITY(1,1) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    fecha_inicio DATETIME NOT NULL,
    fecha_fin DATETIME NOT NULL,
    capacidad INT NOT NULL,
    estado VARCHAR(20) NOT NULL CHECK (estado IN ('BORRADOR', 'PUBLICADO')),
    ubicacion_id INT NOT NULL,
    FOREIGN KEY (ubicacion_id) REFERENCES Ubicacion(id),
    CHECK (fecha_fin > fecha_inicio)
);

-- Tabla de Ponentes
CREATE TABLE Ponente (
    id INT IDENTITY(1,1) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    especialidad VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    tarifa_hora DECIMAL(10, 2) NOT NULL,
    disponibilidad BIT NOT NULL DEFAULT 1
);

-- Tabla de relación Evento-Ponente
CREATE TABLE EventoPonente (
    id INT IDENTITY(1,1) PRIMARY KEY,
    evento_id INT NOT NULL,
    ponente_id INT NOT NULL,
    fecha_asignacion DATETIME NOT NULL DEFAULT GETDATE(),
    FOREIGN KEY (evento_id) REFERENCES Evento(id),
    FOREIGN KEY (ponente_id) REFERENCES Ponente(id),
    CONSTRAINT UQ_EventoPonente UNIQUE (evento_id, ponente_id)
);

-- Tabla de Inscripciones
CREATE TABLE Inscripcion (
    id INT IDENTITY(1,1) PRIMARY KEY,
    usuario_id INT NOT NULL,
    evento_id INT NOT NULL,
    fecha_inscripcion DATETIME NOT NULL DEFAULT GETDATE(),
    estado VARCHAR(20) NOT NULL CHECK (estado IN ('CONFIRMADA', 'PENDIENTE')),
    FOREIGN KEY (usuario_id) REFERENCES Usuario(id),
    FOREIGN KEY (evento_id) REFERENCES Evento(id),
    CONSTRAINT UQ_Inscripcion UNIQUE (usuario_id, evento_id)
);

-- Tabla de Patrocinadores
CREATE TABLE Patrocinador (
    id INT IDENTITY(1,1) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    nivel VARCHAR(20) NOT NULL CHECK (nivel IN ('ORO', 'PLATA', 'BRONCE')),
    aporte_economico DECIMAL(12, 2) NOT NULL,
    evento_id INT NOT NULL,
    FOREIGN KEY (evento_id) REFERENCES Evento(id),
    CONSTRAINT UQ_PatrocinadorEvento UNIQUE (nombre, evento_id)
);

-- Tabla de Reportes
CREATE TABLE Reporte (
    id INT IDENTITY(1,1) PRIMARY KEY,
    tipo VARCHAR(20) NOT NULL CHECK (tipo IN ('ASISTENCIA', 'INGRESOS')),
    fecha_generacion DATETIME NOT NULL DEFAULT GETDATE(),
    datos_brutos NVARCHAR(MAX) NULL,
    usuario_id INT NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES Usuario(id)
);

-- Índices para mejorar el rendimiento
CREATE INDEX IDX_Evento_Estado ON Evento(estado);
CREATE INDEX IDX_Inscripcion_Evento ON Inscripcion(evento_id);
CREATE INDEX IDX_Usuario_Rol ON Usuario(rol);
CREATE INDEX IDX_Patrocinador_Nivel ON Patrocinador(nivel);
CREATE INDEX IDX_Ponente_Disponibilidad ON Ponente(disponibilidad);

GO
USE ConferenciasDB;

-- Insertar Usuarios de prueba
INSERT INTO Usuario (nombre, email, contrasena_hash, rol, fecha_registro)
VALUES 
('Admin Principal', 'admin@conferencias.com', 'hash_password_admin', 'ADMIN', GETDATE()),
('Organizador Test', 'organizador@conferencias.com', 'hash_password_org', 'ORGANIZADOR', GETDATE()),
('Asistente Uno', 'asistente1@mail.com', 'hash_password_asist', 'ASISTENTE', GETDATE()),
('Asistente Dos', 'asistente2@mail.com', 'hash_password_asist', 'ASISTENTE', GETDATE());

-- Insertar Ubicaciones de prueba
INSERT INTO Ubicacion (nombre, direccion, capacidad, tipo)
VALUES 
('Sala Principal', 'Calle Central #123', 200, 'FISICA'),
('Sala Virtual 1', 'https://conferencias.com/sala1', 500, 'VIRTUAL'),
('Auditorio Norte', 'Av. Norte #456', 150, 'FISICA'),
('Sala de Reuniones A', 'Edificio Central, Piso 3', 30, 'FISICA');

-- Insertar Ponentes de prueba
INSERT INTO Ponente (nombre, especialidad, email, tarifa_hora, disponibilidad)
VALUES 
('María Rodríguez', 'Inteligencia Artificial', 'maria@expertos.com', 150.00, 1),
('Carlos Martínez', 'Blockchain', 'carlos@expertos.com', 120.00, 1),
('Laura González', 'Desarrollo Web', 'laura@expertos.com', 100.00, 0);

GO
USE ConferenciasDB;
-- Insertar Eventos de prueba
INSERT INTO Evento (nombre, fecha_inicio, fecha_fin, capacidad, estado, ubicacion_id)
VALUES 
('Conferencia Anual de Tecnología', DATEADD(day, 30, GETDATE()), DATEADD(day, 32, GETDATE()), 200, 'PUBLICADO', 1),
('Webinar: Innovación Digital', DATEADD(day, 15, GETDATE()), DATEADD(day, 16, GETDATE()), 300, 'PUBLICADO', 2),
('Taller de Programación', DATEADD(day, 45, GETDATE()), DATEADD(day, 46, GETDATE()), 30, 'BORRADOR', 4);

GO
-- Asignar Ponentes a Eventos
INSERT INTO EventoPonente (evento_id, ponente_id, fecha_asignacion)
VALUES 
(1, 1, GETDATE()),
(1, 2, GETDATE()),
(2, 3, GETDATE());
GO
-- Insertar Inscripciones de prueba
INSERT INTO Inscripcion (usuario_id, evento_id, fecha_inscripcion, estado)
VALUES 
(3, 1, GETDATE(), 'CONFIRMADA'),
(4, 1, GETDATE(), 'CONFIRMADA'),
(3, 2, GETDATE(), 'PENDIENTE');
GO
-- Insertar Patrocinadores de prueba
INSERT INTO Patrocinador (nombre, nivel, aporte_economico, evento_id)
VALUES 
('TechCorp', 'ORO', 75000.00, 1),
('InnovaSoft', 'PLATA', 30000.00, 1),
('DigitalFuture', 'BRONCE', 15000.00, 1),
('WebMasters', 'ORO', 50000.00, 2);

