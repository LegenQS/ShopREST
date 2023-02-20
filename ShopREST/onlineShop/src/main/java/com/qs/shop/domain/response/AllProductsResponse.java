package com.qs.shop.domain.response;

import com.qs.shop.domain.entity.Product;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AllProductsResponse {
    private String message;
    private List<Product> products;
}
