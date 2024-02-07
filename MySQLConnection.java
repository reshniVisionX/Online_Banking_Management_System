package Online_banking_management;
import java.sql.*;
public class MySQLConnection {

		 static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
		 static final String DB_URL = "jdbc:mysql://localhost:3306/banking_management_systems";
		 static final String USER = "reshni";
		 static final String PASS = "root";

		 static Connection con = null;

		    static {
		        try {
		            Class.forName(JDBC_DRIVER);
		            con = DriverManager.getConnection(DB_URL, USER, PASS);
		            System.out.println("Connected to DB");
		        } 
		        catch (ClassNotFoundException e) {
		            e.printStackTrace();
		            throw new RuntimeException("Error loading JDBC driver", e);
		        } catch (SQLException e) {
		            e.printStackTrace();
		            throw new RuntimeException("Error connecting to the database", e);
		        }
		    }

		    public static Connection getConnection() {
		        return con;
		    }
	}

