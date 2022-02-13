package com.tms.api.repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.tms.api.entity.SearchCriteria;

public class ReportSpecification<T> implements Specification<T> {
    
    private SearchCriteria criteria;
    
    public ReportSpecification(SearchCriteria searchCriteria) {
        this.criteria = searchCriteria;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        switch(criteria.getOperator().toLowerCase()) 
        { 
            case ">": 
                return criteriaBuilder.greaterThan(root.<String> get(criteria.getKey()), criteria.getValue().toString());
            case "<": 
                return criteriaBuilder.lessThan(root.<String> get(criteria.getKey()), criteria.getValue().toString());
            case ">=": 
                return criteriaBuilder.greaterThanOrEqualTo(root.<String> get(criteria.getKey()), criteria.getValue().toString());
            case "<=": 
                return criteriaBuilder.lessThanOrEqualTo(root.<String> get(criteria.getKey()), criteria.getValue().toString());
            case "=": 
                return criteriaBuilder.equal(root.<String> get(criteria.getKey()), criteria.getValue().toString());
            case "like": 
                if (root.get(criteria.getKey()).getJavaType() == String.class) {
                    return criteriaBuilder.like(
                      root.<String>get(criteria.getKey()), "%" + criteria.getValue() + "%");
                } else {
                    return criteriaBuilder.equal(root.get(criteria.getKey()), criteria.getValue());
                }
            default:
                return null;
        }
    }

}
