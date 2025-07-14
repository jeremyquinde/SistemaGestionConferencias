package com.conferencias.negocio.validadores;

import com.conferencias.datos.entidades.Evento;
import com.conferencias.datos.repositorios.EventoRepositorio;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class EventoValidador {

    /**
     * Constante final para instancia de evento repositorio
     */
    private final EventoRepositorio eventoRepositorio;

    /**
     * @param eventoRepositorio recibe un objeto de tipo evento repositorio
     */
    public EventoValidador(final EventoRepositorio eventoRepositorio) {
        this.eventoRepositorio = eventoRepositorio;
    }

    /**
     * @param nombre nombre del evento
     * @param inicio fecha de inicio del evento
     * @param fin fecha de fin del evento
     * @param capacidadStr capacidad del evento del evento
     * @param estado estado del evento
     * @param ubicacionIdStr ubicacion del evento
     * @return devuelve un objeto tipo evento
     * @throws ValidacionEventoException lanza excepcion
     */
    public Evento validarYConstruirEvento(
            final String nombre,
            final LocalDate inicio,
            final LocalDate fin,
            final String capacidadStr,
            final String estado,
            final String ubicacionIdStr
    ) throws ValidacionEventoException {

        // Validaciones básicas de campos
        validarCampoObligatorio(nombre, "nombre del evento");
        validarFechas(inicio, fin);

        // Construcción temprana para validaciones de negocio
        Evento ev = construirEvento(
                nombre.trim(),
                inicio,
                fin,
                capacidadStr,
                estado,
                ubicacionIdStr
        );

        // Validaciones de negocio
        validarCapacidad(ev.getCapacidad());
        validarSolapamiento(ev);

        return ev;
    }

    /**
     * @param nombre nombre del evento
     * @param inicio fecha de inicio del evento
     * @param fin fecha de fin del evento
     * @param capacidadStr capacidad del evento del evento
     * @param estado estado del evento
     * @param ubicacionIdStr ubicacion del evento
     * @return devuelve un objeto tipo evento
     * @throws ValidacionEventoException lanza excepcion
     */
    private Evento construirEvento(
            final String nombre,
            final LocalDate inicio,
            final LocalDate fin,
            final String capacidadStr,
            final String estado,
            final String ubicacionIdStr
    ) throws ValidacionEventoException {

        Evento ev = new Evento();
        ev.setNombre(nombre);
        ev.setFechaInicio(inicio.atStartOfDay());
        ev.setFechaFin(fin.atStartOfDay());

        try {
            ev.setCapacidad(parsearEntero(capacidadStr, "capacidad"));
        } catch (NumberFormatException e) {
            throw new ValidacionEventoException(
                    "La capacidad debe ser un número entero válido");
        }

        validarEstado(estado);
        ev.setEstado(estado.trim().toUpperCase());

        try {
            ev.setUbicacionId(parsearEntero(ubicacionIdStr, "ID de ubicación"));
        } catch (NumberFormatException e) {
            throw new ValidacionEventoException(
                    "El ID de ubicación debe ser un número entero válido");
        }

        return ev;
    }

    /**
     * @param valor entrada a validar
     * @param nombreCampo nombre del campo a validar
     * @throws ValidacionEventoException lanza excepcion si la entrada es nula
     */
    private void validarCampoObligatorio(final String valor, final String nombreCampo)
            throws ValidacionEventoException {

                if (valor == null || valor.trim().isEmpty()) {
                    throw new ValidacionEventoException("El campo " + nombreCampo + " es obligatorio");
                }

    }

    /**
     * @param inicio fecha de inciio del evento
     * @param fin fecha de fin del evento
     * @throws ValidacionEventoException lanza excepcion si la entrada es nula o fecha de fin no es posterior
     */
        public void validarFechas(final LocalDate inicio, final LocalDate fin)
                throws ValidacionEventoException {

            if (inicio == null || fin == null) {
                throw new ValidacionEventoException("Las fechas de inicio y fin son obligatorias");
            }

            if (!fin.isAfter(inicio)) {
                throw new ValidacionEventoException("La fecha de fin debe ser posterior a la fecha de inicio");
            }
        }

    /**
     * @param valor entrada numerica a parsear como tipo entero
     * @param nombreCampo nombre del campo entre capacidad o idubicacion
     * @return retorna la entrada como tipo entero
     * @throws NumberFormatException lanza excepcion si la entrada es vacia
     */
    private int parsearEntero(String valor, String nombreCampo)
            throws NumberFormatException {

        if (valor == null || valor.trim().isEmpty()) {
            throw new NumberFormatException(nombreCampo + " no puede estar vacío");
        }
        return Integer.parseInt(valor.trim());
    }

    /**
     * @param estado estado del evento entre PUBLICADO o BORRADOR
     * @throws ValidacionEventoException lanza excepcion si el estado es invalido o nulo
     */
    public void validarEstado(String estado) throws ValidacionEventoException {

        validarCampoObligatorio(estado, "estado del evento");

        String estadoNormalizado = estado.trim().toUpperCase();

        if (!"PUBLICADO".equals(estadoNormalizado) && !"BORRADOR".equals(estadoNormalizado)) {
            throw new ValidacionEventoException("Estado inválido. Valores permitidos: PUBLICADO o BORRADOR");
        }
    }

    /**
     * @param capacidad caapacidad del evento
     * @throws ValidacionEventoException lanza excepcion si la capacidad no se encuentre entre el rango permitiro
     */
    public void validarCapacidad(final int capacidad)
            throws ValidacionEventoException {

        if (capacidad < 1) {
            throw new ValidacionEventoException("La capacidad mínima debe ser 1 persona");
        }

        if (capacidad > 250) {
            throw new ValidacionEventoException("La capacidad máxima permitida es 250 personas");
        }
    }

    /**
     * @param nuevoEvento objeto de tipo evento
     * @throws ValidacionEventoException lanza excepcion si el evento a crear tiene fecha solapada con uno existente
     */
    private void validarSolapamiento(final Evento nuevoEvento)
            throws ValidacionEventoException {

        try {
            List<Evento> eventosExistentes = Collections.singletonList(
                    eventoRepositorio.buscarPorId(nuevoEvento.getUbicacionId()));

            for (Evento existente : eventosExistentes) {

                if (haySolapamiento(nuevoEvento, existente)) {
                    throw new ValidacionEventoException("Conflicto con evento existente: " + existente.getNombre()
                            + " (ID: " + existente.getId() + "). Fechas solapadas"
                    );
                }

            }
        } catch (Exception e) {
            throw new ValidacionEventoException("Error verificando disponibilidad: " + e.getMessage());
        }
    }

    /**
     * @param nuevo objeto de tipo evento
     * @param existente objeto de tipo evento
     * @return retorna verdadero o falso
     */
    private boolean haySolapamiento(final Evento nuevo, final Evento existente) {
        return !nuevo.getFechaFin().isBefore(existente.getFechaInicio())
                && !nuevo.getFechaInicio().isAfter(existente.getFechaFin());
    }
}

/**
 * Excepción personalizada para validaciones
 */
class ValidacionEventoException extends Exception {
    ValidacionEventoException(final String message) {
        super(message);
    }
}
