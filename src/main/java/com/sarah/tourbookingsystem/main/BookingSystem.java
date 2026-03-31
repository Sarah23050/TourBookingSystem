package com.sarah.tourbookingsystem.main;

import com.sarah.tourbookingsystem.model.*;
import com.sarah.tourbookingsystem.service.BookingService;
import com.sarah.tourbookingsystem.service.SearchService;

import java.util.Scanner;

public class BookingSystem {
    private Admin admin = new Admin("admin", "1234");
    private Customer currentCustomer;
    private Scanner scanner;

    private SearchService searchService = new SearchService();
    private BookingService bookingService = new BookingService();

    public BookingSystem() {
        this.scanner = new Scanner(System.in);
        this.currentCustomer = null;

        bookingService.loadBookingsFromFile();
    }

    public void start() {
        this.showRegisterPage();
    }

    private void showRegisterPage() {
        System.out.println("\ud83c\udf1f Welcome to Tour Booking System \ud83c\udf1f");
        System.out.println("1. Login as Admin");
        System.out.println("2. Register as Customer");
        System.out.println("3. Login as Customer");
        System.out.println("4. Exit");
        System.out.print("Choose (1-4): ");


        if(scanner.hasNextInt()) {
            int var1 = this.scanner.nextInt();
            scanner.nextLine();

            if (var1 == 1) {
                this.loginAdmin();
            } else if (var1 == 2) {
                this.registerCustomer();
            } else if (var1 == 3) {
                this.loginCustomer();
            } else if (var1 == 4) {
                bookingService.saveBookingsToFile();
                System.out.println("Goodbye!");
                System.exit(0);
            }
        } else {
            System.out.println("Please enter a valid number!");
            scanner.nextLine();
        }


    }

    private void loginAdmin() {
        System.out.print("Admin Username: ");
        String var1 = this.scanner.nextLine();
        System.out.print("Password: ");
        String var2 = this.scanner.nextLine();
        if (this.admin.getUsername().equals(var1) && this.admin.checkPassword(var2)) {
            this.adminPanel();
        } else {
            System.out.println("❌ Wrong credentials!");
            this.showRegisterPage();
        }

    }

    private void registerCustomer() {
        System.out.print("Enter username: ");
        String var1 = this.scanner.nextLine();
        System.out.print("Enter password: ");
        String var2 = this.scanner.nextLine();
        this.currentCustomer = new Customer(var1, var2);
        this.admin.addCustomer(this.currentCustomer);
        System.out.println("✅ Customer registered!");
        this.customerPanel();
    }

    private void loginCustomer() {
        System.out.print("Username: ");
        String username = scanner.nextLine();

        for (Customer c : admin.getAllCustomers()) {
            if (c.getUsername().equals(username)) {
                currentCustomer = c;
                customerPanel();
                return;
            }
        }

        System.out.println("❌ Customer not found!");
    }

    private void adminPanel() {
        while(true) {
            System.out.println("\n\ud83d\udc51 Admin Panel");
            System.out.println("1. Show all tours");
            System.out.println("2. Add new tour");
            System.out.println("3. Delete tour");
            System.out.println("4. Show all bookings");
            System.out.println("5. Logout");
            System.out.print("Choose: ");
            int var1 = this.scanner.nextInt();
            this.scanner.nextLine();
            switch (var1) {
                case 1:
                    this.admin.showAllTours();
                    break;
                case 2:
                    this.admin.addTour(this.scanner);
                    break;
                case 3:
                    this.admin.showAllTours();
                    System.out.print("Enter tour number to delete: ");
                    int var2 = this.scanner.nextInt() - 1;
                    this.admin.deleteTour(var2);
                    break;
                case 4:
                    this.admin.showAllBookings();
                    break;
                case 5:
                    this.showRegisterPage();
                    return;
                default:
                    System.out.println("❌ Invalid choice!");
            }
        }
    }

    private void customerPanel() {
        while(true) {
            System.out.println("\n\ud83d\udc64 Customer Panel - " + this.currentCustomer.getUsername());
            System.out.println("1. View available tours");
            System.out.println("2. Search tours");
            System.out.println("3. Filter by price");
            System.out.println("4. Book a tour");
            System.out.println("5. View my bookings");
            System.out.println("6. Logout");
            System.out.print("Choose: ");
            int var1 = this.scanner.nextInt();
            this.scanner.nextLine();
            switch (var1) {
                case 1:
                    this.admin.showAllTours();
                    break;
                case 2:
                    searchTours();
                    break;
                case 3:
                    filterByPrice();
                    break;
                case 4:
                    this.bookTour();
                    break;
                case 5:
                    this.currentCustomer.showBookedTours();
                    break;
                case 6:
                    this.showRegisterPage();
                    return;
                default:
                    System.out.println("❌ Invalid choice!");
            }
        }
    }

    private void searchTours() {
        System.out.print("Enter keyword: ");
        String keyword = scanner.nextLine();

        var results = searchService.searchTours(admin.getAllTours(), keyword);

        if(results.isEmpty()) {
            System.out.println("No tours found.");
        } else {
            for(int i = 0; i< results.size(); i++) {
                System.out.println((i+1) + ". " + results.get(i).getDetails());
            }
        }
    }

    private void filterByPrice() {
        System.out.print("Enter max price: ");
        double price = scanner.nextDouble();
        scanner.nextLine();

        var results = searchService.filterByPrice(admin.getAllTours(), price);

        for (int i = 0; i < results.size(); i++) {
            System.out.println((i+1) + ". " + results.get(i).getDetails());
        }
    }

    private void bookTour() {
        this.admin.showAllTours();
        System.out.print("Enter tour number to book: ");
        int var1 = this.scanner.nextInt() - 1;

        System.out.println("How many seats?");
        int var3 = scanner.nextInt();
        scanner.nextLine();
        if (var1 >= 0 && var1 < this.admin.getAllTours().size()) {
            Tour var2 = (Tour)this.admin.getAllTours().get(var1);
            if (var2.getAvailableSeats() >= var3) {

                for(int i = 0;i < var3; i++) {
                    var2.bookSeat();
                }

                currentCustomer.bookTour(var2);
                System.out.println("✅ Booked " + var3 + " seats!");
            } else {
                System.out.println("❌ Not enough seats!");
            }
        } else {
            System.out.println("❌ Invalid tour number!");
        }

    }
}