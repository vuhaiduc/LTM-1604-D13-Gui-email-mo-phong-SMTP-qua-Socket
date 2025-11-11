package smtpclient;

import common.Email;
import common.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientEmailUI extends JFrame {
    private static final int SEND_PORT = 2525;

    private JTextField recipientField, subjectField;
    private JTextArea bodyArea;
    private DefaultListModel<File> fileListModel;
    private String sender;

    private List<Email> sentEmails = new ArrayList<>();
    private Map<Email, String> sentEmailTimes = new HashMap<>();

    public ClientEmailUI(String senderUsername) {
        this.sender = senderUsername;
        setTitle("Gửi Email - " + sender);
        setSize(750, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- Panel thông tin gửi ---
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridBagLayout());
        topPanel.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Người gửi (hiển thị cố định)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        JLabel senderLabel = new JLabel("Người gửi:");
        topPanel.add(senderLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        JTextField senderField = new JTextField(sender);
        senderField.setEditable(false);
        senderField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        senderField.setBackground(Color.WHITE);
        senderField.setPreferredSize(new Dimension(200, 30));
        topPanel.add(senderField, gbc);

        // Người nhận
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        JLabel recipientLabel = new JLabel("Người nhận:");
        topPanel.add(recipientLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        recipientField = new JTextField();
        recipientField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        recipientField.setBackground(Color.WHITE);
        recipientField.setPreferredSize(new Dimension(200, 30));
        recipientField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        recipientField.setOpaque(true);
        recipientField.setMargin(new Insets(5, 5, 5, 5));
        topPanel.add(recipientField, gbc);

        // Tiêu đề
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        JLabel subjectLabel = new JLabel("Tiêu đề:");
        topPanel.add(subjectLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        subjectField = new JTextField();
        subjectField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        subjectField.setBackground(Color.WHITE);
        subjectField.setPreferredSize(new Dimension(200, 30));
        subjectField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subjectField.setMargin(new Insets(5, 5, 5, 5));
        topPanel.add(subjectField, gbc);

        // --- Nội dung email ---
        bodyArea = new JTextArea();
        bodyArea.setLineWrap(true);
        bodyArea.setWrapStyleWord(true);
        bodyArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        bodyArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        bodyArea.setMargin(new Insets(8, 8, 8, 8));
        JScrollPane bodyScroll = new JScrollPane(bodyArea);

        // --- Danh sách tệp đính kèm ---
        fileListModel = new DefaultListModel<>();
        JList<File> fileList = new JList<>(fileListModel);
        fileList.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        JScrollPane fileScroll = new JScrollPane(fileList);
        fileScroll.setPreferredSize(new Dimension(200, 0));

        // --- Nút bấm ---
        JButton attachBtn = new JButton("Chọn tệp đính kèm");
        styleButton(attachBtn);

        attachBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setMultiSelectionEnabled(true);
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                for (File f : chooser.getSelectedFiles()) {
                    fileListModel.addElement(f);
                }
            }
        });

        JButton sendBtn = new JButton("Gửi");
        styleButton(sendBtn);
        sendBtn.addActionListener(e -> sendEmail());

        JButton viewSentBtn = new JButton("Xem email đã gửi");
        styleButton(viewSentBtn);
        viewSentBtn.addActionListener(e -> showSentEmailsUI());

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(245, 245, 245));
        bottomPanel.add(attachBtn);
        bottomPanel.add(sendBtn);
        bottomPanel.add(viewSentBtn);

        add(topPanel, BorderLayout.NORTH);
        add(bodyScroll, BorderLayout.CENTER);
        add(fileScroll, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void styleButton(JButton btn) {
        btn.setFocusPainted(false);
        btn.setBackground(new Color(70, 130, 180));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void sendEmail() {
        String to = recipientField.getText().trim();
        String subj = subjectField.getText().trim();
        String body = bodyArea.getText();

        if (to.isEmpty() || subj.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ người nhận và tiêu đề.");
            return;
        }

        try (Socket socket = new Socket("localhost", SEND_PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {

            out.flush();

            Email email = new Email(sender, to, subj, body);

            for (int i = 0; i < fileListModel.size(); i++) {
                File f = fileListModel.get(i);
                byte[] data = FileUtils.readFileToBytes(f);
                email.addAttachment(f.getName(), data);
            }

            out.writeObject(email);
            out.flush();

            JOptionPane.showMessageDialog(this, "✅ Đã gửi email thành công!");

            String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            sentEmails.add(email);
            sentEmailTimes.put(email, currentTime);

            recipientField.setText("");
            subjectField.setText("");
            bodyArea.setText("");
            fileListModel.clear();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "❌ Lỗi gửi: " + ex.getMessage());
        }
    }

    private void showSentEmailsUI() {
        JFrame sentFrame = new JFrame("Email đã gửi");
        sentFrame.setSize(600, 400);
        sentFrame.setLocationRelativeTo(this);
        sentFrame.setLayout(new BorderLayout());

        DefaultListModel<String> tableListModel = new DefaultListModel<>();
        for (Email e : sentEmails) {
            String time = sentEmailTimes.getOrDefault(e, "");
            tableListModel.addElement(e.getRecipient() + " | " + e.getSubject() + " | " + time);
        }

        JList<String> sentList = new JList<>(tableListModel);
        sentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(sentList);

        JButton deleteBtn = new JButton("Xóa email");
        styleButton(deleteBtn);
        deleteBtn.addActionListener(e -> {
            int idx = sentList.getSelectedIndex();
            if (idx >= 0) {
                Email removed = sentEmails.remove(idx);
                sentEmailTimes.remove(removed);
                tableListModel.remove(idx);
            }
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(deleteBtn);

        sentFrame.add(scroll, BorderLayout.CENTER);
        sentFrame.add(bottomPanel, BorderLayout.SOUTH);
        sentFrame.setVisible(true);
    }
}
