package com.conferencias.datos.repositorios;

import com.conferencias.datos.entidades.Patrocinador;
import com.conferencias.util.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatrocinadorRepositorio {

    public void guardar(Patrocinador patrocinador) throws SQLException {
        String sql = "INSERT INTO Patrocinador (nombre, nivel, aporte_economico, evento_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, patrocinador.getNombre());
            stmt.setString(2, patrocinador.getNivel());
            stmt.setDouble(3, patrocinador.getAporteEconomico());
            stmt.setInt(4, patrocinador.getEventoId());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    patrocinador.setId(rs.getInt(1));
                }
            }
        }
    }

    // Método para actualizar un patrocinador
    public void actualizar(Patrocinador patrocinador) throws SQLException {
        String sql = "UPDATE Patrocinador SET nombre = ?, nivel = ?, aporte_economico = ?, evento_id = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, patrocinador.getNombre());
            stmt.setString(2, patrocinador.getNivel());
            stmt.setDouble(3, patrocinador.getAporteEconomico());
            stmt.setInt(4, patrocinador.getEventoId());
            stmt.setInt(5, patrocinador.getId());
            stmt.executeUpdate();
        }
    }

    // Método para eliminar un patrocinador
    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM Patrocinador WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }


    public Patrocinador buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Patrocinador WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearPatrocinador(rs);
                }
            }
        }
        return null;
    }

    public List<Patrocinador> listarTodos() throws SQLException {
        List<Patrocinador> patrocinadores = new ArrayList<>();
        String sql = "SELECT * FROM Patrocinador";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                patrocinadores.add(mapearPatrocinador(rs));
            }
        }
        return patrocinadores;
    }

    private Patrocinador mapearPatrocinador(ResultSet rs) throws SQLException {
        Patrocinador patrocinador = new Patrocinador();
        patrocinador.setId(rs.getInt("id"));
        patrocinador.setNombre(rs.getString("nombre"));
        patrocinador.setNivel(rs.getString("nivel"));
        patrocinador.setAporteEconomico(rs.getDouble("aporte_economico"));
        patrocinador.setEventoId(rs.getInt("evento_id"));
        return patrocinador;
    }
}
