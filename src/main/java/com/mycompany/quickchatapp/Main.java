package com.mycompany.quickchatapp;

import java.util.Scanner;
import java.util.Random;

/*
  The Main class is the entry point for the QuickChat application.
  
  REFERENCE:
  Apache Maven Project, "Apache Maven," Apache Maven, 2024. 
  [Online]. Available: https://maven.apache.org/
  
  @author [Andiswa Mkhize] - [ST10512300]
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Login login = new Login();
        
        // ── PART 3: central manager for all arrays ──
        MessageManager manager = new MessageManager("messages.json");
       
       // Part 1: Registration and Login
        System.out.println("===Registration===");

        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        System.out.print("Enter Cell Phone Number (e.g., +27838968976): ");
        String cellPhone = scanner.nextLine();
        
        // Line to create space
        System.out.println();

        // 1. Call the methods first and display individual messages
        boolean usernameValid = login.checkUserName(username);
        boolean passwordValid = login.checkPasswordComplexity(password);
        boolean cellValid = login.checkCellPhoneNumber(cellPhone);

        if (usernameValid) {
            System.out.println("Username successfully captured.");
        } else {
            System.out.println("Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.");
        }

        if (passwordValid) {
            System.out.println("Password successfully captured.");
        } else {
            System.out.println("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.");
        }

        if (cellValid) {
            System.out.println("Cell phone number successfully added.");
        } else {
            System.out.println("Cell phone number incorrectly formatted or does not contain international code.");
        }
        
        // 2. Only proceed if ALL validations passed
        if (usernameValid && passwordValid && cellValid) { 
            login.registerUser(firstName, lastName, username, password, cellPhone);

            System.out.println("\n===Login===");
            System.out.print("Enter Username: ");
            String loginUsername = scanner.nextLine();
            System.out.print("Enter Password: ");
            String loginPassword = scanner.nextLine();

            boolean loginSuccess = login.loginUser(loginUsername, loginPassword);
            System.out.println(login.returnLoginStatus(loginSuccess));
        } else {
            System.out.println("Registration failed. Please restart the application and try again.");
        }
        
        // Part 2: Menu
        System.out.println("\nWelcome to QuickChat.");
        boolean running = true;
        while(running){
            System.out.println("\nMenu:");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Stored Messages");          // ← NEW Part 3
            System.out.println("4) Quit");                      // ← renumbered
            System.out.print("Choose an option: ");
            
            String choice = scanner.nextLine();
            
            switch(choice){
                case "1":
                    sendMessages(scanner, manager, firstName + " " + lastName);
                    break;
                case "2":
                    System.out.println("Coming Soon");
                    break;
                case "3":
                    storedMessagesMenu(scanner, manager);          // ← NEW Part 3
                    break;
                case "4":
                    System.out.println("Quitting QuickChat. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please choose 1, 2, 3 or 4.");
            }
        }
        
        // Close the scanner
        scanner.close();
    }
    
    // ══════════════════════════════════════════════════════════════
    // PART 2 helper – now also feeds the MessageManager
    // ══════════════════════════════════════════════════════════════
    private static void sendMessages(Scanner scanner, MessageManager manager, String senderName) {
        System.out.print("How many messages do you want to send? ");
        int numMessages;
        try {
            numMessages = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number.");
            return;
        }   

        Random random = new Random();

        for (int i = 0; i < numMessages; i++) {
            System.out.println("\n--- Message " + (i + 1) + " ---");
            
            long randomID = 1000000000L + (long)(random.nextDouble() * 9000000000L);
            String messageID = String.valueOf(randomID);

            System.out.print("Enter recipient cell number (e.g., +27838968976): ");
            String recipient = scanner.nextLine();

            System.out.print("Enter message (max 250 characters): ");
            String messageText = scanner.nextLine();

            if (messageText.length() > 250) {
                System.out.println("Please enter a message of less than 250 characters.");
                i--; 
                continue;
            }
            
            Message msg = new Message(messageID, i, recipient, messageText);
            msg.setSender(senderName);                      // ← FIXED: set sender for Part 3

            String recipientCheck = msg.checkRecipientCell();
            if (!recipientCheck.equals("Cell phone number successfully captured.")) {
                System.out.println(recipientCheck);
                i--; 
                continue;
            }

            System.out.println("Choose an option:");
            System.out.println("1) Send Message");
            System.out.println("2) Disregard Message");
            System.out.println("3) Store Message to send later");
            System.out.print("Choice: ");
            int action = Integer.parseInt(scanner.nextLine());

            // FIXED: Lowercase 's' here
            String result = msg.sentMessage(action); 
            System.out.println(result);
            
            // ── PART 3: set status & add to manager ──
            if      (action == 1) msg.setStatus("Sent");
            else if (action == 2) msg.setStatus("Disregard");
            else if (action == 3) msg.setStatus("Stored");
            manager.addMessage(msg);

            if (action == 1 || action == 3) {
                System.out.println("\nMessage ID: " + msg.getMessageID());
                System.out.println("Message Hash: " + msg.getMessageHash());
                System.out.println("Recipient: " + msg.getRecipient());
                System.out.println("Message: " + msg.getMessageText());
            }
        }
        
        // FIXED: Calling the static method directly from the class
        System.out.println("\nTotal messages sent so far: " + Message.returnTotalMessages());
    }
    
    // ══════════════════════════════════════════════════════════════
    // PART 3 – Stored Messages submenu
    // ══════════════════════════════════════════════════════════════
    private static void storedMessagesMenu(Scanner scanner, MessageManager manager) {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Stored Messages Menu ---");
            System.out.println("a) Display sender and recipient of all stored messages");
            System.out.println("b) Display the longest stored message");
            System.out.println("c) Search for a message ID");
            System.out.println("d) Search all messages for a recipient");
            System.out.println("e) Delete a message using the message hash");
            System.out.println("f) Display full report");
            System.out.println("0) Back to main menu");
            System.out.print("Choose an option: ");

            String opt = scanner.nextLine().trim().toLowerCase();

            switch (opt) {
                case "a": {
                    Message[] stored = manager.getStoredMessages();
                    if (stored.length == 0) {
                        System.out.println("No stored messages.");
                    } else {
                        for (Message m : stored) {
                            String sender = (m.getSender() != null) ? m.getSender() : "Unknown";
                            System.out.println("Sender: " + sender + " → Recipient: " + m.getRecipient());
                        }
                    }
                    break;
                }
                case "b": {
                    Message longest = manager.getLongestStoredMessage();
                    if (longest == null) {
                        System.out.println("No stored messages.");
                    } else {
                        System.out.println("Longest stored message:");
                        System.out.println(longest.getMessageText());
                    }
                    break;
                }
                case "c": {
                    System.out.print("Enter message ID: ");
                    String id = scanner.nextLine();
                    Message found = manager.searchMessageByID(id);
                    if (found == null) {
                        System.out.println("Message ID not found.");
                    } else {
                        System.out.println("Recipient: " + found.getRecipient());
                        System.out.println("Message: "   + found.getMessageText());
                    }
                    break;
                }
                case "d": {
                    System.out.print("Enter recipient number: ");
                    String rec = scanner.nextLine();
                    Message[] results = manager.searchMessagesByRecipient(rec);
                    if (results.length == 0) {
                        System.out.println("No messages found for " + rec);
                    } else {
                        for (Message m : results) {
                            System.out.println("- " + m.getMessageText());
                        }
                    }
                    break;
                }
                case "e": {
                    System.out.print("Enter message hash to delete: ");
                    String hash = scanner.nextLine();
                    if (manager.deleteMessageByHash(hash)) {
                        System.out.println("Message with hash " + hash + " successfully deleted.");
                    } else {
                        System.out.println("Message hash not found.");
                    }
                    break;
                }
                case "f": {
                    System.out.println(manager.displayMessageReport());
                    break;
                }
                case "0":
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}