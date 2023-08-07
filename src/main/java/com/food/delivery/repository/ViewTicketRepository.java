package com.food.delivery.repository;

import com.food.delivery.domain.ViewTicket;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViewTicketRepository extends JpaKeyRepository<ViewTicket, Long>, QuerydslPredicateExecutor<ViewTicket>
{
    @Query(value = "select * from vw_ticket  order by priority asc NULLS LAST", nativeQuery = true)
    List<ViewTicket> findAllByOrderByPriorityAsc();
}
