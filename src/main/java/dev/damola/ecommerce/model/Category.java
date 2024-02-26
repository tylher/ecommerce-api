package dev.damola.ecommerce.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String categoryId ;

    private String categoryName;

    @OneToMany(mappedBy = "category")
    private List<Product> productList;
}
