package com.qs.shop.controller;

import com.qs.shop.domain.entity.*;
import com.qs.shop.domain.response.*;
import com.qs.shop.domain.support.AggregateProduct;
import com.qs.shop.domain.support.SoldProduct;
import com.qs.shop.domain.support.TopBuyer;
import com.qs.shop.domain.support.TopProduct;
import com.qs.shop.service.OrderService;
import com.qs.shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin")
public class AdminController {
    private final ProductService productService;
    private final OrderService orderService;

    @Autowired
    public AdminController(ProductService productService, OrderService orderService) {
        this.productService = productService;
        this.orderService = orderService;
    }

    @PostMapping("/add_product")
    public ProductResponse addProductToList(
            @RequestBody Product product) {

        if (!checkIsValidNewProduct(product))
            return ProductResponse.builder()
                    .message("Your input contains either null/empty/invalid values!")
                    .build();

        Integer product_id = productService.createNewProduct(product);
        product.setProduct_id(product_id);

        return ProductResponse.builder()
                .message("Product with product_id: " + product_id + " added successfully!")
                .product(product)
                .build();
    }

    @PostMapping("/update_product")
    public ProductResponse updateProduct(
            @RequestBody Product product) {

        if (!checkIsValidUpdateProduct(product))
            return ProductResponse.builder()
                    .message("Your input should contains at least one field to update")
                    .build();

        Product updated_product = productService.updateProduct(product);

        if (updated_product == null)
            return ProductResponse.builder()
                    .message("Product with product_id: " + product.getProduct_id() + " does not exist!")
                    .build();

        return ProductResponse.builder()
                .message("Product with product_id: " + updated_product.getProduct_id() + " updated successfully!")
                .product(updated_product)
                .build();
    }

    @GetMapping("/list_product")
    public AllProductsResponse getProducts() {

        return AllProductsResponse.builder()
                .message("get all products")
                .products(productService.getAllProducts(true))
                .build();
    }

    // show product detail to the admin
    @PostMapping("/list_product/{product_id}")
    public ProductResponse getProductDetail(@PathVariable Integer product_id) {
        Product product = productService.getProductByID(product_id, true);

        return ProductResponse.builder()
                .message("return detailed info. of the current product")
                .product(product)
                .build();
    }

    @GetMapping("/order")
    public AllOrdersResponse getOrderList() {
        return AllOrdersResponse.builder()
                .orders(orderService.getAllOrders())
                .message("get all orders")
                .build();
    }

    @PostMapping("/update_order")
    public OrderResponse changeOrderStatus(
            @RequestParam Integer order_id,
            @RequestParam String status) {

        if (order_id == null || status == null || status.toLowerCase().equals("processing"))
            return OrderResponse.builder()
                    .message("bad request on Status or Order_id")
                    .build();

        Order order = orderService.updateOrderStatus(order_id, status.toLowerCase());
        if (order == null)
            return OrderResponse.builder()
                    .message("requested order does not exist!")
                    .build();

        if (!order.getStatus().equals(status))
            return OrderResponse.builder()
                    .message("you are not allowed to update the " + order.getStatus() + " order")
                    .build();

        return OrderResponse.builder()
                .order(order)
                .message("order update")
                .build();
    }

    @PostMapping("/order/{order_id}")
    public OrderResponse getOrderDetail(@PathVariable Integer order_id) {
        OrderResponse orderResponse = orderService.getOrderByIDWithProductList(order_id);

        if (orderResponse == null || orderResponse.getOrder() == null)
            return OrderResponse.builder()
                    .message("This order does not exist!")
                    .build();

        return orderResponse;
    }

    @PostMapping("/top_product")
    public AggregateProductsResponse getTopProduct(
            @RequestParam(value = "number", required = false) Integer number) {

        if (number == null || number <= 0) number = 3;

        List<AggregateProduct> topProducts = orderService.getTopProducts(number);

        if (topProducts == null || topProducts.size() == 0)
            return AggregateProductsResponse.builder()
                    .message("No user has purchased anything yet!")
                    .build();

        return AggregateProductsResponse.builder()
                .aggregateProducts(topProducts)
                .message("There are " + topProducts.size() + " top purchased products")
                .build();
    }

    @PostMapping("/top_buyer")
    public AllUsersResponse getTopBuyer(
            @RequestParam(value = "number", required = false) Integer number) {

        if (number == null || number <= 0) number = 3;

        List<TopBuyer> topBuyers = orderService.getUsersByCost(number);

        if (topBuyers == null || topBuyers.size() == 0)
            return AllUsersResponse.builder()
                    .message("No user has purchased anything yet!")
                    .build();

        return AllUsersResponse.builder()
                .message("There are " + topBuyers.size() + " top buyers")
                .topBuyers(topBuyers)
                .build();
    }

    @PostMapping("/product_profit")
    public TopProductsResponse getProductProfit(
            @RequestParam(value = "number", required = false) Integer number) {

        if (number == null || number <= 0) number = 3;

        List<TopProduct> topProducts = productService.getProductByProfit(number);

        if (topProducts == null || topProducts.size() == 0)
            return TopProductsResponse.builder()
                    .message("No user has purchased anything yet!")
                    .build();

        return TopProductsResponse.builder()
                .message("There are " + topProducts.size() + " top products!")
                .topProductList(topProducts)
                .build();
    }

    @GetMapping("/product_sold")
    public SoldProductsResponse getProductSold() {
        List<SoldProduct> soldProducts = orderService.getProductSold();

        if (soldProducts == null || soldProducts.isEmpty())
            return SoldProductsResponse.builder()
                    .message("No user has purchased anything yet!")
                    .build();

        return SoldProductsResponse.builder()
                .message("There are " + soldProducts.size() + " products selling!")
                .soldProductList(soldProducts)
                .build();
    }

    private boolean checkIsValidNewProduct(Product product) {
        System.out.println(product);
        if (product == null || product.getDescription() == null || product.getDescription().trim().isEmpty()
                || product.getName() == null || product.getName().trim().isEmpty()
                || product.getQuantity() == null || product.getQuantity() < 0
                || product.getRetail_price() == null || product.getWholesale_price() == null
                || product.getWholesale_price() > product.getRetail_price())
            return false;

        return true;
    }

    private boolean checkIsValidUpdateProduct(Product product) {
        if (product == null || product.getProduct_id() == null) return false;

        if ((product.getWholesale_price() != null && product.getWholesale_price() > 0)
                || (product.getName() != null && !product.getName().trim().isEmpty())
                || (product.getRetail_price() != null && product.getRetail_price() > 0)
                || (product.getDescription() != null && !product.getDescription().trim().isEmpty())
                || (product.getQuantity() != null && product.getQuantity() > 0))
            return true;

        return false;
    }

}
