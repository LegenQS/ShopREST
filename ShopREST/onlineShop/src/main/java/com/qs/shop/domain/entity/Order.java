package com.qs.shop.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name="`ORDER`")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", unique = true, nullable = false)
    private Integer order_id;

    @Column(name = "user_id")
    private Integer user_id;

    @Column(name = "order_status")
    private String status;

    @Column(name = "date_placed")
    private Timestamp date_placed;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order_id")
    @JsonIgnore
    @ToString.Exclude
    private List<OrderProduct> orderProductList;
}
