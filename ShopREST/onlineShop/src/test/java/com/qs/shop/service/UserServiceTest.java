package com.qs.shop.service;

import com.qs.shop.dao.UserDao;
import com.qs.shop.domain.entity.Product;
import com.qs.shop.domain.entity.User;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
@ContextConfiguration
public class UserServiceTest {
    @InjectMocks    // where the "fake" object will be used
    UserService userService;

    @Mock
    UserDao userDao;

    @Test
    public void test_getUserByName() {
        User user = User.builder()
                .user_id(1)
                .username("user")
                .password("user")
                .email("1")
                .build();

        when(userDao.getUserByName(user.getUsername())).thenReturn(user);

        User act_user = userService.getUserByName(user.getUsername());
        assertEquals(user, act_user);
    }

    @Test
    public void test_UpdateWatchList() {
        User user = User.builder()
                .user_id(1)
                .username("user")
                .password("user")
                .email("1")
                .build();

        Product product = Product.builder()
                .product_id(1)
                .name("test product")
                .description("this is test product")
                .build();

        userService.updateWatchList(user.getUser_id(), product);
        Mockito.verify(userDao, times(1))
                .updateWatchList(user.getUser_id(), product);

    }

    @Test
    public void test_deleteProductFromWatchListByName() {
        User user = User.builder()
                .user_id(1)
                .username("user")
                .password("user")
                .email("1")
                .build();

        Product product1 = Product.builder()
                .product_id(1)
                .name("test product 1")
                .description("this is test product")
                .build();

        userService.updateWatchList(user.getUser_id(), product1);
        userService.deleteProductFromWatchListByName(user.getUsername(), product1.getProduct_id());

        assertFalse(userDao.deleteProductFromWatchListByName(
                user.getUsername(), product1.getProduct_id()));
    }

    @Test
    public void test_getProductsByName(){
        User user = User.builder()
                .user_id(1)
                .username("user")
                .password("user")
                .email("1")
                .build();

        userService.getProductsByName(user.getUsername());
        Mockito.verify(userDao, times(1))
                .getProductsByName(user.getUsername());
    }
}
