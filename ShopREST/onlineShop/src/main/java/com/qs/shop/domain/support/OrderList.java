package com.qs.shop.domain.support;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderList {
    private List<Integer> product_id;
    private List<Integer> quantity;
}
