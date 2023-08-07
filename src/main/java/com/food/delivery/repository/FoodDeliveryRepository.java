package com.food.delivery.repository;

import com.food.delivery.domain.FoodDelivery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.util.List;

@Repository
public interface FoodDeliveryRepository extends JpaKeyRepository<FoodDelivery, Long>, QuerydslPredicateExecutor<FoodDelivery>, FoodDeliveryRepositoryCustom
{
    @Query(value = "select * from food_delivery  order by priority asc NULLS LAST", nativeQuery = true)
    List<FoodDelivery> findAllByOrderByPriorityAsc();

     /*
        if the Customer is VIP then customer should have high priority over other customers.
        If the Expected time of delivery is passed and the order is still not delivered, priority automatically becomes higher then others
    */

    @Query(
            value = "select * from food_delivery  where (customer = 'VIP' and priority != 1) or (customer != 'VIP' and expected_delivery_time < now() and priority != 1) order by priority \n-- #pageable\n",
            countQuery = "select * from food_delivery  where (customer = 'VIP' and priority != 1) or (customer != 'VIP' and expected_delivery_time < now() and priority != 1) order by priority",
            nativeQuery = true)
    Page<FoodDelivery> fetchForPriorityWithPagination(Pageable pageable);

    @QueryHints({@QueryHint(name = "java.persistence.lock.timeout", value = "3000")})
    @Query(
            value = "SELECT * FROM food_delivery fd LEFT JOIN ticket t ON fd.web_id = t.food_delivery_id WHERE t.web_id IS NULL order by priority",
            nativeQuery = true)
    List<FoodDelivery> fetchReceivedDelivery();
}
