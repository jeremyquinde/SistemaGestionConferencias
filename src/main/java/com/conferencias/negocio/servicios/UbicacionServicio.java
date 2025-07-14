package com.conferencias.negocio.servicios;

import com.conferencias.datos.entidades.Ubicacion;
import com.conferencias.datos.repositorios.UbicacionRepositorio;
import com.conferencias.negocio.validadores.UbicacionValidador;

import java.sql.SQLException;
import java.util.List;

public class UbicacionServicio {
    private UbicacionRepositorio ubicacionRepositorio;
    private UbicacionValidador ubicacionValidador;

    public UbicacionServicio() {
        this.ubicacionRepositorio = new UbicacionRepositorio();
        this.ubicacionValidador = new UbicacionValidador();
    }

    public void crearUbicacion(Ubicacion ubicacion) throws SQLException, Exception {
        // Validar datos antes de persistir
        ubicacionValidador.validarUbicacion(ubicacion);
        ubicacionRepositorio.guardar(ubicacion);
    }

    public void actualizarUbicacion(Ubicacion ubicacion) throws SQLException, Exception {
        // Aquí podrías agregar validaciones adicionales si es necesario.
        ubicacionRepositorio.actualizar(ubicacion);
    }

    public void eliminarUbicacion(int id) throws SQLException {
        ubicacionRepositorio.eliminar(id);
    }


    public Ubicacion obtenerUbicacionPorId(int id) throws SQLException {
        return ubicacionRepositorio.buscarPorId(id);
    }

    public List<Ubicacion> listarUbicaciones() throws SQLException {
        return ubicacionRepositorio.listarTodos();
    }
}
