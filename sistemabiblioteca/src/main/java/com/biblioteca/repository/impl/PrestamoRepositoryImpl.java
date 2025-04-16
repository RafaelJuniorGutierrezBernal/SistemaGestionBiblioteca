package com.biblioteca.repository.impl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.biblioteca.model.Prestamo;
import com.biblioteca.repository.PrestamoRepository;

public class PrestamoRepositoryImpl implements PrestamoRepository {
    private Map<String, Prestamo> prestamos = new HashMap<>();

    @Override
    public Prestamo guardarPrestamo(Prestamo prestamo) {
        prestamos.put(prestamo.getId(), prestamo);
        return prestamo;
    }

    @Override
    public List<Prestamo> buscarPorUsuario(String idUsuario) {
        return prestamos.values().stream()
                .filter(prestamo -> prestamo.getIdUsuario().equals(idUsuario))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Prestamo> buscarLibroPrestado(String idLibro) {
        return prestamos.values().stream()
                .filter(prestamo -> prestamo.getIdLibro().equals(idLibro))
                .findFirst();
    }
}