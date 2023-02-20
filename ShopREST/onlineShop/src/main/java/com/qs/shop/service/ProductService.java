package com.qs.shop.service;

import com.qs.shop.domain.entity.Product;
import com.qs.shop.domain.support.TopProduct;
import com.qs.shop.exception.PriceMissMatchException;
import com.qs.shop.dao.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ProductService {
    private final ProductDao productDao;

    @Autowired
    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }


    @Transactional
    public Integer createNewProduct(Product product) {
        return productDao.createNewProduct(product);
    }

    @Transactional
    public List<Product> getAllProducts(boolean is_admin) {
        return productDao.getAllProducts(is_admin);
    }

    @Transactional
    public Product getProductByID(int product_id, boolean is_admin) {
        return productDao.getProductByID(product_id, is_admin);
    }

    @Transactional
    public Product updateProduct(Product product) throws PriceMissMatchException {
        return productDao.updateProduct(product);
    }

    @Transactional
    public List<TopProduct> getProductByProfit(int number) {
        return productDao.getProductByProfit(number);
    }
}
