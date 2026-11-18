import java.util.ArrayList;

public class MessageManager {

    private ArrayList<Message> messages = new ArrayList<>();
    private int messageCounter = 0;

    public void addMessage(Message msg) {
        messages.add(msg);
        messageCounter++;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    // Requirement: A method that returns a formatted string of all messages.
    public String displayMessages() {
        if (messages.isEmpty()) return "No messages sent yet.";
        StringBuilder sb = new StringBuilder();
        for (Message m : messages) {
            sb.append("Message ID: ").append(m.getMessageID())
              .append(" | Hash: ").append(m.getMessageHash())
              .append(" | To: ").append(m.getRecipient())
              .append(" | Text: ").append(m.getMessageText())
              .append("\n");
        }
        return sb.toString();
    }

    public int getMessageCount() {
        return messageCounter;
    }
}   
}
