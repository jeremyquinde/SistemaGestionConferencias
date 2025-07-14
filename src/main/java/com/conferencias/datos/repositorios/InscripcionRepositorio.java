package com.conferencias.datos.repositorios;

import com.conferencias.datos.entidades.Inscripcion;
import com.conferencias.util.DatabaseConfig;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class InscripcionRepositorio {

    public void guardar(Inscripcion inscripcion) throws SQLException {
        String sql = "INSERT INTO Inscripcion (usuario_id, evento_id, fecha_inscripcion, estado) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, inscripcion.getUsuarioId());
            stmt.setInt(2, inscripcion.getEventoId());
            stmt.setTimestamp(3, Timestamp.valueOf(inscripcion.getFechaInscripcion()));
            stmt.setString(4, inscripcion.getEstado());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    inscripcion.setId(rs.getInt(1));
                }
            }
        }
    }

    // Método para actualizar una inscripción
    public void actualizar(Inscripcion inscripcion) throws SQLException {
        String sql = "UPDATE Inscripcion SET usuario_id = ?, evento_id = ?, fecha_inscripcion = ?, estado = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, inscripcion.getUsuarioId());
            stmt.setInt(2, inscripcion.getEventoId());
            stmt.setTimestamp(3, Timestamp.valueOf(inscripcion.getFechaInscripcion()));
            stmt.setString(4, inscripcion.getEstado());
            stmt.setInt(5, inscripcion.getId());
            stmt.executeUpdate();
        }
    }

    // Método para eliminar una inscripción
    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM Inscripcion WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }


    public Inscripcion buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Inscripcion WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearInscripcion(rs);
                }
            }
        }
        return null;
    }

    public List<Inscripcion> listarTodos() throws SQLException {
        List<Inscripcion> inscripciones = new ArrayList<>();
        String sql = "SELECT * FROM Inscripcion";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                inscripciones.add(mapearInscripcion(rs));
            }
        }
        return inscripciones;
    }

    private Inscripcion mapearInscripcion(ResultSet rs) throws SQLException {
        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setId(rs.getInt("id"));
        inscripcion.setUsuarioId(rs.getInt("usuario_id"));
        inscripcion.setEventoId(rs.getInt("evento_id"));
        inscripcion.setFechaInscripcion(rs.getTimestamp("fecha_inscripcion").toLocalDateTime());
        inscripcion.setEstado(rs.getString("estado"));
        return inscripcion;
    }
}
