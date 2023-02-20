package com.qs.shop.service;

import com.qs.shop.domain.entity.Product;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import com.qs.shop.dao.ProductDao;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
@ContextConfiguration
public class ProductServiceTest {
    @InjectMocks    // where the "fake" object will be used
    ProductService productService;

    @Mock
    ProductDao productDao;

    @Test
    public void test_createNewProduct() {
        Product product = Product.builder()
                .product_id(1)
                .name("test product")
                .description("this is test product")
                .build();

        when(productDao.createNewProduct(product)).thenReturn(product.getProduct_id());

        int product_id = productService.createNewProduct(product);

        assertEquals(product_id, 1);
    }

    @Test
    public void test_updateProduct() {
        Product product = Product.builder()
                .product_id(1)
                .name("test product")
                .description("this is test product")
                .build();

        Product update_product = Product.builder()
                .product_id(1)
                .name("test product 1")
                .description("this is test product 1")
                .build();

        when(productDao.updateProduct(update_product)).thenReturn(update_product);
        productService.createNewProduct(product);
        Product return_product = productService.updateProduct(update_product);

        assertEquals(return_product, update_product);
    }

    @Test
    public void test_getProductByID() {
        Product product = Product.builder()
                .product_id(1)
                .name("test product")
                .description("this is test product")
                .build();

        when(productDao.getProductByID(product.getProduct_id(), true)).
                thenReturn(product);
        Product return_product = productService.getProductByID(product.getProduct_id(), true);

        assertEquals(product, return_product);
    }
}
