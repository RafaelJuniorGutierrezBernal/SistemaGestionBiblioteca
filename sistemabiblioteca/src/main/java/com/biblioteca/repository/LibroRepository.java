package com.biblioteca.repository;

import java.util.Optional;

import com.biblioteca.model.Libro;

public interface LibroRepository {
    Libro guardarLibro(Libro libro);
    Optional<Libro> buscarPorId(String id);
    void actualizarLibro(Libro libro);
}
