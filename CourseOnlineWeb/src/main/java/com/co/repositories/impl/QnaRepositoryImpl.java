/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.co.repositories.impl;

import com.co.pojo.Qna;
import com.co.repositories.QnaRepository;
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
public class QnaRepositoryImpl implements QnaRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Qna> getQnas(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Qna> q = b.createQuery(Qna.class);
        Root root = q.from(Qna.class);
        q.select(root);

        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();

            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(b.like(root.get("content"), "%" + kw + "%"));
            }

            q.where(predicates.toArray(new Predicate[0]));
        }
        

        Query query = s.createQuery(q);
        
        if (params.get("page") != null && params.get("pageSize") != null) {
            int page = Integer.parseInt(params.get("page"));
            int pageSize = Integer.parseInt(params.get("pageSize"));
            int start = (page - 1) * pageSize;
            query.setFirstResult(start);
            query.setMaxResults(pageSize);
        }
        
        
        return query.getResultList();
    }

    @Override
    public Qna getQnaById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(Qna.class, id);
    }

    @Override
    public void addOrUpdate(Qna qna) {
        Session s = this.factory.getObject().getCurrentSession();
        if (qna.getId() == null)
            s.persist(qna);
        else
            s.merge(qna);
    }

    @Override
    public void delete(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        Qna q = this.getQnaById(id);
        if (q != null)
            s.remove(q);
    }
    
    @Override
    public long countQnas(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Long> q = b.createQuery(Long.class);
        Root<Qna> root = q.from(Qna.class);
        q.select(b.count(root));

        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();

            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(b.like(root.get("content"), "%" + kw + "%"));
            }

            q.where(predicates.toArray(new Predicate[0]));
        }

        Query<Long> query = s.createQuery(q);
        return query.getSingleResult();
    }

}
