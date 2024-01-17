package com.example.videocallingapp;

public class Chatmessage {
    private String messageId;
    private String messageText;
    private String email;
    private String messageTime;

    public Chatmessage() {
    }

    public Chatmessage(String messageId, String messageText, String email, String messageTime) {
        this.messageId = messageId;
        this.messageText = messageText;
        this.email = email;
        this.messageTime = messageTime;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }
}
