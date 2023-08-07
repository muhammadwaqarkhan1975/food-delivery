package com.food.delivery.repository;

import com.food.delivery.domain.UserAccount;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepository extends JpaKeyRepository<UserAccount, Long>, QuerydslPredicateExecutor<UserAccount> {
    UserAccount findUserAccountByEmail(String email);
}
