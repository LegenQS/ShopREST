package com.qs.shop.domain.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="Order_Product")
@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_product_id", unique = true, nullable = false)
    private Integer order_product_id;

    @Column(name = "order_id")
    private Integer order_id;

    @Column(name = "product_id")
    private Integer product_id;

    @Column(name = "purchased_quantity")
    private Integer quantity;

    @Column(name = "execution_retail_price")
    private Float exe_retail_price;

    @Column(name = "execution_wholesale_price")
    private Float exe_wholesale_price;
}
