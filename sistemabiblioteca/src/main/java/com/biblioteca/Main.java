package com.biblioteca;

import com.biblioteca.model.Libro;
import com.biblioteca.model.Prestamo;
import com.biblioteca.model.Usuario;
import com.biblioteca.repository.LibroRepository;
import com.biblioteca.repository.PrestamoRepository;
import com.biblioteca.repository.UsuarioRepository;
import com.biblioteca.repository.impl.LibroRepositoryImpl;
import com.biblioteca.repository.impl.PrestamoRepositoryImpl;
import com.biblioteca.repository.impl.UsuarioRepositoryImpl;
import com.biblioteca.service.BibliotecaService;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static BibliotecaService bibliotecaService;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Inicializar repositorios
        LibroRepository libroRepository = new LibroRepositoryImpl();
        UsuarioRepository usuarioRepository = new UsuarioRepositoryImpl();
        PrestamoRepository prestamoRepository = new PrestamoRepositoryImpl();
        
        // Inicializar servicio
        bibliotecaService = new BibliotecaService(libroRepository, usuarioRepository, prestamoRepository);
        
        // Menú principal
        boolean salir = false;
        while (!salir) {
            System.out.println("\n===== SISTEMA DE GESTIÓN DE BIBLIOTECA =====");
            System.out.println("1. Agregar un libro");
            System.out.println("2. Buscar un libro por ID");
            System.out.println("3. Registrar un usuario");
            System.out.println("4. Prestar un libro");
            System.out.println("5. Consultar préstamos por usuario");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            
            try {
                int opcion = Integer.parseInt(scanner.nextLine());
                
                switch (opcion) {
                    case 1:
                        agregarLibro();
                        break;
                    case 2:
                        buscarLibro();
                        break;
                    case 3:
                        registrarUsuario();
                        break;
                    case 4:
                        prestarLibro();
                        break;
                    case 5:
                        consultarPrestamosPorUsuario();
                        break;
                    case 0:
                        salir = true;
                        System.out.println("¡Gracias por usar el Sistema de Gestión de Biblioteca!");
                        break;
                    default:
                        System.out.println("Opción no válida. Intente de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Debe ingresar un número válido.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        
        scanner.close();
    }

    private static void agregarLibro() {
        System.out.println("\n-- AGREGAR LIBRO --");
        System.out.print("Ingrese el título del libro: ");
        String titulo = scanner.nextLine();
        if (titulo == null || titulo.isEmpty()) {
            System.out.println("El título del libro es obligatorio.");
        }
        System.out.print("Ingrese el autor del libro: ");
        String autor = scanner.nextLine();
        if (autor == null || autor.isEmpty()) {
            System.out.println("El título del libro es obligatorio.");
        }
        
        try {
            Libro libro = bibliotecaService.agregarLibro(titulo, autor);
            System.out.println("Libro agregado correctamente: " + libro);
            System.out.println("ID del libro: " + libro.getId() + " (guarde este ID para futuras operaciones)");
        } catch (Exception e) {
            System.out.println("Error al agregar el libro: " + e.getMessage());
        }
    }

    private static void buscarLibro() {
        System.out.println("\n-- BUSCAR LIBRO --");
        System.out.print("Ingrese el ID del libro: ");
        String idLibro = scanner.nextLine();
        
        if (idLibro == null || idLibro.isEmpty()) {
            System.out.println("El ID del libro no puede ser nulo.");
            
        }
        
        try {
            Libro libro = bibliotecaService.buscarLibroPorId(idLibro);
            System.out.println("Libro encontrado: " + libro);
            System.out.println("Libro encontrado: " + libro);
            System.out.println("Estado: " + (libro.isPrestado() ? "Prestado" : "Disponible"));
        } catch (Exception e) {
            System.out.println("Error al buscar el libro: " + e.getMessage());
        }
    }

    private static void registrarUsuario() {
        System.out.println("\n-- REGISTRAR USUARIO --");
        System.out.print("Ingrese el nombre del usuario: ");
        String nombre = scanner.nextLine();
        
        try {
            Usuario usuario = bibliotecaService.registrarUsuario(nombre);
            System.out.println("Usuario registrado correctamente: " + usuario);
            System.out.println("ID del usuario: " + usuario.getId() + " (guarde este ID para futuras operaciones)");
        } catch (Exception e) {
            System.out.println("Error al registrar el usuario: " + e.getMessage());
        }
    }

    private static void prestarLibro() {
        System.out.println("\n-- PRESTAR LIBRO --");
        System.out.print("Ingrese el ID del libro: ");
        String idLibro = scanner.nextLine();
        System.out.print("Ingrese el ID del usuario: ");
        String idUsuario = scanner.nextLine();
        
        try {
            Prestamo prestamo = bibliotecaService.prestarLibro(idLibro, idUsuario);
            System.out.println("Préstamo realizado correctamente: " + prestamo);
            System.out.println("Fecha de préstamo: " + prestamo.getFechaPrestamo());
        } catch (Exception e) {
            System.out.println("Error al realizar el préstamo: " + e.getMessage());
        }
    }

    private static void consultarPrestamosPorUsuario() {
        System.out.println("\n-- CONSULTAR PRÉSTAMOS POR USUARIO --");
        System.out.print("Ingrese el ID del usuario: ");
        String idUsuario = scanner.nextLine();
        
        try {
            List<Prestamo> prestamos = bibliotecaService.buscarPrestamosPorUsuario(idUsuario);
            
            if (prestamos.isEmpty()) {
                System.out.println("El usuario no tiene préstamos registrados.");
            } else {
                System.out.println("Préstamos del usuario:");
                for (Prestamo prestamo : prestamos) {
                    System.out.println(prestamo);
                    try {
                        Libro libro = bibliotecaService.buscarLibroPorId(prestamo.getIdLibro());
                        System.out.println("Libro: " + libro.getTitulo() + " de " + libro.getAutor());
                    } catch (Exception e) {
                        System.out.println("No se pudo obtener información del libro.");
                    }
                    System.out.println("Fecha de préstamo: " + prestamo.getFechaPrestamo());
                    System.out.println("------------------------");
                }
            }
        } catch (Exception e) {
            System.out.println("Error al consultar préstamos: " + e.getMessage());
        }
    }
}