import java.awt.*;
import java.awt.event.*;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.text.DecimalFormat;
import java.util.ArrayList;


class Flight {
    String flightNumber;
    String destination;
    String departureTime;
    int totalSeats;
    int windowSeatCount;
    boolean[] seats;
    double fixedAmount;

    Flight(String flightNumber, String destination, String departureTime, int totalSeats, int windowSeatCount, double fixedAmount) {
        this.flightNumber = flightNumber;
        this.destination = destination;
        this.departureTime = departureTime;
        this.totalSeats = totalSeats;
        this.windowSeatCount = windowSeatCount;
        this.seats = new boolean[totalSeats]; // All seats are initially available
        this.fixedAmount = fixedAmount;
    }

    public void showSeatMap(TextArea seatMapArea) {
        seatMapArea.setText("Available seats (W for window, N for normal):\n");
        for (int i = 0; i < seats.length; i++) {
            if (!seats[i]) {
                String seatType = (i < windowSeatCount) ? "W" : "N";
                seatMapArea.append((i + 1) + seatType + " ");
            }
        }
    }

    public boolean bookSeat(int seatNumber) {
        if (seatNumber > 0 && seatNumber <= totalSeats && !seats[seatNumber - 1]) {
            seats[seatNumber - 1] = true;
            return true;
        }
        return false;
    }
}

class Passenger {
    String name;
    boolean isCorporate;
    String corporateId;
    String mealPreference;
    String mealOption;

    Passenger(String name, boolean isCorporate, String corporateId) {
        this.name = name;
        this.isCorporate = isCorporate;
        this.corporateId = corporateId;
    }
}


class Booking {
    Flight flight;
    Passenger passenger;
    int seatNumber;
    boolean wantWindowSeat;

    Booking(Flight flight, Passenger passenger, int seatNumber, boolean wantWindowSeat) {
        this.flight = flight;
        this.passenger = passenger;
        this.seatNumber = seatNumber;
        this.wantWindowSeat = wantWindowSeat;
    }

    public double calculateTotalFare() {
        double totalFare = flight.fixedAmount;
        if (wantWindowSeat) {
            totalFare += 2500; // Additional charge for window seat
        }
        if (passenger.isCorporate && passenger.corporateId != null && passenger.corporateId.startsWith("CP")) {
            double discount = totalFare * 0.10; // 10% discount for corporate passengers
            totalFare -= discount;
        }
        return totalFare;
    }

    public void displayBookingDetails(TextArea outputArea) {
        DecimalFormat df = new DecimalFormat("##,##,###.00");
        double finalFare = calculateTotalFare();
        outputArea.setText("Booking Details:\n");
        outputArea.append("Passenger Name: " + passenger.name + "\n");
        outputArea.append("Corporate ID: " + (passenger.isCorporate ? passenger.corporateId : "N/A") + "\n");
        outputArea.append("Flight Number: " + flight.flightNumber + "\n");
        outputArea.append("Destination: " + flight.destination + "\n");
        outputArea.append("Departure Time: " + flight.departureTime + "\n");
        outputArea.append("Seat Number: " + seatNumber + "\n");
        outputArea.append("Meal Option: " + passenger.mealOption + "\n");
        if (wantWindowSeat) {
            outputArea.append("Window seat selected. Additional charge: INR 2,500\n");
        }
        outputArea.append("Total payment: INR " + df.format(finalFare) + "\n");
    }
}


class FlightManagementSystemApp extends Frame implements ActionListener {
    ArrayList<Flight> flights = new ArrayList<>();
    TextField nameField, corporateIdField, seatNumberField;
    Checkbox corporateCheckbox, windowSeatCheckbox;
    Choice flightChoice, mealChoice, mealPreferenceChoice;
    TextArea seatMapArea, outputArea;
    Button bookButton, resetButton;
    Image backgroundImage;

    FlightManagementSystemApp() {
        
        try {
            backgroundImage = ImageIO.read(new File("1.jpg")); // Provide your image path here
        } catch (IOException e) {
            System.out.println("Error loading background image: " + e.getMessage());
        }

        
        flights.add(new Flight("AI101", "New York", "10:00 AM", 100, 20, 58000));
        flights.add(new Flight("AI112", "London", "8:15 AM", 150, 30, 62000));
        flights.add(new Flight("AI153", "Hawaii", "5:40 PM", 80, 50, 95000));
        flights.add(new Flight("AI177", "Paris", "2:00 PM", 120, 40, 75000));
        flights.add(new Flight("AI189", "Hong Kong", "11:30 PM", 140, 35, 85000));
        flights.add(new Flight("AI210", "Singapore", "1:00 AM", 100, 25, 90000));
        flights.add(new Flight("AI221", "Tokyo", "6:00 AM", 90, 30, 92000));
        flights.add(new Flight("AI232", "Seoul", "9:45 AM", 110, 50, 60000));
        flights.add(new Flight("AI243", "Toronto", "8:00 AM", 130, 45, 88000));
        flights.add(new Flight("AI254", "Berlin", "12:00 PM", 95, 30, 67000));
        flights.add(new Flight("AI265", "Bangkok", "4:00 PM", 160, 60, 98000));
        flights.add(new Flight("AI276", "Beijing", "3:15 PM", 180, 40, 40000));
        flights.add(new Flight("AI287", "Chicago", "7:30 AM", 200, 80, 72000));

        
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

       
        Panel headingPanel = new Panel();
        headingPanel.setBackground(new Color(255, 228, 225));
        Label headingLabel = new Label("Flight Management System", Label.CENTER);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headingLabel.setForeground(Color.GRAY);  // Set color for the heading
        headingPanel.add(headingLabel);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;  // Span across two columns
        add(headingPanel, gbc);
        gbc.gridwidth = 1;  // Reset grid width for other components

        
        Panel passengerPanel = new Panel(new GridBagLayout());
        passengerPanel.setBackground(new Color(255, 228, 225));  // Light Blue Background
        gbc.gridy = 1;
        add(passengerPanel, gbc);

        Panel seatPanel = new Panel(new GridBagLayout());
        seatPanel.setBackground(new Color(255, 228, 225));  // Light Green Background
        gbc.gridy = 2;
        add(seatPanel, gbc);

        Panel bookingPanel = new Panel(new GridBagLayout());
        bookingPanel.setBackground(new Color(255, 228, 225));  // Light Pink Background
        gbc.gridy = 3;
        add(bookingPanel, gbc);

       
        gbc.gridx = 0;
        gbc.gridy = 0;
        passengerPanel.add(new Label("Passenger Name:"), gbc);
        nameField = new TextField(20);
        gbc.gridx = 1;
        passengerPanel.add(nameField, gbc);

      
        gbc.gridx = 0;
        gbc.gridy = 1;
        corporateCheckbox = new Checkbox("Corporate Customer");
        passengerPanel.add(corporateCheckbox, gbc);
        gbc.gridx = 1;
        passengerPanel.add(new Label("Corporate ID (if applicable):"), gbc);
        corporateIdField = new TextField(10);
        gbc.gridx = 2;
        passengerPanel.add(corporateIdField, gbc);

        
        gbc.gridx = 0;
        gbc.gridy = 2;
        passengerPanel.add(new Label("Meal Preference:"), gbc);
        mealPreferenceChoice = new Choice();
        mealPreferenceChoice.add("Veg");
        mealPreferenceChoice.add("Non-Veg");
        gbc.gridx = 1;
        passengerPanel.add(mealPreferenceChoice, gbc);

        
        gbc.gridx = 0;
        gbc.gridy = 3;
        passengerPanel.add(new Label("Meal Option:"), gbc);
        mealChoice = new Choice();
        mealChoice.add("Paneer Curry");
        mealChoice.add("Dal Tadka");
        mealChoice.add("Fruit Platter");
        mealChoice.add("Veg Biryani");
        mealChoice.add("Chicken Biryani");
        mealChoice.add("Fish Fry");
        mealChoice.add("Butter Chicken");
        mealChoice.add("Prawn Gravy");
        gbc.gridx = 1;
        passengerPanel.add(mealChoice, gbc);

       
        gbc.gridx = 0;
        gbc.gridy = 0;
        seatPanel.add(new Label("Select Flight:"), gbc);
        flightChoice = new Choice();
        for (Flight flight : flights) {
            flightChoice.add(flight.flightNumber + " (" + flight.destination + ")");
        }
        gbc.gridx = 1;
        seatPanel.add(flightChoice, gbc);

      
        gbc.gridx = 0;
        gbc.gridy = 1;
        seatPanel.add(new Label("Seat Number:"), gbc);
        seatNumberField = new TextField(5);
        gbc.gridx = 1;
        seatPanel.add(seatNumberField, gbc);


        gbc.gridx = 0;
        gbc.gridy = 2;
        windowSeatCheckbox = new Checkbox("I want a window seat (Additional INR 2500)");
        seatPanel.add(windowSeatCheckbox, gbc);

        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        seatMapArea = new TextArea(5, 30);
        seatPanel.add(seatMapArea, gbc);
        gbc.gridwidth = 1;  // Reset grid width for other components

        
        flightChoice.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                int selectedIndex = flightChoice.getSelectedIndex();
                Flight selectedFlight = flights.get(selectedIndex);
                selectedFlight.showSeatMap(seatMapArea);
            }
        });


        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        outputArea = new TextArea(10, 50);
        bookingPanel.add(outputArea, gbc);
        gbc.gridwidth = 1;  // Reset grid width for other components

        
        bookButton = new Button("Book Flight");
        gbc.gridx = 0;
        gbc.gridy = 1;
        bookingPanel.add(bookButton, gbc);
        bookButton.addActionListener(this);

       
        resetButton = new Button("Reset");
        gbc.gridx = 1;
        gbc.gridy = 1;
        bookingPanel.add(resetButton, gbc);
        resetButton.addActionListener(this);


        setSize(900, 600);
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });
    }

    public void actionPerformed(ActionEvent ae) {
        String action = ae.getActionCommand();

        if (action.equals("Book Flight")) {
            // Get selected flight
            int selectedIndex = flightChoice.getSelectedIndex();
            Flight selectedFlight = flights.get(selectedIndex);
            String name = nameField.getText();
            boolean isCorporate = corporateCheckbox.getState();
            String corporateId = isCorporate ? corporateIdField.getText() : null;
            Passenger passenger = new Passenger(name, isCorporate, corporateId);
            int seatNumber;
            try {
                seatNumber = Integer.parseInt(seatNumberField.getText());
            } catch (NumberFormatException e) {
                outputArea.setText("Invalid seat number. Please enter a valid seat number.");
                return;
            }

                       boolean wantWindowSeat = windowSeatCheckbox.getState();

            
            if (selectedFlight.bookSeat(seatNumber)) {
                passenger.mealOption = mealChoice.getSelectedItem(); // Get selected meal option
                Booking booking = new Booking(selectedFlight, passenger, seatNumber, wantWindowSeat);
                booking.displayBookingDetails(outputArea);
            } else {
                outputArea.setText("Seat number " + seatNumber + " is already booked or invalid. Please select another seat.");
            }
        } else if (action.equals("Reset")) {
            nameField.setText("");
            corporateIdField.setText("");
            seatNumberField.setText("");
            corporateCheckbox.setState(false);
            windowSeatCheckbox.setState(false);
            outputArea.setText("");
        }
    }

    public static void main(String[] args) {
        new FlightManagementSystemApp();
    }


    public void paint(Graphics g) {
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
        }
    }
}  