package hw6_peters_server;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.regex.Pattern;

public class HW6_Server extends Application {

    static ServerSocket serverSocket;

    static Connection connection;

    /**
     * Formatting fonts
     */
    private static final Font titleFont = new Font("Lucida Sans Unicode",30),
            bodyFont = new Font("Lucida Sans Unicode",15),
            buttonFont = new Font("Lucida Sans Unicode",15),
            receiptFont = new Font("Lucida Sans Unicode",10);

    /**
     * Formatting spacing variable
     */
    private static final int sceneSpace = 20;

    private static boolean quitFlag;

    static Label serverLabel;

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        launch();
    }

    @Override
    public void start(Stage stage) throws SQLException, ClassNotFoundException, IOException {
        // Initialize serverScene GUI
        serverLabel = new Label();
        Pane serverPane = new Pane(serverLabel);
        serverLabel.setFont(bodyFont);
        Scene serverScene = new Scene(serverPane);

        // Set stage title and scene
        stage.setTitle("Server POS System");
        stage.setScene(serverScene);
        stage.setOnCloseRequest(windowEvent -> {
            // Quit event
            System.exit(0);
        });

        // Initialize server data
        // Load driver
        Class.forName("org.sqlite.JDBC");

        // Relative path
        String itemDB_RelativePath = "src\\resources\\item.db";
        String itemDB_DRIVER_RelativeUrl = "jdbc:sqlite:" + itemDB_RelativePath;

        // Set up connection
        connection = DriverManager.getConnection(itemDB_DRIVER_RelativeUrl);

        int portNum = 8001;
        serverSocket = new ServerSocket(portNum);

        // Get ID print to server
        InetAddress serverInetAddress = serverSocket.getInetAddress(); // Update serverLabel
        System.out.println("Server address " + serverInetAddress);

//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//                serverLabel.setText("Database connection complete\nServer address :" + serverInetAddress);
//                stage.show();
//            }
//        });

        serverLabel.setText("Database connection complete\nServer address :" + serverInetAddress);

        stage.show();

        while (true) {
            // Connect to client
            Socket socket = serverSocket.accept();

            Thread thread = new Thread(new ServerHandler(socket,connection));
            thread.start();
        }

//        runServer(stage);
    }

    private static void runServer(Stage stage) throws IOException {
        while (true) {
            // Connect to client
            Socket socket = serverSocket.accept();

            // Start thread
//            ServerHandler serverHandler = new ServerHandler(socket,connection);
//            serverHandler.run();

            Thread thread = new Thread(new ServerHandler(socket,connection));
            thread.start();
        }
    }

//    private static void createGUI(Stage stage) throws IOException, SQLException {
//
//    }

//    static void setUpServer() throws IOException, SQLException, ClassNotFoundException {
//
//    }


}
