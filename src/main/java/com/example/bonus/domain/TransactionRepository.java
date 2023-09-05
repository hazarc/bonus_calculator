package com.example.bonus.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Define a custom query to calculate the total reward points for a customer
    @Query("SELECT SUM(t.rewardPoints) FROM Transaction t WHERE t.customer.id = :customerId")
    int calculateTotalRewardPoints(@Param("customerId") Long customerId);
}
