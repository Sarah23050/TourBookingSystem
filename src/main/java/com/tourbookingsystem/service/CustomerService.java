package com.tourbookingsystem.service;

import com.tourbookingsystem.model.Customer;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CustomerService {

    public List<Customer> loadCustomersFromFile() {
        List<Customer> customers = new ArrayList<>();
        File file = new File("customers.txt");

        if (!file.exists()) {
            return customers;
        }

        try (Scanner reader = new Scanner(file)) {
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] parts = line.split(",", 2);

                if (parts.length == 2) {
                    customers.add(new Customer(parts[0], parts[1]));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return customers;
    }

    public void saveCustomersToFile(List<Customer> customers) {
        try (PrintWriter writer = new PrintWriter("customers.txt")) {
            for (Customer customer : customers) {
                writer.println(customer.getUsername() + "," + customer.getPassword());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}