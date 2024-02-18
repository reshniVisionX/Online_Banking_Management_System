package Online_banking_management;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LogIn extends User {

    public LogIn(int userId, String userName, String password,String userType, int age) {
        super(userId, userName, password, userType, age);
    }

    public static void login(String pass, String role) {
        Connection con = MySQLConnection.getConnection();
        String select = "SELECT * FROM Users WHERE password = ? AND userType = ?";
        String apass = "SELECT * FROM SETTING WHERE role='ADMIN' AND isAlive=true";
		String epass = "SELECT * FROM SETTING WHERE role='EMPLOYEE' AND isAlive=true";
        try (PreparedStatement prep = con.prepareStatement(select);
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
            prep.setString(1, pass);
            prep.setString(2, role);
            
            ResultSet rs = prep.executeQuery();
            if(rs.next()) {
             
            	String userRole = rs.getString("userType");
                String name = rs.getString("username");
                int age = rs.getInt("age");
                int userId = rs.getInt("userId");
                if (userRole.equals("ADMIN")) {
                    if (pass.equals(admin_pass)) {
                        System.out.println("Welcome back admin "+name);
                        Admin adm = new Admin();
                        adm.showAdminMenu();
                    } else {
                        System.out.println("Invalid password...you're not the admin");
                        return;
                    }
                } else if (userRole.equals("EMPLOYEE")) {
                    if (pass.equals(emp_pass)) {
                        System.out.println("Welcome Employee ");
                         Employees emp = new Employees();
                         emp.employeeMenuList();
                    } else {
                        System.out.println("Invalid password...you're not the employee of the bank");
                        return;
                    }
                } else if (userRole.equals("CUSTOMER")) {
                    System.out.println("We welcome you customer "+name);
                     Customer cust = new Customer();
                     cust.customerMenuList(userId,age);
                } else {
                    System.out.println("Invalid credentials user");
                    return;
                }
            } else {
                System.out.println("Invalid credentials");
            }

        } catch (SQLException e) {
            System.out.println("Error");
            e.printStackTrace();
        }

    }
}
