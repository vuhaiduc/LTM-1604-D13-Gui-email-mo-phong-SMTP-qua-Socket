package smtpserver;

import common.Email;

import java.io.*;
import java.net.Socket;
import java.util.Map;

public class SenderHandler implements Runnable {
    private final Socket socket;

    public SenderHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.flush();

            while (true) {
                Object obj = in.readObject();
                if (obj == null) break;

                if (obj instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String,String> map = (Map<String,String>) obj;
                    String type = map.get("type");
                    String username = map.get("username");
                    String password = map.get("password");

                    if ("register".equalsIgnoreCase(type)) {
                        boolean ok = SimpleAuth.register(username, password);
                        out.writeObject(ok ? "registered" : "exists");
                        out.flush();
                    } else if ("login".equalsIgnoreCase(type)) {
                        boolean ok = SimpleAuth.login(username, password);
                        out.writeObject(ok ? "success" : "fail");
                        out.flush();
                    } else {
                        out.writeObject("unknown");
                        out.flush();
                    }
                } else if (obj instanceof Email) {
                    Email email = (Email) obj;
                    System.out.println("[Server] Got email from " + email.getSender() + " -> " + email.getRecipient());
                    ObjectOutputStream recvOut = ServerMain.getReceiverStream(email.getRecipient());
                    if (recvOut != null) {
                        try {
                            recvOut.writeObject(email);
                            recvOut.flush();
                            out.writeObject("delivered");
                            out.flush();
                            System.out.println("[Server] Forwarded to " + email.getRecipient());
                        } catch (Exception e) {
                            out.writeObject("deliver_failed");
                            out.flush();
                            e.printStackTrace();
                        }
                    } else {
                        out.writeObject("recipient_offline");
                        out.flush();
                        System.out.println("[Server] Recipient offline: " + email.getRecipient());
                    }
                } else {
                    out.writeObject("unsupported");
                    out.flush();
                }
            }

        } catch (EOFException eof) {
            // client closed
        } catch (Exception e) {
            System.out.println("[Server] SenderHandler error: " + e.getMessage());
        } finally {
            try { socket.close(); } catch (IOException ignored) {}
        }
    }
}
