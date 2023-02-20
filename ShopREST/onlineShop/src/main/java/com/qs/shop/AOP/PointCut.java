package com.qs.shop.AOP;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class PointCut {
    @Pointcut("execution(* com.qs.shop.controller.UserController.placeOrder(..))")
    public void place_order() {}

    @Pointcut("execution(* com.qs.shop.controller.UserController.cancelOrder(..))")
    public void cancel_order() {}

    @Pointcut("execution(* com.qs.shop.controller.AdminController.changeOrderStatus(..))")
    public void update_order() {}
}
