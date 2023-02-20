package com.qs.shop.domain.support;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SoldProduct {
    private Integer user_id;
    private Long total_sold;
}
