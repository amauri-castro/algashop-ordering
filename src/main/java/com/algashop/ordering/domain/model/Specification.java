package com.algashop.ordering.domain.model;

public interface Specification<T> {
    boolean isSatisfiedBy(T t);
}
