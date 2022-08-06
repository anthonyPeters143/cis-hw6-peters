package hw6_peters_server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;

public class HW6_Server {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        // Load driver
        Class.forName("org.sqlite.JDBC");

//        // Absolute path
//        String itemDB_URL = "jdbc:sqlite:\\C:\\Users\\Anthony\\Desktop\\School\\SVSU\\Spring 2022\\CIS 357\\HW6\\cis-hw6-peters\\src\\resources\\item.db";

        // Relative path
        String itemDB_RelativePath = "src\\resources\\item.db";
        String itemDB_DRIVER_RelativeUrl = "jdbc:sqlite:" + itemDB_RelativePath;

        // Set up connection
        Connection connection = DriverManager.getConnection(itemDB_DRIVER_RelativeUrl);
        System.out.println("Database connection complete");

        try {
            // Starting port
            int clientAmount = 0;
            int portNum = 8001;
            ServerSocket serverSocket = new ServerSocket(portNum);

            // Get ID print to server
            InetAddress serverInetAddress = serverSocket.getInetAddress();
            System.out.println("Server address " + serverInetAddress);

            // Forever loop to allow multiple clients
            while (true) {
                // Socket listen and accept request
                Socket socket = serverSocket.accept();

                // Increase client counter
                clientAmount++;

                // Print client address
                InetAddress clientInetAddress = socket.getInetAddress();
                System.out.println("Client " + clientAmount + " address" + clientInetAddress);

                // Start thread
                new ServerHandler(socket,connection).run();
            }

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }


        // Create statement
        Statement statement = connection.createStatement();

        // Execute statement
        ResultSet resultSet = statement.executeQuery("select item_code, item_name, unit_price from item");

        // Cycle results
        while (resultSet.next()) {
            System.out.println(resultSet.getString(1) + "," +
                    resultSet.getString(2) + "," +
                    resultSet.getString(3));
        }

        // Cycle results
        while (resultSet.next()) {
            System.out.println(resultSet.getString(1) + "," +
                    resultSet.getString(2) + "," +
                    resultSet.getString(3));
        }



        connection.close();
    }




}
