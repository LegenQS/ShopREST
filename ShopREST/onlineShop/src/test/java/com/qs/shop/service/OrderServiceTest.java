package com.qs.shop.service;

import com.qs.shop.dao.OrderDao;
import com.qs.shop.domain.entity.Order;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import com.qs.shop.dao.ProductDao;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
@ContextConfiguration
public class OrderServiceTest {
    @InjectMocks    // where the "fake" object will be used
    OrderService orderService;

    @Mock
    OrderDao orderDao;

    @Mock
    ProductDao productDao;

    @Test
    public void test_getOrderByID() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Order order = Order.builder()
                .order_id(1)
                .user_id(1)
                .orderProductList(null)
                .date_placed(now)
                .status("processing")
                .build();

        when(orderDao.getOrderByID(order.getOrder_id())).thenReturn(order);
        Order return_order = orderService.getOrderByID(order.getOrder_id());

        assertEquals(order, return_order);
    }

    @Test
    public void test_updateOrderStatus() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Order order = Order.builder()
                .order_id(1)
                .user_id(1)
                .orderProductList(null)
                .date_placed(now)
                .status("processing")
                .build();

        Order update_order = Order.builder()
                .order_id(1)
                .user_id(1)
                .orderProductList(null)
                .date_placed(now)
                .status("canceled")
                .build();

        when(orderDao.changeStatus(order.getOrder_id(), "canceled", productDao))
                .thenReturn(update_order);
        Order return_order= orderService.updateOrderStatus(update_order.getOrder_id(), "canceled");


        assertEquals(update_order, return_order);
    }
}
