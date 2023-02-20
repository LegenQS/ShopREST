package com.qs.shop.domain.response;


import com.qs.shop.domain.support.TopBuyer;
import lombok.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AllUsersResponse {
    private String message;
    private List<TopBuyer> topBuyers;
}
