package hw6_peters_client;

import java.io.IOException;
import java.text.DecimalFormat;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class HW6_Client {

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

    public static void main(String[] args) throws IOException {
        // Initialize
        int port = 8001;
        String host = "localhost";

//        String codeInput;
//        Scanner in = new Scanner(System.in);
//
//        System.out.print("Input code to search for : ");
//        codeInput = in.nextLine();
//
//        new clientHandler(port,host,codeInput).run();

        // GUI String references
        String  TYPE_HERE_DEFAULT       = "Type here...";

        // Server connection GUI
        // ServerAddress HBox
        Label serverAddressLabel = new Label("Server Address : ");

        // ************************************************************************************************************
        // Sale GUI
        // Code HBox
        Label codeLabel = new Label("Product code : ");
        TextField codeTF = new TextField();
        codeTF.setPromptText(TYPE_HERE_DEFAULT);
        HBox codeHB = new HBox(codeLabel,codeTF);
        codeLabel.setFont(bodyFont);
        codeTF.setFont(bodyFont);
        codeHB.setSpacing(sceneSpace);
        codeHB.setAlignment(Pos.CENTER);

        // Quantity HBox
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

        // New sale button
        Button newSaleButton = new Button("New Sale");
        newSaleButton.setFont(buttonFont);
        newSaleButton.setOnAction(event -> {
            // New sale

            // Reset records
            // Reset GUI fields
        });

        // Add button
        Button addButton = new Button("Add product");
        addButton.setFont(buttonFont);
        addButton.setOnAction(event -> {
            // Get code field input
            // Get quantity field input

            // Add product to sale
        });

        // Button VBox
        VBox inputButtonsVbox = new VBox(newSaleButton,addButton);
        inputButtonsVbox.setSpacing(sceneSpace);
        inputButtonsVbox.setAlignment(Pos.CENTER);

        // Table view

        // Payment HBox
        Label paymentLabel = new Label("Payment : ");
        TextField paymentTF = new TextField();
        paymentTF.setPromptText(TYPE_HERE_DEFAULT);
        Button paymentButton = new Button("Pay");
        paymentButton.setOnAction(event -> {
            // Payment event
        });
        HBox paymentHB = new HBox(paymentLabel,paymentTF,paymentButton);
        paymentLabel.setFont(bodyFont);
        paymentTF.setFont(bodyFont);
        paymentButton.setFont(buttonFont);
        paymentHB.setSpacing(sceneSpace);
        paymentHB.setAlignment(Pos.CENTER);

        // Change HBox
        Label changeLabel = new Label("Change : ");
        TextField changeTF = new TextField();
        HBox changeHB = new HBox(changeLabel,changeTF);
        changeLabel.setFont(bodyFont);
        changeTF.setFont(bodyFont);
        changeHB.setSpacing(sceneSpace);
        changeHB.setAlignment(Pos.CENTER);

        // PayChange VBox
        VBox payChangeVB = new VBox(paymentHB,changeHB);
        payChangeVB.setSpacing(sceneSpace);
        payChangeVB.setAlignment(Pos.CENTER);

        // Scene Vbox

    }


}
