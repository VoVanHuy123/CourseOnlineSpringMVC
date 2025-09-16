/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.co.repositories.impl;

import com.co.dtos.UserDTO;
import com.co.pojo.Lesson;
import com.co.pojo.User;
import com.co.repositories.UserRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ACER
 */
@Repository
@Transactional
public class UserRepositoryImpl implements UserRepository {

    private static final int PAGE_SIZE = 8;
    @Autowired
    private LocalSessionFactoryBean factory;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public User getUserByUsername(String username) {
//        Session s = this.factory.getObject().getCurrentSession();
//        Query q = s.createNamedQuery("User.findByUsername", User.class);
//        q.setParameter("username", username);
//        return (User) q.getSingleResult();
        Session s = this.factory.getObject().getCurrentSession();
        Query<User> q = s.createNamedQuery("User.findByUsername", User.class);
        q.setParameter("username", username);

        List<User> results = q.getResultList();
        if (results.isEmpty()) {
            return null; // không tìm thấy user
        }
        return results.get(0); // trả về user đầu tiên
    }

    @Override
    public List<User> getUsers(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<User> query = b.createQuery(User.class);
        Root root = query.from(User.class);
        query.select(root);
//        boolean isPagination = true;

        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();

            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                Predicate firstNameLike = b.like(root.get("firstName"), String.format("%%%s%%", kw));
                Predicate lastNameLike = b.like(root.get("lastName"), String.format("%%%s%%", kw));
                predicates.add(b.or(firstNameLike, lastNameLike));
            }

            String role = params.get("role");
            if (role != null && !role.isEmpty()) {
                predicates.add(b.equal(root.get("role"), role));
            }

            String isVerify = params.get("isVerify");
            if (isVerify != null && !isVerify.isEmpty()) {
                predicates.add(b.equal(root.get("isVerify"), Boolean.valueOf(isVerify)));
            }

            String username = params.get("username");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(b.like(root.get("username"), String.format("%%%s%%", username)));
            }

            query.where(predicates);

            // sắp xếp
            String orderBy = params.get("orderBy");
            if (orderBy != null && !orderBy.isEmpty()) {
                query.orderBy(b.desc(root.get(orderBy)));
            }
        }

        Query q = s.createQuery(query);

        // phân trang LIMIT OFFSET
        if (params != null) {
            String p = params.get("page");
            if (p != null && !p.isEmpty()) {
                int page = Integer.parseInt(p);

                int start = (page - 1) * PAGE_SIZE;

                q.setMaxResults(PAGE_SIZE);
                q.setFirstResult(start);
            }
        }

        return q.getResultList();
    }

    @Override
    public User getUserById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.find(User.class, id);
    }

    @Override
    public void addOrUpdate(User user) {
        Session s = this.factory.getObject().getCurrentSession();

        if (user.getId() != null) {
//            user.setPassword(this.passwordEncoder.encode(user.getPassword()));
            s.merge(user);
        } else {
            user.setPassword(this.passwordEncoder.encode(user.getPassword()));
            
            s.persist(user);
        }
    }

    @Override
    public void delete(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        User u = this.getUserById(id);
        if (u != null) {
            s.remove(u);
        }
    }

    @Override
    public boolean authenticate(String username, String password) {
        User u = this.getUserByUsername(username);

        return this.passwordEncoder.matches(password, u.getPassword());
    }

    @Override
    public long countUsers(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Long> cq = b.createQuery(Long.class);
        Root<User> root = cq.from(User.class);
        cq.select(b.count(root));
        List<Predicate> predicates = new ArrayList<>();
        if (params != null) {

            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                Predicate firstNameLike = b.like(root.get("firstName"), String.format("%%%s%%", kw));
                Predicate lastNameLike = b.like(root.get("lastName"), String.format("%%%s%%", kw));
                predicates.add(b.or(firstNameLike, lastNameLike));
            }

            String role = params.get("role");
            if (role != null && !role.isEmpty()) {
                predicates.add(b.equal(root.get("role"), role));
            }

            String isVerify = params.get("isVerify");
            if (isVerify != null && !isVerify.isEmpty()) {
                predicates.add(b.equal(root.get("isVerify"), Boolean.valueOf(isVerify)));
            }

            String username = params.get("username");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(b.like(root.get("username"), String.format("%%%s%%", username)));
            }

        }

        cq.where(predicates.toArray(new Predicate[0]));

        return s.createQuery(cq).getSingleResult();
    }

}
