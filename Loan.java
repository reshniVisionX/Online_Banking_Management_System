package Online_banking_management;

import java.time.LocalDateTime;

public class Loan {
    private int loanId;
    private int userId;
    private String type;
    private double amount;
    private double interestRate;
    private boolean isApproved;
    private double payed;
    private String repaymentperiod;
    private LocalDateTime timestamp;

    public Loan(int loanId, int userId, String type, double amount, double interestRate, boolean isApproved,
                double payed, String repaymentperiod, LocalDateTime timestamp) {
        this.loanId = loanId;
        this.userId = userId;
        this.type = type;
        this.amount = amount;
        this.interestRate = interestRate;
        this.isApproved = isApproved;
        this.payed = payed;
        this.repaymentperiod = repaymentperiod;
        this.timestamp = timestamp;
    }


    public int getLoanId() {
        return loanId;
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public double getPayed() {
        return payed;
    }

    public void setPayed(double payed) {
        this.payed = payed;
    }

    public String getRepaymentperiod() {
        return repaymentperiod;
    }

    public void setRepaymentperiod(String repaymentperiod) {
        this.repaymentperiod = repaymentperiod;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
