package com.food.delivery.repository;

import com.food.delivery.domain.RiderRating;
import com.food.delivery.domain.UserAccount;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RiderRatingRepository extends JpaKeyRepository<RiderRating, Long>, QuerydslPredicateExecutor<RiderRating>
{
    List<RiderRating> findAllByRider(UserAccount rider);
}
