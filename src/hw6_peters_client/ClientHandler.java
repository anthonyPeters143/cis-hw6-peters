package hw6_peters_client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private int port;

    private String host;

    private String code;

    private String returnString;

    private boolean validFlag;

//    ClientHandler(int portInput, String hostInput) {
//        port = portInput;
//        host = hostInput;
//    }

    ClientHandler(int portInput, String hostInput, String codeInput) {
        port = portInput;
        host = hostInput;
        code = codeInput;
        returnString = "";
        validFlag = false;
    }

    public void setCode(String codeInput) {
        code = codeInput;
    }

    @Override
    public void run() {
        try {
            // Create socket connection
            Socket socket = new Socket(host,port);

            // Set up dataInput and dataOutput stream
            DataOutputStream clientOutput = new DataOutputStream(socket.getOutputStream());
            DataInputStream clientInput = new DataInputStream(socket.getInputStream());

            // Output to server
            clientOutput.writeUTF(code);

            returnString = clientInput.readUTF();

            // Check if return valid is valid, set valid flag
            if (!returnString.equals("")) {
                validFlag = true;
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean testConnection() {
        try {
            // Try to create socket connection
            Socket socket = new Socket(host,port);

            // Close socket
            socket.close();

            // Connection valid
            return true;
        } catch (IOException ioException) {
            // Connection invalid
            return false;
        }
    }

    public String getReturnString() {
        return returnString;
    }

    public boolean isValidFlag() {
        return validFlag;
    }


}
