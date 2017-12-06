import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class Database {
    private Connection connection;
    Statement statement;

    public static void main(String[] args) {
        Database d = new Database("database_user", "datapassword", "localhost",
                "cs435_lab4");
        d.printList(d.getSchedule("Stop Address 1", "Stop Address 10", "2017-12-01"));
        d.printList(d.getTripStopInfo("1"));
        d.printList(d.getTripStopInfo("4"));
        System.out.println(d.addTripOffering("4", "2017-12-01", "8:00",
                "8:15", "Driver Name 1", "1"));
        d.printList(d.getSchedule("Stop Address 8", "Stop Address 4", "2017-12-01"));
        System.out.println(d.deleteTripOffering("4", "2017-12-01", "8:00"));
        d.printList(d.getSchedule("Stop Address 8", "Stop Address 4", "2017-12-01"));
        d.updateTripOfferingDriver("4", "2017-12-01", "8:30", "newdriver");
        d.printList(d.getSchedule("Stop Address 8", "Stop Address 4", "2017-12-01"));
        d.updateTripOfferingDriver("4", "2017-12-01", "8:30", "Driver Name 7");
        d.printList(d.getSchedule("Stop Address 8", "Stop Address 4", "2017-12-01"));
        d.updateTripOfferingBus("4", "2017-12-01", "8:30", "1");
        d.printList(d.getSchedule("Stop Address 8", "Stop Address 4", "2017-12-01"));
        d.updateTripOfferingBus("4", "2017-12-01", "8:30", "2");
        d.printList(d.getSchedule("Stop Address 8", "Stop Address 4", "2017-12-01"));
        d.printList(d.getDriverWeeklySchedule("Driver Name 7", "2017-12-01"));
        d.addBus("8", "Bus Model 5", "2003");
        d.printList(d.getBusTable());
        d.deleteBus("8");
        d.printList(d.getBusTable());
        System.out.println(d.addDriver("test name", "2222222222"));
        d.printList(d.getDriverTable());
        System.out.println(d.deleteDriver("test name"));
        d.printList(d.getDriverTable());
        d.addTripOffering("4", "2017-12-04", "10:00", "10:15",
                "Driver Name 3", "2");
        d.addActualTripStopInfo("4","2017-12-04","10:00","8",
                "10:00","10:00","10:00","8","0");
        d.addActualTripStopInfo("4","2017-12-04","10:00","6",
                "10:08","10:00","10:10","5","2");
        d.addActualTripStopInfo("4", "2017-12-04", "10:00", "4",
                "10:15", "10:00", "10:20", "0", "9");
        d.printList(d.getActualTripStopInfoTable());
        System.out.println(d.deleteTripOffering("4", "2017-12-04", "10:00"));
    }

    public Database(String user, String password, String host, String databaseName) {
        String connectionString = "jdbc:mysql://" + host + "/" + databaseName + "?user="
                + user + "&password=" + password;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection(connectionString);
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String test(String table) {
        StringBuilder builder = new StringBuilder();
        try {
            // Query
            ResultSet results = statement.executeQuery("SELECT * from " + table + ";");
            // Metadata
            ResultSetMetaData meta = results.getMetaData();
            int colNum = meta.getColumnCount();
            // Get results
            for (int i = 1; i <= colNum; i++) {
                builder.append(meta.getColumnLabel(i) + '\t');
            }
            builder.append('\n');
            while (results.next()) {
                for (int i = 1; i <= colNum; i++) {
                    builder.append(results.getString(i) + '\t');
                }
                builder.append('\n');
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    public void printList(List<String[]> l) {
        for (int i = 0; i < l.size(); i++) {
            String[] entry = l.get(i);
            for (int j = 0; j < entry.length; j++) {
                System.out.print(entry[j] + "\t\t");
            }
            System.out.println();
        }
    }

    public List<String[]> getSchedule(String start, String dest, String date) {
        String query = "SELECT T.StartLocationName, T.DestinationName, O.TripDate,"
                + " O.ScheduledStartTime, O.ScheduledArrivalTime, O.DriverName, O.BusID"
                + " FROM Trip T, TripOffering O"
                + " WHERE T.TripNumber = O.TripNumber AND T.StartLocationName = '" + start
                + "' AND T.DestinationName = '" + dest + "' AND O.TripDate = '" + date + "';";
        return executeStatement(query);
    }

    public List<String[]> getTripStopInfo(String trip) {
        String query = "SELECT I.TripNumber, I.StopNumber, I.DrivingTime"
                + " FROM TripStopInfo I"
                + " WHERE I.TripNumber = " + trip
                + " ORDER BY I.SequenceNumber;";
        return executeStatement(query);
    }

    public List<String[]> getDriverWeeklySchedule(String driver, String date) {
        String query = "SELECT T.StartLocationName, T.DestinationName, O.TripDate,"
                + " O.ScheduledStartTime, O.ScheduledArrivalTime, O.DriverName, O.BusID"
                + " FROM Trip T, TripOffering O"
                + " WHERE T.TripNumber = O.TripNumber AND O.DriverName = '" + driver + "' AND"
                + " O.TripDate >= '" + date + "' AND O.TripDate <= ADDDATE('" + date + "', 7)"
                + " ORDER BY O.TripDate, O.ScheduledStartTime;";
        return executeStatement(query);
    }

    public List<String[]> getBusTable() {
        String query = "SELECT * FROM Bus;";
        return executeStatement(query);
    }

    public List<String[]> getTripTable() {
        String query = "SELECT * FROM Trip;";
        return executeStatement(query);
    }

    public List<String[]> getDriverTable() {
        String query = "SELECT * FROM Driver;";
        return executeStatement(query);
    }

    public List<String[]> getActualTripStopInfoTable() {
        String query = "SELECT * FROM ActualTripStopInfo;";
        return executeStatement(query);
    }

    public List<String[]> getTripOfferingTable() {
        String query = "SELECT * FROM TripOffering;";
        return executeStatement(query);
    }

    public List<String[]> getTripStopInfo() {
        String query = "SELECT * FROM TripStopInfo ORDER BY TripNumber, SequenceNumber;";
        return executeStatement(query);
    }

    public boolean addTripOffering(String tripNumber, String date, String startTime, String arrivalTime,
                                String driver, String bus) {
        String insert = "INSERT INTO TripOffering (TripNumber, TripDate, ScheduledStartTime, "
                + "ScheduledArrivalTime, DriverName, BusID) VALUES ('" + tripNumber + "','" + date
                + "','" + startTime + "','" + arrivalTime + "','" + driver + "','" + bus + "');";
        return executeStatement(insert) != null;
    }

    public boolean deleteTripOffering(String tripNumber, String date, String startTime) {
        String delete = "DELETE FROM TripOffering"
                + " WHERE TripNumber = '" + tripNumber + "' AND TripDate = '" + date + "' AND"
                + " ScheduledStartTime = '" + startTime + "';";
        return executeStatement(delete) != null;
    }

    public boolean updateTripOfferingDriver(String tripNumber, String date, String startTime,
                                            String driverName) {
        String update = "UPDATE TripOffering"
                + " SET DriverName = '" + driverName + "'"
                + " WHERE TripNumber = '" + tripNumber + "' AND TripDate = '" + date
                + "' AND ScheduledStartTime = '" + startTime + "';";
        return executeStatement(update) != null;
    }

    public boolean updateTripOfferingBus(String tripNumber, String date, String startTime, String bus) {
        String update = "UPDATE TripOffering"
                + " SET BusID = '" + bus + "'"
                + " WHERE TripNumber = '" + tripNumber + "' AND TripDate = '" + date
                + "' AND ScheduledStartTime = '" + startTime + "';";
        return executeStatement(update) != null;
    }

    public boolean addDriver(String driverName, String driverPhone) {
        String insert = "INSERT INTO Driver (DriverName, DriverTelephoneNumber) VALUES ('"
                + driverName + "','" + driverPhone + "');";
        return executeStatement(insert) != null;
    }

    public boolean deleteDriver(String driverName) {
        String delete = "DELETE FROM Driver WHERE DriverName = '" + driverName + "';";
        return executeStatement(delete) != null;
    }

    public boolean addBus(String bus, String model, String id) {
        String insert = "INSERT INTO Bus (BusID, Model, Year) VALUES ('" + bus + "','" + model
                + "','" + id + "');";
        return executeStatement(insert) != null;
    }

    public boolean deleteBus(String bus) {
        String delete = "DELETE FROM Bus WHERE BusID = '" + bus + "';";
        return executeStatement(delete) != null;
    }

    public boolean addActualTripStopInfo(String trip, String date, String scheduledStartTime,
                                         String stopNumber, String scheduledArrivalTime, String
                                         actualStartTime, String actualArrivalTime, String in,
                                         String out) {
        String insert = "INSERT INTO ActualTripStopInfo (TripNumber, TripDate, ScheduledStartTime,"
                + " StopNumber, ScheduledArrivalTime, ActualStartTime, ActualArrivalTime,"
                + " NumberOfPassengersIn, NumberOfPassengersOut) VALUES ('" + trip + "','" + date
                + "','" + scheduledStartTime + "','" + stopNumber + "','" + scheduledArrivalTime
                + "','" + actualStartTime + "','" + actualArrivalTime + "','" + in + "','" + out
                + "');";
        return executeStatement(insert) != null;
    }

    private List<String[]> executeStatement(String s) {
        LinkedList<String[]> output = new LinkedList<>();
        try {
            ResultSet results = null;
            if (statement.execute(s)) {
                results = statement.getResultSet();
            } else {
                return output;
            }
            // Metadata
            ResultSetMetaData meta = results.getMetaData();
            int colNum = meta.getColumnCount();
            // Get rows
            while (results.next()) {
                String[] row = new String[colNum];
                for (int i = 1; i <= colNum; i++) {
                    row[i - 1] = results.getString(i);
                }
                output.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return output;
    }
}
