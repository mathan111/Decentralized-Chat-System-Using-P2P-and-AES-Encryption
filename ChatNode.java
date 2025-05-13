import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatNode {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        int udpPort = 8888;
        int tcpPort = 9000 + (int)(Math.random() * 1000);

        CopyOnWriteArrayList<Peer> peers = new CopyOnWriteArrayList<>();
        PeerDiscovery discovery = new PeerDiscovery(name, udpPort, peers);
        discovery.start();

        ServerSocket serverSocket = new ServerSocket(tcpPort);
        new Thread(() -> {
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    new MessageHandler(socket).start();
                } catch (Exception ignored) {}
            }
        }).start();

        while (true) {
            System.out.println("Peers:");
            for (int i = 0; i < peers.size(); i++) {
                System.out.println((i + 1) + ": " + peers.get(i));
            }
            System.out.print("Select peer to chat or 0 to refresh: ");
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice <= 0 || choice > peers.size()) continue;
            Peer peer = peers.get(choice - 1);
            Socket socket = new Socket(peer.getAddress(), peer.getPort());
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            new MessageHandler(socket).start();
            while (true) {
                String msg = scanner.nextLine();
                if (msg.equalsIgnoreCase("/exit")) break;
                String encrypted = CryptoUtils.encrypt(name + ": " + msg);
                out.write(encrypted);
                out.newLine();
                out.flush();
            }
        }
    }
}
