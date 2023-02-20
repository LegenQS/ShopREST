package com.qs.shop.domain.response;


import com.qs.shop.domain.support.SoldProduct;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SoldProductsResponse {
    private String message;
    private List<SoldProduct> soldProductList;
}
