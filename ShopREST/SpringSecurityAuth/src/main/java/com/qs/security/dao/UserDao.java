package com.qs.security.dao;

import com.qs.security.entity.Permission;
import com.qs.security.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Repository
public class UserDao {
    @Autowired
    SessionFactory sessionFactory;

    public User loadUserByUsername(String username){
        Session session = sessionFactory.getCurrentSession();
        String hql = "from User u where u.username = :username";
        Query<User> query = session.createQuery(hql);
        query.setParameter("username", username);
        List<User> users = query.getResultList();

        return users == null ? null : users.get(0);
    }

    public User getUserByName(String username) {
        Session session;
        Optional<User> user = null;
        try{
            session = sessionFactory.getCurrentSession();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<User> cq = cb.createQuery(User.class);
            Root<User> root = cq.from(User.class);
            Predicate predicate = cb.equal(root.get("username"), username);

            cq.select(root).where(predicate);
            user = session.createQuery(cq).uniqueResultOptional();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return (user == null || !user.isPresent()) ? null : user.get();
    }

    public User getUserByEmail(String email) {
        Session session;
        Optional<User> user = null;
        try{
            session = sessionFactory.getCurrentSession();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<User> cq = cb.createQuery(User.class);
            Root<User> root = cq.from(User.class);
            Predicate predicate = cb.equal(root.get("email"), email);

            cq.select(root).where(predicate);
            user = session.createQuery(cq).uniqueResultOptional();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return (user == null || !user.isPresent()) ? null : user.get();
    }

    public int createNewUser(User user) {
        Session session;
        Integer seq = 0;

        Permission admin_perm = new Permission(1, "admin");
        Permission user_perm = new Permission(2, "user");
        List<Permission> permissions = new ArrayList<>();

        if (user.getUsername().equals("admin"))
            permissions.add(admin_perm);
        else permissions.add(user_perm);

        user.setPermissions(permissions);

        try{
            session = sessionFactory.getCurrentSession();
            seq = (Integer) session.save(user);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return seq;
    }

}
