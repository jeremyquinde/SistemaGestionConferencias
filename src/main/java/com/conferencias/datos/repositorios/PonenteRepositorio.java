package com.conferencias.datos.repositorios;

import com.conferencias.datos.entidades.Ponente;
import com.conferencias.util.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PonenteRepositorio {

    public void guardar(Ponente ponente) throws SQLException {
        String sql = "INSERT INTO Ponente (nombre, especialidad, email, tarifa_hora, disponibilidad) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, ponente.getNombre());
            stmt.setString(2, ponente.getEspecialidad());
            stmt.setString(3, ponente.getEmail());
            stmt.setDouble(4, ponente.getTarifaHora());
            stmt.setBoolean(5, ponente.isDisponibilidad());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    ponente.setId(rs.getInt(1));
                }
            }
        }
    }

    // Método para actualizar un ponente
    public void actualizar(Ponente ponente) throws SQLException {
        String sql = "UPDATE Ponente SET nombre = ?, especialidad = ?, email = ?, tarifa_hora = ?, disponibilidad = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ponente.getNombre());
            stmt.setString(2, ponente.getEspecialidad());
            stmt.setString(3, ponente.getEmail());
            stmt.setDouble(4, ponente.getTarifaHora());
            stmt.setBoolean(5, ponente.isDisponibilidad());
            stmt.setInt(6, ponente.getId());
            stmt.executeUpdate();
        }
    }

    // Método para eliminar un ponente
    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM Ponente WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }


    public Ponente buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Ponente WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearPonente(rs);
                }
            }
        }
        return null;
    }

    public List<Ponente> listarTodos() throws SQLException {
        List<Ponente> ponentes = new ArrayList<>();
        String sql = "SELECT * FROM Ponente";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ponentes.add(mapearPonente(rs));
            }
        }
        return ponentes;
    }

    private Ponente mapearPonente(ResultSet rs) throws SQLException {
        Ponente ponente = new Ponente();
        ponente.setId(rs.getInt("id"));
        ponente.setNombre(rs.getString("nombre"));
        ponente.setEspecialidad(rs.getString("especialidad"));
        ponente.setEmail(rs.getString("email"));
        ponente.setTarifaHora(rs.getDouble("tarifa_hora"));
        ponente.setDisponibilidad(rs.getBoolean("disponibilidad"));
        return ponente;
    }
}
