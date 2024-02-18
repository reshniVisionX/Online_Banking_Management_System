package Online_banking_management;
import java.sql.SQLException;
import java.util.*;
public class Main {

	public static void main(String[] args) throws SQLException {
		Scanner sc = new Scanner(System.in);
		System.out.println("We welcome you to the Online Banking System....!!");
		System.out.println("-------------------------------------");
		System.out.println("Choose an option below");
		System.out.println("1.Sign-In ");
		System.out.println("2.Log-In ");
		System.out.println("3.Exit");
		System.out.println("-------------------------------------");
		int n = sc.nextInt();
		sc.nextLine();
		
		 if(n==1) {
			int DEFAULT = 1;
			System.out.println("Enter your Name: ");
		  String name = sc.nextLine();
		  System.out.println("Enter your password: ");
		  String pass = sc.nextLine();
		  System.out.println("Enter your role (ADMIN, EMPLOYEE, CUSTOMER): ");
	      String role = sc.nextLine().toUpperCase();
	      System.out.println("Enter your accurate age:");
	      int age = sc.nextInt();
	      System.out.println(name+" "+pass+" "+role+" "+age);
	      SignUp.storeInDB(name, pass, role,age);

		}
		else if(n==2) {
			System.out.println("Enter your password: ");
			String pass = sc.nextLine();
			System.out.println("Enter your role (ADMIN, EMPLOYEE, CUSTOMER): ");
			String role= sc.nextLine().toUpperCase();
		   
		    LogIn.login(pass,role);
		}
		else
			return;

	}

}
