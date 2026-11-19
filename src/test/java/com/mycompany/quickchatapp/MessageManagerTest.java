package com.mycompany.quickchatapp;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MessageManagerTest {

    private MessageManager manager;

    @Before
    public void setUp() {
        manager = new MessageManager("test-messages.json");

        // Test Message 1 – Sent
        Message message1 = new Message(
                "1111111111", 1, "0838884567", "+27834557896",
                "Did you get the cake?", "11:1:DIDCAKE", "Sent"
        );

        // Test Message 2 – Stored
        Message message2 = new Message(
                "2222222222", 2, "0838884567", "+27838884567",
                "Where are you? You are late! I have asked you to be on time.",
                "22:2:WHEREONTIME", "Stored"
        );

        // Test Message 3 – Disregard
        Message message3 = new Message(
                "3333333333", 3, "0838884567", "+27834484567",
                "Yohoooo, I am at your gate.", "33:3:YOHOOGATE", "Disregard"
        );

        // Test Message 4 – Sent (ID used in search test)
        Message message4 = new Message(
                "0838884567", 4, "0838884567", "Not specified",
                "It is dinner time !", "44:4:ITTIME", "Sent"
        );

        // Test Message 5 – Stored
        Message message5 = new Message(
                "5555555555", 5, "0838884567", "+27838884567",
                "Ok, I am leaving without you.", "55:5:OKYOU", "Stored"
        );

        manager.addMessage(message1);
        manager.addMessage(message2);
        manager.addMessage(message3);
        manager.addMessage(message4);
        manager.addMessage(message5);
    }

    @Test
    public void testSentMessagesArray() {
        Message[] sent = manager.getSentMessages();
        assertEquals(2, sent.length);
        assertEquals("Did you get the cake?", sent[0].getMessage());
        assertEquals("It is dinner time !",  sent[1].getMessage());
    }

    @Test
    public void testLongestStoredMessage() {
        Message longest = manager.getLongestStoredMessage();
        assertNotNull(longest);
        assertEquals("Where are you? You are late! I have asked you to be on time.",
                     longest.getMessage());
    }

    @Test
    public void testSearchMessageByID() {
        Message found = manager.searchMessageByID("0838884567");
        assertNotNull(found);
        assertEquals("It is dinner time !", found.getMessage());
    }

    @Test
    public void testSearchMessagesByRecipient() {
        Message[] results = manager.searchMessagesByRecipient("+27838884567");
        assertEquals(2, results.length);
        assertEquals("Where are you? You are late! I have asked you to be on time.",
                     results[0].getMessage());
        assertEquals("Ok, I am leaving without you.",
                     results[1].getMessage());
    }

    @Test
    public void testDeleteMessageUsingHash() {
        boolean deleted = manager.deleteMessageByHash("22:2:WHEREONTIME");
        assertTrue(deleted);

        Message[] stored = manager.getStoredMessages();
        assertEquals(1, stored.length);
        assertEquals("Ok, I am leaving without you.", stored[0].getMessage());
    }

    @Test
    public void testDisplayMessageReport() {
        String report = manager.displayMessageReport();
        assertTrue(report.contains("Message Hash:"));
        assertTrue(report.contains("Recipient:"));
        assertTrue(report.contains("Message:"));
        assertTrue(report.contains("Did you get the cake?"));
        assertTrue(report.contains("It is dinner time !"));
    }
}