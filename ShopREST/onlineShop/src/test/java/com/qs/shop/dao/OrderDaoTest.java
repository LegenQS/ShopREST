package com.qs.shop.dao;

import com.qs.shop.domain.entity.*;
import com.qs.shop.domain.response.OrderProductResponse;
import com.qs.shop.domain.response.OrderResponse;
import com.qs.shop.domain.support.OrderList;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles(value = "test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderDaoTest {
    @Autowired
    OrderDao orderDao;

    @Autowired
    ProductDao productDao;

    @Autowired
    UserDao userDao;

    @Autowired
    OrderProductDao orderProductDao;

    SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Test
    @Transactional
    public void testSuccess_createOrder() {
        User mockUser = User.builder()
                .user_id(1)
                .username("admin")
                .email("1@gmail.com")
                .password("admin")
                .build();

        userDao.createNewUser(mockUser);
        Integer order_id = orderDao.createNewOrder(mockUser.getUser_id());
        assertNotNull(order_id);
    }

    @Test
    @Transactional
    public void testSuccess_getOrderDetailByID() {
        User mockUser = User.builder()
                .user_id(1)
                .username("admin")
                .email("1@gmail.com")
                .password("admin")
                .build();

        Product product = Product.builder()
                .product_id(1)
                .quantity(5)
                .name("test product")
                .description("this is test product")
                .retail_price(100f)
                .wholesale_price(50f)
                .build();

        OrderList orderList = OrderList.builder()
                .quantity(Arrays.asList(2))
                .build();

        OrderProduct orderProduct = OrderProduct.builder()
                .order_product_id(1)
                .exe_retail_price(100f)
                .quantity(2)
                .exe_wholesale_price(50f)
                .build();

        userDao.createNewUser(mockUser);
        productDao.createNewProduct(product);
        int order_id = orderDao.createNewOrder(mockUser.getUser_id());
        Product p = productDao.getProductByID(product.getProduct_id(), true);

        orderList.setProduct_id(Arrays.asList(p.getProduct_id()));
        orderProduct.setProduct_id(p.getProduct_id());

        orderProductDao.createOrderProduct(order_id, orderList, productDao);
        OrderResponse orderResponse = orderDao.getOrderByIDWithProductList(order_id);

        OrderProductResponse orderProductResponse = new OrderProductResponse();
        Order order = orderDao.getOrderByIDWithProductList(order_id, orderProductResponse);

        List<OrderProduct> orderProductList = orderProductDao.getOrderProductByID(order_id);

        orderProduct.setOrder_id(order_id);
        assertEquals(orderResponse.getOrder(), order);
        assertEquals(orderProduct, orderProductList.get(0));
    }

    @Test
    @Transactional
    public void testSuccess_getOrderByID() {
        User mockUser = User.builder()
                .user_id(1)
                .username("admin")
                .email("1@gmail.com")
                .password("admin")
                .build();

        userDao.createNewUser(mockUser);
        Integer order_id = orderDao.createNewOrder(mockUser.getUser_id());

        Order order = orderDao.getOrderByID(order_id);
        assertNotNull(order);
    }

    @Test
    @Transactional
    public void testFail_getOrderByID() {
        User mockUser = User.builder()
                .user_id(1)
                .username("admin")
                .email("1@gmail.com")
                .password("admin")
                .build();

        userDao.createNewUser(mockUser);
        Integer order_id = orderDao.createNewOrder(mockUser.getUser_id());

        Order order = orderDao.getOrderByID(order_id + 1);
        assertNull(order);
    }

    @Test
    @Transactional
    public void testSuccess_changeOrderStatus() {
        User mockUser = User.builder()
                .user_id(1)
                .username("admin")
                .email("1@gmail.com")
                .password("admin")
                .build();

        userDao.createNewUser(mockUser);
        Integer order_id = orderDao.createNewOrder(mockUser.getUser_id());
        Order order = orderDao.changeStatus(order_id, "completed", productDao);

        assertEquals(order.getStatus(), "completed");
    }
}
