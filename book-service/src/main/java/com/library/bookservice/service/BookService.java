package com.library.bookservice.service;


import com.library.bookservice.dto.BookRequestDTO;
import com.library.bookservice.dto.BookResponseDTO;

public interface BookService {

    BookResponseDTO createBook(BookRequestDTO request);



}
