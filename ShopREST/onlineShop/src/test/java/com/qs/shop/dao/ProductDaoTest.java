package com.qs.shop.dao;

import com.qs.shop.domain.entity.Product;
import org.hibernate.SessionFactory;
import org.junit.Test;
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
public class ProductDaoTest {
    @Autowired
    ProductDao productDao;

    SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Test
    @Transactional
    public void testSuccess_createNewProduct() {
        Product product = Product.builder()
                .product_id(1)
                .name("test product")
                .description("this is a test")
                .quantity(1)
                .retail_price(100f)
                .wholesale_price(50f)
                .build();

        Integer product_id = productDao.createNewProduct(product);

        assertNotNull(product_id);
    }

    @Test
    @Transactional
    public void testSuccess_getAllProducts() {
        Product product1 = Product.builder()
                .product_id(1)
                .name("test product 1")
                .description("this is test product 1")
                .quantity(1)
                .retail_price(100f)
                .wholesale_price(50f)
                .build();

        Product product2 = Product.builder()
                .product_id(2)
                .name("test product 2")
                .description("this is test product 2")
                .quantity(1)
                .retail_price(100f)
                .wholesale_price(50f)
                .build();

        productDao.createNewProduct(product1);
        productDao.createNewProduct(product2);

        List<Product> products = productDao.getAllProducts(true);
        List<Product> products1 = Arrays.asList(product1, product2);

        System.out.println(products);
        System.out.println(products1);

        for (int i = 0; i < products1.size(); i++)
            assertEquals(products.get(i).getName(), products1.get(i).getName());
    }

    @Test
    @Transactional
    public void testSuccess_getProductByID() {
        Product product = Product.builder()
                .product_id(1)
                .quantity(1)
                .name("test product")
                .description("this is test product")
                .build();

        productDao.createNewProduct(product);

        Product product1 = productDao.getProductByID(product.getProduct_id(), true);

        assertEquals(product1, product);
    }

    @Test
    @Transactional
    public void testFail_getProductByID() {
        Product product = Product.builder()
                .product_id(1)
                .quantity(1)
                .name("test product")
                .description("this is test product")
                .build();

        productDao.createNewProduct(product);

        Product product1 = productDao.getProductByID(product.getProduct_id() + 1, true);

        assertNull(product1);
    }

    @Test
    @Transactional
    public void testSuccess_UpdateProduct() {
        Product product = Product.builder()
                .quantity(1)
                .name("test product")
                .description("this is test product")
                .build();

        Product update_product = Product.builder()
                .name("test product change")
                .description("this is test product change")
                .build();

        Product newly_product = Product.builder()
                .quantity(1)
                .name("test product change")
                .description("this is test product change")
                .build();

        int product_id = productDao.createNewProduct(product);
        update_product.setProduct_id(product_id);
        Product return_product = productDao.updateProduct(update_product);
        newly_product.setProduct_id(product_id);

        assertEquals(newly_product, return_product);
    }

    @Test
    @Transactional
    public void testFail_UpdateProduct() {
        Product product = Product.builder()
                .product_id(1)
                .quantity(1)
                .name("test product")
                .description("this is test product")
                .build();

        Product newly_product = Product.builder()
                .product_id(2)
                .quantity(1)
                .name("test product change")
                .description("this is test product change")
                .build();

        productDao.createNewProduct(product);
        Product update_product = productDao.updateProduct(newly_product);


        assertNull(update_product);
    }
}
