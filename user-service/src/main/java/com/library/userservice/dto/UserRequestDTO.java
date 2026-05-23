package com.library.userservice.dto;

import lombok.Data;

@Data
public class UserRequestDTO {
    private String name;

    private String email;

    private String phone;

    private String address;
}
