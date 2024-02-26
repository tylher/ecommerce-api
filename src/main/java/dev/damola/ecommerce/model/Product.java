package dev.damola.ecommerce.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String productId;

    private Category category;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private double price;

    private int stockQuantity;

    @OneToMany(mappedBy = "product",fetch = FetchType.LAZY)
    private List<ImageMetaData> images;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
