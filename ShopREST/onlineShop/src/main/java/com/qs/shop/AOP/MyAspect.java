package com.qs.shop.AOP;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Aspect
@Component
public class MyAspect {
    private Logger logger = LoggerFactory.getLogger(MyAspect.class);

    @After(value = "com.qs.shop.AOP.PointCut.place_order()")
    public void logOrderPlace(){
        logger.info("User has place an order at " + LocalDateTime.now());
    }

    @After(value = "com.qs.shop.AOP.PointCut.cancel_order()")
    public void logOrderCancel(){
        logger.info("User has canceled an order at " + LocalDateTime.now());
    }

    @After(value = "com.qs.shop.AOP.PointCut.update_order()")
    public void logOrderUpdate(){
        logger.info("Admin has changed the order status at " + LocalDateTime.now());
    }

//    @AfterThrowing(value = "shopdemo.AOP.PointCut.update_order()", throwing = "ex")
//    public void logThrownException(JoinPoint joinPoint, Throwable ex){
//        logger.error("From LoggingAspect.logThrownException in controller: " + ex.getMessage() + ": " + joinPoint.getSignature());
//    }
}
