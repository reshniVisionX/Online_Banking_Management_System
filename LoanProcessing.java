package Online_banking_management;

import java.security.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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
	    
	    public static String calculateRepaymentDate(Object approvalDateTime, String loanType) {
	        if (approvalDateTime instanceof LocalDateTime) {
	            int repaymentDurationYears;
	            switch (loanType.toLowerCase()) {
	                case "personal loan":
	                    repaymentDurationYears = 7;
	                    break;
	                case "home loan":
	                    repaymentDurationYears = 10;
	                    break;
	                case "education loan":
	                    repaymentDurationYears = 8;
	                    break;
	                case "business loan":
	                    repaymentDurationYears = 2;
	                    break;
	                case "credit card loan":
	                    repaymentDurationYears = 1;
	                    break;
	                case "construction loan":
	                    repaymentDurationYears = 5;
	                    break;
	                default:
	                    return "Loan type not recognized";
	            }

	            LocalDateTime approvalLocalDateTime = (LocalDateTime) approvalDateTime;
	            LocalDateTime repaymentDateTime = approvalLocalDateTime.plusYears(repaymentDurationYears);

	            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	            return repaymentDateTime.format(formatter);
	        } else {
	            return "Approval date is not in the correct format";
	        }
	    }

	    
	    
	    
		public void applyLoan(int id, double amt,String type) {
			String insert = "INSERT INTO LOAN (loanId,userId,type,amount,interestRate,isApproved,payed,repaymentperiod,Timestamp) VALUE(DEFAULT,?,?,?,?,?,?,?,?)";
			try(PreparedStatement prep = con.prepareStatement(insert)){
				 LocalDateTime timestamp = LocalDateTime.now();
			
				prep.setInt(1, id);
				prep.setString(2, type);
				prep.setDouble(3, amt);
				prep.setDouble(4,0.0);
				prep.setBoolean(5, false);
				prep.setDouble(6, 0.0);
				prep.setString(7, "");
				prep.setObject(8, timestamp);
				int rs = prep.executeUpdate();
				if(rs>0) {
					System.out.println("Loan is processing...");
				}else {
					System.out.println("Error while processing the loan");
				}
				
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		}



	public void approveLoan() {
	 String select = "SELECT * FROM LOAN WHERE isApproved = false";
	 String update = "UPDATE LOAN SET isApproved = true, interestRate = ? ,repaymentperiod = ? WHERE loanId = ?";
	 String person = "SELECT * FROM LOAN WHERE loanId = ?";
	 String query = "SELECT balance FROM ACCOUNTS where userId = ? AND status = true";
	 String accup = "UPDATE ACCOUNTS SET balance = ? WHERE userId = ? AND status = true";
	 try(PreparedStatement prep = con.prepareStatement(select);
			 PreparedStatement uprep = con.prepareStatement(update);
			 PreparedStatement pres = con.prepareStatement(person);
			 PreparedStatement prepup = con.prepareStatement(accup);
			 PreparedStatement selectq = con.prepareStatement(query)){
		 ResultSet rs = prep.executeQuery();
		 int userId=0; 
		 LocalDateTime timestamp = null ;
		 while(rs.next()) {
			 int loanId = rs.getInt("loanId");
	             userId = rs.getInt("userId");
	            String type = rs.getString("type");
	            double amount = rs.getDouble("amount");
	            double interestRate = rs.getDouble("interestRate");
	            boolean isApproved = rs.getBoolean("isApproved");
	            double payed = rs.getDouble("payed");
	            String repaymentperiod = rs.getString("repaymentperiod");
	           timestamp = rs.getObject("timestamp", LocalDateTime.class);

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
			 String loanType="";double amount = 0;Object date=null;
			 if(res.next() && success) {
				 loanType = res.getString("type");
				 amount = res.getDouble("amount");
				 date =  res.getTimestamp("timestamp");
			 double interest = getInterest(loanType); 
			 double penalty = calculateInterestRate(loanType, amount);
			 System.out.println("Your interest is : "+penalty);
			// System.out.println(date);
			  String stringDateTime = date.toString();

		        LocalDateTime localDateTime = LocalDateTime.parse(stringDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"));
               String formattedYearsLater = calculateRepaymentDate(localDateTime ,loanType);
             System.out.println("Your repayment year will be : "+ formattedYearsLater);
			 uprep.setDouble(1,interest);
			 uprep.setString(2,formattedYearsLater);
			 uprep.setInt(3, id);
			 int up = uprep.executeUpdate();
			 if(up>0 && success) {
				 System.out.println("Loan for "+id+" is approved");
				 selectq.setInt(1, userId);
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
			 if(!success) {
				 con.rollback();
			 }
		 }
	 } catch (SQLException e) {
		
		e.printStackTrace();
	}
	 finally {
         
         try {
             if (con != null) {
                 con.close();
             }
         } catch (SQLException e) {
             System.out.println("Error closing connection: " + e.getMessage());
         }
     }
		
	}
	
	
	 public String typeOfloan(int c) {
		 if(c==1)
			 return "Personal Loan";
		 else if(c==2)
			 return "Home Loan";
		 else if(c==3)
			 return "Education Loan";
		 else if(c==4)
			 return "Business Loan";
		 else if(c==5)
			 return "Credit Card Loan";
		 else if(c==6)
			 return "Construction Loan";
		 else
			 return "null";
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
	public void loanpayment(int choice, int id, double amount) {
	
		String select = "SELECT * FROM LOAN WHERE userId = ? AND type = ? AND isApproved=true";
		String payed = "UPDATE LOAN SET payed = ? WHERE userId = ? AND type = ?";

		try(PreparedStatement prep = con.prepareStatement(select);
				PreparedStatement uprep = con.prepareStatement(payed)){
			String type = typeOfloan(choice).toUpperCase();
			System.out.println("You are paying to your "+type);
			prep.setInt(1, id);
			prep.setString(2, type);
			ResultSet rs = prep.executeQuery();
			if(rs.next()) {
				double loan = rs.getDouble("amount");
				double pay = rs.getDouble("payed");
				if(amount+pay<=loan) {
				uprep.setDouble(1, pay+amount);
				uprep.setInt(2, id);
				uprep.setString(3, type);
				int res = uprep.executeUpdate();
				if(res>0) {
					System.out.println("Loan payment Updated successfully");
				}else {
					System.out.println("Error while updating the payment");
				}
				}else {
					System.out.println("Your are paying more than your loan amount...");
					return;
				}
			}else {
				System.out.println("This loan doesn't exist");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	public void refreshLoan() {
		String select = "SELECT * FROM LOAN";
		String update = "UPDATE LOAN SET interestRate = ? WHERE loanId = ?";
		try(PreparedStatement prep = con.prepareStatement(select);
				PreparedStatement uprep = con.prepareStatement(update)){
			ResultSet rs = prep.executeQuery();
			int i=0;
			while(rs.next()) {
				String repay_period = rs.getString("repaymentperiod");
				Object avail_date = rs.getObject("timestamp");
				double interest = rs.getDouble("interestRate");
				double amount = rs.getDouble("amount");
				int loanid = rs.getInt("loanId");
				String type = rs.getString("type");
				LocalDateTime currentDateTime = LocalDateTime.now();
			
				  String stringDateTime = avail_date.toString();
				  LocalDateTime localDateTime = LocalDateTime.parse( stringDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"));


				long monthsDifference = ChronoUnit.MONTHS.between(localDateTime, currentDateTime);
				System.out.println("   Diff:  "+monthsDifference+"  =  "+localDateTime+"  -  "+currentDateTime);
				 double emi = 0;
				if (monthsDifference >= 1) {
				     emi = monthsDifference * interest*amount;
				}
				interest = getInterest(type);
				System.out.println("Emi for "+monthsDifference+"  months is : "+emi+"  Interest rate : "+interest);
				if(emi>1) {
					interest = emi;
				}
                uprep.setDouble(1,interest);
                uprep.setInt(2, loanid);
                int res = uprep.executeUpdate();
                if(res>0) {
                	System.out.println("----Updated "+i+" record----");
                }else {
                	System.out.println("Error updating");
                }
                i++;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	public void checkNoftification(int id) {
		String query = "SELECT * FROM LOAN WHERE userId = ?";
		try(PreparedStatement prep = con.prepareStatement(query)){
			prep.setInt(1,id);
			ResultSet rs = prep.executeQuery();
			System.out.println();
			int f=0;
			while(rs.next()) {
				f=1;
				String loan = rs.getString("type");
				String repay = rs.getString("repaymentperiod");
				System.out.println("   You hava your \'"+loan+"\' to be payed within \'"+repay+"\' try being earliest to avoid risk");
				System.out.println();
			}
			if(f==0) {
				System.out.println("   You don't have any notification right now..!!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
}
