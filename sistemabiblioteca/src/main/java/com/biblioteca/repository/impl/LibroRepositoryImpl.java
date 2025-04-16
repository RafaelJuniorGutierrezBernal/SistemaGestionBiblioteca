package com.biblioteca.repository.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.biblioteca.model.Libro;
import com.biblioteca.repository.LibroRepository;

public class LibroRepositoryImpl implements LibroRepository {
    private Map<String, Libro> libros = new HashMap<>();

    @Override
    public Libro guardarLibro(Libro libro) {
        libros.put(libro.getId(), libro);
        return libro;
    }

    @Override
    public Optional<Libro> buscarPorId(String id) {
        return Optional.ofNullable(libros.get(id));
    }

    @Override
    public void actualizarLibro(Libro libro) {
        libros.put(libro.getId(), libro);
    }
}
