package com.library.inventoryservice.repository;

import com.library.inventoryservice.entity.Inventory;
import org.springframework.data.repository.CrudRepository;

public interface InventoryRepository extends CrudRepository<Inventory,Long> {
}
