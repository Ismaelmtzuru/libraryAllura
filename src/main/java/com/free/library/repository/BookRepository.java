package com.free.library.repository;

import com.free.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    boolean existsByExternalId(Long externalId);
    Optional<Book> findByExternalId(Long externalId);
    long countByLanguage(String language);

    @Query("SELECT DISTINCT b.language FROM Book b")
    List<String> findDistinctLanguages();


    // Buscar libros por coincidencia parcial del nombre del autor (insensible a mayúsculas/minúsculas)
    List<Book> findByAuthors_NameContainingIgnoreCase(String name);

    // Opcional: búsqueda exacta del nombre del autor
    List<Book> findByAuthors_Name(String name);
}
