package com.conferencias.negocio.servicios;

import com.conferencias.datos.entidades.Patrocinador;
import com.conferencias.datos.repositorios.PatrocinadorRepositorio;
import com.conferencias.negocio.validadores.PatrocinadorValidador;

import java.sql.SQLException;
import java.util.List;

public class PatrocinadorServicio {
    private PatrocinadorRepositorio patrocinadorRepositorio;
    private PatrocinadorValidador patrocinadorValidador;

    public PatrocinadorServicio() {
        this.patrocinadorRepositorio = new PatrocinadorRepositorio();
        this.patrocinadorValidador = new PatrocinadorValidador();
    }

    public void actualizarPatrocinador(Patrocinador patrocinador) throws SQLException, Exception {
        // Se pueden agregar validaciones adicionales si es necesario.
        patrocinadorValidador.validarPatrocinador(patrocinador);
        patrocinadorRepositorio.actualizar(patrocinador);
    }

    public void eliminarPatrocinador(int id) throws SQLException {
        patrocinadorRepositorio.eliminar(id);
    }


    public void registrarPatrocinador(Patrocinador patrocinador) throws SQLException, Exception {
        patrocinadorValidador.validarPatrocinador(patrocinador);
        patrocinadorRepositorio.guardar(patrocinador);
    }

    public Patrocinador obtenerPatrocinadorPorId(int id) throws SQLException {
        return patrocinadorRepositorio.buscarPorId(id);
    }

    public List<Patrocinador> listarPatrocinadores() throws SQLException {
        return patrocinadorRepositorio.listarTodos();
    }
}
