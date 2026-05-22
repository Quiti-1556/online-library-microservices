package com.library.bookservice.service.impl;

import com.library.bookservice.dto.BookRequestDTO;
import com.library.bookservice.dto.BookResponseDTO;
import com.library.bookservice.entity.Book;
import com.library.bookservice.exceptions.BookNotFoundException;
import com.library.bookservice.repository.BookRepository;
import com.library.bookservice.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public BookResponseDTO createBook(BookRequestDTO request) {

        if (request.getTitle() == null) {

            throw new BookNotFoundException("Libro no encontrado");
        }

        Book book = new Book();

        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setPrice(request.getPrice());
        book.setStock(request.getStock());

        Book savedBook = bookRepository.save(book);

        BookResponseDTO response = new BookResponseDTO();

        response.setId(savedBook.getId());
        response.setTitle(savedBook.getTitle());
        response.setAuthor(savedBook.getAuthor());
        response.setPrice(savedBook.getPrice());
        response.setStock(savedBook.getStock());

        return response;
    }
}