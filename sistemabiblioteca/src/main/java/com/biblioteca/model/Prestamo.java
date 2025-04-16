package com.biblioteca.model;

import java.time.LocalDate;

public class Prestamo {
    private String id;
    private String idLibro;
    private String idUsuario;
    private LocalDate fechaPrestamo;

    public Prestamo(String id, String idLibro, String idUsuario, LocalDate fechaPrestamo) {
        this.id = id;
        this.idLibro = idLibro;
        this.idUsuario = idUsuario;
        this.fechaPrestamo = fechaPrestamo;
    }

    // Getters y setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(String idLibro) {
        this.idLibro = idLibro;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(LocalDate fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    @Override
    public String toString() {
        return "Prestamo{" +
                "id='" + id + '\'' +
                ", idLibro='" + idLibro + '\'' +
                ", idUsuario='" + idUsuario + '\'' +
                ", fechaPrestamo=" + fechaPrestamo +
                '}';
    }
}