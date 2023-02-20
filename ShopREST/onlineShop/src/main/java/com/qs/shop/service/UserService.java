package com.qs.shop.service;

import com.qs.shop.dao.UserDao;
import com.qs.shop.domain.support.BriefProduct;
import com.qs.shop.domain.entity.Product;
import com.qs.shop.domain.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserService {
    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Transactional
    public User getUserByName(String username) {
        return userDao.getUserByName(username);
    }

    @Transactional
    public List<BriefProduct> getProductsByName(String username) {
        return userDao.getProductsByName(username);
    }

    @Transactional
    public boolean deleteProductFromWatchListByName(String username, int product_id) {
        return userDao.deleteProductFromWatchListByName(username, product_id);
    }

    @Transactional
    public boolean updateWatchList(Integer user_id, Product product) {
        return userDao.updateWatchList(user_id, product);
    }

    @Transactional
    public Integer createNewUser(User user) {
        return userDao.createNewUser(user);
    }
}
