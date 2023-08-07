package com.food.delivery.repository;

import com.food.delivery.domain.Ticket;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaKeyRepository<Ticket, Long>, QuerydslPredicateExecutor<Ticket>
{
}
