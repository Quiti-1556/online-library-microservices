package com.library.bookservice.controller;

import com.library.bookservice.dto.BookRequestDTO;
import com.library.bookservice.dto.BookResponseDTO;
import com.library.bookservice.entity.Book;
import com.library.bookservice.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    public BookResponseDTO createBook(@RequestBody BookRequestDTO request) {

        return bookService.createBook(request);
    }
    @GetMapping
    public String getBooks() {

        return "Book Service funcionando correctamente";
    }
    @GetMapping("/error")
    public String error() {

        throw new RuntimeException("Error de prueba");
    }
    @GetMapping
    public List<Book> getAllBooks() {

        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Long id) {

        return bookService.getBookById(id);
    }

    @PutMapping("/{id}")
    public Book updateBook(@PathVariable Long id,
                           @RequestBody BookRequestDTO request) {

        return bookService.updateBook(id, request);
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable Long id) {

        bookService.deleteBook(id);

        return "Libro eliminado correctamente";
    }
}
