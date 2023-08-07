package com.food.delivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;
@NoRepositoryBean
public interface JpaKeyRepository <T, ID> extends JpaRepository<T, ID>
{
    Optional<T> findByKey(String key);
}
