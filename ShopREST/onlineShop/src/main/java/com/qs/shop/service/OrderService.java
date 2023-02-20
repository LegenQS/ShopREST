package com.qs.shop.service;

import com.qs.shop.domain.entity.*;
import com.qs.shop.domain.support.AggregateProduct;
import com.qs.shop.domain.support.OrderList;
import com.qs.shop.domain.support.SoldProduct;
import com.qs.shop.domain.support.TopBuyer;
import com.qs.shop.dao.OrderDao;
import com.qs.shop.dao.OrderProductDao;
import com.qs.shop.dao.ProductDao;
import com.qs.shop.exception.ProductNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qs.shop.domain.response.OrderProductResponse;
import com.qs.shop.domain.response.OrderResponse;
import com.qs.shop.exception.NotEnoughInventoryException;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class OrderService {
    private final OrderDao orderDao;
    private final OrderProductDao orderProductDao;
    private final ProductDao productDao;

    @Autowired
    public OrderService(OrderDao orderDao, OrderProductDao orderProductDao, ProductDao productDao) {
        this.orderDao = orderDao;
        this.orderProductDao = orderProductDao;
        this.productDao = productDao;
    }

    @Transactional
    public List<Order> getAllOrders() {
        return orderDao.getAllOrders();
    }

    @Transactional
    public Order updateOrderStatus(int order_id, String status) {
        return orderDao.changeStatus(order_id, status, productDao);
    }

    @Transactional(rollbackOn = {NotEnoughInventoryException.class, ProductNotExistException.class})
    public void createOrder(OrderList orderList, int user_id)
            throws NotEnoughInventoryException, ProductNotExistException {
        int order_id = orderDao.createNewOrder(user_id);

        System.out.println(order_id);
        if (order_id < 0)
            throw new NotEnoughInventoryException("Order created error");

        orderProductDao.createOrderProduct(order_id, orderList, productDao);
    }

    @Transactional
    public Order getOrderByID(int order_id) {
        return orderDao.getOrderByID(order_id);
    }

    @Transactional
    public Order getOrderByIDWithProductList(int order_id, OrderProductResponse orderProductResponse) {
        return orderDao.getOrderByIDWithProductList(order_id, orderProductResponse);
    }

    @Transactional
    public OrderResponse getOrderByIDWithProductList(int order_id) {
        return orderDao.getOrderByIDWithProductList(order_id);
    }

    @Transactional
    public List<Order> getOrdersByUserID(int user_id) {
        return orderDao.getOrdersByUserID(user_id);
    }

    @Transactional
    public List<AggregateProduct> getTopProductsByUserID(int user_id, int number){
        return orderDao.getTopProductsByUserID(user_id, number);
    }

    @Transactional
    public List<AggregateProduct> getTopProducts(int number){
        return orderDao.getTopProducts(number);
    }

    @Transactional
    public List<AggregateProduct> getRecentProductsByUserID(int user_id, int number) {
        return orderDao.getRecentProductsByUserID(user_id, number);
    }

    @Transactional
    public List<TopBuyer> getUsersByCost(int number) {
        return orderDao.getUsersByCost(number);
    }

    @Transactional
    public List<SoldProduct> getProductSold() {
        return orderProductDao.getProductSold();
    }
}
