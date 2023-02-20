package com.qs.shop.domain.support;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TopProduct {
    private Integer product_id;
    private Double profit;

    public TopProduct(Integer product_id, Long profit) {
        this.product_id = product_id;
        this.profit = profit.doubleValue();
    }
}
