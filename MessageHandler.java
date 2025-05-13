import java.io.*;
import java.net.Socket;

public class MessageHandler extends Thread {
    private final Socket socket;

    public MessageHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String encrypted;
            while ((encrypted = in.readLine()) != null) {
                String msg = CryptoUtils.decrypt(encrypted);
                System.out.println(">> " + msg);
            }
        } catch (Exception ignored) {}
    }
}
