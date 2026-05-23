package com.library.inventoryservice.dto;

import lombok.Data;

@Data
public class InventoryResponseDTO {
    private Long id;

    private Long bookId;

    private Integer stock;
}
