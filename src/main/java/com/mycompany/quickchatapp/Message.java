package com.mycompany.quickchatapp;

import java.io.FileWriter;
import java.io.IOException;
import org.json.JSONObject;

/**
 * The Message class handles message generation, validation, and storage.
 */
public class Message {
    private String messageID;
    private int messageNumber;
    private String sender;
    private String recipient;
    private String messageText;
    private String messageHash;
    private String status;

    // Static arrays/variables to track messages across the whole application
    private static String[] sentMessages = new String[100];
    private static int totalMessagesSent = 0;

    // Default constructor required for JSON
    public Message() {
    }

    // ──────────────────────────────────────────────────────────────
    // PART 2 CONSTRUCTOR (unchanged – kept for backward compatibility)
    // ──────────────────────────────────────────────────────────────
    public Message(String messageID, int messageNumber, String recipient, String messageText) {
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash();
    }

    // ──────────────────────────────────────────────────────────────
    // PART 3 CONSTRUCTOR (new – used by MessageManagerTest)
    // ──────────────────────────────────────────────────────────────
    public Message(String messageID, int messageNumber, String sender, String recipient,
                   String messageText, String messageHash, String status) {
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.sender = sender;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = messageHash;
        this.status = status;
    }

    // ──────────────────────────────────────────────────────────────
    // EXISTING PART 2 METHODS (unchanged)
    // ──────────────────────────────────────────────────────────────
    public boolean checkMessageID() {
        return this.messageID.length() <= 10;
    }

    public String checkRecipientCell() {
        if (this.recipient != null && this.recipient.matches("^\\+27[0-9]{9}$")) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
    }

    public String createMessageHash() {
        if (messageText == null || messageText.trim().isEmpty()) return "";
        String firstTwoID = messageID.substring(0, 2);
        String[] words = messageText.trim().split("\\s+");
        String firstWord = words[0].replaceAll("[^a-zA-Z0-9]", "");
        String lastWord = words[words.length - 1].replaceAll("[^a-zA-Z0-9]", "");
        return (firstTwoID + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
    }

    public String sentMessage(int choice) {
        switch (choice) {
            case 1:
                sentMessages[totalMessagesSent] = this.messageText;
                totalMessagesSent++;
                return "Message successfully sent.";
            case 2:
                return "Press 0 to delete the message.";
            case 3:
                storeMessage();
                return "Message successfully stored.";
            default:
                return "Invalid choice.";
        }
    }

    public static String printMessages() {
        if (totalMessagesSent == 0) return "No messages sent yet.";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < totalMessagesSent; i++) {
            sb.append(sentMessages[i]).append("\n");
        }
        return sb.toString();
    }

    public static int returnTotalMessages() {
        return totalMessagesSent;
    }
    
    /**
     * Stores the message in a JSON file.
     * 
     * REFERENCE / ATTRIBUTION:
     * [1] Oracle, "Class FileWriter," Java SE 24 & JDK 24 API Specification, 2025. 
     *     [Online]. Available: https://docs.oracle.com/en/java/javase/24/docs/api/java.base/java/io/FileWriter.html
     * [2] JSON.org, "Class JSONObject," JSON in Java, 2024. 
     *     [Online]. Available: https://svn.apache.org/repos/asf/zest/site/content/java/1.4.1/javadocs/org/json/JSONObject.html
     */

   public void storeMessage() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("MessageID", this.messageID);
        jsonObject.put("MessageHash", this.messageHash);
        jsonObject.put("Recipient", this.recipient);
        jsonObject.put("Message", this.messageText);
        jsonObject.put("Sender", this.sender);          // ← NEW: persist sender for Part 3 report
        
        try (FileWriter file = new FileWriter("messages.json", true)) {
            file.write(jsonObject.toString() + "\n");
        } catch (IOException e) {
            System.out.println("Error storing message to JSON file.");
        }
    }

    // ──────────────────────────────────────────────────────────────
    // PART 3 GETTERS / SETTERS (new)
    // ──────────────────────────────────────────────────────────────
    public String getMessageHash()  { return messageHash; }
    public void   setMessageHash(String messageHash) { this.messageHash = messageHash; }

    public String getMessageID()    { return messageID; }
    public String getRecipient()    { return recipient; }
    public String getMessageText()  { return messageText; }
    public String getMessage()      { return messageText; }   // alias used by tests

    public String getStatus()       { return status; }
    public void   setStatus(String status) { this.status = status; }

    public String getSender()       { return sender; }
    public void   setSender(String sender) { this.sender = sender; }
}