package com.qs.shop.domain.support;

import lombok.*;
import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BriefOrderProduct {
    private Integer product_id;
    private Integer quantity;
    private Float exe_retail_price;
}
