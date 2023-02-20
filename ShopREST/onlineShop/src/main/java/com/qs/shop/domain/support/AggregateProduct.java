package com.qs.shop.domain.support;

import lombok.*;
import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class AggregateProduct {
    private Integer product_id;
    private Long purchased_quantity;

    public AggregateProduct(Integer product_id) {
        this.product_id = product_id;
    }
}
