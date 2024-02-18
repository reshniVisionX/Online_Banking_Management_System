package Online_banking_management;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
public class Employees {
Scanner sc = new Scanner(System.in);
TransactionsManager trans;
LoanProcessing  lp;
	public void employeeMenuList(){
		int choice;
		do {
			System.out.println();
			System.out.println("------------------------- Employee Menu -------------------------");
			System.out.println("1. Create a customer account*");
			System.out.println("2. Close a customer account*");
			System.out.println("3. Unfreeze an account*");
			System.out.println("4. Updating customer information*");
			System.out.println("5. Deposit into a customer account*");
			System.out.println("6. Withdraw from an customer account*");
			System.out.println("7. Approve Loan*");
			System.out.println("8. View Customer Details*");
			System.out.println("9. Generate Transaction Report*");
			System.out.println("10. Refresh loan monthly emi*");
			System.out.println("0. Exit");
			System.out.println("-----------------------------------------------------------------");
			
			choice = sc.nextInt();
			switch(choice) {
			 case 1:{
				 //create an account
				 sc.nextLine();
				 SignUp sig;
				 int DEFAULT = 1;
					System.out.println("Enter customer Name: ");
				  String name = sc.nextLine();
				  System.out.println("Enter cutomer password: ");
				  String pass = sc.nextLine();
			      System.out.println("Enter your accurate age:");
			      int age = sc.nextInt();
			      String role = "CUSTOMER";
			      
			    	Connection con = MySQLConnection.getConnection();
			  		String insert = "INSERT INTO USERS(userId,userName,password,userType,age) VALUES(DEFAULT,?,?,?,?);";
			  		try(PreparedStatement prepstmt = con.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)){
			  			 prepstmt.setString(1, name);
			               prepstmt.setString(2, pass);
			               prepstmt.setString(3, role);
			               prepstmt.setInt(4, age);
			  			int rs = prepstmt.executeUpdate();
			  			int userId=0;
			  			if(rs>0) {
			  				System.out.println("User Signed..In Successfully");
			  				 ResultSet generatedKeys = prepstmt.getGeneratedKeys();
						        if (generatedKeys.next()) 
						            userId = generatedKeys.getInt(1);
			  			}
			  			
			  			int accid = 1;
						 String accNum = AccountNumberGenerator.generateAccountNumber();
						 System.out.println("Enter your minimum balance that customer wants to deposit(min-1000): ");
						 double balance = sc.nextDouble();
						 boolean status = true;
						 Account acc = new Account(accid, userId, accNum,balance,status);
						 acc.addAccount(age);
					     System.out.println("Your account number is : "+accNum);
					     System.out.println("Customer account created successfully...");
			  		} catch (SQLException e) {
						e.printStackTrace();
					}
			       
				
				break;
			 }
			 case 2:{
				 //close a customer account
				 System.out.println("Are you sure do you want to close any account ?(Y/N)");
				 char ch = sc.next().charAt(0);
				 sc.nextLine();
				 if('Y'==Character.toUpperCase(ch)) {
					 System.out.println("Enter the customer password :");
					 String password = sc.nextLine();
					 if(Account.closeOpenAccount(password,false)) {
						  System.out.print(" Freezed successfully ..");
					 }else {
						 System.out.println("Error while closing account ");
					     return;
				      }
				 }
					break;
			 }
				 
			 case 3:{
				 //unfreeze an account
				 System.out.println("Are you sure do you want to unfreeze an account ?(Y/N)");
				 char ch = sc.next().charAt(0);
				 sc.nextLine();
				 if('Y'==Character.toUpperCase(ch)) {
					 System.out.println("Enter the customer password :");
					 String password = sc.nextLine();
					 if(Account.closeOpenAccount(password,true)) {
						  System.out.print(" UnFreezed successfully ..");
					 }else {
						 System.out.println("Error while unfreezing account ");
					     return;
				      }
				 }else
					 return;
					break;
				 }
			 case 4:{
				 //update username password,age
				 sc.nextLine();
				 System.out.println("Do you want to update an account? :(y/n)");
				 char ch = sc.next().charAt(0);
				 if('y'==Character.toLowerCase(ch)) {
					 int p=0;
					 sc.nextLine();
					 System.out.println("Enter customer password : ");
						String pass = sc.nextLine();
	                 String newpass="";
					 System.out.println("Do customer want to change password? :(y/n)");
					 char c = sc.next().charAt(0);sc.nextLine();
					 if('y'==Character.toLowerCase(c)) {
						p = 1;
						
						System.out.println("Enter the new password :");
						 newpass = sc.nextLine();
					 }
					 
					 System.out.println("Enter the customer name to update :");
					 String name = sc.nextLine();
					 System.out.println("Enter the customer age to update :");
					 int age = sc.nextInt();
					
					trans = new TransactionsManager();
					if(p==0)
					  trans.updateUser(pass,pass,name,age);
					else
					  trans.updateUser(pass,newpass,name,age);
				 }else
					 return;
					break;
				 }
			 case 5:{
				 System.out.println("Do you want to deposit amount into an account? :(y/n)");
				 char ch = sc.next().charAt(0);
				 sc.nextLine();
				 if('y'==Character.toLowerCase(ch)) {
					 System.out.println("Enter customer Account Number :");
					 String account=sc.nextLine();
					 System.out.println("Enter the amount that customer want to deposit :");
					 double amount= sc.nextDouble();
						trans = new TransactionsManager();
					 trans.deposit(account,amount);
					
					 }
					break;
				 }
			 case 6:{
				 // Withdraw from an customer account
				 System.out.println("Do you want to withdraw amount into an account? :(y/n)");
				 char ch = sc.next().charAt(0);
				 sc.nextLine();
				 if('y'==Character.toLowerCase(ch)) {
					 System.out.println("Enter customer Account Number :");
					 String account=sc.nextLine();
					 System.out.println("Enter the amount that customer want to withdraw :");
					 double amount= sc.nextDouble();
						trans = new TransactionsManager();
					 trans.withdraw(account,-amount);
					
					 }
					break;
				 }
			 case 7:{
				 //approve loan///This amount can be transferred to the borrower's account 
				 System.out.println("Do you want to Approve loan? :(y/n)");
				 char ch = sc.next().charAt(0);
				 sc.nextLine();
				 if('n'==Character.toLowerCase(ch)) {
					break;
				 }else {
					 lp=new LoanProcessing();
					 lp.approveLoan();
				 }
				 break;
			 }
			 case 8:{
				 //view cust details
				 trans = new  TransactionsManager();
				 trans.viewCustDetails();
				 break;
			 }
			 case 9:{
				 //transaction report
				 AdminFunctionalities admin = new AdminFunctionalities();
					 admin.generateTransactionalReport();
				 break;
			 }
			 case 10:{
				 lp=new LoanProcessing();
				 lp.refreshLoan();
			 }
			}
		}while(choice!=0);
	}
}


