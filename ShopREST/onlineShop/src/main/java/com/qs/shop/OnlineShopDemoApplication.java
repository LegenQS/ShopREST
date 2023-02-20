package com.qs.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication(exclude = HibernateJpaAutoConfiguration.class) //exclude a auto-config class to prevent transactionmanager mismatch exceptiom
public class OnlineShopDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineShopDemoApplication.class, args);
    }

}
