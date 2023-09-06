package com.example.bonus.controller;

import com.example.bonus.domain.*;
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

    @GetMapping("/{customerId}/monthly-reward-points")
    public ResponseEntity<RewardResponse> calculateMonthlyRewardPoints(
            @PathVariable Long customerId) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);

        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            Map<String, List<Transaction>> transactionsByMonth = groupTransactionsByMonth(customer.getTransactions());
            Map<String, Integer> monthlyRewardPoints = new LinkedHashMap<>();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");

            int totalRewardPoints = 0;

            for (Map.Entry<String, List<Transaction>> entry : transactionsByMonth.entrySet()) {
                String month = entry.getKey();
                List<Transaction> monthlyTransactions = entry.getValue();
                int monthlyPoints = calculateRewardPoints(monthlyTransactions);
                monthlyRewardPoints.put(month, monthlyPoints);
                totalRewardPoints += monthlyPoints;
            }

            RewardResponse rewardResponse = new RewardResponse();
            rewardResponse.setMonthlyRewards(monthlyRewardPoints);
            rewardResponse.setTotalRewards(totalRewardPoints);

            return ResponseEntity.ok(rewardResponse);
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


    // Calculate reward points based on transaction amount
    private int calculateRewardPoints(List<Transaction> transactions) {
        // Calculate reward points based on transaction amounts and rules
        int monthlyPoints = 0;
        for (Transaction transaction : transactions) {
            monthlyPoints += calculateRewardPoints(transaction.getAmount());
        }
        return monthlyPoints;
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

    // GET endpoint to list all customers and their transactions
    @GetMapping("/list")
    public ResponseEntity<List<Customer>> listAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return ResponseEntity.ok(customers);
    }

}
