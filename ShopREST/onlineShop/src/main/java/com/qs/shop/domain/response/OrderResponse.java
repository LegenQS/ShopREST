package com.qs.shop.domain.response;

import lombok.*;
import com.qs.shop.domain.entity.Order;
import com.qs.shop.domain.entity.OrderProduct;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class OrderResponse {
    private String message;
    private Order order;
    private List<OrderProduct> orderProductList;
}
