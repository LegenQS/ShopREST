package com.qs.shop.domain.response;


import com.qs.shop.domain.entity.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private String message;
    private User user;
}
