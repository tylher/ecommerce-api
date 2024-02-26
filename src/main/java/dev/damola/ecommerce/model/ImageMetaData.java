package dev.damola.ecommerce.model;

import jakarta.persistence.*;


@Entity
public class ImageMetaData {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String imageId;

    private String fileName;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;


}
