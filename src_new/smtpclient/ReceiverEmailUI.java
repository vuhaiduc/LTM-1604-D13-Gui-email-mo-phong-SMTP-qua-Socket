package smtpclient;

import common.Email;
import common.Email.Attachment;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.Socket;
import java.util.List;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class ReceiverEmailUI extends JFrame {
    private DefaultListModel<Email> emailListModel;
    private JList<Email> emailList;
    private JTextArea emailContentArea;
    private JPanel attachmentsPanel;

    private Socket socket;
    private ObjectInputStream ois;

    public ReceiverEmailUI(String username) {
        setTitle("Hộp thư đến - " + username);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 950, 600);

        JPanel contentPane = new JPanel(new BorderLayout(10,10));
        contentPane.setBorder(new EmptyBorder(10,10,10,10));
        contentPane.setBackground(new Color(245,245,245));
        setContentPane(contentPane);

        // Danh sách email bên trái
        emailListModel = new DefaultListModel<>();
        emailList = new JList<>(emailListModel);
        emailList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        emailList.setCellRenderer(new EmailCellRenderer());
        emailList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) showEmail(emailList.getSelectedValue());
        });
        JScrollPane emailScroll = new JScrollPane(emailList);
        emailScroll.setPreferredSize(new Dimension(250,0));
        emailScroll.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY,1));
        contentPane.add(emailScroll, BorderLayout.WEST);

        // Nội dung email bên phải
        JPanel rightPanel = new JPanel(new BorderLayout(10,10));
        rightPanel.setBackground(new Color(245,245,245));

        emailContentArea = new JTextArea();
        emailContentArea.setEditable(false);
        emailContentArea.setLineWrap(true);
        emailContentArea.setWrapStyleWord(true);
        emailContentArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emailContentArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY,1));
        emailContentArea.setBackground(Color.WHITE);
        JScrollPane contentScroll = new JScrollPane(emailContentArea);
        rightPanel.add(contentScroll, BorderLayout.CENTER);

        attachmentsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,10,10));
        attachmentsPanel.setBackground(Color.WHITE);
        JScrollPane attachScroll = new JScrollPane(attachmentsPanel);
        attachScroll.setPreferredSize(new Dimension(0, 220));
        attachScroll.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY,1));
        rightPanel.add(attachScroll, BorderLayout.SOUTH);

        contentPane.add(rightPanel, BorderLayout.CENTER);

        // Kết nối server nhận email
        new Thread(() -> connectReceiver(username)).start();
    }

    /** Kết nối server RECEIVER_PORT và nhận Email */
    private void connectReceiver(String username) {
        try {
            socket = new Socket("localhost", 2626);

            PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
            pw.println(username);

            ois = new ObjectInputStream(socket.getInputStream());

            while (true) {
                Object obj = ois.readObject();
                if (obj instanceof Email) {
                    Email email = (Email) obj;
                    SwingUtilities.invokeLater(() -> emailListModel.addElement(email));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(this, "Mất kết nối với server nhận email!", "Lỗi", JOptionPane.ERROR_MESSAGE)
            );
        }
    }

    /** Hiển thị email và các file đính kèm */
    private void showEmail(Email email) {
        if (email == null) return;
        emailContentArea.setText(
            "Từ: " + email.getSender() + "\n" +
            "Đến: " + email.getRecipient() + "\n" +
            "Chủ đề: " + email.getSubject() + "\n\n" +
            email.getBody()
        );

        attachmentsPanel.removeAll();
        List<Attachment> attachments = email.getAttachments();
        if (attachments != null && !attachments.isEmpty()) {
            for (Attachment attachment : attachments) {
                String fileName = attachment.getFileName();
                try {
                    if (fileName.toLowerCase().matches(".*\\.(png|jpg|jpeg|bmp|gif)$")) {
                        BufferedImage img = ImageIO.read(new ByteArrayInputStream(attachment.getFileData()));
                        if (img != null) {
                            ImageIcon icon = createThumbnail(img, 200);
                            JLabel picLabel = new JLabel(icon);
                            picLabel.setToolTipText(fileName);
                            picLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY,1));
                            picLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                            picLabel.addMouseListener(new MouseAdapter() {
                                public void mouseClicked(MouseEvent e) {
                                    showFullImage(img, fileName);
                                }
                            });
                            attachmentsPanel.add(picLabel);
                        }
                    } else {
                        JLabel fileLabel = new JLabel("[File] " + fileName);
                        fileLabel.setForeground(new Color(70,130,180));
                        fileLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                        fileLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY,1));
                        fileLabel.setOpaque(true);
                        fileLabel.setBackground(Color.WHITE);
                        fileLabel.setPreferredSize(new Dimension(180,30));
                        fileLabel.setHorizontalAlignment(SwingConstants.CENTER);

                        fileLabel.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                try {
                                    File temp = File.createTempFile("email_attach_", "_" + fileName);
                                    temp.deleteOnExit();
                                    try (FileOutputStream fos = new FileOutputStream(temp)) {
                                        fos.write(attachment.getFileData());
                                    }
                                    Desktop.getDesktop().open(temp);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    JOptionPane.showMessageDialog(ReceiverEmailUI.this,
                                            "Không mở được tệp: " + fileName,
                                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        });
                        attachmentsPanel.add(fileLabel);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    attachmentsPanel.add(new JLabel("Tệp lỗi: " + fileName));
                }
            }
        }

        attachmentsPanel.revalidate();
        attachmentsPanel.repaint();
    }

    /** Tạo thumbnail chất lượng cao, giữ nét */
    private ImageIcon createThumbnail(BufferedImage img, int maxSize) {
        int w = img.getWidth();
        int h = img.getHeight();
        float scale = Math.min((float) maxSize / w, (float) maxSize / h);
        int newW = Math.round(w * scale);
        int newH = Math.round(h * scale);

        BufferedImage thumb = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = thumb.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawImage(img, 0, 0, newW, newH, null);
        g2d.dispose();
        return new ImageIcon(thumb);
    }

    /** Mở full-size ảnh, giữ chất lượng */
    private void showFullImage(BufferedImage img, String title) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenW = screenSize.width - 100;
        int screenH = screenSize.height - 100;

        int imgW = img.getWidth();
        int imgH = img.getHeight();

        float scale = Math.min(1.0f, Math.min((float)screenW / imgW, (float)screenH / imgH));
        int dispW = Math.round(imgW * scale);
        int dispH = Math.round(imgH * scale);

        BufferedImage displayImg = new BufferedImage(dispW, dispH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = displayImg.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawImage(img, 0, 0, dispW, dispH, null);
        g2d.dispose();

        JLabel imgLabel = new JLabel(new ImageIcon(displayImg));
        frame.add(imgLabel);
        frame.setSize(dispW, dispH);
        frame.setLocationRelativeTo(this);
        frame.setVisible(true);
    }

    /** Custom renderer danh sách email hiện đại */
    private static class EmailCellRenderer extends JLabel implements ListCellRenderer<Email> {
        public EmailCellRenderer() {
            setOpaque(true);
            setBorder(BorderFactory.createEmptyBorder(5,10,5,10));
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Email> list, Email value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            setText("<html><b>" + value.getSender() + "</b><br/>" + value.getSubject() + "</html>");
            setFont(new Font("Segoe UI", Font.PLAIN, 14));
            if (isSelected) {
                setBackground(new Color(70,130,180));
                setForeground(Color.WHITE);
            } else {
                setBackground(Color.WHITE);
                setForeground(Color.BLACK);
            }
            setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY,1));
            return this;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ReceiverEmailUI("receiverUser").setVisible(true));
    }
}
