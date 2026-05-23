package com.library.cartservice.dto;

import lombok.Data;

@Data
public class CartRequestDTO {
    private Long userId;

    private Long bookId;

    private Integer quantity;

}
