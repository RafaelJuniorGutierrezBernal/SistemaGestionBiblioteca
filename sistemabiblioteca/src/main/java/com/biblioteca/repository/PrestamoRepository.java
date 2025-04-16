package com.biblioteca.repository;

import java.util.List;
import java.util.Optional;

import com.biblioteca.model.Prestamo;

public interface PrestamoRepository {
    Prestamo guardarPrestamo(Prestamo prestamo);
    List<Prestamo> buscarPorUsuario(String idUsuario);
    Optional<Prestamo> buscarLibroPrestado(String idLibro);
}