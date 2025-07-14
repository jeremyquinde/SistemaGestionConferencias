package com.conferencias.negocio.servicios;

import com.conferencias.datos.entidades.Usuario;
import com.conferencias.datos.repositorios.UsuarioRepositorio;
import com.conferencias.negocio.validadores.UsuarioValidador;

import java.sql.SQLException;
import java.util.List;

public class UsuarioServicio {
    private UsuarioRepositorio usuarioRepositorio;
    private UsuarioValidador usuarioValidador;

    public UsuarioServicio() {
        this.usuarioRepositorio = new UsuarioRepositorio();
        this.usuarioValidador = new UsuarioValidador();
    }

    public void registrarUsuario(Usuario usuario) throws SQLException, Exception {
        // Validación de datos del usuario
        usuarioValidador.validarNuevoUsuario(usuario);
        usuarioRepositorio.guardar(usuario);
    }

    public Usuario obtenerUsuarioPorEmail(String email) throws SQLException {
        return usuarioRepositorio.buscarPorEmail(email);
    }

    public List<Usuario> listarUsuarios() throws SQLException {
        return usuarioRepositorio.listarTodos();
    }

    public void actualizarRol(int id, String nuevoRol) throws SQLException {
        usuarioRepositorio.actualizarRol(id, nuevoRol);
    }

    public void eliminarUsuario(int id) throws SQLException {
        usuarioRepositorio.eliminar(id);
    }
}
