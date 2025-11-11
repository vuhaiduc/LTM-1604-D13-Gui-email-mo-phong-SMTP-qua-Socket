package smtpclient;

import javax.swing.*;
import java.awt.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import smtpserver.ServerMain;

public class ClientLoginUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel statusLabel;

    public ClientLoginUI() {
        setTitle("Đăng nhập - Người gửi");
        setSize(450, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel chính với gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(66, 133, 244); // Xanh đậm
                Color color2 = new Color(144, 202, 249); // Xanh nhạt
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Card Panel (bo tròn, nền sáng)
        JPanel card = new JPanel();
        card.setBackground(new Color(245, 245, 245, 230));
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        card.setLayout(new GridBagLayout());
        card.setOpaque(true);
        card.setPreferredSize(new Dimension(350, 280));

        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        card.add(new JLabel("Tên đăng nhập: "), gbc);
        gbc.gridy = 1;
        usernameField = new JTextField(20);
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                usernameField.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        card.add(usernameField, gbc);

        // Password
        gbc.gridy = 2;
        card.add(new JLabel("Mật khẩu:"), gbc);
        gbc.gridy = 3;
        passwordField = new JPasswordField(20);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                passwordField.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        card.add(passwordField, gbc);

        // Status
        gbc.gridy = 4;
        statusLabel = new JLabel("", SwingConstants.CENTER);
        statusLabel.setForeground(Color.RED);
        card.add(statusLabel, gbc);

        // Nút login/register
        gbc.gridy = 5;
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(new Color(0, 0, 0, 0));
        JButton loginButton = new JButton("Đăng nhập");
        JButton registerButton = new JButton("Đăng ký");
        styleButton(loginButton);
        styleButton(registerButton);
        btnPanel.add(loginButton);
        btnPanel.add(registerButton);
        card.add(btnPanel, gbc);

        mainPanel.add(card);
        add(mainPanel);

        // Action
        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> handleRegister());
    }

    private void styleButton(JButton button) {
        button.setFocusPainted(false);
        button.setBackground(new Color(30, 136, 229));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
    }

    private void handleLogin() {
        String user = usernameField.getText().trim();
        String pass = new String(passwordField.getPassword()).trim();
        if (user.isEmpty() || pass.isEmpty()) {
            statusLabel.setText("Nhập đủ thông tin!");
            return;
        }
        try (Socket socket = new Socket("localhost", ServerMain.AUTH_PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            out.flush();
            Map<String, String> req = new HashMap<>();
            req.put("type", "login");
            req.put("username", user);
            req.put("password", pass);
            out.writeObject(req);
            out.flush();
            Object resp = in.readObject();
            if (resp instanceof String && "success".equals(resp)) {
                statusLabel.setText("Đăng nhập thành công");
                SwingUtilities.invokeLater(() -> {
                    new ClientEmailUI(user).setVisible(true);
                    dispose();
                });
            } else {
                statusLabel.setText("Sai tài khoản hoặc mật khẩu");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            statusLabel.setText("Lỗi kết nối tới server");
        }
    }

    private void handleRegister() {
        String user = usernameField.getText().trim();
        String pass = new String(passwordField.getPassword()).trim();
        if (user.isEmpty() || pass.isEmpty()) {
            statusLabel.setText("Nhập đủ thông tin!");
            return;
        }
        try (Socket socket = new Socket("localhost", ServerMain.AUTH_PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            out.flush();
            Map<String, String> req = new HashMap<>();
            req.put("type", "register");
            req.put("username", user);
            req.put("password", pass);
            out.writeObject(req);
            out.flush();
            Object resp = in.readObject();
            if (resp instanceof String && "registered".equals(resp)) {
                statusLabel.setText("Đăng ký thành công");
            } else if (resp instanceof String && "exists".equals(resp)) {
                statusLabel.setText("Tài khoản đã tồn tại");
            } else {
                statusLabel.setText("Đăng ký thất bại");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            statusLabel.setText("Lỗi kết nối tới server");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ClientLoginUI().setVisible(true));
    }
}
