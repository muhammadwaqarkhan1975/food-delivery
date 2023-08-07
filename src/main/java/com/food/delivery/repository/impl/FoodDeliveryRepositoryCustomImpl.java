package com.food.delivery.repository.impl;

import com.food.delivery.domain.FoodDelivery;
import com.food.delivery.repository.FoodDeliveryRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;


public class FoodDeliveryRepositoryCustomImpl implements FoodDeliveryRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Page<FoodDelivery> fetchReceivedDeliveryWithPageable(Pageable pageable)
    {
        String query = "SELECT * FROM food_delivery fd  where fd.delivery_status in ('RECEIVED', 'ORDER_PREPARING')";
        String countQuery = " SELECT count(*) FROM food_delivery fd  where fd.delivery_status in ('RECEIVED', 'ORDER_PREPARING') ";


        Query nativeQuery = entityManager.createNativeQuery(query, FoodDelivery.class);
        nativeQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        nativeQuery.setMaxResults(pageable.getPageSize());

        return new PageImpl<FoodDelivery>(nativeQuery.getResultList(), pageable, fetchReceivedDeliveryWithPageableCount(countQuery));
    }

    @Override
    public Page<Long> fetchReceivedDeliveryIdsWithPageable(Pageable pageable)
    {
        String query = "SELECT web_id FROM food_delivery fd  where fd.delivery_status in ('RECEIVED', 'ORDER_PREPARING')";
        String countQuery = "SELECT count(1) FROM food_delivery fd  where fd.delivery_status in ('RECEIVED', 'ORDER_PREPARING')";


        Query nativeQuery = entityManager.createNativeQuery(query);
        nativeQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        nativeQuery.setMaxResults(pageable.getPageSize());

        return new PageImpl<>((List<Long>) nativeQuery
                .getResultList().stream()
                .map(result -> Long.parseLong(result.toString())).collect(Collectors.toList()),
                pageable, fetchReceivedDeliveryWithPageableCount(countQuery));
    }

    private int fetchReceivedDeliveryWithPageableCount(String countQuery)
    {
        Query query = entityManager.createNativeQuery(countQuery);
        return ((BigInteger) query.getSingleResult()).intValue();

    }
}
