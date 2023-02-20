package com.qs.shop.domain.support;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TopBuyer {
    private Integer user_id;
    private Double total_expense;
}
