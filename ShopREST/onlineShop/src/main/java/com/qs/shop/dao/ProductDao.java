package com.qs.shop.dao;

import com.qs.shop.domain.entity.Product;
import com.qs.shop.domain.support.TopProduct;
import com.qs.shop.exception.PriceMissMatchException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductDao {
    @Autowired
    SessionFactory sessionFactory;

    public Integer createNewProduct(Product product) {
        Session session;
        Integer seq = 0;

        try{
            session = sessionFactory.getCurrentSession();
            seq = (Integer) session.save(product);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return seq;
    }

    public List<Product> getAllProducts(boolean is_admin) {
        Session session = null;
        List<Product> products = null;
        try{
            session = sessionFactory.getCurrentSession();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Product> cq = cb.createQuery(Product.class);
            Root<Product> root = cq.from(Product.class);
            Predicate predicate = cb.greaterThan(root.get("quantity"), 0);
            if (is_admin)
                cq.multiselect(root.get("product_id"), root.get("name"));
            else
                cq.multiselect(root.get("product_id"), root.get("name")).where(predicate);
            products = session.createQuery(cq).getResultList();
        }catch (Exception e){
            e.printStackTrace();
        }
        return (products == null || products.isEmpty()) ? null : products;
    }

    public Product getProductByID(int product_id, boolean is_admin) {
        Session session;
        Optional<Product> product = null;
        try{
            session = sessionFactory.getCurrentSession();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Product> cq = cb.createQuery(Product.class);
            Root<Product> root = cq.from(Product.class);
            Predicate predicate = cb.equal(root.get("product_id"), product_id);

            if (is_admin)
                cq.select(root).where(predicate);
            else
                cq.multiselect(root.get("product_id"), root.get("name"),
                    root.get("description"), root.get("retail_price")).where(predicate);

            product = session.createQuery(cq).uniqueResultOptional();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return (product == null || !product.isPresent()) ? null : product.get();
    }

    public Product updateProduct(Product product) throws PriceMissMatchException {
        Session session = sessionFactory.getCurrentSession();
        Product ori_product = getProductByID(product.getProduct_id(), true);
        if (ori_product == null)
            return null;

        if (product.getQuantity() != null && product.getQuantity() > 0) {
            ori_product.setQuantity(ori_product.getQuantity() + product.getQuantity());
        }
        if (product.getName() != null && !product.getName().trim().isEmpty()) {
            ori_product.setName(product.getName());
        }
        if (product.getRetail_price() != null && product.getRetail_price() > 0) {
            if (product.getRetail_price() < ori_product.getWholesale_price())
                throw new PriceMissMatchException("retail price cannot be lower than wholesale price!");
            ori_product.setRetail_price(product.getRetail_price());
        }
        if (product.getWholesale_price() != null && product.getWholesale_price() > 0) {
            if (product.getRetail_price() < product.getWholesale_price())
                throw new PriceMissMatchException("wholesale price cannot be greater than wholesale price!");
            ori_product.setWholesale_price(product.getWholesale_price());
        }
        if (product.getDescription() != null && !product.getDescription().trim().isEmpty()) {
            ori_product.setDescription(product.getDescription());
        }
        session.update(ori_product);
        System.out.println(ori_product);
        return ori_product;
    }

    public List<TopProduct> getProductByProfit(int number) {
        List<TopProduct> topProductList = null;

        try {
            Session session = sessionFactory.getCurrentSession();
            Query query = session.createQuery(
                    "select new com.qs.shop.domain.support.TopProduct(op.product_id, " +
                            " SUM(op.exe_retail_price - op.exe_wholesale_price))" +
                            " from Order o join fetch OrderProduct op" +
                            " on o.order_id = op.order_id" +
                            " where o.status = 'completed'" +
                            " group by op.product_id" +
                            " order by (SUM(op.exe_retail_price) - SUM(op.exe_wholesale_price)) DESC"
            );
            query.setMaxResults(number);

            topProductList = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return topProductList;
    }
}