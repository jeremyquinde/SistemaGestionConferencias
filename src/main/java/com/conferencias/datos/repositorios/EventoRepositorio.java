package com.conferencias.datos.repositorios;

import com.conferencias.datos.entidades.Evento;
import com.conferencias.util.DatabaseConfig;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventoRepositorio {

    public void guardar(Evento evento) throws SQLException {
        String sql = "INSERT INTO Evento (nombre, fecha_inicio, fecha_fin, capacidad, estado, ubicacion_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, evento.getNombre());
            stmt.setTimestamp(2, Timestamp.valueOf(evento.getFechaInicio()));
            stmt.setTimestamp(3, Timestamp.valueOf(evento.getFechaFin()));
            stmt.setInt(4, evento.getCapacidad());
            stmt.setString(5, evento.getEstado());
            stmt.setInt(6, evento.getUbicacionId());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    evento.setId(rs.getInt(1));
                }
            }
        }
    }

    public Evento buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Evento WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearEvento(rs);
                }
            }
        }
        return null;
    }

    public List<Evento> listarTodos() throws SQLException {
        List<Evento> eventos = new ArrayList<>();
        String sql = "SELECT * FROM Evento";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                eventos.add(mapearEvento(rs));
            }
        }
        return eventos;
    }

    private Evento mapearEvento(ResultSet rs) throws SQLException {
        Evento evento = new Evento();
        evento.setId(rs.getInt("id"));
        evento.setNombre(rs.getString("nombre"));
        evento.setFechaInicio(rs.getTimestamp("fecha_inicio").toLocalDateTime());
        evento.setFechaFin(rs.getTimestamp("fecha_fin").toLocalDateTime());
        evento.setCapacidad(rs.getInt("capacidad"));
        evento.setEstado(rs.getString("estado"));
        evento.setUbicacionId(rs.getInt("ubicacion_id"));
        return evento;
    }

    // Método para actualizar un evento
    public void actualizar(Evento evento) throws SQLException {
        String sql = "UPDATE Evento SET nombre = ?, fecha_inicio = ?, fecha_fin = ?, capacidad = ?, estado = ?, ubicacion_id = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, evento.getNombre());
            stmt.setTimestamp(2, Timestamp.valueOf(evento.getFechaInicio()));
            stmt.setTimestamp(3, Timestamp.valueOf(evento.getFechaFin()));
            stmt.setInt(4, evento.getCapacidad());
            stmt.setString(5, evento.getEstado());
            stmt.setInt(6, evento.getUbicacionId());
            stmt.setInt(7, evento.getId());
            stmt.executeUpdate();
        }
    }

    // Método para eliminar un evento
    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM Evento WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }



}
