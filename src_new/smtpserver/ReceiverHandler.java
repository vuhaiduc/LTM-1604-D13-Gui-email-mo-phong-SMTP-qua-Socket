package smtpserver;

import java.io.*;
import java.net.Socket;

public class ReceiverHandler implements Runnable {
    private final Socket socket;

    public ReceiverHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        String username = null;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {

            // Protocol: receiver sends username as a single line when connects
            username = br.readLine();
            if (username == null || username.trim().isEmpty()) {
                socket.close();
                return;
            }
            username = username.trim();
            out.flush();

            // register receiver stream so server can forward Email objects
            ServerMain.registerReceiver(username, out);

            // keep reading possible commands from receiver (optional)
            try {
                while (true) {
                    String line = br.readLine();
                    if (line == null) break;
                    // currently we don't process commands from receiver; just print for debug
                    System.out.println("[Server] cmd from receiver " + username + ": " + line);
                }
            } catch (IOException ignored) {
                // disconnect
            }

        } catch (Exception e) {
            // e.printStackTrace();
        } finally {
            if (username != null) ServerMain.unregisterReceiver(username);
            try { socket.close(); } catch (IOException ignored) {}
            System.out.println("[Server] Receiver disconnected: " + username);
        }
    }
}
