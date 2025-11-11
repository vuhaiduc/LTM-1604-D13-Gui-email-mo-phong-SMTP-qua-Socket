package smtpserver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ServerMain {
    public static final int AUTH_PORT = 2525;      // login/register + send short connections
    public static final int RECEIVER_PORT = 2626;  // persistent connections for receivers

    // username -> ObjectOutputStream (to send Email objects)
    private static final ConcurrentMap<String, ObjectOutputStream> onlineReceivers = new ConcurrentHashMap<>();

    public static void registerReceiver(String username, ObjectOutputStream out) {
        onlineReceivers.put(username, out);
        System.out.println("[Server] Registered receiver: " + username);
    }

    public static void unregisterReceiver(String username) {
        onlineReceivers.remove(username);
        System.out.println("[Server] Unregistered receiver: " + username);
    }

    public static ObjectOutputStream getReceiverStream(String username) {
        return onlineReceivers.get(username);
    }

    public static void main(String[] args) {
        System.out.println("[Server] Starting. AUTH port=" + AUTH_PORT + ", RECEIVER port=" + RECEIVER_PORT);

        // Thread for auth/send (short-lived connections)
        new Thread(() -> {
            try (ServerSocket server = new ServerSocket(AUTH_PORT)) {
                System.out.println("[Server] AUTH server listening on " + AUTH_PORT);
                while (true) {
                    Socket s = server.accept();
                    new Thread(new SenderHandler(s)).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "AuthServer").start();

        // Thread for persistent receiver connections
        new Thread(() -> {
            try (ServerSocket server = new ServerSocket(RECEIVER_PORT)) {
                System.out.println("[Server] RECEIVER server listening on " + RECEIVER_PORT);
                while (true) {
                    Socket s = server.accept();
                    new Thread(new ReceiverHandler(s)).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "ReceiverServer").start();
    }
}
