package com.library.bookservice.service;


import com.library.bookservice.dto.BookRequestDTO;
import com.library.bookservice.dto.BookResponseDTO;
import com.library.bookservice.entity.Book;

import java.util.List;

public interface BookService {

    BookResponseDTO createBook(BookRequestDTO request);

    List<Book> getAllBooks();

    Book getBookById(Long id);

    Book updateBook(Long id, BookRequestDTO request);

    void deleteBook(Long id);




}
