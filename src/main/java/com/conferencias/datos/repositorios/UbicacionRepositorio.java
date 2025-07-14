package com.conferencias.datos.repositorios;

import com.conferencias.datos.entidades.Ubicacion;
import com.conferencias.util.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UbicacionRepositorio {

    public void guardar(Ubicacion ubicacion) throws SQLException {
        String sql = "INSERT INTO Ubicacion (nombre, direccion, capacidad, tipo) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, ubicacion.getNombre());
            stmt.setString(2, ubicacion.getDireccion());
            stmt.setInt(3, ubicacion.getCapacidad());
            stmt.setString(4, ubicacion.getTipo());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    ubicacion.setId(rs.getInt(1));
                }
            }
        }
    }

    // Método para actualizar una ubicación
    public void actualizar(Ubicacion ubicacion) throws SQLException {
        String sql = "UPDATE Ubicacion SET nombre = ?, direccion = ?, capacidad = ?, tipo = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ubicacion.getNombre());
            stmt.setString(2, ubicacion.getDireccion());
            stmt.setInt(3, ubicacion.getCapacidad());
            stmt.setString(4, ubicacion.getTipo());
            stmt.setInt(5, ubicacion.getId());
            stmt.executeUpdate();
        }
    }

    // Método para eliminar una ubicación
    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM Ubicacion WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }


    public Ubicacion buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Ubicacion WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearUbicacion(rs);
                }
            }
        }
        return null;
    }

    public List<Ubicacion> listarTodos() throws SQLException {
        List<Ubicacion> ubicaciones = new ArrayList<>();
        String sql = "SELECT * FROM Ubicacion";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ubicaciones.add(mapearUbicacion(rs));
            }
        }
        return ubicaciones;
    }

    private Ubicacion mapearUbicacion(ResultSet rs) throws SQLException {
        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setId(rs.getInt("id"));
        ubicacion.setNombre(rs.getString("nombre"));
        ubicacion.setDireccion(rs.getString("direccion"));
        ubicacion.setCapacidad(rs.getInt("capacidad"));
        ubicacion.setTipo(rs.getString("tipo"));
        return ubicacion;
    }
}
