package Online_banking_management;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Employee extends User {
    private int empId;
    private String position;
    private double salary;
    private boolean isActive;

    public Employee(int userId, String userName, String password, String userType, int age , int empId, String position,boolean isActive, double salary) {
        super( userId,userName,password,userType,age);
        this.empId = empId;
        this.position = position;
        this.salary = salary;
        this.isActive = isActive;
    }

 
    public int getEmpId() {
        return empId;
    }

    public String getPosition() {
        return position;
    }

    public double getSalary() {
        return salary;
    }

   
    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }


    public boolean signEmp(int empid, int userId, String pos, double salary) {
        String query = "INSERT INTO EMPLOYEE(empId, userId, position, isActive, salary) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = MySQLConnection.getConnection();
             PreparedStatement prep = con.prepareStatement(query)) {
            prep.setInt(1, empid);
            prep.setInt(2, userId);
            prep.setString(3, pos);
            prep.setBoolean(4, true);
            prep.setDouble(5, salary);
            
            int rs = prep.executeUpdate();
            if (rs > 0) {
                System.out.println("Employee details updated");
                return true;
            } else {
                System.out.println("Error in updating employee details. Please contact admin.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error occurred while updating employee details: " + e.getMessage());
            return false;
        }
    }

}
