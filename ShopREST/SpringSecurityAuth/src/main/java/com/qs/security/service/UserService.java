package com.qs.security.service;

import com.qs.security.dao.UserDao;
import com.qs.security.entity.Permission;
import com.qs.security.entity.User;
import com.qs.security.security.AuthUserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private UserDao userDao;

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.loadUserByUsername(username);

        if (user == null){
            throw new UsernameNotFoundException("Username does not exist");
        }

        return AuthUserDetail.builder() // spring security's userDetail
                .username(username)
                .password(new BCryptPasswordEncoder().encode(user.getPassword()))
                .authorities(getAuthoritiesFromUser(user))
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .build();
    }

    private List<GrantedAuthority> getAuthoritiesFromUser(User user){
        List<GrantedAuthority> userAuthorities = new ArrayList<>();

        if (user == null || user.getPermissions() == null) return userAuthorities;

        for (Permission permission :  user.getPermissions()){
            userAuthorities.add(new SimpleGrantedAuthority(permission.getRole()));    // SimpleGrantedAuthority can be created from role Strings
        }

        return userAuthorities;
    }

    @Transactional
    public User getUserByName(String username) {
        return userDao.getUserByName(username);
    }

    @Transactional
    public User getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }

    @Transactional
    public Integer createNewUser(User user) {
        return userDao.createNewUser(user);
    }
}
