import java.net.InetAddress;

public class Peer {
    private final String name;
    private final InetAddress address;
    private final int port;

    public Peer(String name, InetAddress address, int port) {
        this.name = name;
        this.address = address;
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public InetAddress getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public String toString() {
        return name + "@" + address.getHostAddress() + ":" + port;
    }
}
