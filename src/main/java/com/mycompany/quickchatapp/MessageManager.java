package com.mycompany.quickchatapp;

import java.io.*;
import java.util.ArrayList;
import org.json.JSONObject;

/**
 * MessageManager centralises all Part 3 array management:
 *  – Sent Messages
 *  – Stored Messages
 *  – Disregarded Messages
 *  – Message Hashes
 *  – Message IDs
 *
 * It also provides the search / delete / report methods required by Part 3.
 */
public class MessageManager {

    private ArrayList<Message> messages = new ArrayList<>();
    private String filePath;

    // ──────────────────────────────────────────────────────────────
    // Constructor – loads previously stored messages from JSON
    // ──────────────────────────────────────────────────────────────
    public MessageManager(String filePath) {
        this.filePath = filePath;
        loadStoredMessages();
    }

    private void loadStoredMessages() {
        File file = new File(filePath);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                JSONObject obj = new JSONObject(line);

                String id        = obj.getString("MessageID");
                String hash      = obj.getString("MessageHash");
                String recipient = obj.getString("Recipient");
                String text      = obj.getString("Message");
                String sender    = obj.optString("Sender", null);   // ← NEW: null-safe for old files

                Message msg = new Message(id, 0, sender, recipient, text, hash, "Stored");  // ← 7-param ctor
                messages.add(msg);
            }
        } catch (IOException e) {
            System.out.println("Error reading JSON file: " + e.getMessage());
        }
    }

    // ──────────────────────────────────────────────────────────────
    // Add a message to the in-memory manager
    // ──────────────────────────────────────────────────────────────
    public void addMessage(Message msg) {
        messages.add(msg);
    }

    // ──────────────────────────────────────────────────────────────
    // Array getters (filter by status)
    // ──────────────────────────────────────────────────────────────
    public Message[] getSentMessages() {
        return filterByStatus("Sent");
    }

    public Message[] getStoredMessages() {
        return filterByStatus("Stored");
    }

    public Message[] getDisregardedMessages() {
        return filterByStatus("Disregard");
    }

    private Message[] filterByStatus(String status) {
        ArrayList<Message> result = new ArrayList<>();
        for (Message m : messages) {
            if (status.equalsIgnoreCase(m.getStatus())) {
                result.add(m);
            }
        }
        return result.toArray(new Message[0]);
    }

    // ──────────────────────────────────────────────────────────────
    // Hash / ID arrays
    // ──────────────────────────────────────────────────────────────
    public String[] getMessageHashes() {
        String[] hashes = new String[messages.size()];
        for (int i = 0; i < messages.size(); i++) {
            hashes[i] = messages.get(i).getMessageHash();
        }
        return hashes;
    }

    public String[] getMessageIDs() {
        String[] ids = new String[messages.size()];
        for (int i = 0; i < messages.size(); i++) {
            ids[i] = messages.get(i).getMessageID();
        }
        return ids;
    }

    // ──────────────────────────────────────────────────────────────
    // Longest stored message
    // ──────────────────────────────────────────────────────────────
    public Message getLongestStoredMessage() {
        Message[] stored = getStoredMessages();
        if (stored.length == 0) return null;

        Message longest = stored[0];
        for (Message m : stored) {
            if (m.getMessageText().length() > longest.getMessageText().length()) {
                longest = m;
            }
        }
        return longest;
    }

    // ──────────────────────────────────────────────────────────────
    // Search by message ID
    // ──────────────────────────────────────────────────────────────
    public Message searchMessageByID(String id) {
        for (Message m : messages) {
            if (m.getMessageID().equals(id)) {
                return m;
            }
        }
        return null;
    }

    // ──────────────────────────────────────────────────────────────
    // Search all messages for a recipient
    // ──────────────────────────────────────────────────────────────
    public Message[] searchMessagesByRecipient(String recipient) {
        ArrayList<Message> result = new ArrayList<>();
        for (Message m : messages) {
            if (m.getRecipient().equals(recipient)) {
                result.add(m);
            }
        }
        return result.toArray(new Message[0]);
    }

    // ──────────────────────────────────────────────────────────────
    // Delete a message using its hash
    // ──────────────────────────────────────────────────────────────
    public boolean deleteMessageByHash(String hash) {
        Message toDelete = null;
        for (Message m : messages) {
            if (m.getMessageHash().equals(hash)) {
                toDelete = m;
                break;
            }
        }
        if (toDelete == null) return false;

        messages.remove(toDelete);

        // If it was stored, also remove from the JSON file
        if ("Stored".equalsIgnoreCase(toDelete.getStatus())) {
            removeFromJSON(hash);
        }
        return true;
    }

    private void removeFromJSON(String hash) {
        File file = new File(filePath);
        if (!file.exists()) return;

        ArrayList<String> remainingLines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                JSONObject obj = new JSONObject(line);
                if (!obj.getString("MessageHash").equals(hash)) {
                    remainingLines.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading JSON file: " + e.getMessage());
            return;
        }

        try (FileWriter writer = new FileWriter(file, false)) {
            for (String line : remainingLines) {
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error writing JSON file: " + e.getMessage());
        }
    }

    // ──────────────────────────────────────────────────────────────
    // Full report
    // ──────────────────────────────────────────────────────────────
    public String displayMessageReport() {
        if (messages.isEmpty()) return "No messages available.";

        StringBuilder sb = new StringBuilder();
        for (Message m : messages) {
            sb.append("Message ID: ").append(m.getMessageID()).append("\n")
              .append("Message Hash: ").append(m.getMessageHash()).append("\n")
              .append("Recipient: ").append(m.getRecipient()).append("\n")
              .append("Message: ").append(m.getMessageText()).append("\n")
              .append("Status: ").append(m.getStatus()).append("\n")
              .append("---------------------------\n");
        }
        return sb.toString();
    }
}
