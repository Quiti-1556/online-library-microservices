package com.library.inventoryservice.service;

import com.library.inventoryservice.dto.InventoryRequestDTO;
import com.library.inventoryservice.dto.InventoryResponseDTO;
import com.library.inventoryservice.entity.Inventory;

import java.util.List;


public interface InventoryService {
    InventoryResponseDTO createInventory(InventoryRequestDTO request);

}
