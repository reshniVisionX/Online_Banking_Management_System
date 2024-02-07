package Online_banking_management;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Account {
   private int accountId;
   private int userId;
   private String accNumber;
   private double balance;
   private boolean status;
   
   Account(int accountId,int userId,String accNumber,double balance,boolean status){
	   this.accountId = accountId;
	   this.userId = userId;
	   this.accNumber = accNumber;
	   this.balance = balance;
	   this.status = true;
			   
   }
   public int getAccountId() {
	   return accountId;
   }
   public void setAccountId(int a) {
	   this.accountId=a;
   }
   public int getUserId() {
	   return userId;
   }
   public void setUserId(int u) {
	   this.userId=u;
   }
   public String getAccNumber() {
	   return accNumber;
   }
   public void setAccNumber(String num) {
	   this.accNumber=num;
   }
   public double getBalance() {
	   return balance;
   }
   public void setBalance(double bal) {
	   this.balance = bal;
   }
   
   static Connection con = MySQLConnection.getConnection();
   public void addAccount(int age) {
	   Connection con = MySQLConnection.getConnection();
	   String existing = "SELECT * FROM ACCOUNTS WHERE userId = ?";
	   String query = "INSERT INTO ACCOUNTS (accountId,userId,accNumber,balance,status) VALUES(DEFAULT,?,?,?,?)";
	   try(PreparedStatement prep = con.prepareStatement(query);
			   PreparedStatement exprep = con.prepareStatement(existing)){
		
		   prep.setInt(1,  getUserId());
		   prep.setString(2, getAccNumber());
		   prep.setDouble(3, getBalance());
		   prep.setBoolean(4, status);
		   exprep.setInt(1, getUserId());
		   ResultSet res = exprep.executeQuery();
		   if(age<18) {
			   System.out.println("You cant open an account here you're not above 18");
			   return;
		   }
		   if(res.next()) {
			   System.out.println("Account already exist...Thanks for  the signUp");
			   return;
		   }
		   else {
		    int rs = prep.executeUpdate();
		    if(rs>0) {
			   System.out.println("Account created successfully");
		    }
		   }
		   
	   } catch (SQLException e) {
		
		e.printStackTrace();
	}
   }
 public static boolean closeOpenAccount(String password,boolean val) {
   
   String getId = "SELECT * FROM USER WHERE password = ?";
   String closeAcc = "UPDATE ACCOUNTS SET status = ? WHERE userId = ?";
   try(PreparedStatement prep = con.prepareStatement(getId);
		 PreparedStatement closeprep = con.prepareStatement(closeAcc)  ){
	   prep.setString(1, password);
	   ResultSet rs = prep.executeQuery();
	   if(rs.next()) {
		   int userId = rs.getInt("userId");
		   System.out.println("The user Id is "+userId);
		   closeprep.setBoolean(1, val);
		   closeprep.setInt(2, userId);
		   int close = closeprep.executeUpdate();
		   if(close>0) {
			   System.out.print("Account");
			   return true;
		   }else {
			   System.out.println("The user dont have an account..");
		   }
	   }else {
		   System.out.println("User doesn't exist...");
		   return false;
	   }
   }
   
   catch (SQLException e) {
	
	e.printStackTrace();
}
	return false;
}

}
