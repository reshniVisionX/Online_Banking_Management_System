package Online_banking_management;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class SignUp extends User{
	  static Scanner sc = new Scanner(System.in); 
     public SignUp(int userId, String userName, String password, String userType, int age) {
		super(userId, userName, password, userType, age);
		
	}
    
	public static void storeInDB(String name, String pass, String role,int age) throws SQLException  {
		
		Connection con = MySQLConnection.getConnection();
		String insert = "INSERT INTO USERS(userID,userName,password,userType,age) VALUES(DEFAULT,?,?,?,?);";
		String select = "SELECT * FROM USERS WHERE password = ? AND username = ?";
		String apass = "SELECT * FROM SETTING WHERE role='ADMIN' AND isAlive=true";
		String epass = "SELECT * FROM SETTING WHERE role='EMPLOYEE' AND isAlive=true";
		try(PreparedStatement prepstmt = con.prepareStatement(insert);
				PreparedStatement prep = con.prepareStatement(select);
				PreparedStatement aprep = con.prepareStatement(apass);
				PreparedStatement eprep = con.prepareStatement(epass)){
			ResultSet admin = aprep.executeQuery();
			String emp_pass="",admin_pass="";
			if(admin.next()) {
				 admin_pass = admin.getString("role_pass");
			}
			ResultSet employee = eprep.executeQuery();
			if(employee.next()) {
				 emp_pass = employee.getString("role_pass");
			}
			 int f = 1;
			 if(!pass.equals(emp_pass) && !pass.equals(admin_pass)) {
			 prep.setString(1,pass);
			 prep.setString(2,name);
			 ResultSet res1 = prep.executeQuery();
			 if(res1.next()) {
			    System.out.println("User already exist...");
			    return;
			 }
			 }
			 prepstmt.setString(1, name);
             prepstmt.setString(2,  pass);
             prepstmt.setString(3, role);
             prepstmt.setInt(4, age);
             if(role.equalsIgnoreCase("ADMIN")) {
            	 if(pass.equals(admin_pass)) {
            		f=1;
            	 }else
            		f=0;
             }
             else if(role.equalsIgnoreCase("EMPLOYEE")) {
            	 if(pass.equals(emp_pass)) {
            		 f =1;
            	 }else
            		 f=0;
             }
             else if(role.equalsIgnoreCase("CUSTOMER"))
            	 f =1;
             else
            	 f=0;
             if(f==1) {
            	
			int rs = prepstmt.executeUpdate();
			int userId=0,ages=0;String password="";
			if(rs>0) {
				 prep.setString(1,pass);
				 prep.setString(2, name);
				 ResultSet res = prep.executeQuery();
				 if(res.next()) {
					userId = res.getInt("userID");
					  ages = res.getInt("age");
					  password = res.getString("password");
				 }else {
					 System.out.println("Can't find the user in db");
				 }
				System.out.println("Sign-Up Successfull...! ");
				if(role.equals("CUSTOMER")) {
					int DEFAULT =1;
					
					 System.out.println("We welcome you customer "+name);
					
					 Customer cust = new Customer();
	                 try {
						cust.customerMenuList(userId,age);
					} catch (Exception e) {
						
						e.printStackTrace();
					}
			    	  
	     		   
				}
				
			}
			else {
				System.out.println("Error during insertion Sign_Up");
			}
			
			if(role.equalsIgnoreCase("ADMIN")) {
				if(pass.equals(admin_pass)) {
				System.out.println("Welcome back Admin "+name);
				Admin adm = new Admin();
				
                adm.showAdminMenu();}
			}
			else if(role.equalsIgnoreCase("EMPLOYEE")) {
				if(pass.equals(emp_pass)) {
					int DEFAULT =1;
					 System.out.println("Enter you Employee_id :");
			    	  int empid = sc.nextInt();
			    	  sc.nextLine();
			    	  System.out.println("Enter your job_position (MANAGER/OFFICER) :");
			    	  String pos = sc.nextLine().toUpperCase();
			    	  if(pos.equals("MANAGER") || pos.equals("OFFICER")) {
			    	  System.out.println("Enter your salary per month :");
			    	  double salary = sc.nextDouble();
			    	  boolean isActive = true;
			    	  Employee emp = new Employee(DEFAULT,name,pass,role,age,empid,pos,isActive,salary);
			    	  if(emp.signEmp(empid,userId,pos,salary))
			    	 
				     System.out.println("Welcome Employee "+name);
                      Employees empl = new Employees();
                     empl.employeeMenuList();}}
				 else {
                    	
                    	 System.out.println("Your password is invalid");
                    	 return;
                     }
				}
			}
			else {
				
				System.out.println("Invalid role or password ...!! Please try again entering valid data");
				return;
			}
             
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
	}

}