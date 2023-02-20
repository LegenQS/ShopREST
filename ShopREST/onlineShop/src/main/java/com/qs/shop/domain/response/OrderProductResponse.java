package com.qs.shop.domain.response;

import com.qs.shop.domain.support.BriefOrderProduct;
import com.qs.shop.domain.entity.Order;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderProductResponse {
    private String message;
    private Order order;
    private List<BriefOrderProduct> briefOrderProducts;
}
