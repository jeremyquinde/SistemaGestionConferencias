package com.conferencias.negocio.validadores;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.List;

import com.conferencias.datos.entidades.Evento;
import com.conferencias.datos.repositorios.EventoRepositorio;
import com.conferencias.negocio.validadores.EventoValidador;
import org.junit.jupiter.api.Test;

class EventoValidadorTest {

    private final EventoValidador validador = new EventoValidador(new DummyEventoRepositorio());

    // Clase dummy para el repositorio (no se usa en estas pruebas)
    final private static class DummyEventoRepositorio extends EventoRepositorio {
        public List<Evento> listarPorUbicacionYEstado(int ubicacionId, String estado) {
            return List.of();
        }
    }

    /* ================== PRUEBAS PARA validarFechas ================== */
    @Test
    void validarFechas_Correcto() {
        LocalDate hoy = LocalDate.now();
        LocalDate manana = hoy.plusDays(1);

        assertDoesNotThrow(() -> validador.validarFechas(hoy, manana));
    }

    @Test
    void validarFechas_Incorrecto_FechaNula() {

        // Caso: Ambas nulas
        assertThrows(ValidacionEventoException.class, () ->
                validador.validarFechas(null, null)
        );
    }

    @Test
    void validarFechas_Incorrecto_FinNoPosterior() {
        LocalDate hoy = LocalDate.now();
        LocalDate ayer = hoy.minusDays(1);

        // Caso: Fin anterior a inicio
        assertThrows(ValidacionEventoException.class, () ->
                validador.validarFechas(hoy, ayer)
        );
    }

    /* ================== PRUEBAS PARA validarEstado ================== */
    @Test
    void validarEstado_Correcto() {
        assertDoesNotThrow(() -> validador.validarEstado("PUBLICADO"));
        assertDoesNotThrow(() -> validador.validarEstado(" borrador "));
    }

    @Test
    void validarEstado_Incorrecto() {
        // Caso 1: Estado nulo
        assertThrows(ValidacionEventoException.class, () ->
                validador.validarEstado(null)
        );

        // Caso 2: Estado con espacios
        assertThrows(ValidacionEventoException.class, () ->
                validador.validarEstado("   ")
        );

        // Caso 3: Estado minúscula inválido
        assertThrows(ValidacionEventoException.class, () ->
                validador.validarEstado("pendiente")
        );
    }

    /* ================== PRUEBAS PARA validarCapacidad ================== */
    @Test
    void validarCapacidad_Correcto() {
        assertDoesNotThrow(() -> validador.validarCapacidad(1));
        assertDoesNotThrow(() -> validador.validarCapacidad(250));
    }

    @Test
    void validarCapacidad_Incorrecto() {
        // Caso 1: Menor al mínimo
        assertThrows(ValidacionEventoException.class, () ->
                validador.validarCapacidad(0)
        );

        // Caso 2: Mayor al máximo
        assertThrows(ValidacionEventoException.class, () ->
                validador.validarCapacidad(1000)
        );
    }
}