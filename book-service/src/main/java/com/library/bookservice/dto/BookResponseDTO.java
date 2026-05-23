package com.library.bookservice.dto;

import lombok.Data;

@Data
public class BookResponseDTO {
    private Long id;

    private String title;

    private String author;

    private Double price;

    private Integer stock;
}
