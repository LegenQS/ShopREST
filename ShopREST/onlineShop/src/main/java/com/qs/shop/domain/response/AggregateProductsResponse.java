package com.qs.shop.domain.response;


import com.qs.shop.domain.support.AggregateProduct;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class AggregateProductsResponse {
    private String message;
    private List<AggregateProduct> aggregateProducts;
}
