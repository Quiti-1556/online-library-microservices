package com.library.bookservice.dto;

import lombok.Data;

@Data
public class bookRequestDTO {
    private String title;

    private String author;

    private Double price;

    private Integer stock;
}

