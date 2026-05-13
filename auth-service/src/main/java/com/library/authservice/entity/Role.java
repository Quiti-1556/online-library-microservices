package com.library.authservice.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity @Data @AllArgsConstructor  @NoArgsConstructor
public class Role {
    String admin;
    String client;
}
