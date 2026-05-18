package com.library.cartservice.dto;

import lombok.Data;

@Data
public class CartResponseDTO {
    private Long id;

    private Long userId;

    private Long bookId;

    private Integer quantity;
}
