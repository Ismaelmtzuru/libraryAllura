package com.free.library.service;

import com.free.library.entity.Author;
import com.free.library.entity.Book;
import com.free.library.repository.AuthorRepository;
import com.free.library.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersistenceService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public PersistenceService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;


    }



    @Transactional
    public void saveBooks(List<Book> books) {
        for (Book book : books) {
            List<Author> processedAuthors = new ArrayList<>();

            for (Author author : book.getAuthors()) {
                if (author.getId() != null && authorRepository.existsById(author.getId())) {
                    processedAuthors.add(authorRepository.findById(author.getId()).get());
                } else {
                    Author savedAuthor = authorRepository.save(author);
                    processedAuthors.add(savedAuthor);
                }
            }

            book.setAuthors(processedAuthors);

            bookRepository.save(book);
        }
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
}
