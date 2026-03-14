package com.exchange.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import jakarta.persistence.GeneratedValue;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String action;

    private String details;

    private LocalDateTime timestamp;
}