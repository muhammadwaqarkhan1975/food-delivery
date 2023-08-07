package com.food.delivery.repository;

import com.food.delivery.domain.OrderTicket;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderTicketRepository extends JpaKeyRepository<OrderTicket, Long>, QuerydslPredicateExecutor<OrderTicket>
{
    @Query(value = "select * from order_ticket  order by priority asc NULLS LAST", nativeQuery = true)
    List<OrderTicket> findAllByOrderByPriorityAsc();
}
