package com.qs.shop.controller;

import com.qs.shop.domain.entity.*;
import com.qs.shop.domain.response.*;
import com.qs.shop.domain.support.AggregateProduct;
import com.qs.shop.domain.support.BriefProduct;
import com.qs.shop.domain.support.OrderList;
import com.qs.shop.service.OrderService;
import org.springframework.security.core.context.SecurityContextHolder;
import com.qs.shop.service.ProductService;
import com.qs.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {

    private final ProductService productService;
    private final UserService userService;
    private final OrderService orderService;

    @Autowired
    public UserController(ProductService productService, UserService userService,
                          OrderService orderService) {
        this.productService = productService;
        this.userService = userService;
        this.orderService = orderService;
    }

    @GetMapping("/list_product")
    public AllProductsResponse getAllProducts() {
//        System.out.println(SecurityContextHolder.getContext().getAuthentication());

        return AllProductsResponse.builder()
                .message("get all products selling")
                .products(productService.getAllProducts(false))
                .build();
    }

    // show product detail to the user
    @PostMapping("/list_product/{product_id}")
    public ProductResponse getProductByID(@PathVariable Integer product_id) {
        Product product = productService.getProductByID(product_id, false);

        if (product == null)
            return ProductResponse.builder()
                    .message("request product not exists!")
                    .build();

        return ProductResponse.builder()
                .message("return the product requested.")
                .product(product)
                .build();
    }

    @PostMapping("/watch_list")
    public ProductResponse addToWatchList(@RequestParam Integer product_id) {
        Product product = productService.getProductByID(product_id, true);
        if (product == null)
            return ProductResponse.builder()
                    .message("Such product doesn't exist")
                    .build();

        User user = userService.getUserByName((String) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal());

        boolean sign = userService.updateWatchList(user.getUser_id(), product);

        if (!sign)
            return ProductResponse.builder()
                    .message("Product already exists in your watch list.")
                    .build();

        return ProductResponse.builder()
                .message("Product added to the watch list")
                .build();
    }

    @GetMapping("/watch_list")
    public BriefProductResponse showWatchList() {

        List<BriefProduct> products = userService.getProductsByName((String) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal());

        return BriefProductResponse.builder()
                .message("show products in watch list")
                .products(products)
                .build();
    }

    @DeleteMapping("/watch_list/{product_id}")
    public ProductResponse deleteWatchList(@PathVariable Integer product_id) {
        boolean sign = userService.deleteProductFromWatchListByName((String) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal(), product_id);

        if (sign)
            return ProductResponse.builder()
                    .message("product deleted from watch list")
                    .build();
        else
            return ProductResponse.builder()
                    .message("product not in your watch list")
                    .build();
    }

    @PutMapping("/place_order")
    public OrderResponse placeOrder(@RequestBody OrderList orderList) {
        System.out.println(orderList.getProduct_id());
        System.out.println(orderList.getQuantity());

        if (orderList == null
                || orderList.getProduct_id() == null || orderList.getProduct_id().isEmpty()
                || orderList.getQuantity() == null || orderList.getQuantity().isEmpty())
            return OrderResponse.builder()
                    .message("Not valid input")
                    .build();

        int user_id = userService.getUserByName((String) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUser_id();

        System.out.println(user_id);
        orderService.createOrder(orderList, user_id);

        return OrderResponse.builder()
                .message("order created successfully!")
                .build();
    }

    @PostMapping("/cancel_order")
    public OrderResponse cancelOrder(
            @RequestParam Integer order_id) {

        Order order = orderService.getOrderByID(order_id);
        User user = userService.getUserByName((String) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal());

        if (order == null)
            return OrderResponse.builder()
                    .message("This order does not exist!")
                    .build();

        if (order.getUser_id() != user.getUser_id())
            return OrderResponse.builder()
                    .message("You are not associated with this order")
                    .build();

        if (!order.getStatus().equals("processing"))
            return OrderResponse.builder()
                    .message("Order is either canceled or completed! Access denied")
                    .order(order)
                    .build();

        orderService.updateOrderStatus(order_id, "canceled");
        order = orderService.getOrderByID(order_id);

        return OrderResponse.builder()
                .message("Order status updated.")
                .order(order)
                .build();
    }

    @GetMapping("/order")
    public AllOrdersResponse getAllOrder() {
        User user = userService.getUserByName((String) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal());

        List<Order> orders = orderService.getOrdersByUserID(user.getUser_id());

        return AllOrdersResponse.builder()
                .message("Get back all the orders.")
                .orders(orders)
                .build();
    }

    @PostMapping("/order/{order_id}")
    public OrderProductResponse getOrderDetail(@PathVariable Integer order_id) {
        User user = userService.getUserByName((String) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal());

        OrderProductResponse orderProductResponse = new OrderProductResponse();
        Order order = orderService.getOrderByIDWithProductList(order_id, orderProductResponse);

        if (order == null)
            return OrderProductResponse.builder()
                    .message("This order does not exist!")
                    .build();

        if (order.getUser_id() != user.getUser_id())
            return OrderProductResponse.builder()
                    .message("This order does not associated with you!")
                    .build();

        orderProductResponse.setOrder(order);
        orderProductResponse.setMessage("get back order details");
        return orderProductResponse;
    }

    @PostMapping("/top_product")
    public AggregateProductsResponse topPurchasedProduct(
            @RequestParam(value = "number", required = false) Integer number) {

        if (number == null || number <= 0) number = 3;
        User user = userService.getUserByName((String) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal());

        List<AggregateProduct> topProducts = orderService.getTopProductsByUserID(user.getUser_id(), number);

        if (topProducts == null || topProducts.size() == 0)
            return AggregateProductsResponse.builder()
                    .message("You haven't purchased anything yet")
                    .build();

        return AggregateProductsResponse.builder()
                .aggregateProducts(topProducts)
                .message("You have " + topProducts.size() + " top purchased products")
                .build();
    }

    @PostMapping("/recent_product")
    public AggregateProductsResponse recentPurchasedProduct(
            @RequestParam(value = "number", required = false) Integer number) {

        if (number == null) number = 3;

        User user = userService.getUserByName((String) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal());

        List<AggregateProduct> aggregateProducts = orderService
                .getRecentProductsByUserID(user.getUser_id(), number);

        if (aggregateProducts == null || aggregateProducts.isEmpty())
            return AggregateProductsResponse.builder()
                    .message("you haven't purchased any products yet")
                    .build();

        return AggregateProductsResponse.builder()
                .aggregateProducts(aggregateProducts)
                .message("return your recent " + aggregateProducts.size() + " purchased products.")
                .build();
    }
}
