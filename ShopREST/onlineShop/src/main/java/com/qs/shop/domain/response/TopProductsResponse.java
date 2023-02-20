package com.qs.shop.domain.response;

import com.qs.shop.domain.support.TopProduct;
import lombok.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TopProductsResponse {
    private String message;
    private List<TopProduct> topProductList;
}
