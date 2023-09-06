package com.example.bonus;

import com.example.bonus.domain.Customer;
import com.example.bonus.domain.CustomerRepository;
import com.example.bonus.domain.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public void run(String... args) {
        try {
            createAndSaveCustomer("Customer 1", 1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void createAndSaveCustomer(String name, int customerId) throws ParseException {
        Customer customer = new Customer();
        customer.setName(name);

        // Create and add 3 sample transactions for given customer with different amounts and dates
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // Transaction 1
        Transaction transaction1 = new Transaction();
        transaction1.setAmount(120.0); // Sample transaction amount
        transaction1.setTransactionDate(dateFormat.parse("2023-07-01")); // Sample transaction date
        customer.addTransaction(transaction1);

        // Transaction 2
        Transaction transaction2 = new Transaction();
        transaction2.setAmount(75.0); // Sample transaction amount
        transaction2.setTransactionDate(dateFormat.parse("2023-08-15")); // Sample transaction date
        customer.addTransaction(transaction2);

        // Transaction 3
        Transaction transaction3 = new Transaction();
        transaction3.setAmount(60.0); // Sample transaction amount
        transaction3.setTransactionDate(dateFormat.parse("2023-09-30")); // Sample transaction date
        customer.addTransaction(transaction3);

        // Save the customer with transactions
        customerRepository.save(customer);
    }
}
