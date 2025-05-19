package server;

import java.net.InetAddress;

public class WorkThisMessage {
    private final Object message;
    private final InetAddress clientIp;
    private final int clientPort;

    public WorkThisMessage(Object message, InetAddress clientIp, int clientPort) {
        this.message = message;
        this.clientIp = clientIp;
        this.clientPort = clientPort;
    }

    public Object getMessage() {
        return message;
    }

    public InetAddress getClientIp() {
        return clientIp;
    }

    public int getClientPort() {
        return clientPort;
    }
}
