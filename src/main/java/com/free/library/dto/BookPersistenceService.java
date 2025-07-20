package com.free.library.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.free.library.entity.Author;   // ENTIDAD JPA para guardar
import com.free.library.entity.Book;     // ENTIDAD JPA para guardar
import com.free.library.model.BookResponse;  // MODELO para JSON
import com.free.library.repository.AuthorRepository;
import com.free.library.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookPersistenceService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final ObjectMapper mapper;

    public BookPersistenceService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.mapper = new ObjectMapper();
    }

    @Transactional(readOnly = true) // Es una operación de solo lectura
    public List<Author> findAliveAuthorsInYear(Integer year) {
        // Validación básica de la entrada del usuario
        if (year == null || year < 0 || year > 3000) { // Establece un rango razonable
            throw new IllegalArgumentException("Año inválido. Por favor, ingrese un año entre 0 y 3000.");
        }
        return authorRepository.findAliveAuthorsInYear(year);
        // Si usaras la @Query:
        // return authorRepository.findAliveAuthorsInYear(year);
    }

    @Transactional
    public void saveBooksFromJson(String jsonResponse) {
        try {
            BookResponse response = mapper.readValue(jsonResponse, BookResponse.class);

            for (com.free.library.model.Book apiBook : response.getResults()) {
                if (bookRepository.existsByExternalId(apiBook.getId())) continue;

                List<Author> authorEntities = new ArrayList<>();

                if (apiBook.getAuthors() != null) {
                    for (com.free.library.model.Author apiAuthor : apiBook.getAuthors()) {
                        // Buscar autor existente o crear nuevo en la entidad
                        Author author = authorRepository.findByName(apiAuthor.getName())
                                .orElseGet(() -> {
                                    Author newAuthor = new Author();
                                    newAuthor.setName(apiAuthor.getName());
                                    newAuthor.setBirthYear(apiAuthor.getBirthYear());
                                    newAuthor.setDeathYear(apiAuthor.getDeathYear());
                                    return authorRepository.save(newAuthor);
                                });
                        authorEntities.add(author);
                    }
                }


                Book book = new Book();
                book.setTitle(apiBook.getTitle());
                book.setDownloadCount(apiBook.getDownloadCount());
                book.setExternalId(apiBook.getId());
                if (apiBook.getLanguages() != null && !apiBook.getLanguages().isEmpty()) {
                    book.setLanguage(apiBook.getLanguages().get(0));  // toma el primer idioma
                } else {
                    book.setLanguage("unknown");
                }
                book.setAuthors(authorEntities);

                bookRepository.save(book);
                System.out.println(" -> Guardado en la base de datos.");
            }
        } catch (Exception e) {
            System.err.println("Error saving books: " + e.getMessage());
        }
    }
}
