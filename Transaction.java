package Online_banking_management;


import java.time.LocalDateTime;

public class Transaction {
    private int transId;
    private int accountId;
    private String transType;
    private double amount;
    private String sender;
    private String receiver;
    private int userId;
    private LocalDateTime timestamp;

    public Transaction(int transId, int accountId, String transType, double amount, String sender, String receiver,
            int userId, LocalDateTime timestamp) {
      this.transId = transId;
      this.accountId = accountId;
      this.transType = transType;
      this.amount = amount;
      this.sender = sender;
      this.receiver = receiver;
      this.userId = userId;
      this.timestamp = timestamp;
}
    public int getTransId() {
        return transId;
    }

    public int getAccountId() {
        return accountId;
    }

    public String getTransType() {
        return transType;
    }

    public double getAmount() {
        return amount;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public int getUserId() {
        return userId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

  
    public void setTransId(int transId) {
        this.transId = transId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}

