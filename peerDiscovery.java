import java.net.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class PeerDiscovery extends Thread {
    private final int udpPort;
    private final String name;
    private final CopyOnWriteArrayList<Peer> peers;
    private boolean running = true;

    public PeerDiscovery(String name, int udpPort, CopyOnWriteArrayList<Peer> peers) {
        this.name = name;
        this.udpPort = udpPort;
        this.peers = peers;
    }

    public void run() {
        try {
            DatagramSocket socket = new DatagramSocket();
            DatagramSocket listener = new DatagramSocket(udpPort, InetAddress.getByName("0.0.0.0"));
            listener.setBroadcast(true);

            new Thread(() -> {
                while (running) {
                    try {
                        String msg = name + ":" + udpPort;
                        byte[] buffer = msg.getBytes();
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length,
                                InetAddress.getByName("255.255.255.255"), udpPort);
                        socket.send(packet);
                        Thread.sleep(3000);
                    } catch (Exception ignored) {}
                }
            }).start();

            byte[] recvBuf = new byte[15000];
            while (running) {
                DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
                listener.receive(packet);
                String data = new String(packet.getData(), 0, packet.getLength());
                if (!data.startsWith(name)) {
                    String[] parts = data.split(":");
                    if (parts.length == 2) {
                        String peerName = parts[0];
                        int port = Integer.parseInt(parts[1]);
                        Peer peer = new Peer(peerName, packet.getAddress(), port);
                        if (peers.stream().noneMatch(p -> p.toString().equals(peer.toString()))) {
                            peers.add(peer);
                        }
                    }
                }
            }
        } catch (Exception ignored) {}
    }

    public void stopDiscovery() {
        running = false;
    }
}
