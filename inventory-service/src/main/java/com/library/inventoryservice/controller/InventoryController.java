package com.library.inventoryservice.controller;

import com.library.inventoryservice.dto.InventoryRequestDTO;
import com.library.inventoryservice.dto.InventoryResponseDTO;
import com.library.inventoryservice.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private static final Logger log = LoggerFactory.getLogger(InventoryController.class);

    private final InventoryService inventoryService;

    @GetMapping("/book/{bookId}")
    public ResponseEntity<InventoryResponseDTO> getInventoryByBookId(@PathVariable Long bookId) {
        log.info("Consultando inventario para bookId {}", bookId);
        return ResponseEntity.ok(inventoryService.getInventoryByBookId(bookId));
    }


    @PostMapping
    public ResponseEntity<InventoryResponseDTO> createInventory(@Valid @RequestBody InventoryRequestDTO request) {
        log.info("Creando inventario para bookId {}", request.getBookId());
        return ResponseEntity.status(HttpStatus.CREATED).body(inventoryService.createInventory(request));
    }
}