package com.qs.shop.dao;

import com.qs.shop.domain.support.BriefProduct;
import com.qs.shop.domain.entity.Product;
import com.qs.shop.domain.entity.User;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles(value = "test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserDaoTest {
    @Autowired
    UserDao userDao;

    @Autowired
    ProductDao productDao;

    SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Test
    @Transactional
    public void testSuccess_getUserByName() {
        User mockUser = User.builder()
                .user_id(1)
                .username("admin")
                .email("1@gmail.com")
                .password("admin")
                .build();
        userDao.createNewUser(mockUser);
        User user = userDao.getUserByName("admin");

        assertEquals(mockUser, user);
    }

    @Test
    @Transactional
    public void testFail_getUserByName() {
        User mockUser = User.builder()
                .user_id(1)
                .username("admin")
                .email("1@gmail.com")
                .password("admin")
                .build();
        userDao.createNewUser(mockUser);
        User user = userDao.getUserByName("user");

        assertNull(user);
    }

    @Test
    @Transactional
    public void testSuccess_updateWatchList() {
        User mockUser = User.builder()
                .user_id(1)
                .username("admin")
                .email("1@gmail.com")
                .password("admin")
                .build();

        Product product = Product.builder()
                .product_id(1)
                .quantity(1)
                .description("this is a test")
                .name("test product")
                .build();


        userDao.createNewUser(mockUser);
        productDao.createNewProduct(product);
        userDao.updateWatchList(mockUser.getUser_id(), product);
        List<BriefProduct> briefProductList = userDao.getProductsByName(mockUser.getUsername());
        List<BriefProduct> briefProducts = new ArrayList<>();
        briefProducts.add(new BriefProduct(product.getProduct_id(), product.getName()));

        assertEquals(briefProducts, briefProductList);
    }

    @Test
    @Transactional
    public void testSuccess_getUserByID() {
        User mockUser = User.builder()
                .user_id(1)
                .username("admin")
                .email("1@gmail.com")
                .password("admin")
                .build();

        userDao.createNewUser(mockUser);
        User user = userDao.getUserByID(mockUser.getUser_id());
        System.out.println(user);
        System.out.println(mockUser);

        assertEquals(user, mockUser);
    }

    @Test
    @Transactional
    public void testFail_getUserByID() {
        User mockUser = User.builder()
                .user_id(1)
                .username("admin")
                .email("1@gmail.com")
                .password("admin")
                .build();

        userDao.createNewUser(mockUser);
        User user = userDao.getUserByID(mockUser.getUser_id() + 1);

        assertNull(user);
    }

    @Test
    @Transactional
    public void testSuccess_deleteProductFromWatchListByName() {
        User mockUser = User.builder()
                .user_id(1)
                .username("user")
                .email("12@gmail.com")
                .password("user")
                .build();

        Product product1 = Product.builder()
                .product_id(1)
                .quantity(1)
                .name("test product 1")
                .description("this is a test")
                .build();

        Product product2 = Product.builder()
                .product_id(2)
                .quantity(1)
                .name("test product 2")
                .description("this is a test")
                .build();

        userDao.createNewUser(mockUser);
        productDao.createNewProduct(product1);
        productDao.createNewProduct(product2);
        userDao.updateWatchList(mockUser.getUser_id(), product1);
        userDao.updateWatchList(mockUser.getUser_id(), product2);
        userDao.deleteProductFromWatchListByName(mockUser.getUsername(), product2.getProduct_id());

        List<BriefProduct> briefProductList = userDao.getProductsByName(mockUser.getUsername());
        List<BriefProduct> briefProducts = new ArrayList<>();
        briefProducts.add(new BriefProduct(product1.getProduct_id(), product1.getName()));

        assertEquals(briefProducts, briefProductList);
    }
}
