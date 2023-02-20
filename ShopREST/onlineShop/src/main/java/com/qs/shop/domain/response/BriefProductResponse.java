package com.qs.shop.domain.response;

import com.qs.shop.domain.support.BriefProduct;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BriefProductResponse {
    private String message;
    private List<BriefProduct> products;
}