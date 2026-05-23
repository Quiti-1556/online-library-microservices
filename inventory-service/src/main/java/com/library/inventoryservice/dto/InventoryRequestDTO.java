package com.library.inventoryservice.dto;

import lombok.Data;

@Data
public class InventoryRequestDTO {
    private Long bookId;

    private Integer stock;
}
