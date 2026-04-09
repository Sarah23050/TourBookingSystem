package com.tourbookingsystem.main;

import com.tourbookingsystem.model.Admin;
import com.tourbookingsystem.model.Booking;
import com.tourbookingsystem.model.Customer;
import com.tourbookingsystem.model.Tour;
import com.tourbookingsystem.service.BookingService;
import com.tourbookingsystem.service.CustomerService;
import com.tourbookingsystem.service.TourApiService;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class TourBookingFxApp extends Application {

    private final Admin admin = new Admin("admin", "1234");
    private final BookingService bookingService = new BookingService();
    private final CustomerService customerService = new CustomerService();
    private final TourApiService tourApiService = new TourApiService();

    private Customer currentCustomer;
    private Stage primaryStage;
    private List<String> imageUrls = new ArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        loadData();
        imageUrls = tourApiService.fetchTourImageUrls(18);

        primaryStage.setTitle("Tour Booking Studio");
        primaryStage.setMinWidth(1100);
        primaryStage.setMinHeight(760);
        primaryStage.setOnCloseRequest(event -> saveAllData());

        showAuthScene();
        primaryStage.show();
    }

    private void loadData() {
        for (Customer customer : customerService.loadCustomersFromFile()) {
            admin.addCustomer(customer);
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
    }

    private Scene styledScene(Parent root) {
        Scene scene = new Scene(root, 1200, 780);
        String css = getClass().getResource("/styles/app.css").toExternalForm();
        scene.getStylesheets().add(css);
        return scene;
    }

    private void showAuthScene() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("app-root");

        VBox branding = new VBox(14);
        branding.getStyleClass().add("branding-panel");
        branding.setPadding(new Insets(42));

        Label brandTitle = new Label("Tour Booking Studio");
        brandTitle.getStyleClass().add("brand-title");

        Label brandSubtitle = new Label("Book tours in a visual experience with cards, live images, and quick actions.");
        brandSubtitle.getStyleClass().add("brand-subtitle");
        brandSubtitle.setWrapText(true);

        branding.getChildren().addAll(brandTitle, brandSubtitle);

        VBox authPanel = new VBox(14);
        authPanel.getStyleClass().add("auth-panel");
        authPanel.setPadding(new Insets(30));

        Label loginHeader = new Label("Customer Login");
        loginHeader.getStyleClass().add("panel-title");

        TextField loginUser = new TextField();
        loginUser.setPromptText("Username");

        PasswordField loginPassword = new PasswordField();
        loginPassword.setPromptText("Password");

        Label loginMessage = new Label();
        loginMessage.getStyleClass().add("error-text");

        Button loginButton = new Button("Login");
        loginButton.getStyleClass().addAll("btn", "btn-primary");
        loginButton.setMaxWidth(Double.MAX_VALUE);
        loginButton.setOnAction(event -> {
            String username = loginUser.getText().trim();
            String password = loginPassword.getText();

            if (containsComma(username) || containsComma(password)) {
                loginMessage.setText("Username/password cannot contain commas.");
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
        });

        Label registerHeader = new Label("Create Account");
        registerHeader.getStyleClass().add("panel-title");

        TextField registerUser = new TextField();
        registerUser.setPromptText("New username");

        PasswordField registerPassword = new PasswordField();
        registerPassword.setPromptText("New password");

        Label registerMessage = new Label();
        registerMessage.getStyleClass().add("error-text");

        Button registerButton = new Button("Register");
        registerButton.getStyleClass().addAll("btn", "btn-secondary");
        registerButton.setMaxWidth(Double.MAX_VALUE);
        registerButton.setOnAction(event -> {
            String username = registerUser.getText().trim();
            String password = registerPassword.getText();

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

            if (findCustomerByUsername(username) != null) {
                registerMessage.setText("Username already exists.");
                return;
            }

            currentCustomer = new Customer(username, password);
            admin.addCustomer(currentCustomer);
            saveAllData();
            showCustomerDashboard();
        });

        authPanel.getChildren().addAll(
                loginHeader,
                loginUser,
                loginPassword,
                loginButton,
                loginMessage,
                registerHeader,
                registerUser,
                registerPassword,
                registerButton,
                registerMessage
        );

        root.setLeft(branding);
        root.setCenter(authPanel);

        primaryStage.setScene(styledScene(root));
    }

    private void showCustomerDashboard() {
        BorderPane content = new BorderPane();
        StackPane rootLayer = new StackPane(content);
        rootLayer.getStyleClass().add("app-root");

        HBox topBar = new HBox(12);
        topBar.setPadding(new Insets(16, 20, 16, 20));
        topBar.getStyleClass().add("top-bar");

        Label welcome = new Label("Welcome, " + currentCustomer.getUsername());
        welcome.getStyleClass().add("top-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button changePasswordButton = new Button("Change Password");
        changePasswordButton.getStyleClass().addAll("btn", "btn-ghost");
        changePasswordButton.setOnAction(event -> showChangePasswordPanel(rootLayer));

        Button logoutButton = new Button("Logout");
        logoutButton.getStyleClass().addAll("btn", "btn-danger");
        logoutButton.setOnAction(event -> {
            currentCustomer = null;
            saveAllData();
            showAuthScene();
        });

        topBar.getChildren().addAll(welcome, spacer, changePasswordButton, logoutButton);

        FlowPane cardsPane = new FlowPane();
        cardsPane.setHgap(18);
        cardsPane.setVgap(18);
        cardsPane.setPadding(new Insets(20));

        Label pageMessage = new Label();
        pageMessage.getStyleClass().add("success-text");

        List<Tour> tours = admin.getAllTours();
        for (int i = 0; i < tours.size(); i++) {
            cardsPane.getChildren().add(buildTourCard(tours.get(i), i, pageMessage));
        }

        ScrollPane scrollPane = new ScrollPane(cardsPane);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("tour-scroll");

        VBox centerBox = new VBox(8, pageMessage, scrollPane);
        centerBox.setPadding(new Insets(0, 10, 14, 10));

        VBox bookingPanel = buildBookingPanel(pageMessage);

        content.setTop(topBar);
        content.setCenter(centerBox);
        content.setRight(bookingPanel);

        primaryStage.setScene(styledScene(rootLayer));
    }

    private VBox buildTourCard(Tour tour, int index, Label messageLabel) {
        VBox card = new VBox(10);
        card.getStyleClass().add("tour-card");
        card.setPrefWidth(300);

        String imageUrl = imageUrls.get(index % imageUrls.size());

        ImageView imageView = new ImageView(new Image(imageUrl, 300, 180, false, true, true));
        imageView.setFitWidth(300);
        imageView.setFitHeight(180);
        imageView.setPreserveRatio(false);

        StackPane imageContainer = new StackPane(imageView);
        imageContainer.getStyleClass().add("tour-image-wrap");

        Label title = new Label(tour.getName());
        title.getStyleClass().add("tour-title");

        Label subtitle = new Label(tour.getDetails());
        subtitle.getStyleClass().add("tour-subtitle");
        subtitle.setWrapText(true);

        Label seatLabel = new Label("Seats left: " + tour.getAvailableSeats());
        seatLabel.getStyleClass().add("meta-text");

        int maxSeats = Math.max(1, tour.getAvailableSeats());
        Spinner<Integer> seatSpinner = new Spinner<>(1, maxSeats, 1);
        seatSpinner.setEditable(false);

        Button bookButton = new Button("Book Now");
        bookButton.getStyleClass().addAll("btn", "btn-primary");
        bookButton.setDisable(tour.getAvailableSeats() <= 0);
        bookButton.setOnAction(event -> {
            int seats = seatSpinner.getValue();
            if (tour.getAvailableSeats() < seats) {
                messageLabel.getStyleClass().remove("success-text");
                messageLabel.getStyleClass().add("error-text");
                messageLabel.setText("Not enough seats for " + tour.getName() + ".");
                return;
            }

            for (int i = 0; i < seats; i++) {
                tour.bookSeat();
            }

            Booking booking = currentCustomer.bookTour(tour, seats);
            bookingService.addBooking(booking);
            bookingService.saveBookingsToFile();

            messageLabel.getStyleClass().remove("error-text");
            if (!messageLabel.getStyleClass().contains("success-text")) {
                messageLabel.getStyleClass().add("success-text");
            }
            messageLabel.setText("Booked " + seats + " seat(s) for " + tour.getName() + ".");
            showCustomerDashboard();
        });

        card.getChildren().addAll(imageContainer, title, subtitle, seatLabel, seatSpinner, bookButton);
        return card;
    }

    private VBox buildBookingPanel(Label messageLabel) {
        VBox panel = new VBox(10);
        panel.getStyleClass().add("booking-panel");
        panel.setPadding(new Insets(20));
        panel.setPrefWidth(340);

        Label heading = new Label("My Bookings");
        heading.getStyleClass().add("panel-title");

        ListView<Booking> bookingList = new ListView<>(FXCollections.observableArrayList(currentCustomer.getBookings()));
        bookingList.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Booking item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getTourName() + " | Seats: " + item.getNumberOfTickets());
                }
            }
        });

        Button cancelBooking = new Button("Cancel Selected Booking");
        cancelBooking.getStyleClass().addAll("btn", "btn-danger");
        cancelBooking.setMaxWidth(Double.MAX_VALUE);
        cancelBooking.setOnAction(event -> {
            int selectedIndex = bookingList.getSelectionModel().getSelectedIndex();
            if (selectedIndex < 0) {
                messageLabel.getStyleClass().remove("success-text");
                if (!messageLabel.getStyleClass().contains("error-text")) {
                    messageLabel.getStyleClass().add("error-text");
                }
                messageLabel.setText("Select a booking to cancel.");
                return;
            }

            Booking canceled = currentCustomer.cancelBooking(selectedIndex);
            if (canceled == null) {
                return;
            }

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

            messageLabel.getStyleClass().remove("error-text");
            if (!messageLabel.getStyleClass().contains("success-text")) {
                messageLabel.getStyleClass().add("success-text");
            }
            messageLabel.setText("Canceled booking for " + canceled.getTourName() + ".");
            showCustomerDashboard();
        });

        panel.getChildren().addAll(heading, bookingList, cancelBooking);
        VBox.setVgrow(bookingList, Priority.ALWAYS);

        return panel;
    }

    private void showChangePasswordPanel(StackPane rootLayer) {
        VBox overlayBox = new VBox(10);
        overlayBox.getStyleClass().add("dialog-card");
        overlayBox.setPadding(new Insets(18));
        overlayBox.setAlignment(Pos.CENTER_LEFT);
        overlayBox.setMaxWidth(420);

        Label title = new Label("Change Password");
        title.getStyleClass().add("panel-title");

        PasswordField current = new PasswordField();
        current.setPromptText("Current password");

        PasswordField next = new PasswordField();
        next.setPromptText("New password");

        PasswordField confirm = new PasswordField();
        confirm.setPromptText("Confirm new password");

        Label feedback = new Label();
        feedback.getStyleClass().add("error-text");

        Button apply = new Button("Save Password");
        apply.getStyleClass().addAll("btn", "btn-primary");

        Button close = new Button("Close");
        close.getStyleClass().addAll("btn", "btn-ghost");

        HBox actions = new HBox(8, apply, close);

        StackPane overlay = new StackPane(overlayBox);
        overlay.getStyleClass().add("dialog-overlay");
        overlay.setPadding(new Insets(30));

        apply.setOnAction(event -> {
            if (!currentCustomer.checkPassword(current.getText())) {
                feedback.setText("Current password is incorrect.");
                return;
            }

            if (isBlank(next.getText())) {
                feedback.setText("New password cannot be empty.");
                return;
            }

            if (containsComma(next.getText())) {
                feedback.setText("Password cannot contain commas.");
                return;
            }

            if (!next.getText().equals(confirm.getText())) {
                feedback.setText("Confirmation does not match.");
                return;
            }

            currentCustomer.setPassword(next.getText());
            customerService.saveCustomersToFile(admin.getAllCustomers());
            rootLayer.getChildren().remove(overlay);
        });

        close.setOnAction(event -> rootLayer.getChildren().remove(overlay));

        overlayBox.getChildren().addAll(title, current, next, confirm, feedback, actions);
        rootLayer.getChildren().add(overlay);
    }

    private boolean containsComma(String value) {
        return value != null && value.contains(",");
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
