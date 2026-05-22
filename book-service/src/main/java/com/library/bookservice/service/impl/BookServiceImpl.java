package com.library.bookservice.service.impl;

import com.library.bookservice.dto.BookRequestDTO;
import com.library.bookservice.dto.BookResponseDTO;
import com.library.bookservice.entity.Book;
import com.library.bookservice.exceptions.BookNotFoundException;
import com.library.bookservice.repository.BookRepository;
import com.library.bookservice.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private static final Logger logger =
            LoggerFactory.getLogger(BookServiceImpl.class);

    private final BookRepository bookRepository;

    @Override
    public BookResponseDTO createBook(BookRequestDTO request) {
        logger.info("Creando libro");

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
    @Override
    public List<Book> getAllBooks() {

        return bookRepository.findAll();
    }

    @Override
    public Book getBookById(Long id) {

        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Libro no encontrado"));
    }

    @Override
    public Book updateBook(Long id, BookRequestDTO request) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Libro no encontrado"));

        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setPrice(request.getPrice());
        book.setStock(request.getStock());

        logger.info("Actualizando libro con ID: {}", id);

        return bookRepository.save(book);
    }

    @Override
    public void deleteBook(Long id) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Libro no encontrado"));

        logger.info("Eliminando libro con ID: {}", id);
        bookRepository.delete(book);
    }
}