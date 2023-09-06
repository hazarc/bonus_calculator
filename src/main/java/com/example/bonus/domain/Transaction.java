package com.example.bonus.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Data
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double amount;
    private int rewardPoints;

    @JsonBackReference
    @ManyToOne
    private Customer customer;

    @Temporal(TemporalType.DATE)
    private Date transactionDate;
}

