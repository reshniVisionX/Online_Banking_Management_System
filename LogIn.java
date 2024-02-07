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
        try (PreparedStatement prep = con.prepareStatement(select)) {
            prep.setString(1, pass);
            prep.setString(2, role);
            ResultSet rs = prep.executeQuery();
            if(rs.next()) {
                String password = rs.getString("password");
                String userRole = rs.getString("userType");
                String name = rs.getString("username");
                int age = rs.getInt("age");
                int userId = rs.getInt("userId");
                if (userRole.equals("ADMIN")) {
                    if (password.equals("admin123")) {
                        System.out.println("Welcome back admin "+name);
                        Admin adm = new Admin();
                        adm.showAdminMenu();
                    } else {
                        System.out.println("Invalid password...you're not the admin");
                        return;
                    }
                } else if (userRole.equals("EMPLOYEE")) {
                    if (password.equals("employee@123")) {
                        System.out.println("Welcome Employee "+name);
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
