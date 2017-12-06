import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class UserInterface implements Runnable {
    private Scanner in;
    private Database database;

    public static void main (String[] args) {
        UserInterface u = new UserInterface();
        u.run();
    }

    public UserInterface() {
        in = new Scanner(System.in);
        database = new Database("database_user", "datapassword", "localhost",
                "cs435_lab4");
    }

    private void menu() {
        boolean running = true;
        while (running) {
            mainMenuText();
            String selection = in.nextLine().trim();
            if (selection.equals("1")) {
                System.out.println("Schedule:");
                schedule();
                System.out.println();
            } else if (selection.equals("2")) {
                System.out.println("Add a trip offering:");
                boolean submenu = true;
                while (submenu) {
                    addTripOffering();
                    System.out.println();
                    addTripOfferingsText();
                    String submenuSelection = in.nextLine().trim();
                    if (!submenuSelection.equals("1")) {
                        submenu = false;
                    }
                }
            } else if (selection.equals("3")) {
                System.out.println("Delete a trip offering:");
                deleteTripOffering();
                System.out.println();
            } else if (selection.equals("4")) {
                System.out.println("Change trip driver:");
                changeTripOfferingDriver();
                System.out.println();
            } else if (selection.equals("5")) {
                System.out.println("Change trip bus:");
                changeTripOfferingBus();
                System.out.println();
            } else if (selection.equals("6")) {
                System.out.println("Display stops:");
                displayTripStops();
                System.out.println();
            } else if (selection.equals("7")) {
                System.out.println("Display driver weekly schedule:");
                displayDriverWeeklySchedule();
                System.out.println();
            } else if (selection.equals("8")) {
                System.out.println("Add a driver:");
                addDriver();
                System.out.println();
            } else if (selection.equals("9")) {
                System.out.println("Delete a driver:");
                deleteDriver();
                System.out.println();
            } else if (selection.equals("10")) {
                System.out.println("Add a bus:");
                addBus();
                System.out.println();
            } else if (selection.equals("11")) {
                System.out.println("Delete a bus:");
                deleteBus();
                System.out.println();
            } else if (selection.equals("12")) {
                System.out.println("Add actual trip stop info:");
                addActualTripStopInfo();
                System.out.println();
            } else if (selection.equals("13")) {
                System.out.println("Exiting");
                running = false;
            }
        }
    }

    private void mainMenuText() {
        System.out.println("Select an option:");
        System.out.println("\t1. Print schedule\n\t2. Add trip offerings\n\t3. Delete a trip "
                + "offering\n\t4. Change a trip driver\n\t5. Change a trip bus\n\t6. Display "
                + "trip stops\n\t7. Display driver weekly schedule\n\t8. Add a driver\n\t9. "
                + "Delete a driver\n\t10. Add a bus\n\t11. Delete a bus\n\t12. Add actual "
                + "trip stop info\n\t13. Exit");
        System.out.print("Enter an option number:  ");
    }

    private void addTripOfferingsText() {
        System.out.println("Select an option:");
        System.out.println("\t1. Add another trip offering\n\t2. Return to main menu");
        System.out.print("Enter an option number:  ");
    }

    private void schedule() {
        String start;
        String dest;
        String date;
        System.out.print("Enter starting location address:  ");
        start = in.nextLine().trim();
        System.out.print("Enter destination address:  ");
        dest = in.nextLine().trim();
        System.out.print("Enter date (YYYY-MM-DD):  ");
        date = in.nextLine().trim();
        LinkedList<String[]> rows = (LinkedList<String[]>) database.getSchedule(start, dest, date);
        String[] titles = {"Start Address", "Destination Address", "Trip Date", "Scheduled Start",
                    "Scheduled Arrival", "Driver Name", "Bus ID"};
        rows.addFirst(titles);
        printList(rows);
    }

    private void addTripOffering() {
        String tripNumber;
        String date;
        String startTime;
        String arrivalTime;
        String driver;
        String bus;
        // trip
        printTripList();
        System.out.print("Enter trip number:  ");
        tripNumber = in.nextLine().trim();
        // date
        System.out.print("Enter date (YYYY-MM-DD):  ");
        date = in.nextLine().trim();
        // start time
        System.out.print("Enter starting time (HH:MM):  ");
        startTime = in.nextLine().trim();
        // arrival time
        System.out.print("Enter arrival time (HH:MM):  ");
        arrivalTime = in.nextLine().trim();
        // driver
        System.out.print("Enter driver name:  ");
        driver = in.nextLine().trim();
        // bus
        System.out.print("Enter bus ID:  ");
        bus = in.nextLine().trim();
        // add trip offering
        boolean status = database.addTripOffering(tripNumber, date, startTime, arrivalTime, driver, bus);
        if (status) {
            System.out.println("Trip offering added successfully.");
            printTripOfferingsList();
        } else {
            System.out.println("Trip offering could not be added.");
        }
    }

    private void deleteTripOffering() {
        String trip;
        String date;
        String startTime;
        printTripOfferingsList();
        System.out.print("Enter trip number:  ");
        trip = in.nextLine().trim();
        System.out.print("Enter date (YYYY-MM-DD):  ");
        date = in.nextLine().trim();
        System.out.print("Enter start time (HH:MM):  ");
        startTime = in.nextLine().trim();
        // delete
        boolean status = database.deleteTripOffering(trip, date, startTime);
        if (status) {
            System.out.println("Trip offering deleted successfully.");
            printTripOfferingsList();
        } else {
            System.out.println("Trip offering could not be deleted.");
        }
    }

    private void changeTripOfferingDriver() {
        String trip;
        String date;
        String startTime;
        String driver;
        printTripOfferingsList();
        System.out.print("Enter trip number:  ");
        trip = in.nextLine().trim();
        System.out.print("Enter date (YYYY-MM-DD):  ");
        date = in.nextLine().trim();
        System.out.print("Enter start time (HH:MM):  ");
        startTime = in.nextLine().trim();
        System.out.print("Enter new driver name:  ");
        driver = in.nextLine().trim();
        // change driver
        boolean status = database.updateTripOfferingDriver(trip, date, startTime, driver);
        if (status) {
            System.out.println("Trip offering driver updated.");
            printTripOfferingsList();
        } else {
            System.out.println("Trip offering driver could not be updated.");
        }
    }

    private void changeTripOfferingBus() {
        String trip;
        String date;
        String startTime;
        String bus;
        printTripOfferingsList();
        System.out.print("Enter trip number:  ");
        trip = in.nextLine().trim();
        System.out.print("Enter date (YYYY-MM-DD):  ");
        date = in.nextLine().trim();
        System.out.print("Enter start time (HH:MM):  ");
        startTime = in.nextLine().trim();
        System.out.print("Enter new bus ID:  ");
        bus = in.nextLine().trim();
        // change bus
        boolean status = database.updateTripOfferingBus(trip, date, startTime, bus);
        if (status) {
            System.out.println("Trip offering bus updated.");
            printTripOfferingsList();
        } else {
            System.out.println("Trip offering bus could not be updated.");
        }
    }

    private void displayTripStops() {
        String trip;
        printTripList();
        System.out.print("Enter trip number:  ");
        trip = in.nextLine().trim();
        LinkedList<String[]> stops = (LinkedList<String[]>) database.getTripStopInfo(trip);
        String[] stopsCols = {"Trip Number", "Stop Number", "Driving Time"};
        stops.addFirst(stopsCols);
        printList(stops);
    }

    private void displayDriverWeeklySchedule() {
        String driver;
        String date;
        printDriverList();
        System.out.print("Enter driver name:  ");
        driver = in.nextLine().trim();
        System.out.print("Enter start of week (YYYY-MM-DD):  ");
        date = in.nextLine().trim();
        LinkedList<String[]> schedule =
                (LinkedList<String[]>) database.getDriverWeeklySchedule(driver, date);
        String[] scheduleCols = {"Start Location", "Destination", "Date", "Start Time",
                "Arrival Time", "Driver", "Bus ID"};
        schedule.addFirst(scheduleCols);
        printList(schedule);
    }

    private void addDriver() {
        String driver;
        String phone;
        System.out.print("Enter new driver name:  ");
        driver = in.nextLine().trim();
        System.out.print("Enter new driver phone:  ");
        phone = in.nextLine().trim();
        boolean status = database.addDriver(driver, phone);
        if (status) {
            System.out.println("Enter new driver name:  ");
            printDriverList();
        } else {
            System.out.println("Enter new driver phone:  ");
        }
    }

    private void deleteDriver() {
        String driver;
        printDriverList();
        System.out.print("Enter driver name:  ");
        driver = in.nextLine().trim();
        boolean status = database.deleteDriver(driver);
        if (status) {
            System.out.println("Driver deleted successfully.");
            printDriverList();
        } else {
            System.out.println("Driver could not be deleted.");
        }
    }

    private void addBus() {
        String bus;
        String model;
        String year;
        LinkedList<String[]> buses = (LinkedList<String[]>) database.getBusTable();
        String[] last = buses.get(buses.size() - 1);
        int lastIndex = Integer.parseInt(last[0]);
        int newIndex = lastIndex + 1;
        bus = "" + newIndex;
        System.out.print("Enter model:  ");
        model = in.nextLine().trim();
        System.out.print("Enter year:  ");
        year = in.nextLine().trim();
        boolean status = database.addBus(bus, model, year);
        if (status) {
            System.out.println("Bus added successfully.");
            printBusList();
        } else {
            System.out.println("Bus could not be added.");
        }
    }

    private void deleteBus() {
        String bus;
        printBusList();
        System.out.print("Enter bus ID:  ");
        bus = in.nextLine().trim();
        boolean status = database.deleteBus(bus);
        if (status) {
            System.out.println("Bus deleted successfully");
            printBusList();
        } else {
            System.out.println("Bus could not be deleted");
        }
    }

    private void addActualTripStopInfo() {
        String trip;
        String date;
        String scheduledStart;
        String stop;
        String scheduledArrival;
        String actualStart;
        String actualArrival;
        String passengersIn;
        String passengersOut;
        printStopsInfoList();
        System.out.print("Enter trip number:  ");
        trip = in.nextLine().trim();
        System.out.print("Enter date (YYYY-MM-DD):  ");
        date = in.nextLine().trim();
        System.out.print("Enter scheduled start time (HH:MM):  ");
        scheduledStart = in.nextLine().trim();
        System.out.print("Enter stop number:  ");
        stop = in.nextLine().trim();
        System.out.print("Enter scheduled arrival time (HH:MM):  ");
        scheduledArrival = in.nextLine().trim();
        System.out.print("Enter actual start time (HH:MM):  ");
        actualStart = in.nextLine().trim();
        System.out.print("Enter actual arrival time (HH:MM):  ");
        actualArrival = in.nextLine().trim();
        System.out.print("Enter number of passengers boarding:  ");
        passengersIn = in.nextLine().trim();
        System.out.print("Enter number of passengers exiting:  ");
        passengersOut = in.nextLine().trim();
        boolean status = database.addActualTripStopInfo(trip, date, scheduledStart, stop,
                scheduledArrival, actualStart, actualArrival, passengersIn, passengersOut);
        if (status) {
            System.out.println("Actual stop information added successfuly");
            printActualStopsInfoList();
        } else {
            System.out.println("Actual stop information could not be added");
        }
    }

    private void printList(List<String[]> list) {
        System.out.println();
        for (int i = 0; i < list.size(); i++) {
            String[] row = list.get(i);
            for (int j = 0; j < row.length; j++) {
                System.out.printf("%-22s", row[j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    private void printDriverList() {
        System.out.println("Drivers:");
        LinkedList<String[]> drivers = (LinkedList<String[]>) database.getDriverTable();
        String[] driverCols = {"Driver Name", "Driver Phone"};
        drivers.addFirst(driverCols);
        printList(drivers);
    }

    private void printTripOfferingsList() {
        System.out.println("Trip Offerings:");
        LinkedList<String[]> offer = (LinkedList<String[]>) database.getTripOfferingTable();
        String[] offerCols = {"Trip Number", "Date", "Scheduled Start", "Scheduled Arrival",
                "Driver", "Bus ID"};
        offer.addFirst(offerCols);
        printList(offer);
    }

    private void printBusList() {
        System.out.println("Buses:");
        LinkedList<String[]> buses = (LinkedList<String[]>) database.getBusTable();
        String[] busCols = {"Bus ID", "Model", "Year"};
        buses.addFirst(busCols);
        printList(buses);
    }

    private void printTripList() {
        System.out.println("Trips:");
        LinkedList<String[]> trips = (LinkedList<String[]>) database.getTripTable();
        String[] tripCols = {"Trip Number", "Start Address", "Destination Address"};
        trips.addFirst(tripCols);
        printList(trips);
    }

    private void printStopsInfoList() {
        System.out.println("Trip Stops:");
        LinkedList<String[]> stops = (LinkedList<String[]>) database.getTripStopInfo();
        String[] stopCols = {"Trip Number", "Stop Number", "Sequence Number", "Driving Time"};
        stops.addFirst(stopCols);
        printList(stops);
    }

    private void printActualStopsInfoList() {
        System.out.println("Actual Stops:");
        LinkedList<String[]> stops = (LinkedList<String[]>) database.getActualTripStopInfoTable();
        String[] stopsCols = {"Trip Number", "Date", "Scheduled Start Time", "Stop Number",
                "Scheduled Arrival Time", "Actual Start Time", "Actual Arrival Time",
                "Number Boarding", "Number Exiting"};
        stops.addFirst(stopsCols);
        printList(stops);
    }

    public void run() {
        menu();
    }
}
