package hw6_peters_server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ServerHandler implements Runnable {
    private Socket socket;

    private Connection connection;

    private static ResultSet resultSet;

    private static String returnString;

    private static boolean resultSetEmpty;

    public ServerHandler(Socket socketInput, Connection connectionInput) {
        socket = socketInput;
        connection = connectionInput;
        returnString = "";
        resultSetEmpty = true;
    }

    /**
     *
     *
     * @param dbConnection
     * @param codeInput
     * @return
     * @throws SQLException
     */
    public static void setResultSetFromCode(Connection dbConnection, String codeInput) {
        try {
            // Create statement for database
            Statement statement = dbConnection.createStatement();

            // Find and return statement from database
            resultSet = statement.executeQuery("select * from item where item_code = '" + codeInput + "';");

        } catch (Exception exception) {
            resultSet = null;
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

            // Set resultSet from code input
            setResultSetFromCode(connection,codeInput);

            if (!resultSet.next()) {
                // Input invalid, send empty string to clientOutput to indicate invalid input
                returnString = "";
            } else {
                do {
                    // Input valid, send return string to clientOutput
                    // Concat result list from database
                    returnString = returnString.concat(
                            resultSet.getString(1) + "," +
                                    resultSet.getString(2) + "," +
                                    resultSet.getString(3));

                } while (resultSet.next());
            }

            // Send returnString to client
            clientOutput.writeUTF(returnString);

        } catch (EOFException | SQLException ignored) {

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return returnString;
    }
}
