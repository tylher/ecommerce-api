package dev.damola.ecommerce.model;

import dev.damola.ecommerce.enums.OrderStatus;
import jakarta.persistence.*;

@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String orderId;

    private OrderStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}
