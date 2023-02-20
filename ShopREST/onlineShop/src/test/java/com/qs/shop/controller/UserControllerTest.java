package com.qs.shop.controller;

import com.google.gson.Gson;
import com.qs.shop.domain.support.BriefProduct;
import com.qs.shop.domain.entity.Order;
import com.qs.shop.domain.entity.Product;
import com.qs.shop.domain.entity.User;
import com.qs.shop.domain.response.AllProductsResponse;
import com.qs.shop.domain.response.BriefProductResponse;
import com.qs.shop.domain.response.OrderResponse;
import com.qs.shop.domain.response.ProductResponse;
import com.qs.shop.service.OrderService;
import com.qs.shop.service.ProductService;
import com.qs.shop.service.UserService;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@RunWith(SpringRunner.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private UserService userService;

    @MockBean
    private OrderService orderService;

    @BeforeEach
    public void init() {
        System.out.println("------ Test Start ------");
    }

    @AfterEach
    public void end() {
        System.out.println("------ Test End ------");
    }

    @Test
    public void testFail_getAllProducts() throws Exception {
        AllProductsResponse mockAllProductsResponse = AllProductsResponse.builder()
                .message("get all products selling")
                .build();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/user/list_product")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        AllProductsResponse allProductsResponse = new Gson().fromJson(result.getResponse()
                .getContentAsString(), AllProductsResponse.class);

//        System.out.println(productResponse);
        assertEquals(mockAllProductsResponse.getMessage(), allProductsResponse.getMessage());
    }

    @Test
    public void testSuccess_getProductByID() throws Exception {
        Product mockProduct = Product.builder()
                .product_id(0)
                .name("test product")
                .description("test description")
                .retail_price(100f)
                .wholesale_price(50f)
                .quantity(10)
                .build();

        when(productService.getProductByID(0, false)).thenReturn(mockProduct);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/user/list_product/{product_id}", 0)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ProductResponse productResponse = new Gson().fromJson(result.getResponse()
                .getContentAsString(), ProductResponse.class);

        assertEquals(mockProduct, productResponse.getProduct());
    }

    @Test
    public void testFail_getProductByID() throws Exception {
        ProductResponse mockProductResponse = ProductResponse.builder()
                .message("request product not exists!")
                .build();

        when(productService.getProductByID(0, false)).thenReturn(null);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/user/list_product/{product_id}", 0)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ProductResponse productResponse = new Gson().fromJson(result.getResponse()
                .getContentAsString(), ProductResponse.class);

        assertEquals(mockProductResponse.getMessage(), productResponse.getMessage());
    }

    @Test
    public void testSuccess_addToWatchList() throws Exception {
        Product mockProduct = Product.builder()
                .product_id(0)
                .name("test product change")
                .description("test description change")
                .retail_price(80f)
                .wholesale_price(50f)
                .quantity(3)
                .build();

        when(productService.getProductByID(0, false)).thenReturn(null);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/user/watch_list")
                        .param("product_id", String.valueOf(0))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ProductResponse productResponse = new Gson().fromJson(result.getResponse()
                .getContentAsString(), ProductResponse.class);

        assertEquals("Such product doesn't exist", productResponse.getMessage());
    }

    @Test
    public void testSuccess_showWatchList() throws Exception {
        List<BriefProduct> mockBriefProducts = new ArrayList<>();
        BriefProduct mockBriefProduct = BriefProduct.builder()
                .product_id(0)
                .name("test product change")
                .build();
        mockBriefProducts.add(mockBriefProduct);

        when(userService.getProductsByName("anonymousUser")).thenReturn(mockBriefProducts);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/user/watch_list")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BriefProductResponse briefProductResponse = new Gson().fromJson(result.getResponse()
                .getContentAsString(), BriefProductResponse.class);

        assertEquals(mockBriefProducts.get(0), briefProductResponse.getProducts().get(0));
    }

    @Test
    public void testFail_placeOrder() throws Exception {
        OrderResponse mockOrderResponse = OrderResponse.builder()
                .message("Not valid input")
                .build();

        JSONObject json = new JSONObject();
        json.put("product_id", null);
        json.put("quantity", null);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/user/place_order")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        OrderResponse orderResponse = new Gson().fromJson(result.getResponse()
                .getContentAsString(), OrderResponse.class);

        assertEquals(mockOrderResponse.getMessage(), orderResponse.getMessage());
    }

    @Test
    public void testSuccess_cancel_order() throws Exception {
        OrderResponse mockOrderResponse = OrderResponse.builder()
                .message("You are not associated with this order")
                .build();

        User mockUser = User.builder()
                .user_id(0)
                .username("anonymousUser")
                .build();
        Order mockOrder = Order.builder()
                .order_id(0)
                .user_id(1)
                .build();

        when(userService.getUserByName("anonymousUser")).thenReturn(mockUser);
        when(orderService.getOrderByID(0)).thenReturn(mockOrder);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/user/cancel_order")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("order_id", String.valueOf(0))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        OrderResponse orderResponse = new Gson().fromJson(result.getResponse()
                .getContentAsString(), OrderResponse.class);

        assertEquals(mockOrderResponse.getMessage(), orderResponse.getMessage());
    }
}
