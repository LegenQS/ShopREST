package com.qs.shop.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class DataBaseInitializer {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Bean
    CommandLineRunner loadDatabase() {
        return new CommandLineRunner() {

            @Override
            public void run(String... args) throws Exception {

                jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS `User` (\n" +
                        "    user_id INT auto_increment PRIMARY KEY,\n" +
                        "    email varchar(30) NOT NULL UNIQUE,\n" +
                        "    username varchar(25) NOT NULL UNIQUE,\n" +
                        "    `password` varchar(20) NOT NULL\n" +
                        ");");

                jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS `Order` (\n" +
                        "    order_id INT auto_increment PRIMARY KEY,\n" +
                        "    user_id INT,\n" +
                        "    order_status varchar(50),\n" +
                        "    date_placed timestamp,\n" +
                        "    FOREIGN KEY (user_id) REFERENCES `User`(user_id)\n" +
                        ");");

                jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS Product(\n" +
                        "    product_id INT auto_increment PRIMARY KEY,\n" +
                        "    name varchar(30) NOT NULL,\n" +
                        "    `description` varchar(1000) NOT NULL,\n" +
                        "    retail_price float(20),\n" +
                        "    wholesale_price float(20),\n" +
                        "    stock_quantity INT\n" +
                        ");");

                jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS Order_Product(\n" +
                        "    order_product_id INT auto_increment PRIMARY KEY,\n" +
                        "    order_id INT,\n" +
                        "    product_id INT,\n" +
                        "    purchased_quantity INT,\n" +
                        "    execution_retail_price float(20),\n" +
                        "    execution_wholesale_price float(20),\n" +
                        "    FOREIGN KEY (order_id) REFERENCES `Order`(order_id),\n" +
                        "    FOREIGN KEY (product_id) REFERENCES Product(product_id)\n" +
                        ");");

                jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS Product_WatchList(\n" +
                        "    user_id int,\n" +
                        "    product_id int,\n" +
                        "    Primary key(user_id, product_id),\n" +
                        "    FOREIGN KEY (user_id) REFERENCES `User`(user_id),\n" +
                        "    FOREIGN KEY (product_id) REFERENCES Product(product_id)\n" +
                        ");");

                jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS Permission (\n" +
                        "    perm_id INT auto_increment PRIMARY KEY,\n" +
                        "    `role` varchar(15)\n" +
                        ");");

                jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS User_Permission (\n" +
                        "    user_id INT,\n" +
                        "    perm_id INT,\n" +
                        "    PRIMARY KEY(user_id, perm_id),\n" +
                        "    FOREIGN KEY (user_id) REFERENCES `User`(user_id),\n" +
                        "    FOREIGN KEY (perm_id) REFERENCES Permission(perm_id)\n" +
                        ");");

                jdbcTemplate.execute("insert IGNORE into Permission(perm_id, role) values\n" +
                        "(1, 'admin'), " +
                        "(2, 'user');");
            }
        };
    }
}