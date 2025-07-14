package com.conferencias.negocio.validadores;

import com.conferencias.datos.entidades.Patrocinador;

public class PatrocinadorValidador {
    public void validarPatrocinador(Patrocinador patrocinador) throws Exception {
        if (patrocinador.getNombre() == null || patrocinador.getNombre().trim().isEmpty()) {
            throw new Exception("El nombre del patrocinador es obligatorio.");
        }
        if (patrocinador.getAporteEconomico() <= 0) {
            throw new Exception("El aporte económico debe ser mayor a cero.");
        }
        // Aquí se puede agregar lógica para validar aporte mínimo en función del nivel.
        if (patrocinador.getNivel() == null || patrocinador.getNivel().trim().isEmpty()) {
            throw new Exception("El nivel del patrocinador es obligatorio.");
        }
        // Ejemplo de validación mínima por nivel:
        switch (patrocinador.getNivel().toUpperCase()) {
            case "ORO":
                if (patrocinador.getAporteEconomico() < 50000) {
                    throw new Exception("Para nivel ORO, el aporte mínimo es de 50,000.");
                }
                break;
            case "PLATA":
                if (patrocinador.getAporteEconomico() < 20000) {
                    throw new Exception("Para nivel PLATA, el aporte mínimo es de 20,000.");
                }
                break;
            case "BRONCE":
                if (patrocinador.getAporteEconomico() < 10000) {
                    throw new Exception("Para nivel BRONCE, el aporte mínimo es de 10,000.");
                }
                break;
            default:
                throw new Exception("Nivel de patrocinador inválido. Debe ser ORO, PLATA o BRONCE.");
        }
    }
}
