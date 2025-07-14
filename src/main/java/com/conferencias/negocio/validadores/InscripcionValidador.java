package com.conferencias.negocio.validadores;

import com.conferencias.datos.entidades.Evento;
import com.conferencias.datos.entidades.Inscripcion;
import com.conferencias.datos.repositorios.EventoRepositorio;
import com.conferencias.datos.repositorios.InscripcionRepositorio;

import java.util.List;

public class InscripcionValidador {
    public void validarInscripcion(Inscripcion inscripcion) throws Exception {
        if (inscripcion.getUsuarioId() <= 0) {
            throw new Exception("El ID del usuario es inválido.");
        }
        if (inscripcion.getEventoId() <= 0) {
            throw new Exception("El ID del evento es inválido.");
        }
        // Validar inscripciones duplicadas
        if (existeInscripcionDuplicada(inscripcion.getUsuarioId(), inscripcion.getEventoId())) {
            throw new Exception("El usuario ya está inscrito en el evento.");
        }

        // Validar que haya cupo disponible en el evento
        if (!hayCupoDisponible(inscripcion.getEventoId())) {
            throw new Exception("No hay cupo disponible para este evento.");
        }
    }

    // Metodo auxiliar para verificar inscripciones duplicadas
    private boolean existeInscripcionDuplicada(int usuarioId, int eventoId) throws Exception {
        InscripcionRepositorio repo = new InscripcionRepositorio();
        List<Inscripcion> lista = repo.listarTodos();
        for (Inscripcion i : lista) {
            if (i.getUsuarioId() == usuarioId && i.getEventoId() == eventoId) {
                return true;
            }
        }
        return false;
    }

    // Metodo auxiliar para verificar el cupo disponible en un evento
    private boolean hayCupoDisponible(int eventoId) throws Exception {
        EventoRepositorio eventoRepo = new EventoRepositorio();
        InscripcionRepositorio insRepo = new InscripcionRepositorio();
        Evento evento = eventoRepo.buscarPorId(eventoId);
        if (evento == null) {
            throw new Exception("El evento no existe.");
        }
        List<Inscripcion> lista = insRepo.listarTodos();
        long count = lista.stream().filter(i -> i.getEventoId() == eventoId).count();
        return count < evento.getCapacidad();
    }
}
