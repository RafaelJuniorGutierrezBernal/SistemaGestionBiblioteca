package com.biblioteca.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.biblioteca.exceptions.BibliotecaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.biblioteca.model.Libro;
import com.biblioteca.model.Prestamo;
import com.biblioteca.model.Usuario;
import com.biblioteca.repository.LibroRepository;
import com.biblioteca.repository.PrestamoRepository;
import com.biblioteca.repository.UsuarioRepository;

class BibliotecaServiceTest {

    @Mock
    private LibroRepository libroRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PrestamoRepository prestamoRepository;

    private BibliotecaService bibliotecaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bibliotecaService = new BibliotecaService(libroRepository, usuarioRepository, prestamoRepository);
    }

    @Test
    void agregarLibro_DebeGuardarYRetornarLibro() {
        // Arrange
        String titulo = "Don Quijote";
        String autor = "Miguel de Cervantes";
        Libro libroEsperado = new Libro("1", titulo, autor);
        
        when(libroRepository.guardarLibro(any(Libro.class))).thenReturn(libroEsperado);

        // Act
        Libro libroResultado = bibliotecaService.agregarLibro(titulo, autor);

        // Assert
        assertNotNull(libroResultado);
        assertEquals(titulo, libroResultado.getTitulo());
        assertEquals(autor, libroResultado.getAutor());
        verify(libroRepository, times(1)).guardarLibro(any(Libro.class));
    }

    @Test
    void buscarLibroPorId_LibroExistente_DebeRetornarLibro() {
        // Arrange
        String id = "1";
        Libro libroEsperado = new Libro(id, "Cien años de soledad", "Gabriel García Márquez");
        
        when(libroRepository.buscarPorId(id)).thenReturn(Optional.of(libroEsperado));

        // Act
        Libro libroResultado = bibliotecaService.buscarLibroPorId(id);

        // Assert
        assertNotNull(libroResultado);
        assertEquals(id, libroResultado.getId());
        verify(libroRepository, times(1)).buscarPorId(id);
    }

    @Test
    void buscarLibroPorId_LibroNoExistente_DebeLanzarExcepcion() {
        // Arrange
        String id = "999";
        when(libroRepository.buscarPorId(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BibliotecaException.LibroNoEncontradoException.class, () -> {
            bibliotecaService.buscarLibroPorId(id);
        });
        verify(libroRepository, times(1)).buscarPorId(id);
    }

    @Test
    void registrarUsuario_DebeGuardarYRetornarUsuario() {
        // Arrange
        String nombre = "Juan Pérez";
        Usuario usuarioEsperado = new Usuario("1", nombre);
        
        when(usuarioRepository.guardarUsuario(any(Usuario.class))).thenReturn(usuarioEsperado);

        // Act
        Usuario usuarioResultado = bibliotecaService.registrarUsuario(nombre);

        // Assert
        assertNotNull(usuarioResultado);
        assertEquals(nombre, usuarioResultado.getNombre());
        verify(usuarioRepository, times(1)).guardarUsuario(any(Usuario.class));
    }

    @Test
    void prestarLibro_LibroYUsuarioExistentes_DebeRealizarPrestamo() {
        // Arrange
        String idLibro = "1";
        String idUsuario = "1";
        Libro libro = new Libro(idLibro, "El Principito", "Antoine de Saint-Exupéry");
        Usuario usuario = new Usuario(idUsuario, "María López");
        Prestamo prestamoEsperado = new Prestamo("1", idLibro, idUsuario, LocalDate.now());
        
        when(libroRepository.buscarPorId(idLibro)).thenReturn(Optional.of(libro));
        when(usuarioRepository.buscarPorId(idUsuario)).thenReturn(Optional.of(usuario));
        when(prestamoRepository.guardarPrestamo(any(Prestamo.class))).thenReturn(prestamoEsperado);

        // Act
        Prestamo prestamoResultado = bibliotecaService.prestarLibro(idLibro, idUsuario);

        // Assert
        assertNotNull(prestamoResultado);
        assertEquals(idLibro, prestamoResultado.getIdLibro());
        assertEquals(idUsuario, prestamoResultado.getIdUsuario());
        verify(libroRepository, times(1)).buscarPorId(idLibro);
        verify(usuarioRepository, times(1)).buscarPorId(idUsuario);
        verify(libroRepository, times(1)).actualizarLibro(libro);
        verify(prestamoRepository, times(1)).guardarPrestamo(any(Prestamo.class));
        assertTrue(libro.isPrestado());
    }

    @Test
    void prestarLibro_LibroNoExistente_DebeLanzarExcepcion() {
        // Arrange
        String idLibro = "999";
        String idUsuario = "1";
        
        when(libroRepository.buscarPorId(idLibro)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BibliotecaException.LibroNoEncontradoException.class, () -> {
            bibliotecaService.prestarLibro(idLibro, idUsuario);
        });
        verify(libroRepository, times(1)).buscarPorId(idLibro);
        verify(usuarioRepository, never()).buscarPorId(anyString());
    }

    @Test
    void prestarLibro_UsuarioNoExistente_DebeLanzarExcepcion() {
        // Arrange
        String idLibro = "1";
        String idUsuario = "999";
        Libro libro = new Libro(idLibro, "El Principito", "Antoine de Saint-Exupéry");
        
        when(libroRepository.buscarPorId(idLibro)).thenReturn(Optional.of(libro));
        when(usuarioRepository.buscarPorId(idUsuario)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BibliotecaException.UsuarioNoEncontradoException.class, () -> {
            bibliotecaService.prestarLibro(idLibro, idUsuario);
        });
        verify(libroRepository, times(1)).buscarPorId(idLibro);
        verify(usuarioRepository, times(1)).buscarPorId(idUsuario);
    }

    @Test
    void prestarLibro_LibroYaPrestado_DebeLanzarExcepcion() {
        // Arrange
        String idLibro = "1";
        String idUsuario = "1";
        Libro libro = new Libro(idLibro, "El Principito", "Antoine de Saint-Exupéry");
        libro.setPrestado(true);
        Usuario usuario = new Usuario(idUsuario, "María López");
        
        when(libroRepository.buscarPorId(idLibro)).thenReturn(Optional.of(libro));
        when(usuarioRepository.buscarPorId(idUsuario)).thenReturn(Optional.of(usuario));

        // Act & Assert
        assertThrows(BibliotecaException.LibroYaPrestadoException.class, () -> {
            bibliotecaService.prestarLibro(idLibro, idUsuario);
        });
        verify(libroRepository, times(1)).buscarPorId(idLibro);
        verify(usuarioRepository, times(1)).buscarPorId(idUsuario);
        verify(libroRepository, never()).actualizarLibro(any(Libro.class));
    }

    @Test
    void buscarPrestamosPorUsuario_UsuarioExistente_DebeRetornarPrestamos() {
        // Arrange
        String idUsuario = "1";
        Usuario usuario = new Usuario(idUsuario, "Carlos Ruiz");
        Prestamo prestamo1 = new Prestamo("1", "1", idUsuario, LocalDate.now());
        Prestamo prestamo2 = new Prestamo("2", "2", idUsuario, LocalDate.now());
        List<Prestamo> prestamosEsperados = Arrays.asList(prestamo1, prestamo2);
        
        when(usuarioRepository.buscarPorId(idUsuario)).thenReturn(Optional.of(usuario));
        when(prestamoRepository.buscarPorUsuario(idUsuario)).thenReturn(prestamosEsperados);

        // Act
        List<Prestamo> prestamosResultado = bibliotecaService.buscarPrestamosPorUsuario(idUsuario);

        // Assert
        assertNotNull(prestamosResultado);
        assertEquals(2, prestamosResultado.size());
        verify(usuarioRepository, times(1)).buscarPorId(idUsuario);
        verify(prestamoRepository, times(1)).buscarPorUsuario(idUsuario);
    }

    @Test
    void buscarPrestamosPorUsuario_UsuarioNoExistente_DebeLanzarExcepcion() {
        // Arrange
        String idUsuario = "999";
        
        when(usuarioRepository.buscarPorId(idUsuario)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BibliotecaException.UsuarioNoEncontradoException.class, () -> {
            bibliotecaService.buscarPrestamosPorUsuario(idUsuario);
        });
        verify(usuarioRepository, times(1)).buscarPorId(idUsuario);
        verify(prestamoRepository, never()).buscarPorUsuario(anyString());
    }
}