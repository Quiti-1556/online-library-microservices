package com.library.cartservice.repository;

import com.library.cartservice.entity.Cart;
import org.springframework.data.repository.CrudRepository;

public interface CartRepository extends CrudRepository<Cart,Long> {

}
