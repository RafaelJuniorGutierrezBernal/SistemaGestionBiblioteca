package com.biblioteca.service;

import com.biblioteca.exceptions.BibliotecaException;
import com.biblioteca.model.Libro;
import com.biblioteca.model.Prestamo;
import com.biblioteca.model.Usuario;
import com.biblioteca.repository.LibroRepository;
import com.biblioteca.repository.PrestamoRepository;
import com.biblioteca.repository.UsuarioRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class BibliotecaService {
    private final LibroRepository libroRepository;
    private final UsuarioRepository usuarioRepository;
    private final PrestamoRepository prestamoRepository;

    public BibliotecaService(LibroRepository libroRepository, UsuarioRepository usuarioRepository,
                             PrestamoRepository prestamoRepository) {
        this.libroRepository = libroRepository;
        this.usuarioRepository = usuarioRepository;
        this.prestamoRepository = prestamoRepository;
    }

    /**
     * Agrega un nuevo libro al sistema.
     * 
     * @param titulo Título del libro
     * @param autor Autor del libro
     * @return Libro agregado
     */
    public Libro agregarLibro(String titulo, String autor) {
        String id = UUID.randomUUID().toString();
        Libro libro = new Libro(id, titulo, autor);
        return libroRepository.guardarLibro(libro);
    }

    /**
     * Busca un libro por su ID.
     * 
     * @param id ID del libro a buscar
     * @return Libro encontrado
     * @throws BibliotecaException.LibroNoEncontradoException si no se encuentra el libro
     */
    public Libro buscarLibroPorId(String id) {
        return libroRepository.buscarPorId(id)
                .orElseThrow(() -> new BibliotecaException.LibroNoEncontradoException("Libro no encontrado con ID: " + id));
    }

    /**
     * Registra un nuevo usuario en el sistema.
     * 
     * @param nombre Nombre del usuario
     * @return Usuario registrado
     */
    public Usuario registrarUsuario(String nombre) {
        String id = UUID.randomUUID().toString();
        Usuario usuario = new Usuario(id, nombre);
        return usuarioRepository.guardarUsuario(usuario);
    }

    /**
     * Realiza el préstamo de un libro a un usuario.
     * 
     * @param idLibro ID del libro a prestar
     * @param idUsuario ID del usuario que solicita el préstamo
     * @return Préstamo realizado
     * @throws BibliotecaException.LibroNoEncontradoException si no se encuentra el libro
     * @throws BibliotecaException.UsuarioNoEncontradoException si no se encuentra el usuario
     * @throws BibliotecaException.LibroYaPrestadoException si el libro ya está prestado
     */
    public Prestamo prestarLibro(String idLibro, String idUsuario) {
        // Verificar si el libro existe
        Libro libro = libroRepository.buscarPorId(idLibro)
                .orElseThrow(() -> new BibliotecaException.LibroNoEncontradoException("Libro no encontrado con ID: " + idLibro));
        
        // Verificar si el usuario existe
        Usuario usuario = usuarioRepository.buscarPorId(idUsuario)
                .orElseThrow(() -> new BibliotecaException.UsuarioNoEncontradoException("Usuario no encontrado con ID: " + idUsuario));
        
        // Verificar si el libro ya está prestado
        if (libro.isPrestado()) {
            throw new BibliotecaException.LibroYaPrestadoException("El libro con ID: " + idLibro + " ya está prestado");
        }
        
        // Marcar el libro como prestado
        libro.setPrestado(true);
        libroRepository.actualizarLibro(libro);
        
        // Crear y guardar el préstamo
        String idPrestamo = UUID.randomUUID().toString();
        Prestamo prestamo = new Prestamo(idPrestamo, idLibro, idUsuario, LocalDate.now());
        return prestamoRepository.guardarPrestamo(prestamo);
    }

    /**
     * Busca todos los préstamos realizados por un usuario.
     * 
     * @param idUsuario ID del usuario
     * @return Lista de préstamos del usuario
     * @throws BibliotecaException.UsuarioNoEncontradoException si no se encuentra el usuario
     */
    public List<Prestamo> buscarPrestamosPorUsuario(String idUsuario) {
        // Verificar si el usuario existe
        usuarioRepository.buscarPorId(idUsuario)
                .orElseThrow(() -> new BibliotecaException.UsuarioNoEncontradoException("Usuario no encontrado con ID: " + idUsuario));
        
        return prestamoRepository.buscarPorUsuario(idUsuario);
    }
}