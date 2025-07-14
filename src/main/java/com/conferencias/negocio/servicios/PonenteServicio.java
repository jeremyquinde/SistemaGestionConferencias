package com.conferencias.negocio.servicios;

import com.conferencias.datos.entidades.Ponente;
import com.conferencias.datos.repositorios.PonenteRepositorio;
import com.conferencias.negocio.validadores.PonenteValidador;

import java.sql.SQLException;
import java.util.List;

public class PonenteServicio {
    private PonenteRepositorio ponenteRepositorio;
    private PonenteValidador ponenteValidador;

    public PonenteServicio() {
        this.ponenteRepositorio = new PonenteRepositorio();
        this.ponenteValidador = new PonenteValidador();
    }

    public void registrarPonente(Ponente ponente) throws SQLException, Exception {
        ponenteValidador.validarPonente(ponente);
        ponenteRepositorio.guardar(ponente);
    }

    public void actualizarPonente(Ponente ponente) throws SQLException, Exception {
        ponenteValidador.validarPonente(ponente);
        ponenteRepositorio.actualizar(ponente);
    }

    public void eliminarPonente(int id) throws SQLException {
        ponenteRepositorio.eliminar(id);
    }


    public Ponente obtenerPonentePorId(int id) throws SQLException {
        return ponenteRepositorio.buscarPorId(id);
    }

    public List<Ponente> listarPonentes() throws SQLException {
        return ponenteRepositorio.listarTodos();
    }
}
