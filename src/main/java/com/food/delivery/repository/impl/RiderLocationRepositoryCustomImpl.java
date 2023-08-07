package com.food.delivery.repository.impl;

import com.food.delivery.model.AvailableRider;
import com.food.delivery.repository.RiderLocationRepositoryCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.stream.Collectors;

public class RiderLocationRepositoryCustomImpl implements RiderLocationRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<AvailableRider> findNearestRider(Double latitude, Double longitude) {

        String query = """
                SELECT rl.rider_id , point(:longitude ,:latitude) <@>  (point(longitude, latitude)::::point) as distance
                FROM rider_location rl ORDER BY distance;
                """;
        Query nativeQuery = entityManager.createNativeQuery(query);
        nativeQuery.setParameter("longitude",longitude);
        nativeQuery.setParameter("latitude",latitude);
        List<Object[]> riders = nativeQuery.getResultList();
        return riders.stream().map(statistics -> new AvailableRider(Long.parseLong(statistics[0].toString()), Double.parseDouble(statistics[1].toString()))).collect(Collectors.toList());
    }
}
