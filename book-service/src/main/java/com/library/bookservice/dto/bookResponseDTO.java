package com.library.bookservice.dto;

import lombok.Data;

@Data
public class bookResponseDTO {
    private Long id;

    private String title;

    private String author;

    private Double price;

    private Integer stock;
}
