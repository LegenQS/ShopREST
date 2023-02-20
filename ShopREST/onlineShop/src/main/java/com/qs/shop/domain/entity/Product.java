package com.qs.shop.domain.entity;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name="Product")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", unique = true, nullable = false)
    private Integer product_id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "retail_price", nullable = false)
    private Float retail_price;

    @Column(name = "wholesale_price")
    private Float wholesale_price;

    @Column(name = "stock_quantity")
    private Integer quantity;

    public Product(Integer product_id, String name) {
        this.product_id = product_id;
        this.name = name;
    }

    public Product(Integer product_id, String name, String description, Float retail_price) {
        this(product_id, name);
        this.description = description;
        this.retail_price = retail_price;
    }
}
