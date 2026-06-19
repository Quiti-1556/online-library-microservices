package com.library.inventoryservice.service.impl;

import com.library.inventoryservice.dto.InventoryRequestDTO;
import com.library.inventoryservice.dto.InventoryResponseDTO;
import com.library.inventoryservice.entity.Inventory;
import com.library.inventoryservice.repository.InventoryRepository;
import com.library.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private static final Logger logger =
            LoggerFactory.getLogger(InventoryServiceImpl.class);

    private final InventoryRepository inventoryRepository;

    private InventoryResponseDTO mapToResponse(Inventory inventory) {
        InventoryResponseDTO response = new InventoryResponseDTO();
        response.setId(inventory.getId());
        response.setBookId(inventory.getBookId());
        response.setStock(inventory.getStock());
        return response;
    }

    @Override
    public InventoryResponseDTO createInventory(InventoryRequestDTO request) {
        logger.info("Creando inventario para bookId {}", request.getBookId());

        if (inventoryRepository.existsByBookId(request.getBookId())) {
            throw new RuntimeException("Ya existe inventario para ese libro");
        }

        Inventory inventory = new Inventory();
        inventory.setBookId(request.getBookId());
        inventory.setStock(request.getStock());

        Inventory savedInventory = inventoryRepository.save(inventory);

        logger.info("Inventario creado correctamente con id {}", savedInventory.getId());

        return mapToResponse(savedInventory);
    }
}
