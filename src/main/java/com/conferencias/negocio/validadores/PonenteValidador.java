package com.conferencias.negocio.validadores;

import com.conferencias.datos.entidades.Ponente;

import java.util.regex.Pattern;

public class PonenteValidador {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,}$");

    public void validarPonente(Ponente ponente) throws Exception {
        if (ponente.getNombre() == null || ponente.getNombre().trim().isEmpty()) {
            throw new Exception("El nombre del ponente es obligatorio.");
        }
        if (ponente.getEmail() == null || ponente.getEmail().trim().isEmpty()) {
            throw new Exception("El email del ponente es obligatorio.");
        }
        // Validación del formato del email
        if (!EMAIL_PATTERN.matcher(ponente.getEmail()).matches()) {
            throw new Exception("El email del ponente tiene un formato inválido.");
        }
    }
}
