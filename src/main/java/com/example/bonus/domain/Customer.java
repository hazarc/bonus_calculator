package com.example.bonus.domain;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
    @ToString.Exclude
    private List<Transaction> transactions = new ArrayList<>();

    private int totalRewardPoints;

    // Add a transaction to the customer's list of transactions
    public void addTransaction(Transaction transaction) {
        if (transaction != null) {
            transactions.add(transaction);
            transaction.setCustomer(this); // Set the customer for the transaction
        }
    }
}

