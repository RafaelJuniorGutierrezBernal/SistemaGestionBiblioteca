package com.biblioteca.repository;

import java.util.Optional;

import com.biblioteca.model.Usuario;

public interface UsuarioRepository {
    Usuario guardarUsuario(Usuario usuario);
    Optional<Usuario> buscarPorId(String id);
}