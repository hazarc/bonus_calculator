package com.example.bonus.controller;

import com.example.bonus.domain.Customer;
import com.example.bonus.domain.CustomerRepository;
import com.example.bonus.domain.Transaction;
import com.example.bonus.domain.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

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

    @GetMapping("/{customerId}/monthly-reward-points")
    public ResponseEntity<Map<String, Integer>> calculateMonthlyRewardPoints(
            @PathVariable Long customerId) {
        // Retrieve the customer by ID from the repository
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);

        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();

            // Group transactions by month
            Map<String, List<Transaction>> transactionsByMonth = groupTransactionsByMonth(customer.getTransactions());

            // Calculate reward points for each month
            Map<String, Integer> monthlyRewardPoints = new LinkedHashMap<>();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");

            for (Map.Entry<String, List<Transaction>> entry : transactionsByMonth.entrySet()) {
                String month = entry.getKey();
                List<Transaction> monthlyTransactions = entry.getValue();
                int monthlyPoints = calculateRewardPoints(monthlyTransactions);
                monthlyRewardPoints.put(month, monthlyPoints);
            }

            return ResponseEntity.ok(monthlyRewardPoints);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private Map<String, List<Transaction>> groupTransactionsByMonth(List<Transaction> transactions) {
        // Initialize a map to store transactions grouped by month
        Map<String, List<Transaction>> transactionsByMonth = new HashMap<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");

        for (Transaction transaction : transactions) {
            String monthKey = dateFormat.format(transaction.getTransactionDate());

            // If the monthKey doesn't exist in the map, create a new list for it
            transactionsByMonth.computeIfAbsent(monthKey, k -> new ArrayList<>()).add(transaction);
        }

        return transactionsByMonth;
    }

    @PostMapping("/add")
    public ResponseEntity<Customer> addCustomer(@RequestBody Customer customer) {
        Customer createdCustomer = customerRepository.save(customer);
        return ResponseEntity.ok(createdCustomer);
    }

    // Calculate reward points based on transaction amount
    private int calculateRewardPoints(List<Transaction> transactions) {
        // Calculate reward points based on transaction amounts and rules
        int totalRewardPoints = 0;

        for (Transaction transaction : transactions) {
            totalRewardPoints += transaction.getRewardPoints();
        }
        return totalRewardPoints;
    }

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
