package com.example.bonus.controller;

import com.example.bonus.domain.Customer;
import com.example.bonus.domain.CustomerRepository;
import com.example.bonus.domain.Transaction;
import com.example.bonus.domain.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    // Endpoint for adding transactions
    @PostMapping("/{customerId}/transactions")
    public ResponseEntity<String> addTransaction(
            @PathVariable Long customerId,
            @RequestBody Transaction transaction) {

        // Find the customer by ID or return a 404 if not found
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            transaction.setCustomer(customer);

            // Calculate reward points for the transaction
            int rewardPoints = calculateRewardPoints(transaction.getAmount());
            transaction.setRewardPoints(rewardPoints);
            transactionRepository.save(transaction);

            return ResponseEntity.ok("Transaction added successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint for calculating reward points per month and total
    @GetMapping("/{customerId}/reward-points")
    public ResponseEntity<Customer> calculateRewardPoints(
            @PathVariable Long customerId) {

        // Find the customer by ID or return a 404 if not found
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();

            // Calculate total reward points for the customer
            int totalRewardPoints = transactionRepository.calculateTotalRewardPoints(customerId);

            // Set the total reward points for the customer
            customer.setTotalRewardPoints(totalRewardPoints);

            return ResponseEntity.ok(customer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Customer> addCustomer(@RequestBody Customer customer) {
        Customer createdCustomer = customerRepository.save(customer);
        return ResponseEntity.ok(createdCustomer);
    }

    // Calculate reward points based on transaction amount
    private int calculateRewardPoints(double transactionAmount) {
        int rewardPoints = 0;

        // Calculate 2 points for every dollar spent over $100
        if (transactionAmount > 100) {
            rewardPoints += (int) ((transactionAmount - 100) * 2);
        }

        // Calculate 1 point for every dollar spent between $50 and $100
        if (transactionAmount > 50) {
            rewardPoints += (int) Math.min(transactionAmount, 100) - 50;
        }

        return rewardPoints;
    }
}
