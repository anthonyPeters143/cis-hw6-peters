package hw6_peters_client;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class HW6_Client extends Application {

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

    /**
     * Format for currency
     */
    private static final DecimalFormat currencyFormat = new DecimalFormat("#,##0.00");

    private static ClientRecords clientRecords;

    private static final String LOCAL_ADDRESS = "localhost";

    private static int port = 8001;
    private static String host = "";

    @Override
    public void start(Stage stage) throws Exception {
        // Initialize
        clientRecords = new ClientRecords(currencyFormat);

        Scene connectionScene, saleScene, receiptScene = null;


//        String codeInput = "A001";
//
//        new ClientHandler(port,host,codeInput).run();

        // GUI String references
        String  TYPE_HERE_DEFAULT       = "Type here...";

        // GUI tableView references
//        TableView<SalesRecord> tableView = new TableView<>(clientRecords.getSalesRecordObservableList());
        TableView<TableEntry> tableView = new TableView<>(clientRecords.getTableEntryObservableList());
        double COLUMN_WIDTH = 100;

        // ************************************************************************************************************
        // receiptScene GUI

        Label receiptLabel = new Label();
        receiptLabel.setFont(bodyFont);
        receiptScene = new Scene(receiptLabel);

        Stage receiptStage = new Stage();
        receiptStage.setScene(receiptScene);
        receiptStage.setTitle("Receipt");

        // ************************************************************************************************************
        // saleScene GUI
        // Table view
        // Set tableView to SalesRecord observationList from ClientRecords
        tableView.setItems(clientRecords.getTableEntryObservableList());

        // Create table columns and set them to tableView
        TableColumn<TableEntry, String> productNameColumn = new TableColumn<>("Product Name");
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productNameColumn.setMinWidth(COLUMN_WIDTH);

        TableColumn<TableEntry, Integer> productQuantityColumn = new TableColumn<>("Quantity");
        productQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        productQuantityColumn.setMinWidth(COLUMN_WIDTH);

        // FORMAT TOTAL BEFORE PASSING TO COLUMN
        TableColumn<TableEntry, String> subtotalColumn = new TableColumn<>("Subtotal");
        subtotalColumn.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
        subtotalColumn.setMinWidth(COLUMN_WIDTH);

        // FORMAT TOTAL BEFORE PASSING TO COLUMN
        TableColumn<TableEntry, String> totalColumn = new TableColumn<>("Total");
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));
        totalColumn.setMinWidth(COLUMN_WIDTH);

        // NEED TO CHECK WHAT IS WRONG WITH THIS METHOD
        tableView.getColumns().addAll(productNameColumn,productQuantityColumn,subtotalColumn,totalColumn);

        // Change HBox
        Label changeLabel = new Label("Change : ");
        TextField changeTF = new TextField();
        HBox changeHB = new HBox(changeLabel,changeTF);
        changeLabel.setFont(bodyFont);
        changeTF.setFont(bodyFont);
        changeHB.setSpacing(sceneSpace);
        changeHB.setAlignment(Pos.CENTER);

        // New sale button
        Button newSaleButton = new Button("New Sale");
        newSaleButton.setFont(buttonFont);
        newSaleButton.setOnAction(event -> {
            // New sale

            // Reset records
            // Reset GUI fields

            // Reset tableview
        });

        // changeNewSale VBox
        VBox changeNewSale = new VBox(changeHB,newSaleButton);
        changeNewSale.setSpacing(sceneSpace);
        changeNewSale.setAlignment(Pos.CENTER);

        // Payment HBox
        Label paymentLabel = new Label("Payment : ");
        TextField paymentTF = new TextField();
        paymentTF.setPromptText(TYPE_HERE_DEFAULT);
        Button paymentButton = new Button("Pay");
        paymentButton.setOnAction(event -> {
            // Payment event
            // Check if payment input value is bigger or equal total
            try {
                if (checkPaymentInput(Double.parseDouble(paymentTF.getText()))) {
                    // Calc change
                    double changeAmount = checkOut(Double.parseDouble(paymentTF.getText()));

                    // Check out with payment input and set formatted change amount to change field
                    changeTF.setText(currencyFormat.format(changeAmount));

                    // Reset payment
                    paymentTF.setText("");

                    // Update tableview
                    tableView.setItems(clientRecords.getTableEntryObservableList());

                    // Set receipt then show receiptStage
                    receiptLabel.setText(clientRecords.createReceipt(changeAmount));
                    receiptStage.show();

                    // Reset clientRecords
                    clientRecords.resetClientRecords();
                } else {
                    paymentTF.setText("");
                }
            } catch (Exception exception) {
                // Input invalid, reset payment field
                paymentTF.setText("");
            }
        });
        HBox paymentHB = new HBox(paymentLabel,paymentTF,paymentButton);
        paymentLabel.setFont(bodyFont);
        paymentTF.setFont(bodyFont);
        paymentButton.setFont(buttonFont);
        paymentHB.setSpacing(sceneSpace);
        paymentHB.setAlignment(Pos.CENTER);

        // PayChange VBox
        VBox payChangeVB = new VBox(paymentHB,changeNewSale);
        payChangeVB.setSpacing(sceneSpace);
        payChangeVB.setAlignment(Pos.CENTER);

        // Code input HBox
        Label codeLabel = new Label("Product code : ");
        TextField codeTF = new TextField();
        codeTF.setPromptText(TYPE_HERE_DEFAULT);
        HBox codeHB = new HBox(codeLabel,codeTF);
        codeLabel.setFont(bodyFont);
        codeTF.setFont(bodyFont);
        codeHB.setSpacing(sceneSpace);
        codeHB.setAlignment(Pos.CENTER);

        // Quantity input HBox
        Label quantityLabel = new Label("Quantity amount : ");
        TextField quantityTF = new TextField();
        quantityTF.setPromptText(TYPE_HERE_DEFAULT);
        HBox quantityHB = new HBox(quantityLabel,quantityTF);
        quantityLabel.setFont(bodyFont);
        quantityTF.setFont(bodyFont);
        quantityHB.setSpacing(sceneSpace);
        quantityHB.setAlignment(Pos.CENTER);

        // Input VBox
        VBox inputVB = new VBox(codeHB,quantityHB);
        inputVB.setSpacing(sceneSpace);
        inputVB.setAlignment(Pos.CENTER);

        // Add button
        Button addButton = new Button("Add product");
        addButton.setFont(buttonFont);
        addButton.setOnAction(event -> {
            // Get code and quantity fields inputs
            try {
                // Field input valid
                if (!(codeTF.getText().equals("")) && (Integer.parseInt(quantityTF.getText()) > 0)) {
                    // Prompt server for product data
                    ClientHandler clientHandler = new ClientHandler(port,host,codeTF.getText());
                    clientHandler.run();

                    // Check if input is valid
                    if (clientHandler.isValidFlag()) {
                        // Valid
                        // Add product data and quantity to clientRecords
                        clientRecords.addProductSale(clientHandler.getReturnString(), Integer.parseInt(quantityTF.getText()));

                        // Update tableview
                        // Set tableView to SalesRecord observationList from ClientRecords
                        tableView.setItems(clientRecords.getTableEntryObservableList());
                    }
                    // Reset code and quantity fields
                    codeTF.setText("");
                    quantityTF.setText("");

                    // Reset change field if value is entered
                    if (!changeTF.getText().equals("")) {
                        changeTF.setText("");
                    }
                }
            } catch (Exception e) {
                // Field input invalid
                // Reset code and quantity fields
                codeTF.setText("");
                quantityTF.setText("");
            }
        });

        // Sale input HBox
        HBox saleInputHB = new HBox(inputVB,addButton);
        saleInputHB.setSpacing(sceneSpace);
        saleInputHB.setAlignment(Pos.CENTER);

        // Create and set sale node to saleScene
        VBox saleNode = new VBox(saleInputHB,tableView,payChangeVB);
        saleNode.setSpacing(sceneSpace);
        saleNode.setAlignment(Pos.CENTER);
        saleScene = new Scene(saleNode);

        // ************************************************************************************************************
        // connectionScene GUI
        // ServerAddress HBox
        Label serverAddressLabel = new Label("Server Address : ");
        TextField serverAddressTF = new TextField();
        serverAddressTF.setPromptText(TYPE_HERE_DEFAULT);
        HBox serverAddressHBox = new HBox(serverAddressLabel,serverAddressTF);
        serverAddressLabel.setFont(bodyFont);
        serverAddressTF.setFont(bodyFont);
        serverAddressHBox.setSpacing(sceneSpace);
        serverAddressHBox.setAlignment(Pos.CENTER);

        // serverConnectionButtons HBox
        Button connectButton = new Button("Connect");
        connectButton.setOnAction(event -> {
//            // Test if connectionAddress is valid
//            if (testConnectionAddress(serverAddressTF.getText(),port)) {
//                // Set connection parameters to address
//                setConnectionAddress(serverAddressTF.getText(),port);
//
//                // Set scene to saleScene
//                stage.setScene(saleScene);
//            }
            // Set connection parameters to address
            setConnectionAddress(serverAddressTF.getText(),port);

            // Set scene to saleScene
            stage.setScene(saleScene);
        });
        Button localHostButton = new Button("Local host connection");
        localHostButton.setOnAction(event -> {
//            // Connect to local host address
//            // Test if connection to local host is valid
//            if (testConnectionAddress(LOCAL_ADDRESS,port)) {
//                // Set connection parameters to localhost
//                setConnectionAddress(LOCAL_ADDRESS,port);
//
//                // Set scene to saleScene
//                stage.setScene(saleScene);
//            }
            // Connect to local host address
            // Set connection parameters to localhost
            setConnectionAddress(LOCAL_ADDRESS,port);

            // Set scene to saleScene
            stage.setScene(saleScene);
        });
        HBox serverConnectionButtonsHB = new HBox(connectButton,localHostButton);
        connectButton.setFont(buttonFont);
        localHostButton.setFont(buttonFont);
        serverConnectionButtonsHB.setSpacing(sceneSpace);
        serverConnectionButtonsHB.setAlignment(Pos.CENTER);

        // Create and set serverConnection node to connectionScene
        VBox serverConnectionVB = new VBox(serverAddressHBox,serverConnectionButtonsHB);
        serverConnectionVB.setSpacing(sceneSpace);
        serverConnectionVB.setAlignment(Pos.CENTER);
        connectionScene = new Scene(serverConnectionVB);

        // Set stage title and set to connection scene
        stage.setTitle("Client POS System");
        stage.setScene(connectionScene);
        stage.setOnCloseRequest(windowEvent -> {
            // Quit system
            System.exit(0);
        });
        stage.show();
    }

    public static void createReceiptGUI(String receiptInput, Stage receiptStage, Scene receiptScene) {
        TextField receiptTF = new TextField();
        receiptTF.setText(receiptInput);
        receiptTF.setFont(bodyFont);
        receiptStage.setTitle("Receipt");
        receiptStage.setScene(receiptScene);
        receiptStage.show();
        receiptStage.setOnCloseRequest(windowEvent -> {
            receiptStage.close();
        });
    }

    // Return true if greater than or equal to total
    public static boolean checkPaymentInput(Double input) {
        return clientRecords.checkPayment(input);
    }

    // Take input and return change amount
    public static double checkOut(Double input) {
        return clientRecords.checkout(input);
    }

    public static void setConnectionAddress(String hostInput, int portInput) {
        host = hostInput;
        port = portInput;
    }

    // Test if connection is valid, return true if valid, false if invalid
//    public static boolean testConnectionAddress(String hostInput, int portInput) {
//        return new ClientHandler(portInput,hostInput).testConnection();
//    }

    public static void main(String[] args) throws Exception {
        launch();
    }
}
