package com.free.library.service;

import com.free.library.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticsService {

    private final BookRepository bookRepository;

    public StatisticsService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public long countBooksByLanguage(String language) {
        return bookRepository.countByLanguage(language);
    }

    public List<String> getLanguages() {
        return bookRepository.findDistinctLanguages();
    }
}
