package com.tourbookingsystem.main;

import com.tourbookingsystem.model.Admin;
import com.tourbookingsystem.model.Booking;
import com.tourbookingsystem.model.Customer;
import com.tourbookingsystem.model.Tour;
import com.tourbookingsystem.service.BookingService;
import com.tourbookingsystem.service.CustomerService;
import com.tourbookingsystem.service.TourService;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.FileChooser;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class TourBookingFxApp extends Application {

    private final Admin admin = new Admin("admin", "1234");
    private final BookingService bookingService = new BookingService();
    private final CustomerService customerService = new CustomerService();
    private final TourService tourService = new TourService();
    private static final String FALLBACK_IMAGE_URL = "https://picsum.photos/seed/wanderlust-default/900/600";

    private Customer currentCustomer;
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        loadData();

        primaryStage.setTitle("Tour Booking Studio");
        primaryStage.setMinWidth(1200);
        primaryStage.setMinHeight(800);
        primaryStage.setOnCloseRequest(event -> saveAllData());

        showAuthScene();
        primaryStage.show();
    }

    private void loadData() {
        for (Customer customer : customerService.loadCustomersFromFile()) {
            admin.addCustomer(customer);
        }

        List<Tour> loadedTours = tourService.loadToursFromFile();
        if (loadedTours.isEmpty()) {
            admin.loadSampleTours();
        } else {
            for (Tour tour : loadedTours) {
                admin.addTour(tour);
            }
        }

        bookingService.loadBookingsFromFile();
        restoreCustomersFromSavedBookings();
    }

    private void restoreCustomersFromSavedBookings() {
        for (Booking booking : bookingService.getAllBookings()) {
            Customer customer = findCustomerByUsername(booking.getCustomerName());
            if (customer == null) {
                customer = new Customer(booking.getCustomerName(), "");
                admin.addCustomer(customer);
            }
            customer.getBookings().add(booking);
        }
    }

    private Customer findCustomerByUsername(String username) {
        for (Customer customer : admin.getAllCustomers()) {
            if (customer.getUsername().equals(username)) {
                return customer;
            }
        }
        return null;
    }

    private void saveAllData() {
        customerService.saveCustomersToFile(admin.getAllCustomers());
        bookingService.saveBookingsToFile();
        tourService.saveToursToFile(admin.getAllTours());
    }

    private Scene styledScene(Parent root) {
        Scene scene = new Scene(root, 1200, 800);
        String css = getClass().getResource("/styles/app.css").toExternalForm();
        scene.getStylesheets().add(css);
        return scene;
    }

    private void showAuthScene() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("app-root");

        VBox branding = new VBox(18);
        branding.getStyleClass().add("branding-panel");
        branding.setPadding(new Insets(60));

        Label brandTitle = new Label("Wanderlust");
        brandTitle.getStyleClass().add("brand-title");

        Label brandSubtitle = new Label("Handpicked adventures for every kind of traveler");
        brandSubtitle.getStyleClass().add("brand-subtitle");
        brandSubtitle.setWrapText(true);

        branding.getChildren().addAll(brandTitle, new Region(), brandSubtitle);

        VBox authPanel = new VBox(20);
        authPanel.getStyleClass().add("auth-panel");
        authPanel.setPadding(new Insets(36));
        authPanel.setMaxWidth(380);

        HBox tabButtons = new HBox(16);
        tabButtons.getStyleClass().add("auth-tabs");
        tabButtons.setPrefHeight(40);

        Button customerTab = new Button("Customer");
        customerTab.getStyleClass().addAll("auth-tab-button", "active");
        customerTab.setPrefWidth(100);

        Button adminTab = new Button("Admin");
        adminTab.getStyleClass().add("auth-tab-button");
        adminTab.setPrefWidth(100);

        tabButtons.getChildren().addAll(customerTab, adminTab);

        VBox loginPanel = new VBox(14);
        TextField loginUser = new TextField();
        loginUser.setPromptText("Username");
        loginUser.getStyleClass().add("search-box");

        PasswordField loginPassword = new PasswordField();
        loginPassword.setPromptText("Password");
        loginPassword.getStyleClass().add("search-box");

        Label loginMessage = new Label();
        loginMessage.getStyleClass().add("error-text");

        Button loginButton = new Button("Sign In");
        loginButton.getStyleClass().addAll("btn", "btn-primary");
        loginButton.setMaxWidth(Double.MAX_VALUE);
        loginButton.setPrefHeight(40);

        loginPassword.setOnAction(event -> loginButton.fire());

        loginPanel.getChildren().addAll(loginUser, loginPassword, loginButton, loginMessage);

        VBox registerPanel = new VBox(14);
        registerPanel.setVisible(false);

        TextField registerUser = new TextField();
        registerUser.setPromptText("Username");
        registerUser.getStyleClass().add("search-box");

        PasswordField registerPassword = new PasswordField();
        registerPassword.setPromptText("Password");
        registerPassword.getStyleClass().add("search-box");

        PasswordField registerConfirm = new PasswordField();
        registerConfirm.setPromptText("Confirm Password");
        registerConfirm.getStyleClass().add("search-box");

        Label registerMessage = new Label();
        registerMessage.getStyleClass().add("error-text");

        Button registerButton = new Button("Create Account");
        registerButton.getStyleClass().addAll("btn", "btn-primary");
        registerButton.setMaxWidth(Double.MAX_VALUE);
        registerButton.setPrefHeight(40);

        registerPanel.getChildren().addAll(registerUser, registerPassword, registerConfirm, registerButton, registerMessage);

        Label noAccountLabel = new Label("Don't have an account? ");
        Hyperlink registerLink = new Hyperlink("Sign up");
        registerLink.getStyleClass().add("register-link");

        HBox registerPrompt = new HBox(2);
        registerPrompt.setAlignment(Pos.CENTER);
        registerPrompt.getChildren().addAll(noAccountLabel, registerLink);

        customerTab.setOnAction(event -> {
            loginPanel.setVisible(true);
            registerPanel.setVisible(false);
            customerTab.getStyleClass().add("active");
            adminTab.getStyleClass().remove("active");
        });

        adminTab.setOnAction(event -> {
            loginPanel.setVisible(true);
            registerPanel.setVisible(false);
            adminTab.getStyleClass().add("active");
            customerTab.getStyleClass().remove("active");
        });

        registerLink.setOnAction(event -> {
            loginPanel.setVisible(false);
            registerPanel.setVisible(true);
        });

        loginButton.setOnAction(event -> {
            String username = loginUser.getText().trim();
            String password = loginPassword.getText();

            if (adminTab.getStyleClass().contains("active")) {
                if (admin.getUsername().equals(username) && admin.checkPassword(password)) {
                    showAdminDashboard();
                    return;
                } else {
                    loginMessage.setText("Invalid admin credentials.");
                }
            } else {
                if (containsComma(username) || containsComma(password)) {
                    loginMessage.setText("Invalid format.");
                    return;
                }

                if (isBlank(username) || isBlank(password)) {
                    loginMessage.setText("Enter both username and password.");
                    return;
                }

                Customer customer = findCustomerByUsername(username);
                if (customer != null && customer.checkPassword(password)) {
                    currentCustomer = customer;
                    showCustomerDashboard();
                } else {
                    loginMessage.setText("Invalid username or password.");
                }
            }
        });

        registerButton.setOnAction(event -> {
            String username = registerUser.getText().trim();
            String password = registerPassword.getText();
            String confirm = registerConfirm.getText();

            if (containsComma(username) || containsComma(password)) {
                registerMessage.setText("Username/password cannot contain commas.");
                return;
            }

            if (isBlank(username)) {
                registerMessage.setText("Username cannot be empty.");
                return;
            }

            if (isBlank(password)) {
                registerMessage.setText("Password cannot be empty.");
                return;
            }

            if (!password.equals(confirm)) {
                registerMessage.setText("Passwords do not match.");
                return;
            }

            if (findCustomerByUsername(username) != null) {
                registerMessage.setText("Username already exists.");
                return;
            }

            currentCustomer = new Customer(username, password);
            admin.addCustomer(currentCustomer);
            saveAllData();
            showCustomerDashboard();
        });

        authPanel.getChildren().addAll(tabButtons, new Separator(), loginPanel, registerPanel, registerPrompt);

        root.setLeft(branding);
        root.setCenter(authPanel);

        Scene scene = styledScene(root);
        primaryStage.setScene(scene);
    }

    private void showCustomerDashboard() {
        StackPane rootLayer = new StackPane();
        BorderPane root = new BorderPane();
        rootLayer.getChildren().add(root);
        root.getStyleClass().add("app-root");
        root.getStyleClass().add("customer-shell");

        HBox topBar = new HBox(16);
        topBar.setPadding(new Insets(14, 24, 14, 24));
        topBar.getStyleClass().add("top-bar");
        topBar.setAlignment(Pos.CENTER_LEFT);

        Label welcome = new Label("Wanderlust");
        welcome.getStyleClass().add("top-title");

        Label subtitle = new Label("Discover Tours");
        subtitle.getStyleClass().add("top-subtitle");

        VBox titleBlock = new VBox(2, welcome, subtitle);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button myBookingsButton = new Button("My Bookings");
        myBookingsButton.getStyleClass().addAll("btn", "btn-secondary");
        myBookingsButton.setOnAction(event -> showMyBookingsPage());

        Button profileButton = new Button("My Profile");
        profileButton.getStyleClass().addAll("btn", "btn-secondary");
        profileButton.setOnAction(event -> showMyProfilePage(rootLayer));

        Button logoutButton = new Button("Logout");
        logoutButton.getStyleClass().addAll("btn", "btn-danger");
        logoutButton.setOnAction(event -> {
            currentCustomer = null;
            saveAllData();
            showAuthScene();
        });

        topBar.getChildren().addAll(titleBlock, spacer, myBookingsButton, profileButton, logoutButton);

        int totalTours = admin.getAllTours().size();
        int totalBookings = currentCustomer.getBookings().size();

        HBox metricsRow = new HBox(14);
        metricsRow.getStyleClass().add("metrics-row");
        metricsRow.setMaxWidth(Double.MAX_VALUE);

        VBox toursMetric = metricCard("Tours Available", String.valueOf(totalTours));
        VBox bookingsMetric = metricCard("My Active Bookings", String.valueOf(totalBookings));
        HBox.setHgrow(toursMetric, Priority.ALWAYS);
        HBox.setHgrow(bookingsMetric, Priority.ALWAYS);
        metricsRow.getChildren().addAll(toursMetric, bookingsMetric);

        VBox filterPanel = new VBox(12);
        filterPanel.setPadding(new Insets(20));
        filterPanel.getStyleClass().add("filter-control");

        TextField searchField = new TextField();
        searchField.setPromptText("Search tours by name...");
        searchField.getStyleClass().add("search-box");
        searchField.setPrefHeight(36);

        Label pageMessage = new Label();
        pageMessage.getStyleClass().add("success-text");

        filterPanel.getChildren().addAll(searchField, pageMessage);

        TilePane cardsPane = new TilePane();
        cardsPane.setAlignment(Pos.TOP_LEFT);
        cardsPane.setPrefColumns(3);
        cardsPane.setHgap(10);
        cardsPane.setVgap(10);
        cardsPane.setPadding(new Insets(4));
        cardsPane.setTileAlignment(Pos.TOP_LEFT);

        ScrollPane scrollPane = new ScrollPane(cardsPane);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("tour-scroll");
        cardsPane.prefTileWidthProperty().bind(Bindings.createDoubleBinding(
            () -> {
                double viewportWidth = scrollPane.getViewportBounds().getWidth();
                double padding = 8; // left (4) + right (4)
                double totalGap = 20; // hgap (10) * (3 columns - 1)
                double availableWidth = viewportWidth - padding - totalGap;

                // Small buffer avoids rounding pushes that can wrap card 3.
                return Math.max(220, (availableWidth - 5) / 3);
            },
            scrollPane.viewportBoundsProperty()
        ));

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            updateTourCards(cardsPane, newVal, pageMessage);
        });

        updateTourCards(cardsPane, "", pageMessage);

        VBox centerBox = new VBox(8);
        centerBox.getChildren().addAll(metricsRow, filterPanel, scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        centerBox.setPadding(new Insets(6, 6, 6, 6));

        root.setTop(topBar);
        root.setCenter(centerBox);

        Scene scene = styledScene(rootLayer);
        primaryStage.setScene(scene);
    }

    private void updateTourCards(TilePane cardsPane, String searchText, Label message) {
        cardsPane.getChildren().clear();

        List<Tour> filtered = admin.getAllTours().stream()
                .filter(t -> t.getName().toLowerCase().contains(searchText.toLowerCase()))
            .collect(Collectors.collectingAndThen(
                Collectors.toMap(
                    tour -> tour.getName().trim().toLowerCase(),
                    tour -> tour,
                    (first, second) -> first,
                    java.util.LinkedHashMap::new
                ),
                map -> List.copyOf(map.values())
            ));

        if (filtered.isEmpty()) {
            message.setText("No tours match your search.");
        } else {
            message.setText("Found " + filtered.size() + " tour(s).");
        }

        for (int i = 0; i < filtered.size(); i++) {
            cardsPane.getChildren().add(buildTourCard(filtered.get(i), message, cardsPane));
        }
    }

    private VBox metricCard(String labelText, String valueText) {
        Label label = new Label(labelText);
        label.getStyleClass().add("metric-label");

        Label value = new Label(valueText);
        value.getStyleClass().add("metric-value");

        VBox card = new VBox(4, label, value);
        card.getStyleClass().add("metric-card");
        card.setPadding(new Insets(14));
        card.setMaxWidth(Double.MAX_VALUE);
        return card;
    }

    private VBox buildTourCard(Tour tour, Label messageLabel, TilePane cardsPane) {
        VBox card = new VBox(8);
        card.getStyleClass().add("tour-card");
        card.setMinWidth(0);
        card.setMaxWidth(Double.MAX_VALUE);

        String displayImage = tour.getImageUrl() != null && !tour.getImageUrl().isBlank()
            ? tour.getImageUrl()
            : FALLBACK_IMAGE_URL;

        ImageView imageView = new ImageView(new Image(displayImage, 360, 200, false, true, true));
        imageView.fitWidthProperty().bind(card.widthProperty());
        imageView.setFitHeight(160);
        imageView.setPreserveRatio(false);

        StackPane imageContainer = new StackPane(imageView);
        imageContainer.getStyleClass().add("tour-image-wrap");

        Label title = new Label(tour.getName());
        title.getStyleClass().add("tour-title");
        title.setWrapText(true);

        Label subtitle = new Label(tour.getDescription());
        subtitle.getStyleClass().add("tour-subtitle");
        subtitle.setWrapText(true);

        Label price = new Label("$" + String.format("%.2f", tour.getPrice()));
        price.getStyleClass().add("meta-text");

        Label seatLabel = new Label("Seats: " + tour.getAvailableSeats());
        seatLabel.getStyleClass().add("meta-text");

        int maxSeats = Math.max(1, tour.getAvailableSeats());
        Spinner<Integer> seatSpinner = new Spinner<>(1, maxSeats, 1);
        seatSpinner.setEditable(false);

        DatePicker bookingDatePicker = new DatePicker(LocalDate.now());
        bookingDatePicker.setPromptText("Booking date");
        bookingDatePicker.getStyleClass().add("search-box");

        Button bookButton = new Button("Book Now");
        bookButton.getStyleClass().addAll("btn", "btn-primary");
        bookButton.setPrefHeight(38);
        bookButton.setMaxWidth(Double.MAX_VALUE);
        bookButton.setDisable(tour.getAvailableSeats() <= 0);
        bookButton.setOnAction(event -> {
            int seats = seatSpinner.getValue();
            if (tour.getAvailableSeats() < seats) {
                messageLabel.getStyleClass().remove("success-text");
                messageLabel.getStyleClass().add("error-text");
                messageLabel.setText("Not enough seats for " + tour.getName() + ".");
                return;
            }

            LocalDate bookingDate = bookingDatePicker.getValue();
            if (bookingDate == null) {
                messageLabel.getStyleClass().remove("success-text");
                messageLabel.getStyleClass().add("error-text");
                messageLabel.setText("Pick a booking date.");
                return;
            }

            for (int i = 0; i < seats; i++) {
                tour.bookSeat();
            }

            Booking booking = currentCustomer.bookTour(tour, seats, bookingDate);
            bookingService.addBooking(booking);
            bookingService.saveBookingsToFile();

            messageLabel.getStyleClass().remove("error-text");
            if (!messageLabel.getStyleClass().contains("success-text")) {
                messageLabel.getStyleClass().add("success-text");
            }
            messageLabel.setText("✓ Booked " + seats + " seat(s) for " + tour.getName());
            showCustomerDashboard();
        });

        card.setPadding(new Insets(0));
        VBox cardContent = new VBox(6, title, subtitle, price, seatLabel, seatSpinner, bookingDatePicker, bookButton);
        cardContent.setPadding(new Insets(12));
        card.getChildren().addAll(imageContainer, cardContent);

        return card;
    }

    private void showMyBookingsPage() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("app-root");

        HBox topBar = new HBox(12);
        topBar.setPadding(new Insets(14, 24, 14, 24));
        topBar.getStyleClass().add("top-bar");

        Label title = new Label("My Bookings");
        title.getStyleClass().add("top-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button backButton = new Button("← Back");
        backButton.getStyleClass().addAll("btn", "btn-ghost");
        backButton.setOnAction(event -> showCustomerDashboard());

        topBar.getChildren().addAll(title, spacer, backButton);

        VBox contentPanel = new VBox(16);
        contentPanel.setPadding(new Insets(24));

        if (currentCustomer.getBookings().isEmpty()) {
            Label emptyLabel = new Label("No bookings yet. Start exploring tours!");
            emptyLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #999999;");
            contentPanel.getChildren().add(emptyLabel);
        } else {
            ListView<Booking> bookingList = new ListView<>(
                    FXCollections.observableArrayList(currentCustomer.getBookings())
            );
            bookingList.setPrefHeight(400);
            bookingList.setCellFactory(list -> new ListCell<>() {
                @Override
                protected void updateItem(Booking item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setGraphic(null);
                    } else {
                        HBox cell = new HBox(16);
                        cell.setPadding(new Insets(12));
                        cell.setStyle("-fx-border-color: #F0E6D3; -fx-border-width: 0 0 1 0; -fx-background-color: white;");

                        VBox info = new VBox(4);
                        Label tourName = new Label(item.getTourName());
                        tourName.setStyle("-fx-font-weight: 600; -fx-font-size: 14px;");
                        Label seats = new Label("Seats: " + item.getNumberOfTickets());
                        seats.setStyle("-fx-text-fill: #666666;");
                        Label bookedDate = new Label("Date: " + item.getBookingDate().format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
                        bookedDate.setStyle("-fx-text-fill: #888888;");
                        info.getChildren().addAll(tourName, seats, bookedDate);

                        Region filler = new Region();
                        HBox.setHgrow(filler, Priority.ALWAYS);

                        Button cancelBtn = new Button("Cancel");
                        cancelBtn.getStyleClass().addAll("btn", "btn-danger");
                        cancelBtn.setOnAction(e -> {
                            int index = getIndex();
                            Booking canceled = currentCustomer.cancelBooking(index);
                            if (canceled != null) {
                                bookingService.cancelBooking(canceled.getBookingId());
                                bookingService.saveBookingsToFile();
                                for (Tour tour : admin.getAllTours()) {
                                    if (tour.getName().equals(canceled.getTourName())) {
                                        for (int i = 0; i < canceled.getNumberOfTickets(); i++) {
                                            tour.releaseSeat();
                                        }
                                        break;
                                    }
                                }
                                showMyBookingsPage();
                            }
                        });

                        cell.getChildren().addAll(info, filler, cancelBtn);
                        setGraphic(cell);
                    }
                }
            });

            contentPanel.getChildren().add(bookingList);
        }

        root.setTop(topBar);
        root.setCenter(contentPanel);

        primaryStage.setScene(styledScene(root));
    }

    private void showMyProfilePage(StackPane rootLayer) {
        StackPane overlay = new StackPane();
        overlay.getStyleClass().add("dialog-overlay");

        VBox profileCard = new VBox(18);
        profileCard.getStyleClass().add("dialog-card");
        profileCard.setMaxWidth(380);

        Label title = new Label("My Profile");
        title.getStyleClass().add("panel-title");

        Label usernameLabel = new Label("Username");
        usernameLabel.setStyle("-fx-font-weight: 600;");
        TextField usernameField = new TextField(currentCustomer.getUsername());
        usernameField.getStyleClass().add("search-box");

        Button usernameButton = new Button("Update Username");
        usernameButton.getStyleClass().addAll("btn", "btn-secondary");
        usernameButton.setMaxWidth(Double.MAX_VALUE);

        Label changePasswordLabel = new Label("Change Password");
        changePasswordLabel.setStyle("-fx-font-weight: 600; -fx-font-size: 14px; -fx-margin-top: 16;");

        PasswordField currentPassword = new PasswordField();
        currentPassword.setPromptText("Current password");
        currentPassword.getStyleClass().add("search-box");

        PasswordField newPassword = new PasswordField();
        newPassword.setPromptText("New password");
        newPassword.getStyleClass().add("search-box");

        PasswordField confirmPassword = new PasswordField();
        confirmPassword.setPromptText("Confirm new password");
        confirmPassword.getStyleClass().add("search-box");

        Label feedback = new Label();
        feedback.getStyleClass().add("error-text");

        usernameButton.setOnAction(event -> {
            String newUsername = usernameField.getText().trim();

            if (isBlank(newUsername)) {
                feedback.setText("Username cannot be empty.");
                return;
            }

            if (containsComma(newUsername)) {
                feedback.setText("Username cannot contain commas.");
                return;
            }

            Customer existing = findCustomerByUsername(newUsername);
            if (existing != null && existing != currentCustomer) {
                feedback.setText("That username is already taken.");
                return;
            }

            if (newUsername.equals(currentCustomer.getUsername())) {
                feedback.getStyleClass().remove("error-text");
                if (!feedback.getStyleClass().contains("success-text")) {
                    feedback.getStyleClass().add("success-text");
                }
                feedback.setText("No changes made.");
                return;
            }

            currentCustomer.setUsername(newUsername);
            for (Booking booking : currentCustomer.getBookings()) {
                booking.setCustomerName(newUsername);
            }
            customerService.saveCustomersToFile(admin.getAllCustomers());
            bookingService.saveBookingsToFile();

            feedback.getStyleClass().remove("error-text");
            if (!feedback.getStyleClass().contains("success-text")) {
                feedback.getStyleClass().add("success-text");
            }
            feedback.setText("Username updated successfully.");
        });

        Button saveButton = new Button("Update Password");
        saveButton.getStyleClass().addAll("btn", "btn-primary");
        saveButton.setMaxWidth(Double.MAX_VALUE);
        saveButton.setOnAction(event -> {
            if (!currentCustomer.checkPassword(currentPassword.getText())) {
                feedback.setText("Current password is incorrect.");
                return;
            }

            if (isBlank(newPassword.getText())) {
                feedback.setText("New password cannot be empty.");
                return;
            }

            if (containsComma(newPassword.getText())) {
                feedback.setText("Password cannot contain commas.");
                return;
            }

            if (!newPassword.getText().equals(confirmPassword.getText())) {
                feedback.setText("Confirmation does not match.");
                return;
            }

            currentCustomer.setPassword(newPassword.getText());
            customerService.saveCustomersToFile(admin.getAllCustomers());
            feedback.getStyleClass().remove("error-text");
            feedback.getStyleClass().add("success-text");
            feedback.setText("✓ Password updated successfully.");
        });

        Button closeButton = new Button("Close");
        closeButton.getStyleClass().addAll("btn", "btn-ghost");
        closeButton.setMaxWidth(Double.MAX_VALUE);

        HBox actions = new HBox(8);
        actions.setAlignment(Pos.CENTER);
        actions.getChildren().addAll(saveButton, closeButton);

        profileCard.getChildren().addAll(
                title,
                usernameLabel,
            usernameField,
            usernameButton,
                new Separator(),
                changePasswordLabel,
                currentPassword,
                newPassword,
                confirmPassword,
                feedback,
                actions
        );

        overlay.getChildren().add(profileCard);
        overlay.setPadding(new Insets(60));
        StackPane.setAlignment(profileCard, Pos.CENTER);

        rootLayer.getChildren().add(overlay);

        closeButton.setOnAction(event -> {
            rootLayer.getChildren().remove(overlay);
            showCustomerDashboard();
        });

        overlay.setOnMouseClicked(event -> {
            if (event.getTarget() == overlay) {
                rootLayer.getChildren().remove(overlay);
                showCustomerDashboard();
            }
        });
    }

    private void showAdminDashboard() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("app-root");

        HBox topBar = new HBox(12);
        topBar.setPadding(new Insets(14, 24, 14, 24));
        topBar.getStyleClass().add("top-bar");

        Label welcome = new Label("Admin Panel");
        welcome.getStyleClass().add("top-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button logoutButton = new Button("Logout");
        logoutButton.getStyleClass().addAll("btn", "btn-danger");
        logoutButton.setOnAction(event -> {
            saveAllData();
            showAuthScene();
        });

        topBar.getChildren().addAll(welcome, spacer, logoutButton);

        VBox listPanel = new VBox(10);
        listPanel.setPadding(new Insets(16));

        Label listTitle = new Label("Tours");
        listTitle.getStyleClass().add("panel-title");

        ListView<Tour> tourList = new ListView<>(FXCollections.observableArrayList(admin.getAllTours()));
        tourList.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Tour item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName() + " ($" + String.format("%.2f", item.getPrice()) + ") - Seats: " + item.getAvailableSeats());
                }
            }
        });

        Button deleteButton = new Button("Delete Selected");
        deleteButton.getStyleClass().addAll("btn", "btn-danger");
        deleteButton.setMaxWidth(Double.MAX_VALUE);
        deleteButton.setOnAction(event -> {
            int index = tourList.getSelectionModel().getSelectedIndex();
            if (index >= 0) {
                admin.getAllTours().remove(index);
                tourList.setItems(FXCollections.observableArrayList(admin.getAllTours()));
                tourList.refresh();
            }
        });

        listPanel.getChildren().addAll(listTitle, tourList, deleteButton);
        VBox.setVgrow(tourList, Priority.ALWAYS);

        VBox formPanel = new VBox(10);
        formPanel.setPadding(new Insets(16));
        formPanel.setPrefWidth(360);
        formPanel.getStyleClass().add("booking-panel");

        Label formTitle = new Label("Add / Edit Tour");
        formTitle.getStyleClass().add("panel-title");

        TextField nameField = new TextField();
        nameField.setPromptText("Tour name");
        nameField.getStyleClass().add("search-box");

        TextField descField = new TextField();
        descField.setPromptText("Description");
        descField.getStyleClass().add("search-box");

        TextField priceField = new TextField();
        priceField.setPromptText("Price ($)");
        priceField.getStyleClass().add("search-box");

        TextField imageField = new TextField();
        imageField.setPromptText("Image URL or file path");
        imageField.getStyleClass().add("search-box");

        Button browseImageButton = new Button("Upload Image");
        browseImageButton.getStyleClass().addAll("btn", "btn-ghost");
        browseImageButton.setMaxWidth(Double.MAX_VALUE);
        browseImageButton.setOnAction(event -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Choose Tour Image");
            chooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.webp")
            );
            File file = chooser.showOpenDialog(primaryStage);
            if (file != null) {
                imageField.setText(file.toURI().toString());
            }
        });

        Spinner<Integer> seatsSpinner = new Spinner<>(1, 1000, 10);
        seatsSpinner.setEditable(false);

        TextField dateField = new TextField();
        dateField.setPromptText("Date (number)");
        dateField.getStyleClass().add("search-box");

        Label feedback = new Label();
        feedback.getStyleClass().add("error-text");

        Button addButton = new Button("Add Tour");
        addButton.getStyleClass().addAll("btn", "btn-primary");
        addButton.setMaxWidth(Double.MAX_VALUE);
        addButton.setOnAction(event -> {
            try {
                String name = nameField.getText().trim();
                String desc = descField.getText().trim();
                double price = Double.parseDouble(priceField.getText());
                int seats = seatsSpinner.getValue();
                double date = Double.parseDouble(dateField.getText());
                String imageUrl = imageField.getText().trim();

                if (isBlank(name) || isBlank(desc)) {
                    feedback.setText("Name and description required.");
                    return;
                }

                Tour newTour = new Tour(name, desc, price, seats, date, imageUrl);
                admin.getAllTours().add(newTour);
                tourList.setItems(FXCollections.observableArrayList(admin.getAllTours()));
                tourList.refresh();

                nameField.clear();
                descField.clear();
                priceField.clear();
                dateField.clear();
                imageField.clear();
                seatsSpinner.getValueFactory().setValue(10);
                feedback.getStyleClass().remove("error-text");
                feedback.getStyleClass().add("success-text");
                feedback.setText("✓ Tour added.");
            } catch (NumberFormatException e) {
                feedback.getStyleClass().remove("success-text");
                feedback.getStyleClass().add("error-text");
                feedback.setText("Price and date must be numbers.");
            }
        });

        Button editButton = new Button("Update Selected");
        editButton.getStyleClass().addAll("btn", "btn-secondary");
        editButton.setMaxWidth(Double.MAX_VALUE);
        editButton.setOnAction(event -> {
            int index = tourList.getSelectionModel().getSelectedIndex();
            if (index >= 0) {
                try {
                    Tour selected = admin.getAllTours().get(index);
                    String name = nameField.getText().trim();
                    String desc = descField.getText().trim();
                    double price = Double.parseDouble(priceField.getText());
                    int seats = seatsSpinner.getValue();
                    double date = Double.parseDouble(dateField.getText());
                    String imageUrl = imageField.getText().trim();

                    if (isBlank(name) || isBlank(desc)) {
                        feedback.setText("Name and description required.");
                        return;
                    }

                    selected.setName(name);
                    selected.setDescription(desc);
                    selected.setPrice(price);
                    selected.setAvailableSeats(seats);
                    selected.setDate(date);
                    selected.setImageUrl(imageUrl);

                    tourList.refresh();
                    nameField.clear();
                    descField.clear();
                    priceField.clear();
                    dateField.clear();
                    imageField.clear();
                    seatsSpinner.getValueFactory().setValue(10);
                    feedback.getStyleClass().remove("error-text");
                    feedback.getStyleClass().add("success-text");
                    feedback.setText("✓ Tour updated.");
                } catch (NumberFormatException e) {
                    feedback.getStyleClass().remove("success-text");
                    feedback.getStyleClass().add("error-text");
                    feedback.setText("Price and date must be numbers.");
                }
            }
        });

        tourList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                nameField.setText(newVal.getName());
                descField.setText(newVal.getDescription());
                priceField.setText(String.valueOf(newVal.getPrice()));
                seatsSpinner.getValueFactory().setValue((int)newVal.getAvailableSeats());
                dateField.setText(String.valueOf(newVal.getDate()));
                imageField.setText(newVal.getImageUrl() == null ? "" : newVal.getImageUrl());
            }
        });

        formPanel.getChildren().addAll(
                formTitle,
                new Label("Name:"), nameField,
                new Label("Description:"), descField,
                new Label("Price:"), priceField,
                new Label("Seats:"), seatsSpinner,
                new Label("Date:"), dateField,
                new Label("Image:"), imageField,
                browseImageButton,
                feedback,
                addButton,
                editButton
        );

        root.setTop(topBar);
        root.setCenter(listPanel);
        root.setRight(formPanel);

        primaryStage.setScene(styledScene(root));
    }

    private boolean containsComma(String value) {
        return value != null && value.contains(",");
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
