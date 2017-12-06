import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class Database {
    private Connection connection;
    Statement statement;

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
