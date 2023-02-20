package com.qs.shop.domain.support;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@EqualsAndHashCode
public class BriefProduct {
    private Integer product_id;
    private String name;
}
