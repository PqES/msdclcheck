package com.entities;

public class MessageUser {
	private String recipient;
    private String sender;
    private String message;
    private String body;

    public MessageUser(String recipient, String body){
        this.recipient = recipient;
        this.sender = "elena.augusta94@gmail.com";
        this.body = body;
    }

    public String toString(){           
        message = "From: " + recipient + "\nTo: " + sender; 
        return message;
    }


	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public String getSender() {
		return sender;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
    
}
