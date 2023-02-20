package com.qs.shop.dao;

import com.qs.shop.domain.support.OrderList;
import com.qs.shop.domain.entity.OrderProduct;
import com.qs.shop.domain.entity.Product;
import com.qs.shop.domain.support.SoldProduct;
import com.qs.shop.exception.NotEnoughInventoryException;
import com.qs.shop.exception.ProductNotExistException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
public class OrderProductDao {
    @Autowired
    SessionFactory sessionFactory;

    public void createOrderProduct(int order_id, OrderList orderList,
                                   ProductDao productDao) throws NotEnoughInventoryException, ProductNotExistException
    {
        List<Integer> product_id = orderList.getProduct_id();
        List<Integer> quantity = orderList.getQuantity();
        int num = quantity.size();
        boolean update = false;

        Session session = sessionFactory.getCurrentSession();
        for (int i = 0; i < num; i++) {
            Product p = productDao.getProductByID(product_id.get(i), true);
            if (p == null)
                throw new ProductNotExistException("Product provided with ID: " + product_id.get(i) + " does not exist!");

            if (p.getQuantity() < quantity.get(i))
                throw new NotEnoughInventoryException("Not enough product for product " + p.getProduct_id());

            if (quantity.get(i) == 0)
                continue;

            OrderProduct orderProduct = new OrderProduct();
            session.save(orderProduct);
            orderProduct.setProduct_id(p.getProduct_id());
            orderProduct.setOrder_id(order_id);
            orderProduct.setQuantity(quantity.get(i));
            orderProduct.setExe_retail_price(p.getRetail_price());
            orderProduct.setExe_wholesale_price(p.getWholesale_price());
            p.setQuantity(p.getQuantity() - quantity.get(i));
            update = true;
        }

        if (!update) throw new NotEnoughInventoryException("No valid product quantity within the order");

    }

    public List<OrderProduct> getOrderProductByID(int order_id) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from OrderProduct op " +
                "where op.order_id = :order_id");
        query.setParameter("order_id", order_id);
        return query.getResultList();
    }

    public List<SoldProduct> getProductSold() {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(
                " select new com.qs.shop.domain.support.SoldProduct(op.product_id, SUM(op.quantity)) " +
                " from Order o left join fetch OrderProduct op " +
                " on o.order_id = op.order_id" +
                " where o.status = 'completed'" +
                " group by op.product_id " +
                " order by SUM(op.quantity) DESC");

        return query.getResultList();
    }
}
