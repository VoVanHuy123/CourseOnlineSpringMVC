/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.co.repositories.impl;

import com.co.pojo.Payments;
import com.co.repositories.PaymentsRepository;
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
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ADMIN
 */
@Repository
@Transactional
public class PaymentsRepositoryImpl implements  PaymentsRepository{
    private static final int PAGE_SIZE = 8;
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Payments> getPayments(Map<String, String> params) {
        Session s = factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Payments> cq = b.createQuery(Payments.class);
        Root<Payments> root = cq.from(Payments.class);
        cq.select(root);

        List<Predicate> predicates = new ArrayList<>();
        if (params != null) {
            String status = params.get("status");
            if (status != null && !status.isEmpty())
                predicates.add(b.equal(root.get("status"), status));

            String method = params.get("method");
            if (method != null && !method.isEmpty())
                predicates.add(b.equal(root.get("method"), method));

            cq.where(predicates.toArray(new Predicate[0]));
        }

        Query q = s.createQuery(cq);

        if (params != null) {
            String p = params.get("page");
            if (p != null && !p.isEmpty()) {
                int page = Integer.parseInt(p);
                q.setFirstResult((page - 1) * PAGE_SIZE);
                q.setMaxResults(PAGE_SIZE);
            }
        }
        return q.getResultList();
    }

    @Override
    public Payments getPaymentById(int id) {
        Session s = factory.getObject().getCurrentSession();
        return s.find(Payments.class, id);
    }

    @Override
    public void addOrUpdate(Payments payment) {
        Session s = factory.getObject().getCurrentSession();
        if (payment.getId() != null)
            s.merge(payment);
        else
            s.persist(payment);
    }

    @Override
    public void delete(int id) {
        Session s = factory.getObject().getCurrentSession();
        Payments p = getPaymentById(id);
        if (p != null)
            s.remove(p);
    }

    @Override
    public long countPayments(Map<String, String> params) {
        Session s = factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Long> cq = b.createQuery(Long.class);
        Root<Payments> root = cq.from(Payments.class);
        cq.select(b.count(root));

        List<Predicate> predicates = new ArrayList<>();
        if (params != null) {
            String status = params.get("status");
            if (status != null && !status.isEmpty())
                predicates.add(b.equal(root.get("status"), status));
        }
        cq.where(predicates.toArray(new Predicate[0]));
        return s.createQuery(cq).getSingleResult();
    } 
}
