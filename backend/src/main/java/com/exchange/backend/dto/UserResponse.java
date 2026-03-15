package com.exchange.backend.dto;

import lombok.*;

@Data
@AllArgsConstructor
public class UserResponse {

    private String email;
    private String role;

}