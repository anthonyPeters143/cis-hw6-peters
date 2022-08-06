package hw6_peters_server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ServerHandler implements Runnable {
    private Socket socket;

    private Connection connection;

    public ServerHandler(Socket socketInput, Connection connectionInput) {
        socket = socketInput;
        connection = connectionInput;
    }

    /**
     *
     *
     * @param dbConnection
     * @param codeInput
     * @return
     * @throws SQLException
     */
    public static ResultSet getResultFromCode(Connection dbConnection, String codeInput) {
        try {
            // Create statement for database
            Statement statement = dbConnection.createStatement();

            // Find and return statement from database
            return statement.executeQuery("select * from item where item_code = '" + codeInput + "';");

        } catch (SQLException sqlException) {
            return null;
        }

    }

    @Override
    public void run() {
        try {
            // Set up dataInput and dataOutput streams
            DataInputStream clientInput = new DataInputStream(socket.getInputStream());
            DataOutputStream clientOutput = new DataOutputStream(socket.getOutputStream());

            // Get client code input
            String codeInput = clientInput.readUTF();

            // Find results
            ResultSet resultSet = getResultFromCode(connection,codeInput);

            String returnString = "";

            // Cycle result list
            while (resultSet.next()) {
                returnString = returnString.concat(resultSet.getString(1) + "," +
                        resultSet.getString(2) + "," +
                        resultSet.getString(3)
                );
            }

            // Send data to client
            clientOutput.writeUTF(returnString);

            // Update gui


        } catch (IOException | NullPointerException exception) {
            exception.printStackTrace();

            // UPDATE TO SEND BY FEEDBACK
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
//            throw new RuntimeException(sqlException);

            // UPDATE TO SEND BY FEEDBACK
        }

    }

}
