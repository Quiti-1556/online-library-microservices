package com.library.userservice.repository;

import com.library.userservice.entity.UserProfile;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserProfile, Long> {
}
