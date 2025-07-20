package com.free.library.repository;

import com.free.library.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // Import this
import org.springframework.data.repository.query.Param; // Import this

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    Optional<Author> findByName(String name);

    // This is the ONLY method for finding alive authors by year that should be here
    @Query("SELECT a FROM Author a WHERE a.birthYear <= :year AND (a.deathYear IS NULL OR a.deathYear >= :year)")
    List<Author> findAliveAuthorsInYear(@Param("year") Integer year);

    // Make sure the old problematic method is GONE:
    // List<Author> findByBirthYearLessThanEqualAndDeathYearIsNullOrCreateDeathYearGreaterThanEqual(Integer targetYear, Integer targetYear2);
}