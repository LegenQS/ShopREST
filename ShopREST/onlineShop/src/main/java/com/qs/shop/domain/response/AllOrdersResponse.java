package com.qs.shop.domain.response;

import lombok.*;
import com.qs.shop.domain.entity.Order;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AllOrdersResponse {
    private String message;
    private List<Order> orders;
}
