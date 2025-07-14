package com.conferencias.negocio.servicios;

import com.conferencias.datos.entidades.Reporte;
import com.conferencias.datos.repositorios.ReporteRepositorio;
import com.conferencias.util.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ReporteServicio {
    private ReporteRepositorio reporteRepositorio;

    public ReporteServicio() {
        this.reporteRepositorio = new ReporteRepositorio();
    }

    public void generarReporte(Reporte reporte) throws SQLException {
        // Lógica para reporte de INGRESOS
        if ("INGRESOS".equalsIgnoreCase(reporte.getTipo())) {
            double totalIngresos = calcularTotalIngresos(1);
            reporte.setDatosBrutos("Total ingresos del evento 1: " + totalIngresos);
        } else if ("ASISTENCIA".equalsIgnoreCase(reporte.getTipo())) {
            long totalAsistencia = calcularAsistencia(1);
            reporte.setDatosBrutos("Total asistencia confirmada del evento 1: " + totalAsistencia);
        }
        reporteRepositorio.guardar(reporte);
    }

    public void actualizarReporte(Reporte reporte) throws SQLException {
        reporteRepositorio.actualizar(reporte);
    }

    public void eliminarReporte(int id) throws SQLException {
        reporteRepositorio.eliminar(id);
    }

    public Reporte obtenerReportePorId(int id) throws SQLException {
        return reporteRepositorio.buscarPorId(id);
    }

    public List<Reporte> listarReportes() throws SQLException {
        return reporteRepositorio.listarTodos();
    }

    private double calcularTotalIngresos(int eventoId) throws SQLException {
        double total = 0.0;
        String sql = "SELECT SUM(aporte_economico) AS total FROM Patrocinador WHERE evento_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, eventoId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    total = rs.getDouble("total");
                }
            }
        }
        return total;
    }

    private long calcularAsistencia(int eventoId) throws SQLException {
        long total = 0;
        String sql = "SELECT COUNT(*) AS total FROM Inscripcion WHERE evento_id = ? AND estado = 'CONFIRMADA'";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, eventoId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    total = rs.getLong("total");
                }
            }
        }
        return total;
    }
}
