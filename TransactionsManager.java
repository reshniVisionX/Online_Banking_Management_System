package Online_banking_management;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class TransactionsManager {
Connection con = MySQLConnection.getConnection();

	public void transferAmount(String src, String dest, int amount) throws SQLException {
		String srcQ = "SELECT * FROM ACCOUNTS WHERE accNumber = ?";
		String destQ = "SELECT * FROM ACCOUNTS WHERE accNumber = ?";
		String transfer = "UPDATE ACCOUNTS SET balance =  ? WHERE accNumber = ?";
		String transaction = "INSERT INTO TRANSACTIONS(transId,accountId,transType,amount,sender,receiver,userId,Timestamp) VALUES(DEFAULT,?,?,?,?,?,?,?)";
		
		try(PreparedStatement srcprep = con.prepareStatement(srcQ);
				PreparedStatement destprep = con.prepareStatement(destQ);
				PreparedStatement transprep = con.prepareStatement(transfer);
				PreparedStatement trans_rec = con.prepareStatement(transaction);
				PreparedStatement trans_rec_dest = con.prepareStatement(transaction)){
			con.setAutoCommit(false);
			boolean com = true;
			srcprep.setString(1, src);
			ResultSet rs = srcprep.executeQuery();
			double balance=0;
			int sen_accid=0;
			int rec_accid=0,s_id = 0;
			if(rs.next()) {
				 sen_accid = rs.getInt("accountId");
				 balance = rs.getDouble("balance");
				 s_id = rs.getInt("userId");
				if(balance>=amount) {
					if(amount>50000) {
						com = false;
						System.out.println("Transfer limit (50000) is exceeded");
						
					}
				}else {
					com = false;
					System.out.println("Insuffiencent balance in your account. you cant make this transaction");
					
				}
			}else {
				com = false;
				System.out.println("You dont have an account");
				
			}
			destprep.setString(1, dest);
			ResultSet red = destprep.executeQuery();
			double d_balance = 0;int d_id=0;
			if(!red.next()) {
				com = false;
				//check is the account active status too....
				System.out.println("Tranfer account number is not found .");
				
			}else {
				d_id = red.getInt("userId");
				rec_accid = red.getInt("accountId");
				 d_balance = red.getDouble("balance");
			}
			
			transprep.setDouble(1,d_balance+amount);
			transprep.setString(2,dest);
			int add = transprep.executeUpdate();
			if(add>0 && com) {
				System.out.println("Successfully transfered to "+dest);
			}else {
				com = false;
				System.out.println("Error while transferring");
				
			}
			
			transprep.setDouble(1,balance-amount);
			transprep.setString(2,src);
			int sub = transprep.executeUpdate();
			if(sub>0 && com) {
				System.out.println("Successfully debited "+amount+" from your account ");
				System.out.println("Your current balance is "+(balance-amount));
				
				 
			}else {
				com = false;
				System.out.println("Error in debiting");
				
			}
			//transaction record updation
			LocalDateTime timestamp = LocalDateTime.now(); 
			trans_rec.setInt(1,sen_accid);
			trans_rec.setString(2, "TRANSFER");
			trans_rec.setDouble(3, amount);
			trans_rec.setString(4, src);
			trans_rec.setString(5, dest);
			trans_rec.setInt(6, s_id);
			trans_rec.setObject(7, timestamp);
			int rec = trans_rec.executeUpdate();
			if(rec>0 && com) {
				System.out.print(" Transferred...!!!");
			}else {
				com = false;
				System.out.println("Error in transfering..");
			}
			trans_rec_dest.setInt(1,rec_accid);
			trans_rec_dest.setString(2, "RECEIVER");
			trans_rec_dest.setDouble(3, amount);
			trans_rec_dest.setString(4, dest);
			trans_rec_dest.setString(5, src);
			trans_rec_dest.setInt(6, d_id);
			trans_rec_dest.setObject(7, timestamp);
			int dest_rec = trans_rec_dest.executeUpdate();
			if(dest_rec>0 && com) {
				System.out.println(" Deposited..!!!");
			}else {
				com = false;
				System.out.println("Deposit failed..");
			}
			
			if(com) {
				con.commit();
			}
			
		} catch (SQLException e) {
			  con.rollback();
			e.printStackTrace();
		}
		
	}

	public void currentBalance(int id) {
	  String balance = "SELECT balance from ACCOUNTS WHERE userId = ?";
	  try(PreparedStatement prep = con.prepareStatement(balance)){
		  prep.setInt(1, id);
		  ResultSet rs = prep.executeQuery();
		  if(rs.next()) {
			  double bal = rs.getDouble("balance");
			  System.out.println("The current balance is : "+bal);
		  }else 
		  {  
			  System.out.println("User not found or no balance available.");
		  }
		  
	  } catch (SQLException e) {
	
		e.printStackTrace();
	  }
		
	}
   
	public void transaction_record(int id) {
		String record = "SELECT * FROM TRANSACTIONS WHERE userId = ?";
		try(PreparedStatement prep = con.prepareStatement(record)){
			prep.setInt(1, id);
			ResultSet rs = prep.executeQuery();
			int i=1;
			if(rs.next()) {
				    int transId = rs.getInt("transId");
	                int accountId = rs.getInt("accountId");
	                String transType = rs.getString("transType");
	                double amount = rs.getDouble("amount");
	                String sender = rs.getString("sender");
	                String receiver = rs.getString("receiver");
	                LocalDateTime timestamp = rs.getObject("timestamp", LocalDateTime.class);

	              
	                System.out.println(i+". "+"Transaction ID: \'" + transId+"\' AccountId: \'"+accountId+"\' Transfer Type: \'"+transType+"\' Amount: \'"+amount+"\' Sender: \'"+sender+"\' Receiver: \'"+receiver+"\' DateTime: \'"+timestamp+"\'");
			i++;
			}
		} catch (SQLException e) {
		    
			e.printStackTrace();
		}
		
	}

	public void updateUser(String pass, String pass2, String name, int age) {
		String update = "UPDATE USERS SET password = ?,userName = ?,age = ? WHERE password = ? AND userType = 'CUSTOMER'";
		try(PreparedStatement prep = con.prepareStatement(update)){
			prep.setString(1, pass2);
			prep.setString(2, name);
			prep.setInt(3, age);
			prep.setString(4, pass);
			int rs = prep.executeUpdate();
			if(rs>0) {
				System.out.println("Updated customer updation successfully..");
			}else {
				System.out.println("Failed to update ..check if user exists");
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
	}



	public void deposit(String acc_num, double amount) {
		
		String select = "SELECT * FROM ACCOUNTS WHERE accNumber = ? AND status = true";
		String deposit =  "UPDATE ACCOUNTS SET balance =  ? WHERE accNumber = ?";
		String trans = "INSERT INTO TRANSACTIONS(transId,accountId,transType,amount,sender,receiver,userId,Timestamp) VALUES(DEFAULT,?,?,?,?,?,?,?)";
		try(PreparedStatement prep = con.prepareStatement(deposit);
				PreparedStatement dprep = con.prepareStatement(select);
				PreparedStatement tprep = con.prepareStatement(trans)){
			dprep.setString(1, acc_num);
			ResultSet rs = dprep.executeQuery();
			boolean success = true;
			con.setAutoCommit(false);
			if(rs.next()) {
				System.out.print("Amount ..");
				double balance = rs.getDouble("balance");
//				String acc_no = rs.getString("accNumber");
				int uid = rs.getInt("userId");
				int id = rs.getInt("accountId");
				prep.setDouble(1, balance+amount);
				prep.setString(2, acc_num);
			
				int send = prep.executeUpdate();
				
				if(send>0 && success)
					 System.out.print(" deposited successfully");
				else { 
					success = false;
				
					System.out.println("Error while updating");}
				
				//record - update
				 LocalDateTime timestamp = LocalDateTime.now();
		            tprep.setInt(1, id);
		            tprep.setString(2, "DEPOSIT");
		            tprep.setDouble(3, amount);
		            tprep.setString(4, "BANK_DEPOSIT");
		            tprep.setString(5, acc_num);
		            tprep.setInt(6, uid);
		            tprep.setObject(7, timestamp);
		            int tr = tprep.executeUpdate();
				if(tr>0) {
					System.out.println("Updated in record....");
				}else {
					success= false;
					System.out.println("Error in updating");
				}
				 if(success) {
		                con.commit();
		                System.out.print(" Commit success..");
		            } else {
		            	System.out.println("Error rolledback..");
		                con.rollback();
		            }
			}else {
				System.out.println("Account is not active");
			}
		} catch (SQLException e) {
			System.out.println("Error...!");
			e.printStackTrace();
		}
		
	}



	public void withdraw(String acc_num, double amount) {
		String select = "SELECT * FROM ACCOUNTS WHERE accNumber = ? AND status = true";
		String deposit =  "UPDATE ACCOUNTS SET balance =  ? WHERE accNumber = ?";
		String trans = "INSERT INTO TRANSACTIONS(transId,accountId,transType,amount,sender,receiver,userId,Timestamp) VALUES(DEFAULT,?,?,?,?,?,?,?)";
		try(PreparedStatement prep = con.prepareStatement(deposit);
				PreparedStatement dprep = con.prepareStatement(select);
				PreparedStatement tprep = con.prepareStatement(trans)){
			dprep.setString(1, acc_num);
			ResultSet rs = dprep.executeQuery();
			boolean success = true;
			con.setAutoCommit(false);
			if(rs.next()) {
				System.out.print("Amount ..");
				double balance = rs.getDouble("balance");
				int uid = rs.getInt("userId");
				int id = rs.getInt("accountId");
				if(balance<(balance+amount)) {
					success=false;
					System.out.println("You dont have sufficient balance");
				}
				prep.setDouble(1, balance+amount);
				prep.setString(2, acc_num);
			
				int send = prep.executeUpdate();
				
				if(send>0 && success)
					 System.out.print(" withdraw successfully");
				else { 
					success = false;
				
					System.out.println("Error while updating");}
				
				//record - update
				 LocalDateTime timestamp = LocalDateTime.now();
		            tprep.setInt(1, id);
		            tprep.setString(2, "WITHDRAW");
		            tprep.setDouble(3, amount);
		            tprep.setString(4, "BANK");
		            tprep.setString(5, acc_num);
		            tprep.setInt(6, uid);
		            tprep.setObject(7, timestamp);
		            int tr = tprep.executeUpdate();
				if(tr>0) {
					System.out.println(" Updated in record....");
				}else {
					success= false;
					System.out.println("Error in updating");
				}
				 if(success) {
		                con.commit();
		                System.out.print(" Commit success..");
		            } else {
		            	System.out.println("Error rolledback..");
		                con.rollback();
		            }
			}else {
				System.out.println("Account is not active");
			}
		} catch (SQLException e) {
			System.out.println("Error...!");
			e.printStackTrace();
		}
		
	}



	public void applyLoan(int id, double amt,String type,String repay) {
		String insert = "INSERT INTO LOAN (loanId,userId,type,amount,interestRate,isApproved,payed,repaymentperiod,Timestamp) VALUE(DEFAULT,?,?,?,?,?,?,?,?)";
		try(PreparedStatement prep = con.prepareStatement(insert)){
			 LocalDateTime timestamp = LocalDateTime.now();
			prep.setInt(1, id);
			prep.setString(2, type);
			prep.setDouble(3, amt);
			prep.setDouble(4,0.0);
			prep.setBoolean(5, false);
			prep.setDouble(6, 0.0);
			prep.setString(7, repay);
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



	public void withdrawATM(int id, double amount) {
		String select = "SELECT * FROM ACCOUNTS WHERE userId = ? AND status = true";
		String deposit =  "UPDATE ACCOUNTS SET balance =  ? WHERE userId = ?";
		String trans = "INSERT INTO TRANSACTIONS(transId,accountId,transType,amount,sender,receiver,userId,Timestamp) VALUES(DEFAULT,?,?,?,?,?,?,?)";
		try(PreparedStatement prep = con.prepareStatement(deposit);
				PreparedStatement dprep = con.prepareStatement(select);
				PreparedStatement tprep = con.prepareStatement(trans)){
			dprep.setInt(1, id);
			ResultSet rs = dprep.executeQuery();
			boolean success = true;
			con.setAutoCommit(false);
			if(rs.next()) {
				System.out.print("Amount ..");
				double balance = rs.getDouble("balance");
				int uid = rs.getInt("userId");
				int acc_id = rs.getInt("accountId");
				String acc_num = rs.getString("accNumber");
				if(balance<(balance+amount)) {
					success=false;
					System.out.println("You dont have sufficient balance");
				}
				prep.setDouble(1, balance+amount);
				prep.setInt(2, id);
			
				int send = prep.executeUpdate();
				
				if(send>0 && success) {
					 System.out.print(" withdraw successfully");}
				else { 
					success = false;
				
					System.out.println("Error while updating");}
				
				//record - update
				 LocalDateTime timestamp = LocalDateTime.now();
		            tprep.setInt(1, id);
		            tprep.setString(2, "WITHDRAW");
		            tprep.setDouble(3, amount);
		            tprep.setString(4, "ATM");
		            tprep.setString(5, acc_num);
		            tprep.setInt(6, uid);
		            tprep.setObject(7, timestamp);
		            int tr = tprep.executeUpdate();
				if(tr>0) {
					System.out.println(" Updated in record....");
				}else {
					success= false;
					System.out.println("Error in updating");
				}
				 if(success) {
		                con.commit();
		                System.out.print(" Commit success..");
		            } else {
		            	System.out.println("Error rolledback..");
		                con.rollback();
		            }
			}else {
				System.out.println("Account is not active");
			}
		} catch (SQLException e) {
			System.out.println("Error...!");
			e.printStackTrace();
		}
		
	}

	public void viewCustDetails() {
		String query = "SELECT * FROM USERS WHERE userType = 'CUSTOMER'";
		try(PreparedStatement prep = con.prepareStatement(query)){
			ResultSet rs = prep.executeQuery();
			int k=1;
			System.out.println("----------------------------------User Details-----------------------------------");
			while(rs.next()) {
				 int userID = rs.getInt("userID");
		            String username = rs.getString("username");
		            String password = rs.getString("password");
		           
		            int age = rs.getInt("age");

		            System.out.println(k+". User ID: " + userID +
		                    ", Username: " + username +
		                    ", Password: " + password +
		                    ", Age: " + age);
		            k++;
			}
			System.out.println("-------------------------------------------------------------------------------");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

}
