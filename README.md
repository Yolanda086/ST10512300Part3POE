# ST10512300Part3POE
# QuickChatApp - PROG5121 Part 1, Part 2 and Part 3

 ## PART 1
User registration and login system built with Java.

## Features
- Username validation (contains underscore, max 5 characters)
- Password complexity check (≥8 chars, uppercase, digit, special character)
- Cell phone validation (South African format: +27 followed by 9 digits)
- Registration flow → Login flow
- JUnit 4.13.2 unit tests (12 tests, all passing)

## Files
- `Login.java` — validation and authentication logic
- `Main.java` — console interface
- `LoginTest.java` — JUnit test suite

  ## PART 2
## Features
**User Registration & Login:** 
    *   Validates usernames (must contain an underscore, max 5 characters).
    *   Validates passwords (min 8 characters, 1 capital, 1 number, 1 special character).
    *   Validates South African cell phone numbers using Regular Expressions (`+27...`).
**Messaging System:**
    *   Generate unique 10-digit Message IDs.
    *   Create custom Message Hashes (e.g., `00:0:HITHANKS`).
    *   Send, Disregard, or Store messages.
**Data Storage:**
    *   Stores messages in a local `messages.json` file for persistent data.
**Automated Testing:**
    *   Includes a comprehensive suite of JUnit 4 unit tests.
    *   Integrated with GitHub Actions for Continuous Integration (CI) to automatically run tests on every push.
    
## Files
    *   Message.java
    *   MessageTest.java
    *   Main.java
    *   Login.java
    *   LoginTest.java

  ## PART 3
## Features

**Message Arrays :**
    *   Sent Messages — every message the user sent this session.
    *   Disregarded Messages — every message the user discarded.
    *   Stored Messages — every message persisted in `messages.json`, re-loaded into the array on app start.
    *   Message Hashes — every hash generated.
    *   Message IDs — every ID generated.

**Stored Messages Submenu (main menu option 3):**
    *   a) Display the sender and recipient of all stored messages.
    *   b) Display the longest stored message.
    *   c) Search for a message ID and display the corresponding recipient and message.
    *   d) Search for all messages stored for a particular recipient.
    *   e) Delete a message using its message hash — also removes the line from `messages.json`.
    *   f) Display a full report listing Message ID, Message Hash, Recipient, Message and Status for every stored message.

## Files
    *   `MessageManager.java` — central array manager: loads stored messages from JSON, filters by status (Sent / Stored / Disregard), exposes hash & ID arrays, and implements longest / search‑by‑ID / search‑by‑recipient / delete‑by‑hash / full‑report operations.
    *   `MessageManagerTest.java` — 6 JUnit 4 tests covering every Part 3 rubric row.
    *   `Message.java` — extended with a 7‑parameter constructor, status and sender getters/setters, and sender persistence in `storeMessage()`.
    *   `Main.java` — extended menu (`3) Stored Messages`, `4) Quit`) and new `storedMessagesMenu()` sub-menu.
    *   `messages.json` — persistent message store (JSON Lines).

## Unit Tests — Part 3
    *   `testSentMessagesArray` — verifies the Sent Messages array contains exactly the two expected entries.
    *   `testLongestStoredMessage` — verifies the longest stored message is returned.
    *   `testSearchMessageByID` — verifies search by message ID.
    *   `testSearchMessagesByRecipient` — verifies search returns both messages for a recipient.
    *   `testDeleteMessageUsingHash` — verifies deletion removes the message from both memory and `messages.json`.
    *   `testDisplayMessageReport` — verifies the full report contains Message Hash, Recipient and Message fields for every entry.

## Author
Yolanda086 - Andiswa Mkhize
