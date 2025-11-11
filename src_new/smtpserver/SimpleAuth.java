package smtpserver;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class SimpleAuth {
    private static final String USERS_FILE = "server_users.txt";

    public static synchronized boolean register(String user, String pass) {
        Map<String,String> users = loadUsers();
        if (users.containsKey(user)) return false;
        users.put(user, pass);
        saveUsers(users);
        return true;
    }

    public static synchronized boolean login(String user, String pass) {
        Map<String,String> users = loadUsers();
        return users.containsKey(user) && users.get(user).equals(pass);
    }

    private static Map<String,String> loadUsers() {
        Map<String,String> m = new HashMap<>();
        File f = new File(USERS_FILE);
        if (!f.exists()) return m;
        try(BufferedReader br = new BufferedReader(new FileReader(f))) {
            String l;
            while ((l = br.readLine()) != null) {
                String[] p = l.split(":",2);
                if (p.length==2) m.put(p[0], p[1]);
            }
        } catch(Exception ignored){}
        return m;
    }

    private static void saveUsers(Map<String,String> users) {
        try(PrintWriter pw = new PrintWriter(new FileWriter(USERS_FILE))) {
            for (Map.Entry<String,String> e: users.entrySet()) pw.println(e.getKey()+":"+e.getValue());
        } catch(Exception ignored){}
    }
}
