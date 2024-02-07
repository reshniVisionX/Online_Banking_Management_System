package Online_banking_management;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.Scanner;

public class LoanProcessing {
	Scanner sc = new Scanner(System.in);
	Connection con = MySQLConnection.getConnection();
	
	    private static final double PERSONAL_LOAN_RATE = 1.1;
	    private static final double HOME_LOAN_RATE = 1.08;
	    private static final double STUDENT_LOAN_RATE = 0.05;
	    private static final double BUSINESS_LOAN_RATE = 1.15;
	    private static final double CREDIT_CARD_LOAN_RATE = 0.12;
	    private static final double CONSTRUCTION_LOAN_RATE = 1.18;

	   public static double getInterest(String s) {
		   s=s.toLowerCase();
		   if(s.equals("personal loan"))
			   return 1.1;
		   else if(s.equals("home loan"))
			   return 1.08;
			   else if(s.equals("education loan"))
				   return 0.05;
				   else if(s.equals("business loan"))
					   return 1.15;
					   else if(s.equals("credit card loan"))
						   return 0.12;
						   else if(s.equals("construction loan"))
							   return 1.18;
						   else
							   return 0;
		
		   
	   }
	    public static double calculateInterestRate(String loanType, double amount) {
	        switch (loanType.toLowerCase()) {
	            case "personal loan":
	                return amount * PERSONAL_LOAN_RATE;
	            case "home loan":
	                return amount * HOME_LOAN_RATE;
	            case "education loan":
	                return amount * STUDENT_LOAN_RATE;
	            case "business loan":
	                return amount * BUSINESS_LOAN_RATE;
	            case "credit card loan":
	                return amount * CREDIT_CARD_LOAN_RATE;
	            case "construction loan":
	                return amount * CONSTRUCTION_LOAN_RATE;
	            default:
	                System.out.println("Invalid loan type");
	                return 0.0;
	        }
	    }

	public void approveLoan() {
	 String select = "SELECT * FROM LOAN WHERE isApproved = false";
	 String update = "UPDATE LOAN SET isApproved = true, interestRate = ? ,repaymentperiod = ? WHERE loanId = ?";
	 String person = "SELECT * FROM LOAN WHERE loanId = ?";
	 String query = "SELECT balance FROM ACCOUNTS";
	 String accup = "UPDATE ACCOUNTS SET balance = ? WHERE userId = ? AND status = true";
	 try(PreparedStatement prep = con.prepareStatement(select);
			 PreparedStatement uprep = con.prepareStatement(update);
			 PreparedStatement pres = con.prepareStatement(person);
			 PreparedStatement prepup = con.prepareStatement(accup);
			 PreparedStatement selectq = con.prepareStatement(query)){
		 ResultSet rs = prep.executeQuery();
		 int userId=0;
		 while(rs.next()) {
			 int loanId = rs.getInt("loanId");
	             userId = rs.getInt("userId");
	            String type = rs.getString("type");
	            double amount = rs.getDouble("amount");
	            double interestRate = rs.getDouble("interestRate");
	            boolean isApproved = rs.getBoolean("isApproved");
	            double payed = rs.getDouble("payed");
	            String repaymentperiod= rs.getString("repaymentperiod");
	            LocalDateTime timestamp = rs.getObject("timestamp", LocalDateTime.class);

	           System.out.println("Loan ID: \'" + loanId + "\' User ID: \'" + userId + "\' Loan Type: \'" + type + "\' Amount: \'" + amount + "\' Interest Rate: \'" + interestRate + "\' Is Approved: \'" + isApproved + "\' Payed Amount: \'" + payed + "\' Repayment_period: \'" + repaymentperiod + "\' Timestamp: \'" + timestamp + "\'");
               System.out.println("----------------------------------------------------------------------------");
               
  
		 }
		 System.out.println("Do you want to approve any loan ?(y/n)");
		 char ch = sc.next().charAt(0);
		 boolean success = true;
		 if(ch=='n' || ch=='N') {
			 return;
		 }else {
			 con.setAutoCommit(false);
			 System.out.println("Enter the loanId :");
			 int id = sc.nextInt();
			 pres.setInt(1, id);
			 ResultSet res = pres.executeQuery();
			 String loanType="";double amount = 0;
			 if(res.next() && success) {
				 loanType = res.getString("type");
				 amount = res.getDouble("amount");
			 
			 double interest = getInterest(loanType); 
			 double penalty = calculateInterestRate(loanType, amount);
			 
			 uprep.setDouble(1,interest);
			 uprep.setDouble(2, penalty);
			 uprep.setInt(3, id);
			 int up = uprep.executeUpdate();
			 if(up>0 && success) {
				 System.out.println("Loan for "+id+" is approved");
				 ResultSet q = selectq.executeQuery();
				 double balance=0;
				 if(q.next() && success) {
					  balance  = q.getDouble("balance");
				 }else {
					 System.out.println("User doesn't have an account to deposit the loan");
					 success = false;
					
				 }
				 prepup.setDouble(1,balance+amount);
				 prepup.setInt(2, userId);
				int acc =  prepup.executeUpdate();
				if(acc>0 && success) {
					System.out.println("Loan is provided to the userId "+userId);
					con.commit();
					System.out.print(" ..committed");
				}else {
					System.out.println("Either account is inactive or user doesn't have an account");
					 success = false;
					
				}
			 }else {
				 System.out.println("Approval Denied");
				 success = false;
				 return;
			 }
			 }else {
				 System.out.println("Cant find user");
				 success = false;
			 }
		 }
	 } catch (SQLException e) {
		
		e.printStackTrace();
	}
		
	}
	 public String typeOfloan(int c) {
		 if(c==1)
			 return "Personal Loan";
		 else if(c==2)
			 return "Home Loan";
		 else if(c==3)
			 return "Student Loan";
		 else if(c==4)
			 return "Business Loan";
		 else if(c==5)
			 return "Credit Card Loan";
		 else if(c==6)
			 return "Construction Loan";
		 else
			 return "null";
	 }
	public void loanpayment(int choice, int id) {
		String type = typeOfloan(choice);
		System.out.println("You are paying "+type);
	}
	
	 public String generateOTP(int length) {
	      
	        String chars = "0123456789";
	        StringBuilder otp = new StringBuilder();
	        Random random = new Random();
	        
	    
	        for (int i = 0; i < length; i++) {
	            int index = random.nextInt(chars.length());
	            otp.append(chars.charAt(index));
	        }
	        
	        return otp.toString();
	    }
}
