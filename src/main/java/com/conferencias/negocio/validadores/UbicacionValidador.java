package com.conferencias.negocio.validadores;

import com.conferencias.datos.entidades.Ubicacion;

public class UbicacionValidador {
    public void validarUbicacion(Ubicacion ubicacion) throws Exception {
        if (ubicacion.getNombre() == null || ubicacion.getNombre().trim().isEmpty()) {
            throw new Exception("El nombre de la ubicación es obligatorio.");
        }
        if (ubicacion.getDireccion() == null || ubicacion.getDireccion().trim().isEmpty()) {
            throw new Exception("La dirección es obligatoria.");
        }
        if (ubicacion.getCapacidad() < 10) {
            throw new Exception("La capacidad debe ser al menos 10.");
        }
    }
}
