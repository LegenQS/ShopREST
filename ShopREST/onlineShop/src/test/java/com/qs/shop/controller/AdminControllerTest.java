package com.qs.shop.controller;

import com.google.gson.Gson;
import com.qs.shop.domain.entity.Product;
import com.qs.shop.domain.response.*;
import com.qs.shop.service.OrderService;
import com.qs.shop.service.ProductService;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
@RunWith(SpringRunner.class)
public class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean       // this annotation is provided by Spring Boot
    private OrderService orderService;

    @MockBean
    private ProductService productService;

    @BeforeEach
    public void init() {
        System.out.println("------ Test Start ------");
    }

    @AfterEach
    public void end() {
        System.out.println("------ Test End ------");
    }

    @Test
    public void testSuccess_AddProductToList() throws Exception {
        Product mockProduct = Product.builder()
                .product_id(0)
                .name("test product")
                .description("test description")
                .retail_price(100f)
                .wholesale_price(50f)
                .quantity(10)
                .build();

        ProductResponse mockProductResponse = ProductResponse.builder()
                .message("Product with product_id: " + mockProduct.getProduct_id() + " added successfully!")
                .product(mockProduct)
                .build();

        JSONObject json = new JSONObject();
        json.put("name", mockProduct.getName());
        json.put("product_id", mockProduct.getProduct_id());
        json.put("description", mockProduct.getDescription());
        json.put("retail_price", mockProduct.getRetail_price());
        json.put("wholesale_price", mockProduct.getWholesale_price());
        json.put("quantity", mockProduct.getQuantity());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/admin/add_product")
                .content(json.toString())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ProductResponse productResponse = new Gson().fromJson(result.getResponse()
                .getContentAsString(), ProductResponse.class);

//        System.out.println(productResponse);
        assertEquals(mockProductResponse, productResponse);
    }

    @Test
    public void testFail_AddProductToList() throws Exception {
        Product mockProduct = Product.builder()
                .product_id(0)
                .description("test description")
                .retail_price(100f)
                .wholesale_price(50f)
                .quantity(10)
                .build();

        ProductResponse mockProductResponse = ProductResponse.builder()
                .message("Your input contains either null/empty/invalid values!")
                .build();

        JSONObject json = new JSONObject();
        json.put("product_id", mockProduct.getProduct_id());
        json.put("description", mockProduct.getDescription());
        json.put("retail_price", mockProduct.getRetail_price());
        json.put("wholesale_price", mockProduct.getWholesale_price());
        json.put("quantity", mockProduct.getQuantity());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/admin/add_product")
                        .content(json.toString())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ProductResponse productResponse = new Gson().fromJson(result.getResponse()
                .getContentAsString(), ProductResponse.class);

        assertEquals(mockProductResponse.getMessage(), productResponse.getMessage());
    }

    @Test
    @Disabled
    public void testSuccess_UpdateProduct() throws Exception {
        testSuccess_AddProductToList();
        Product mockProduct = Product.builder()
                .product_id(0)
                .name("test product change")
                .description("test description change")
                .retail_price(80f)
                .wholesale_price(50f)
                .quantity(3)
                .build();

        ProductResponse mockProductResponse = ProductResponse.builder()
                .message("Product with product_id: " + mockProduct.getProduct_id() + " does not exist!")
                .build();

        JSONObject json = new JSONObject();
        json.put("product_id", mockProduct.getProduct_id());
        json.put("name", mockProduct.getName());
        json.put("description", mockProduct.getDescription());
        json.put("retail_price", mockProduct.getRetail_price());
        json.put("wholesale_price", mockProduct.getWholesale_price());
        json.put("quantity", mockProduct.getQuantity());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/admin/update_product")
                        .content(json.toString())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ProductResponse productResponse = new Gson().fromJson(result.getResponse()
                .getContentAsString(), ProductResponse.class);

        System.out.println(productResponse);
        System.out.println(mockProduct);
        assertEquals(mockProductResponse.getMessage(), productResponse.getMessage());
    }

    @Test
    public void testFail_UpdateProduct() throws Exception {
        Product mockProduct = Product.builder()
                .product_id(1)
                .name("test product")
                .description("test description")
                .retail_price(100f)
                .wholesale_price(50f)
                .quantity(10)
                .build();

        ProductResponse mockProductResponse = ProductResponse.builder()
                .message("Product with product_id: " + mockProduct.getProduct_id() + " does not exist!")
                .build();

        JSONObject json = new JSONObject();
        json.put("product_id", mockProduct.getProduct_id());
        json.put("name", mockProduct.getName());
        json.put("description", mockProduct.getDescription());
        json.put("retail_price", mockProduct.getRetail_price());
        json.put("wholesale_price", mockProduct.getWholesale_price());
        json.put("quantity", mockProduct.getQuantity());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/admin/update_product")
                        .content(json.toString())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ProductResponse productResponse = new Gson().fromJson(result.getResponse()
                .getContentAsString(), ProductResponse.class);

        assertEquals(mockProductResponse.getMessage(), productResponse.getMessage());
    }

    @Test
    public void testFail_GetAllProducts() throws Exception {
        ProductResponse mockProductResponse = ProductResponse.builder()
                .message("get all products")
                .build();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/admin/list_product")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ProductResponse productResponse = new Gson().fromJson(result.getResponse()
                .getContentAsString(), ProductResponse.class);

        assertEquals(mockProductResponse.getMessage(), productResponse.getMessage());
    }

    @Test
    public void testSuccess_getProductDetail() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/admin/list_product/{product_id}", "1")
                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
                .andReturn();

        ProductResponse productResponse = new Gson().fromJson(result.getResponse()
                .getContentAsString(), ProductResponse.class);

        assertNull(productResponse.getProduct());
    }

    @Test
    public void testFail_getOrderList() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/admin/order")
                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
                .andReturn();

        AllOrdersResponse allOrdersResponse = new Gson().fromJson(result.getResponse()
                .getContentAsString(), AllOrdersResponse.class);

        assertEquals(new ArrayList<>() , allOrdersResponse.getOrders());
    }

    @Test
    public void testFail_changeOrderStatus() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/admin/update_order")
                        .param("order_id", "1")
                        .param("status", "completed")
                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
                .andReturn();

        AllOrdersResponse allOrdersResponse = new Gson().fromJson(result.getResponse()
                .getContentAsString(), AllOrdersResponse.class);

        assertNull(allOrdersResponse.getOrders());
    }

    @Test
    public void testFail_getOrderDetail() throws Exception {
        OrderResponse mockOrderResponse = OrderResponse.builder()
                .message("This order does not exist!")
                .build();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/admin/order/{order_id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
                .andReturn();

        OrderResponse orderResponse = new Gson().fromJson(result.getResponse()
                .getContentAsString(), OrderResponse.class);

        assertEquals(mockOrderResponse, orderResponse);
    }

    @Test
    public void testFail_getTopProduct() throws Exception {
        AggregateProductsResponse mockAggregateProductsResponse = AggregateProductsResponse.builder()
                .message("No user has purchased anything yet!")
                .build();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/admin/top_product")
                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
                .andReturn();

        AggregateProductsResponse aggregateProductsResponse = new Gson().fromJson(result.getResponse()
                .getContentAsString(), AggregateProductsResponse.class);

        assertEquals(mockAggregateProductsResponse.getMessage(), aggregateProductsResponse.getMessage());
    }

    @Test
    public void testFail_getTopBuyers() throws Exception {
        AllUsersResponse mockAllUsersResponse = AllUsersResponse.builder()
                .message("No user has purchased anything yet!")
                .build();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/admin/top_buyer")
                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
                .andReturn();

        AllUsersResponse allUsersResponse = new Gson().fromJson(result.getResponse()
                .getContentAsString(), AllUsersResponse.class);

        assertEquals(mockAllUsersResponse.getMessage(), allUsersResponse.getMessage());
    }

    @Test
    public void testFail_getProductProfit() throws Exception{
        TopProductsResponse mockTopProductsResponse = TopProductsResponse.builder()
                .message("No user has purchased anything yet!")
                .build();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/admin/product_profit")
                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
                .andReturn();

        TopProductsResponse topProductsResponse = new Gson().fromJson(result.getResponse()
                .getContentAsString(), TopProductsResponse.class);

        assertEquals(mockTopProductsResponse.getMessage(), topProductsResponse.getMessage());
    }


}
