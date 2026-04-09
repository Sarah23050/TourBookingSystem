package com.tourbookingsystem.main;

import com.tourbookingsystem.model.*;
import com.tourbookingsystem.service.BookingService;
import com.tourbookingsystem.service.CustomerService;
import com.tourbookingsystem.service.SearchService;

import java.util.Scanner;

public class BookingSystem {
    private Admin admin = new Admin("admin", "1234");
    private Customer currentCustomer;
    private Scanner scanner;

    private SearchService searchService = new SearchService();
    private BookingService bookingService = new BookingService();
    private CustomerService customerService = new CustomerService();

    public BookingSystem() {
        this.scanner = new Scanner(System.in);
        this.currentCustomer = null;
        loadCustomersFromFile();
        bookingService.loadBookingsFromFile();
        restoreCustomersFromSavedBookings();

    }

    private void restoreCustomersFromSavedBookings() {
        for (Booking booking : bookingService.getAllBookings()) {
            Customer customer = null;

            for (Customer existing : admin.getAllCustomers()) {
                if (existing.getUsername().equals(booking.getCustomerName())) {
                    customer = existing;
                    break;
                }
            }

            if (customer == null) {
                customer = new Customer(booking.getCustomerName(), "");
                admin.addCustomer(customer);
            }

            customer.getBookings().add(booking);
        }
    }

    private void loadCustomersFromFile() {
        for (Customer customer : customerService.loadCustomersFromFile()) {
            admin.addCustomer(customer);
        }
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
                customerService.saveCustomersToFile(admin.getAllCustomers());
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

        if (isBlank(var2)) {
            System.out.println("❌ Password cannot be empty.");
            this.showRegisterPage();
            return;
        }

        if (containsComma(var1) || containsComma(var2)) {
            System.out.println("❌ Username and password cannot contain commas.");
            this.showRegisterPage();
            return;
        }

        for (Customer customer : admin.getAllCustomers()) {
            if (customer.getUsername().equals(var1)) {
                System.out.println("❌ Username already exists!");
                this.showRegisterPage();
                return;
            }
        }

        this.currentCustomer = new Customer(var1, var2);
        this.admin.addCustomer(this.currentCustomer);
        customerService.saveCustomersToFile(admin.getAllCustomers());
        System.out.println("✅ Customer registered!");
        this.customerPanel();
    }

    private void loginCustomer() {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        if (containsComma(username) || containsComma(password)) {
            System.out.println("❌ Invalid username or password!");
            return;
        }

        for (Customer c : admin.getAllCustomers()) {
            if (c.getUsername().equals(username) && c.checkPassword(password)) {
                currentCustomer = c;
                customerPanel();
                return;
            }
        }

        System.out.println("❌ Invalid username or password!");
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
            System.out.println("5. Cancel booking");
            System.out.println("6. View my bookings");
            System.out.println("7. Change password");
            System.out.println("8. Logout");
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
                    cancelBooking();
                    break;
                case 6:
                    this.currentCustomer.showBookedTours();
                    break;
                case 7:
                    changeCustomerPassword();
                    break;
                case 8:
                    this.showRegisterPage();
                    return;
                default:
                    System.out.println("❌ Invalid choice!");
            }
        }
    }

    private void changeCustomerPassword() {
        System.out.print("Enter current password: ");
        String currentPassword = scanner.nextLine();

        if (!currentCustomer.checkPassword(currentPassword)) {
            System.out.println("❌ Current password is incorrect!");
            return;
        }

        System.out.print("Enter new password: ");
        String newPassword = scanner.nextLine();
        System.out.print("Confirm new password: ");
        String confirmPassword = scanner.nextLine();

        if (isBlank(newPassword)) {
            System.out.println("❌ Password cannot be empty.");
            return;
        }

        if (containsComma(newPassword)) {
            System.out.println("❌ Password cannot contain commas.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            System.out.println("❌ Password confirmation does not match.");
            return;
        }

        currentCustomer.setPassword(newPassword);
        customerService.saveCustomersToFile(admin.getAllCustomers());
        System.out.println("✅ Password updated successfully.");
    }

    private boolean containsComma(String value) {
        return value != null && value.contains(",");
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
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

        if(results.isEmpty()) {
            System.out.println("No tours found under this price. Try again.");
            return;
        }

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

                Booking newBooking = currentCustomer.bookTour(var2, var3);
                bookingService.addBooking(newBooking);
                bookingService.saveBookingsToFile();

                System.out.println("✅ Booked " + var3 + " seats!");
            } else {
                System.out.println("❌ Not enough seats!");
            }
        } else {
            System.out.println("❌ Invalid tour number!");
        }

    }

    private void cancelBooking() {
        currentCustomer.showBookedTours();

        System.out.print("Enter booking number to cancel: ");
        int index = scanner.nextInt() - 1;
        scanner.nextLine();

        Booking cancelled = currentCustomer.cancelBooking(index);

        if (cancelled != null) {
            bookingService.cancelBooking(cancelled.getBookingId());
            bookingService.saveBookingsToFile();
        }
    }
}