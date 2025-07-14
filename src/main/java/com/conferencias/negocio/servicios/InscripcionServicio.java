package com.conferencias.negocio.servicios;

import com.conferencias.datos.entidades.Inscripcion;
import com.conferencias.datos.repositorios.InscripcionRepositorio;
import com.conferencias.negocio.validadores.InscripcionValidador;

import java.sql.SQLException;
import java.util.List;

public class InscripcionServicio {
    private InscripcionRepositorio inscripcionRepositorio;
    private InscripcionValidador inscripcionValidador;

    public InscripcionServicio() {
        this.inscripcionRepositorio = new InscripcionRepositorio();
        this.inscripcionValidador = new InscripcionValidador();
    }

    public void actualizarInscripcion(Inscripcion inscripcion) throws SQLException, Exception {
        // Opcionalmente, agregar validaciones específicas de inscripciones.
        inscripcionRepositorio.actualizar(inscripcion);
    }

    public void eliminarInscripcion(int id) throws SQLException {
        inscripcionRepositorio.eliminar(id);
    }


    public void inscribir(Inscripcion inscripcion) throws SQLException, Exception {
        // Validar cupo, duplicados u otras reglas de negocio
        inscripcionValidador.validarInscripcion(inscripcion);
        inscripcionRepositorio.guardar(inscripcion);
    }

    public Inscripcion obtenerInscripcionPorId(int id) throws SQLException {
        return inscripcionRepositorio.buscarPorId(id);
    }

    public List<Inscripcion> listarInscripciones() throws SQLException {
        return inscripcionRepositorio.listarTodos();
    }
}
