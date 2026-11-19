package com.mycompany.quickchatapp;

import org.json.JSONObject;
import java.io.*;
import java.util.ArrayList;

public class JSONManager {
    private static final String FILE_NAME = "messages.json";

    // 1. Read all messages from the JSON file
    public static ArrayList<Message> loadMessages() {
        ArrayList<Message> messages = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                JSONObject obj = new JSONObject(line);
                String id = obj.getString("MessageID");
                String hash = obj.getString("MessageHash");
                String recipient = obj.getString("Recipient");
                String text = obj.getString("Message");
                
                Message msg = new Message(id, 0, recipient, text);
                msg.setMessageHash(hash); // Restore original hash
                messages.add(msg);
            }
        } catch (FileNotFoundException e) {
            // File doesn't exist yet (first run), return empty list
        } catch (IOException e) {
            System.out.println("Error reading JSON file: " + e.getMessage());
        }
        return messages;
    }

    // 2. Delete a message by its hash
    public static boolean deleteMessageByHash(String hash) {
        ArrayList<Message> messages = loadMessages();
        boolean removed = messages.removeIf(m -> m.getMessageHash().equals(hash));
        
        if (removed) {
            saveAllMessages(messages); // Rewrite the file without the deleted message
        }
        return removed;
    }

    // Helper method to overwrite the JSON file
    private static void saveAllMessages(ArrayList<Message> messages) {
        try (FileWriter file = new FileWriter(FILE_NAME, false)) { // 'false' overwrites file
            for (Message msg : messages) {
                JSONObject obj = new JSONObject();
                obj.put("MessageID", msg.getMessageID());
                obj.put("MessageHash", msg.getMessageHash());
                obj.put("Recipient", msg.getRecipient());
                obj.put("Message", msg.getMessageText());
                file.write(obj.toString() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving messages to JSON file: " + e.getMessage());
        }
    }

    // 3. Find the longest message
    public static Message getLongestMessage() {
        ArrayList<Message> messages = loadMessages();
        if (messages.isEmpty()) return null;
        
        Message longest = messages.get(0);
        for (Message m : messages) {
            if (m.getMessageText().length() > longest.getMessageText().length()) {
                longest = m;
            }
        }
        return longest;
    }

    // 4. Search for a message by ID
    public static Message searchByMessageID(String id) {
        ArrayList<Message> messages = loadMessages();
        for (Message m : messages) {
            if (m.getMessageID().equals(id)) {
                return m;
            }
        }
        return null; // Not found
    }

    // 5. Search for messages by recipient
    public static ArrayList<Message> searchByRecipient(String recipient) {
        ArrayList<Message> results = new ArrayList<>();
        ArrayList<Message> messages = loadMessages();
        for (Message m : messages) {
            if (m.getRecipient().equals(recipient)) {
                results.add(m);
            }
        }
        return results;
    }

    // 6. Display a full formatted report of all messages
    public static String displayReport() {
        ArrayList<Message> messages = loadMessages();
        if (messages.isEmpty()) return "No messages stored.";
        
        StringBuilder sb = new StringBuilder();
        for (Message m : messages) {
            sb.append("Message ID: ").append(m.getMessageID()).append("\n")
              .append("Message Hash: ").append(m.getMessageHash()).append("\n")
              .append("Recipient: ").append(m.getRecipient()).append("\n")
              .append("Message: ").append(m.getMessageText()).append("\n")
              .append("---------------------------\n");
        }
        return sb.toString();
    }
}    

