package com.conferencias.datos.repositorios;

import com.conferencias.datos.entidades.Reporte;
import com.conferencias.util.DatabaseConfig;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReporteRepositorio {

    public void guardar(Reporte reporte) throws SQLException {
        String sql = "INSERT INTO Reporte (tipo, fecha_generacion, datos_brutos, usuario_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, reporte.getTipo());
            stmt.setTimestamp(2, Timestamp.valueOf(reporte.getFechaGeneracion()));
            stmt.setString(3, reporte.getDatosBrutos());
            stmt.setInt(4, reporte.getUsuarioId());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    reporte.setId(rs.getInt(1));
                }
            }
        }
    }

    // Método para actualizar un reporte (si es necesario)
    public void actualizar(Reporte reporte) throws SQLException {
        String sql = "UPDATE Reporte SET tipo = ?, fecha_generacion = ?, datos_brutos = ?, usuario_id = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, reporte.getTipo());
            stmt.setTimestamp(2, Timestamp.valueOf(reporte.getFechaGeneracion()));
            stmt.setString(3, reporte.getDatosBrutos());
            stmt.setInt(4, reporte.getUsuarioId());
            stmt.setInt(5, reporte.getId());
            stmt.executeUpdate();
        }
    }

    // Método para eliminar un reporte
    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM Reporte WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public Reporte buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Reporte WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearReporte(rs);
                }
            }
        }
        return null;
    }

    public List<Reporte> listarTodos() throws SQLException {
        List<Reporte> reportes = new ArrayList<>();
        String sql = "SELECT * FROM Reporte";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                reportes.add(mapearReporte(rs));
            }
        }
        return reportes;
    }

    private Reporte mapearReporte(ResultSet rs) throws SQLException {
        Reporte reporte = new Reporte();
        reporte.setId(rs.getInt("id"));
        reporte.setTipo(rs.getString("tipo"));
        reporte.setFechaGeneracion(rs.getTimestamp("fecha_generacion").toLocalDateTime());
        reporte.setDatosBrutos(rs.getString("datos_brutos"));
        reporte.setUsuarioId(rs.getInt("usuario_id"));
        return reporte;
    }
}
