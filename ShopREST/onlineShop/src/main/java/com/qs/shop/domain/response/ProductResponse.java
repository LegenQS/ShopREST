package com.qs.shop.domain.response;

import com.qs.shop.domain.entity.Product;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ProductResponse {
    private String message;
    private Product product;

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;

        if (!(o instanceof ProductResponse)) return false;

        ProductResponse productResponse = (ProductResponse) o;
        if (productResponse.product.equals(this.product) && productResponse.message.equals(this.message))
            return true;
        return false;
    }
}
