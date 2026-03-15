package com.exchange.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "portfolio",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "stock_symbol"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String stockSymbol;

    private int quantity;
}