package com.co.repositories.impl;

import com.co.pojo.Course;
import com.co.pojo.Enrollment;
import com.co.pojo.Payments;
import com.co.repositories.StatsRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class StatsRepositoryImpl implements StatsRepository {
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Object[]> countCoursesByTeacherInMonthYear(Integer month, Integer year) {
    Session s = this.factory.getObject().getCurrentSession();
    CriteriaBuilder cb = s.getCriteriaBuilder();
    CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
    Root<Course> root = cq.from(Course.class);

    List<Predicate> predicates = new ArrayList<>();

    // Lọc theo tháng
    if (month != null) {
        Expression<Integer> monthExpr = cb.function("month", Integer.class, root.get("createdAt"));
        predicates.add(cb.equal(monthExpr, month));
    }

    // Lọc theo năm
    if (year != null) {
        Expression<Integer> yearExpr = cb.function("year", Integer.class, root.get("createdAt"));
        predicates.add(cb.equal(yearExpr, year));
    }

    cq.select(cb.array(
            root.get("teacherId"),     // Trả về object User
            cb.count(root)            // Trả về số lượng Course
    ));

    cq.where(predicates.toArray(new Predicate[0]));
    cq.groupBy(root.get("teacherId"));

    return s.createQuery(cq).getResultList();
}


    @Override
    public List<Object[]> countCoursesByTeacher() {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();

        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<Course> root = cq.from(Course.class);

        Expression<Long> publicCount = cb.count(cb.selectCase()
                .when(cb.isTrue(root.get("public1")), root.get("id")));
        Expression<Long> privateCount = cb.count(cb.selectCase()
                .when(cb.isFalse(root.get("public1")), root.get("id")));

        cq.select(cb.array(root.get("teacherId"), publicCount, privateCount));
        cq.groupBy(root.get("teacherId"));

        return s.createQuery(cq).getResultList();
    }

    @Override
    public BigDecimal getRevenueByMonth(Integer month) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();

        CriteriaQuery<BigDecimal> cq = cb.createQuery(BigDecimal.class);
        Root<Payments> root = cq.from(Payments.class);

        List<Predicate> predicates = new ArrayList<>();
        if (month != null) {
            Expression<Integer> monthExpr = cb.function("month", Integer.class, root.get("paidAt"));
            predicates.add(cb.equal(monthExpr, month));
        }

        cq.select(cb.coalesce(cb.sum(root.get("amount")), BigDecimal.ZERO))
                .where(predicates.toArray(new Predicate[0]));

        return s.createQuery(cq).getSingleResult();
    }

    @Override
    public List<Object[]> getCourseRevenueByTeacher(Integer teacherId, Integer month) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();

        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<Payments> pRoot = cq.from(Payments.class);
        Join<Payments, Enrollment> eJoin = pRoot.join("enrollmentId");
        Join<Enrollment, Course> cJoin = eJoin.join("courseId");

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(cJoin.get("teacherId").get("id"), teacherId));
        if (month != null) {
            Expression<Integer> monthExpr = cb.function("month", Integer.class, pRoot.get("paidAt"));
            predicates.add(cb.equal(monthExpr, month));
        }

        cq.select(cb.array(cJoin, cb.coalesce(cb.sum(pRoot.get("amount")), BigDecimal.ZERO)));
        cq.where(predicates.toArray(new Predicate[0]));
        cq.groupBy(cJoin);

        return s.createQuery(cq).getResultList();
    }
}
