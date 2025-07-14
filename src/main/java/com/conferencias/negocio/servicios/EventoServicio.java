package com.conferencias.negocio.servicios;

import com.conferencias.datos.entidades.Evento;
import com.conferencias.datos.repositorios.EventoRepositorio;
import com.conferencias.negocio.validadores.EventoValidador;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class EventoServicio {
    private EventoRepositorio eventoRepositorio;
    private EventoValidador validador;

    public EventoServicio() {
        this.eventoRepositorio = new EventoRepositorio();
        this.validador = new EventoValidador(eventoRepositorio);
    }

    public void crearEvento(
            String nombre,
            LocalDate inicio,
            LocalDate fin,
            String capacidadStr,
            String estado,
            String ubicacionIdStr
    ) throws Exception {
        // 1) Validación completa en el validador
        Evento ev = validador.validarYConstruirEvento(
                nombre, inicio, fin,
                capacidadStr, estado, ubicacionIdStr
        );
        // 2) Si no hubo excepción, persisto
        eventoRepositorio.guardar(ev);
    }

    public Evento obtenerEventoPorId(int id) throws SQLException {
        return eventoRepositorio.buscarPorId(id);
    }

    public List<Evento> listarEventos() throws SQLException {
        return eventoRepositorio.listarTodos();
    }

    // Metodo para actualizar un evento
    public void actualizarEvento(
            long id,
            String nombre,
            LocalDate inicio,
            LocalDate fin,
            String capacidadStr,
            String estado,
            String ubicacionIdStr
    ) throws Exception {
        // traigo la entidad para mantener id
        Evento ev = eventoRepositorio.buscarPorId((int) id);
        // validación y actualización de campos
        Evento evValidado = validador.validarYConstruirEvento(
                nombre, inicio, fin,
                capacidadStr, estado, ubicacionIdStr
        );
        // conservar ID en la instancia resultante
        evValidado.setId((int) id);
        eventoRepositorio.actualizar(evValidado);
    }

    // Metodo para eliminar un evento
    public void eliminarEvento(int id) throws SQLException {
        eventoRepositorio.eliminar(id);
    }

}
