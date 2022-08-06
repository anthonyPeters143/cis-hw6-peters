package hw6_peters_client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private int port;

    private String host;

    private String code;

    ClientHandler(int portInput, String hostInput, String codeInput) {
        port = portInput;
        host = hostInput;
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

            System.out.println(clientInput.readUTF());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
