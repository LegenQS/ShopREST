package com.qs.shop.dao;

import com.qs.shop.domain.support.BriefProduct;
import com.qs.shop.domain.entity.Permission;
import com.qs.shop.domain.entity.Product;
import com.qs.shop.domain.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
    SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
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
//            e.printStackTrace();
            return null;
        }

        return (user == null || !user.isPresent()) ? null : user.get();
    }

    public List<BriefProduct> getProductsByName(String username) {
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

        if (user == null || !user.isPresent())
            return null;

        List<BriefProduct> products = new ArrayList<>();

        if (user.get().getWatchList().isEmpty())
            return products;

        for (Product p : user.get().getWatchList()) {
//            System.out.println(p);
            if (p.getQuantity() > 0) {
                products.add(new BriefProduct(
                        p.getProduct_id(), p.getName()
                ));
            }
        }

        return products;
    }

    public boolean updateWatchList(int user_id, Product product) {
        Session session;

        try{
            session = sessionFactory.getCurrentSession();
            User user = getUserByID(user_id);
            if (user == null)
                return false;

            if (user.getWatchList() == null)
                user.setWatchList(new ArrayList<>());
            user.getWatchList().isEmpty();

            if (user.getWatchList().contains(product))
                return false;

            user.getWatchList().add(product);
            session.saveOrUpdate(user);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    public User getUserByID(int user_id){
        Session session;
        Optional<User> user = null;
        try{
            session = sessionFactory.getCurrentSession();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<User> cq = cb.createQuery(User.class);
            Root<User> root = cq.from(User.class);
            Predicate predicate = cb.equal(root.get("user_id"), user_id);

            cq.select(root).where(predicate);
            user = session.createQuery(cq).uniqueResultOptional();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return (user == null || !user.isPresent()) ? null : user.get();
    }

    public boolean deleteProductFromWatchListByName(String username, int product_id){
        Session session;
        User user = getUserByName(username);

        try{
            session = sessionFactory.getCurrentSession();
            if (user.getWatchList() == null || user.getWatchList().isEmpty())
                return false;

            for (Product p : user.getWatchList()) {
                if (p.getProduct_id() == product_id) {
                    user.getWatchList().remove(p);
                    return true;
                }
            }
            session.save(user);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return false;
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
        System.out.println(user);
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
