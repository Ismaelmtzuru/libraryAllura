package com.free.library.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.free.library.dto.AuthorDto;
import com.free.library.dto.BookDto;
import com.free.library.dto.BookPersistenceService;
import com.free.library.entity.Author;
import com.free.library.model.Book;
import com.free.library.model.BookResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Service
public class ConsoleMenu {
    private final StatisticsService statisticsService;

    private final ApiCall apiCall;
    private final Scanner scanner;
    private final BookPersistenceService persistenceService;

    public ConsoleMenu(ApiCall apiCall, BookPersistenceService persistenceService, StatisticsService statisticsService) {
        this.apiCall = apiCall;
        this.persistenceService = persistenceService;
        this.statisticsService = statisticsService;
        this.scanner = new Scanner(System.in);
    }

    public void displayMenu(){
        int option =1;
        while (option !=0){
            System.out.println("\n ----Library app menu----");
            System.out.println(("1. See all books"));
            System.out.println(("2. Search book by title"));
            System.out.println(("3. Search books by author"));
            System.out.println(("4. Count Books by Language"));
            System.out.println(("5. Find Authors lived in/by year "));
            System.out.println(("0. End program"));

            try{
                option = scanner.nextInt();
                scanner.nextLine();
                switch (option){
                    case 1:
                        callApiDisplay();
                        break;
                    case 2:
                        searchByTitle();
                        break;
                    case 3:
                        searchByAuthor();
                        break;
                    case 4:
                        showBookCountByLanguage();
                        break;
                    case 5:
                        handleFindAuthorsAliveInYear();
                        break;
                    case 0:
                        System.out.println("Library closed");
                        break;

                    default:
                        System.out.println("Choose a valid option");
                        break;
                }
            }catch (NumberFormatException e){
                System.out.println("Invalid option, Choose the right number");
            }catch (RuntimeException e){
                System.out.println("Operational errorr " + e.getMessage());
            }
        }
        scanner.close();

    }

    private void callApiDisplay(){
        var url = "https://gutendex.com/books/";
        try{
            System.out.println("Loading data...");
            String jsonResponse = apiCall.obtenerDatos(url);
            System.out.println(jsonResponse.substring(0,Math.min(jsonResponse.length(),100)) + "...");
        }catch (RuntimeException e){
            System.err.println("Data can't be loaded");
        }
    }

    private void handleFindAuthorsAliveInYear() {
        System.out.print("Enter the year to find alive authors: ");
        String yearInput = scanner.nextLine();
        Integer year = null;
        try {
            year = Integer.parseInt(yearInput);
        } catch (NumberFormatException e) {
            System.out.println("Invalid year format. Please enter a number (e.g., 2023).");
            return;
        }

        try {
            // --- ¡Esta línea ahora debería funcionar! ---
            List<Author> aliveAuthors = persistenceService.findAliveAuthorsInYear(year);

            if (aliveAuthors.isEmpty()) {
                System.out.println("No authors found alive in the year " + year + ".");
            } else {
                System.out.println("\n--- Authors alive in " + year + " ---");
                for (Author author : aliveAuthors) {
                    System.out.println("ID: " + author.getId() + ", Name: " + author.getName() +
                            ", Born: " + author.getBirthYear() +
                            (author.getDeathYear() != null ? ", Died: " + author.getDeathYear() : " (Still alive)"));
                }
                System.out.println("------------------------------------");
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }
    }












    private void searchByTitle() {
        var url = "https://gutendex.com/books?search=";
        System.out.println("Input name to search: ");
        scanner.nextLine();
        String busqueda = scanner.nextLine();
        busqueda = busqueda.replace(" ", "%20");
        String urlFinal = url + busqueda;

        try {
            String jsonResponse = apiCall.obtenerDatos(urlFinal);

            ObjectMapper mapper = new ObjectMapper();
            BookResponse bookResponse = mapper.readValue(jsonResponse, BookResponse.class);

            if (bookResponse.getResults().isEmpty()) {
                System.out.println("No books found with that title.");
            } else {
                System.out.println("\nBooks found:");
                for (Book book : bookResponse.getResults()) {
                    System.out.println("- " + book.getTitle());

                    // Mostrar autores
                    if (book.getAuthors() != null && !book.getAuthors().isEmpty()) {
                        String authorNames = book.getAuthors().stream()
                                .map(a -> a.getName() +
                                        (a.getBirthYear() != null ? " (" + a.getBirthYear() : "") +
                                        (a.getDeathYear() != null ? " - " + a.getDeathYear() + ")" : ""))
                                .collect(Collectors.joining(", "));
                        System.out.println("  Author(s): " + authorNames);
                    } else {
                        System.out.println("  Author(s): Unknown");
                    }

                    System.out.println("  Downloads: " + book.getDownloadCount());

                    // Verificación por consola
                    System.out.println("-> Título: " + book.getTitle());
                    System.out.println("-> ID externo: " + book.getId());
                    System.out.println("-> Descargas: " + book.getDownloadCount());
                    System.out.println("-> Autores: " + book.getAuthors().stream()
                            .map(a -> a.getName())
                            .collect(Collectors.toList()));

                    // Convertir modelo a DTO
                    BookDto dto = new BookDto(
                            book.getId(),
                            book.getTitle(),
                            book.getDownloadCount(),
                            book.getAuthors().stream()
                                    .map(a -> new AuthorDto(a.getName(), a.getBirthYear(), a.getDeathYear()))
                                    .collect(Collectors.toList())
                    );

                    persistenceService.saveBooksFromJson(jsonResponse);

                    System.out.println("  -> Guardado en la base de datos.");
                }
            }

        } catch (Exception e) {
            System.out.println("Error loading books: " + e.getMessage());
        }
    }
    private void showBookCountByLanguage() {
        System.out.println("Available languages: " + String.join(", ", statisticsService.getLanguages()));
        System.out.print("Enter language code: ");
        scanner.nextLine(); // limpiar buffer
        String lang = scanner.nextLine().trim().toLowerCase();

        long count = statisticsService.countBooksByLanguage(lang);
        System.out.println("There are " + count + " book(s) in language: " + lang);
    }


    public void searchByAuthor(){
        searchByTitle();
    }
}
