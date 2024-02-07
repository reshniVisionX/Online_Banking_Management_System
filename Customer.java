package Online_banking_management;
import java.sql.SQLException;
import java.util.*;
public class Customer {
	TransactionsManager  trans;
	LoanProcessing lp;
 Scanner sc = new Scanner(System.in);
	public void customerMenuList(int id,int age) {
	
		int choice;
		do {
			System.out.println();
			System.out.println("----------------------- Customer Menu --------------------------");
			System.out.println("1. Create a new Account*");
			System.out.println("2. Transfer money to another account*");
			System.out.println("3. Check my current balance*");
			System.out.println("4. View my Transaction History*");
			System.out.println("5. Payment categories");
			System.out.println("6. Apply for Loan*");
			System.out.println("7. Cardless ATM Withdrawals using OTP*");
			System.out.println("8. Exit");
			System.out.println("-----------------------------------------------------------------");
			
			choice = sc.nextInt();
			
			switch(choice) {
			 case 1:{
				 int accid = 1;
				 String accNum = AccountNumberGenerator.generateAccountNumber();
				 System.out.println("Enter your minimum balance that you want to deposit(1000): ");
				 double balance = sc.nextDouble();
				 boolean status = true;
				 Account acc = new Account(accid, id, accNum,balance,status);
				 acc.addAccount(age);
			     System.out.println("Your account number is : "+accNum);
				
				break;
			 }
			 case 2:{
				 //Transfer money to another account
				 sc.nextLine();
				 System.out.println("Enter your Account Number :");
				 String src = sc.nextLine();
				 System.out.println("Enter the amount you want transfer(limit:50,000) :");
				 int amount = sc.nextInt();
				 sc.nextLine();
				 System.out.println("Enter the Account Number you want to transfer :");
				 String dest = sc.nextLine();
				 trans = new TransactionsManager();
				 try {
					trans.transferAmount(src,dest,amount);
				} catch (SQLException e) {
					
					e.printStackTrace();
				}
					break;
				 }
			 case 3:{
				//current balance
				 trans = new TransactionsManager();
				 trans.currentBalance(id);
					break;
				 }
			 case 4:{
				 //transaction history
				 trans = new TransactionsManager();
				 trans.transaction_record(id);
					break;
				 }
			 case 5:{
				//payment categories
				 int ch =0;char pay;
				 do {
					 System.out.println("---------select a payment category u want to pay----------");
					 System.out.println("1. Personal Loan");
					 System.out.println("2. Home Loan");
					 System.out.println("3. Student Loan");
					 System.out.println("4. Business Loan");
					 System.out.println("5. Credit Card Loan");
					 System.out.println("6. Construction Loan");
					 System.out.println("0. Exit");
					 ch = sc.nextInt();
					 System.out.println("Are you sure you want to pay "+lp.typeOfloan(ch)+" ?(y/n)");
					 pay = sc.next().charAt(0);
				     if(Character.toLowerCase(pay)!='n') {
				    	 lp.loanpayment(ch,id);
				     }
					
				 }while(ch!=0);
				 break;
			 }
			 case 6:{
					//apply loan
				 int ch = 0,loan=0;
				 do {
					 
					    System.out.println("Select the loan that you want...");
					    System.out.println("1. Personal Loan");
					    System.out.println("2. Home Loan");
					    System.out.println("3. Student Loan");
					    System.out.println("4. Business Loan");
					    System.out.println("5. Credit Card Loan");
					    System.out.println("6. Construction Loan");
					    System.out.println("0. Exit");

					    loan = sc.nextInt();

					    if (loan == 0) {
					        break;
					    }

					    System.out.println("Are you sure you want to apply for a loan? (y/n) ");
					    ch = sc.next().charAt(0);

					    if (Character.toLowerCase(ch) == 'y') {
					        System.out.println("Enter the loan amount you want..?");
					        double amt = sc.nextDouble();
					        sc.nextLine();
                            System.out.println("Enter your repayment date(YYYY-MM-DD) :");
                            String repay = sc.nextLine();
					        TransactionsManager trans = new TransactionsManager();

					        switch (loan) {
					            case 1:
					                if (amt <= 100000) {
					                    trans.applyLoan(id, amt, "PERSONAL LOAN",repay);
					                } else {
					                    System.out.println("You can't apply for more than 100000 for Personal Loan");
					                }
					                break;
					            case 2:
					                if (amt <= 500000) {
					                    trans.applyLoan(id, amt, "HOME LOAN",repay);
					                } else {
					                    System.out.println("You can't apply for more than 500000 for Home Loan");
					                }
					                break;
					          
					            case 3:
					            	 trans.applyLoan(id, amt, "EDUCATION LOAN",repay);
					                break;
					            case 4:
					                if (amt <= 1000000) {
					                    trans.applyLoan(id, amt, "BUSINESS LOAN",repay);
					                } else {
					                    System.out.println("You can't apply for more than 1000000 for Business Loan");
					                }
					                break;
					            case 5:
					            	if (amt <= 1000000) {
					                    trans.applyLoan(id, amt, "CREDIT CARD LOAN",repay);
					                } else {
					                    System.out.println("You can't apply for more than 1000000 for credit card Loan");
					                }
					                break;
					            case 6:
					                if (amt <= 2000000) {
					                    trans.applyLoan(id, amt, "CONSTRUCTION LOAN",repay);
					                } else {
					                    System.out.println("You can't apply for more than 2000000 for Construction Loan");
					                }
					                break;
					            default:
					                System.out.println("Invalid loan type selected.");
					        }
					    } else {
					        return;
					    }

					} while (loan != 0);

				 
			 
				 break;
			 }
			 case 7:{
				 //Cardless ATM Withdrawals using OTP.
				 System.out.println("Do you want to withdraw amount (y/n)? :");
				 char ch = sc.next().charAt(0);
				 if(Character.toLowerCase(ch)!='n') {
					 String otp = lp.generateOTP(4);
					 System.out.println("Your otp is : "+otp);
					 System.out.println("Enter your OTP(ATM) : ");
					 sc.nextLine();
					 String enter = sc.next();
					 if(otp.equals(enter)) {
						 System.out.println("Enter the amount that customer want to withdraw :");
						 double amount= sc.nextDouble();
							trans = new TransactionsManager();
						 trans.withdrawATM(id,-amount);
						
					 }
				 }
				 
			 break;
			}
			case 8:{
				return;
			}
			}
			
		}while(choice!=0 || choice!=8);
		
	}
}
