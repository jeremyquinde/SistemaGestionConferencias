package com.conferencias.negocio.validadores;

import com.conferencias.datos.entidades.Usuario;
import com.conferencias.datos.repositorios.UsuarioRepositorio;

public class UsuarioValidador {

    public void validarNuevoUsuario(Usuario usuario) throws Exception {
        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            throw new Exception("El nombre es obligatorio.");
        }
        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new Exception("El email es obligatorio.");
        }
        // Validación simple para el hash (en un caso real se usaría un hash seguro)
        if (usuario.getContrasenaHash() == null || usuario.getContrasenaHash().length() < 8) {
            throw new Exception("La contraseña debe tener al menos 8 caracteres.");
        }
        // Verificar unicidad del email
        UsuarioRepositorio repo = new UsuarioRepositorio();
        if (repo.buscarPorEmail(usuario.getEmail()) != null) {
            throw new Exception("El email ya se encuentra registrado.");
        }
    }
}
