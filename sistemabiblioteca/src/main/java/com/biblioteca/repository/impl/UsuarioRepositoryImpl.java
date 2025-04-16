package com.biblioteca.repository.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.biblioteca.model.Usuario;
import com.biblioteca.repository.UsuarioRepository;

public class UsuarioRepositoryImpl implements UsuarioRepository {
    private Map<String, Usuario> usuarios = new HashMap<>();

    @Override
    public Usuario guardarUsuario(Usuario usuario) {
        usuarios.put(usuario.getId(), usuario);
        return usuario;
    }

    @Override
    public Optional<Usuario> buscarPorId(String id) {
        return Optional.ofNullable(usuarios.get(id));
    }
}