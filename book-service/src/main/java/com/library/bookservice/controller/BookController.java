package com.library.bookservice.controller;

import com.library.bookservice.dto.BookRequestDTO;
import com.library.bookservice.dto.BookResponseDTO;
import com.library.bookservice.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    public BookResponseDTO createBook(@RequestBody BookRequestDTO request) {

        return bookService.createBook(request);
    }
}
