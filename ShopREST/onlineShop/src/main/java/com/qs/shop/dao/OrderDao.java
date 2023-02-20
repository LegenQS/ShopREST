package com.qs.shop.dao;

import com.qs.shop.domain.entity.*;
import com.qs.shop.domain.response.OrderProductResponse;
import com.qs.shop.domain.response.OrderResponse;
import com.qs.shop.domain.support.AggregateProduct;
import com.qs.shop.domain.support.BriefOrderProduct;
import com.qs.shop.domain.support.TopBuyer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.persistence.Query;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderDao {
    @Autowired
    SessionFactory sessionFactory;

    public List<Order> getAllOrders() {
        Session session = null;
        List<Order> orders = null;
        try{
            session = sessionFactory.getCurrentSession();
            Query query = session.createQuery("from Order");
            orders = query.getResultList();
        } catch (Exception e){
            e.printStackTrace();
        }

        return (orders == null || orders.isEmpty()) ? null : orders;
    }

    public Order getOrderByID(int order_id) {
        Session session = null;
        List<Order> orders = null;
        try{
            session = sessionFactory.getCurrentSession();
            Query query = session.createQuery("from Order o where o.order_id = :order_id");
            query.setParameter("order_id", order_id);
            orders = query.getResultList();
        } catch (Exception e){
            e.printStackTrace();
        }

        return (orders == null || orders.isEmpty()) ? null : orders.get(0);
    }

    public Order getOrderByIDWithProductList(int order_id, OrderProductResponse briefProductResponse) {
        Session session;
        List<Order> orders = null;
        try{
            session = sessionFactory.getCurrentSession();
            Query query = session.createQuery("from Order o where o.order_id = :order_id");
            query.setParameter("order_id", order_id);
            orders = query.getResultList();
        } catch (Exception e){
            e.printStackTrace();
        }

        if (orders == null || orders.isEmpty()) return null;

        Order order = orders.get(0);
        if (order.getOrderProductList() == null)
            return order;

        List<BriefOrderProduct> briefOrderProducts = new ArrayList<>();
        for (OrderProduct orderProduct : order.getOrderProductList()) {
            briefOrderProducts.add(new BriefOrderProduct(
                    orderProduct.getProduct_id(), orderProduct.getQuantity(), orderProduct.getExe_retail_price()
            ));
        }
        briefProductResponse.setBriefOrderProducts(briefOrderProducts);

        return order;
    }

    public OrderResponse getOrderByIDWithProductList(int order_id) {
        Session session;
        List<Order> orders = null;
        try{
            session = sessionFactory.getCurrentSession();
            Query query = session.createQuery("from Order o where o.order_id = :order_id");
            query.setParameter("order_id", order_id);
            orders = query.getResultList();
        } catch (Exception e){
            e.printStackTrace();
        }

        if (orders == null || orders.isEmpty())
            return OrderResponse.builder()
                    .message("no such order!")
                    .build();

        Order order = orders.get(0);
        if (order.getOrderProductList() != null)
            order.getOrderProductList().isEmpty();

        return OrderResponse.builder()
                .message("return the order detail")
                .order(order)
                .orderProductList(order.getOrderProductList())
                .build();
    }

    public List<Order> getOrdersByUserID(int user_id) {
        Session session = null;
        List<Order> orders = null;
        try{
            session = sessionFactory.getCurrentSession();
            Query query = session.createQuery("from Order o where o.user_id = :user_id");
            query.setParameter("user_id", user_id);
            orders = query.getResultList();
        } catch (Exception e){
            e.printStackTrace();
        }

        return orders;
    }

    public Order changeStatus(int order_id, String status, ProductDao productDao) {
        Session session = null;
        Order order = null;
        try {
            session = sessionFactory.getCurrentSession();
            order = getOrderByID(order_id);
            if (order.getStatus().equals("completed")) {
                return order;
            }
            order.setStatus(status);
            session.update(order);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (order == null) return order;

        if (status.equals("canceled")) {
            // revert all purchased quantity products back to quantity
            // TBD
            for (OrderProduct orderProduct:  order.getOrderProductList()) {
                Product p = productDao.getProductByID(orderProduct.getProduct_id(), true);
                p.setQuantity(p.getQuantity() + orderProduct.getQuantity());
            }
        }

        return order;
    }

    public int createNewOrder(int user_id){
        Order order = new Order();

        Integer order_id = -1;
        try {
            Session session = sessionFactory.getCurrentSession();
            order.setStatus("processing");
            order.setDate_placed(new Timestamp(System.currentTimeMillis()));
            order.setUser_id(user_id);
            order_id = (Integer) session.save(order);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return order_id;
    }

    public List<AggregateProduct> getTopProductsByUserID(int user_id, int number) {
        List<AggregateProduct> topProducts = new ArrayList<>();
        try {
            Session session = sessionFactory.getCurrentSession();
            Query query = session.createQuery(
                    "select new com.qs.shop.domain.support" +
                            ".AggregateProduct(op.product_id, SUM(op.quantity))" +
                            " from Order o left join fetch OrderProduct op" +
                            " on o.order_id = op.order_id " +
                            " where o.user_id = :user_id and o.status = 'completed'" +
                            " group by op.product_id " +
                            " order by SUM(op.quantity) DESC "
            );
            query.setParameter("user_id", user_id);
            query.setMaxResults(number);

            topProducts = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return topProducts;
    }

    public List<AggregateProduct> getTopProducts(int number) {
        List<AggregateProduct> topProducts = new ArrayList<>();
        try {
            Session session = sessionFactory.getCurrentSession();
            Query query = session.createQuery(
                    "select new com.qs.shop.domain.support" +
                            ".AggregateProduct(op.product_id, SUM(op.quantity))" +
                            " from Order o left join fetch OrderProduct op" +
                            " on o.order_id = op.order_id " +
                            " where o.status = 'completed'" +
                            " group by op.product_id " +
                            " order by SUM(op.quantity) DESC "
            );
            query.setMaxResults(number);

            topProducts = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return topProducts;
    }

    public List<AggregateProduct> getRecentProductsByUserID(int user_id, int number) {
        List<AggregateProduct> recentProducts = new ArrayList<>();
        try {
            Session session = sessionFactory.getCurrentSession();
            Query query = session.createQuery(
                    "select new com.qs.shop.domain.support" +
                            ".AggregateProduct(op.product_id)" +
                            " from Order o left join fetch OrderProduct op" +
                            " on o.order_id = op.order_id " +
                            " where o.user_id = :user_id and o.status = 'completed'" +
                            " order by o.date_placed DESC"
            );
            query.setParameter("user_id", user_id);
            query.setMaxResults(number);

            recentProducts = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return recentProducts;
    }

    public List<TopBuyer> getUsersByCost(int number) {
        List<TopBuyer> topBuyers = null;

        try {
            Session session = sessionFactory.getCurrentSession();
            Query query = session.createQuery(
                    "select new com.qs.shop.domain.support.TopBuyer(o.user_id, SUM(op.exe_retail_price))" +
                            " from Order o join fetch OrderProduct op" +
                            " on o.order_id = op.order_id" +
                            " where o.status = 'completed'" +
                            " group by o.user_id" +
                            " order by SUM(op.exe_retail_price) DESC"
            );
            query.setMaxResults(number);

            topBuyers = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return topBuyers;
    }
}
