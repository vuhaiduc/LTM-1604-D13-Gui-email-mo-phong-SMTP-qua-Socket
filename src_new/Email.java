package common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Email implements Serializable {
    private static final long serialVersionUID = 1L;

    private String sender;
    private String recipient;
    private String subject;
    private String body;
    private List<Attachment> attachments = new ArrayList<>();

    public Email() {}

    public Email(String sender, String recipient, String subject, String body) {
        this.sender = sender;
        this.recipient = recipient;
        this.subject = subject;
        this.body = body;
    }

    // getters
    public String getSender() { return sender; }
    public String getRecipient() { return recipient; }
    public String getSubject() { return subject; }
    public String getBody() { return body; }
    public List<Attachment> getAttachments() { return attachments; }

    public void addAttachment(String fileName, byte[] fileData) {
        attachments.add(new Attachment(fileName, fileData));
    }

    @Override
    public String toString() {
        return String.format("%s — %s", subject, sender);
    }

    public static class Attachment implements Serializable {
        private static final long serialVersionUID = 1L;
        private String fileName;
        private byte[] fileData;

        public Attachment() {}

        public Attachment(String fileName, byte[] fileData) {
            this.fileName = fileName;
            this.fileData = fileData;
        }

        public String getFileName() { return fileName; }
        public byte[] getFileData() { return fileData; }
    }
}
