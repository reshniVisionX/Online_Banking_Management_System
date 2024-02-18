package Online_banking_management;

import java.security.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminFunctionalities {

	Connection con = MySQLConnection.getConnection();

	public void generateTdyRepo() {
		String query = "SELECT * FROM loan WHERE DATE(timestamp) = CURDATE()";
		  String query2 = "SELECT * FROM Transactions WHERE DATE(timestamp) = CURDATE()";
		try(PreparedStatement prep = con.prepareStatement(query);
				PreparedStatement prep1 =con.prepareStatement(query2)){
			ResultSet rs = prep.executeQuery();
			System.out.println("-----------------Today's loan reports----------------");
			while(rs.next()) {
				
				 int loanId = rs.getInt("loanId");
		            int userId = rs.getInt("userId");
		            String type = rs.getString("type");
		            double amount = rs.getDouble("amount");
		            double interestRate = rs.getDouble("interestRate");
		            boolean isApproved = rs.getBoolean("isApproved");
		            double payed = rs.getDouble("payed");
		            String repaymentperiod = rs.getString("repaymentperiod");
		            java.sql.Timestamp timestamp = rs.getTimestamp("timestamp");
				System.out.println("Loan ID: " + loanId +
		                   ", User ID: " + userId +
		                   ", Type: " + type +
		                   ", Amount: " + amount +
		                   ", Interest Rate: " + interestRate +
		                   ", Is Approved: " + isApproved +
		                   ", Payed: " + payed +
		                   ", Repayment Period: " + repaymentperiod +
		                   ", Timestamp: " + timestamp 
		                   );}
			 System.out.println("---------------------------------------------------End---------------------------------------------------");
				 ResultSet rs2 = prep1.executeQuery();
				 System.out.println("------------Today's Transactions reports-----------");
			        while (rs2.next()) {
			           
			            int transId = rs2.getInt("transId");
			            int userId2 = rs2.getInt("userId");
			            int acc_id = rs2.getInt("accountId");
			            String transType = rs2.getString("transType");
			            double amount2 = rs2.getDouble("amount");
			            String sender = rs2.getString("sender");
			            String receiver = rs2.getString("receiver");
			            java.sql.Timestamp timestamp2 = rs2.getTimestamp("timestamp");
			            System.out.println("Transaction ID: " + transId +
			                    ", User ID: " + userId2 +
			                    ", Account Id: "+acc_id +
			                    ", Transaction Type: " + transType +
			                    ", Amount: " + amount2 +
			                    ", Sender: " + sender +
			                    ", Receiver: " + receiver +
			                    ", Timestamp: " + timestamp2 
			                    );

			}
			        System.out.println("---------------------------------------------------End---------------------------------------------------");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public void deactivate_an_emp(String name) {
		String select = "SELECT * FROM users where username = ? AND userType = 'EMPLOYEE'";
		String emp = "UPDATE EMPLOYEE SET isActive = false where userId = ? ";
		try(PreparedStatement prep = con.prepareStatement(select);
				PreparedStatement empact = con.prepareStatement(emp)){
			con.setAutoCommit(false);
			boolean success = true;
			prep.setString(1, name);
			ResultSet rs = prep.executeQuery();
			while(rs.next()) {
				int userId = rs.getInt("userId");
				empact.setInt(1,userId );
				int act = empact.executeUpdate();
				if(act>0) {
					System.out.println("Employee is inactive state");
				}else {
					success = false;
					System.out.println("Employee active state updation error");
				}
				if(success)
					con.commit();
				else
					con.rollback();
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
	}

	public void view_emp_detail() {
		String query = "SELECT * FROM EMPLOYEE ";
		try(PreparedStatement prep = con.prepareStatement(query)){
			ResultSet res= prep.executeQuery();
			int k =1;
			 while (res.next()) {
				 System.out.println("------------Emp"+k+"---------------");
		            int empId = res.getInt("empId");
		            int userId = res.getInt("userId");
		            String position = res.getString("position");
		            boolean isActive = res.getBoolean("isActive");
		            double salary = res.getDouble("salary");

		            System.out.println("Employee ID: " + empId);
		            System.out.println("User ID: " + userId);
		            System.out.println("Position: " + position);
		            System.out.println("Active: " + (isActive ? "Yes" : "No"));
		            System.out.println("Salary: " + salary);
		           
		            k++;
		        }
			 System.out.println("----------------------------------");
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
	}

	public void update_an_emp(String name, String uname, int age, String pos, double sal) {
		String query = "SELECT * FROM USERS WHERE username = ?";
		String update = "UPDATE EMPLOYEE SET position = ?,salary = ? where userId = ?";
		String user = "UPDATE USERS SET age = ? ,username = ? WHERE userId = ?";
		try(PreparedStatement prep = con.prepareStatement(query);
				PreparedStatement uprep = con.prepareStatement(update);
				PreparedStatement cprep  = con.prepareStatement(user)){
			prep.setString(1, name);
			boolean success = true;
			con.setAutoCommit(false);
			ResultSet rs = prep.executeQuery();
			if(rs.next()) {
				int userId = rs.getInt("userId");
				 System.out.println("Position to update: " + pos);
				uprep.setString(1, pos);
				uprep.setDouble(2, sal);
				uprep.setInt(3,userId);
				int up = uprep.executeUpdate();
				if(up>0 && success) {
					System.out.println("Updated employee data..");
				}else {
					success=false;
					System.out.println(" Error while updating employee db");
				}
				cprep.setInt(1,age);
				cprep.setString(2, uname);
				cprep.setInt(3, userId);
				int cp = cprep.executeUpdate();
				if(cp>0 && success) {
					System.out.print("Updated the user db successfully");
				}else {
					success= false;
					System.out.println("Error while updating user db");
				}
			}else {
				success = false;
				System.out.println("Employee doesnt exist..");
			}
			if(success)
				con.commit();
			else
				con.rollback();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public void generateTransactionalReport() {
		String Query = "SELECT * FROM TRANSACTIONS";
		try(PreparedStatement prep = con.prepareStatement(Query)){
			ResultSet rs = prep.executeQuery();
			System.out.println();
			while(rs.next()) {
				 int transId = rs.getInt("transId");
		            int accountId = rs.getInt("accountId");
		            String transType = rs.getString("transType");
		            double amount = rs.getDouble("amount");
		            String sender = rs.getString("sender");
		            String receiver = rs.getString("receiver");
		            int userId = rs.getInt("userId");
		            java.sql.Timestamp timestamp = rs.getTimestamp("timestamp");

		            System.out.println("Transaction ID: " + transId +
		                    ", Account ID: " + accountId +
		                    ", Transaction Type: " + transType +
		                    ", Amount: " + amount +
		                    ", Sender: " + sender +
		                    ", Receiver: " + receiver +
		                    ", User ID: " + userId +
		                    ", Timestamp: " + timestamp);
		         
			}
			System.out.println("-----------------------------------------End of report-------------------------------------------");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public void change_role_pass(String role,String pass) {
		String select = "SELECT * FROM SETTING WHERE role=? AND isAlive = true";
		String update = "UPDATE SETTING SET isAlive = false WHERE role = ?";
		String insert = "INSERT INTO SETTING(role,role_pass,isAlive) VALUES(?,?,?)";
		String update_user = "UPDATE USERS SET password = ? WHERE userType = ?";
		try(PreparedStatement sprep=con.prepareStatement(select);
				PreparedStatement uprep=con.prepareStatement(update);
				PreparedStatement iprep=con.prepareStatement(insert);
				PreparedStatement userprep = con.prepareStatement(update_user)){
			sprep.setString(1, role.toUpperCase());
			ResultSet rs = sprep.executeQuery();
            con.setAutoCommit(false);
            boolean success = true;
			if(rs.next()) {
				uprep.setString(1, role);
				int res = uprep.executeUpdate();
				if(res>0) {
				iprep.setString(1, role);
				iprep.setString(2, pass);
				iprep.setBoolean(3, true);
				int re = iprep.executeUpdate();
				if(re>0 && success) {
					System.out.println("Password updated successfully");
					userprep.setString(1, pass);
					userprep.setString(2, role);
					int upd = userprep.executeUpdate();
					if(upd>0 && success) {
						System.out.println("Updated in users table");
					}
				}else {
					success = false;
					System.out.println("Error in inserting");
					
				}
				}else {
					System.out.println("Error in updating");
					success = false;
				}
			}
			if(success)
				con.commit();
			else
				con.rollback();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
	}

	public void update_password(String pass,String role) {
		String update = "UPDATE USERS SET password = ? WHERE userType = ?";
		try(PreparedStatement uprep = con.prepareStatement(update)){
			uprep.setString(1,pass);
			uprep.setString(2, role);
			
			int upd = uprep.executeUpdate();
			if(upd>0) {
				System.out.println("Updated in users table");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	
}
