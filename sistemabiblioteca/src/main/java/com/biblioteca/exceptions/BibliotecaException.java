package com.biblioteca.exceptions;

public class BibliotecaException {
    public static class LibroNoEncontradoException extends RuntimeException {
        public LibroNoEncontradoException(String mensaje) {
            super(mensaje);
        }
    }

    public static class UsuarioNoEncontradoException extends IllegalArgumentException {
        public UsuarioNoEncontradoException(String mensaje) {
            super(mensaje);
        }
    }

    public static class LibroYaPrestadoException extends RuntimeException {
        public LibroYaPrestadoException(String mensaje) {
            super(mensaje);
        }
    }
}