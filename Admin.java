package Online_banking_management;
import java.util.*;
public class Admin {
  Scanner sc = new Scanner(System.in);
  AdminFunctionalities admin ;
	public void showAdminMenu() {
		int choice;
		do {
			System.out.println();
			System.out.println("------------------------Admin Menu-----------------------");
			System.out.println("1. Generate today's transactional report*");
			System.out.println("2. Deactivate an employee account*");
			System.out.println("3. update the employee details*");
			System.out.println("4. View all Employee details*");
			System.out.println("5. Reset Password*");
			System.out.println("6. Generate Transaction report*");
			System.out.println("----------------------------------------------------------");
			choice = sc.nextInt();
			sc.nextLine();
			switch(choice) {
			 case 1:{
				 admin = new AdminFunctionalities();
				 admin.generateTdyRepo();
				break;
			  }
			  case 2:{
				  //remove emp
				    admin = new AdminFunctionalities();
				    System.out.println("Do you want to update the name?(y/n):");
					  char ch = sc.next().charAt(0);
					if('N'!=Character.toUpperCase(ch)) {
						sc.nextLine();
				     System.out.println("Enter the name of the employee : ");
				     String name = sc.nextLine();
					 admin.deactivate_an_emp(name);
					}
					break;
				 }
			  case 3:{
				  //update employee det
				  System.out.println("Enter Name of the Employee :");
				  String name = sc.next();
				  System.out.println("Do you want to update the name?(y/n):");
				  char ch = sc.next().charAt(0);
				  sc.nextLine();
				  String uname = name;
				  if('N'!=Character.toUpperCase(ch)) {
					  System.out.println("Enter the updated name : ");
					  sc.nextLine();
					   uname = sc.nextLine();
				  }
				  System.out.println("Enter the updated employee position :");
				  String pos = sc.nextLine().toUpperCase();
				  System.out.println("Enter the updated employee salary :");
				  double sal = sc.nextDouble();
				  System.out.println("Enter the updated employee age :");
				  int age = sc.nextInt();
				  admin = new AdminFunctionalities();
				  admin.update_an_emp(name,uname,age,pos,sal);
			 		break;
				 }
			  case 4:{
				  //view emp
				  admin = new AdminFunctionalities();
					 admin.view_emp_detail();
					break;
				 }
			  case 5:{
				  //updating emp password
				  System.out.println("Are you sure you want to change the password:(y/n)?");
				  char ch = sc.next().charAt(0);
				  sc.nextLine();
				  if('n'!=Character.toLowerCase(ch)) {
					  System.out.println("Do you want to change (ADMIN/EMPLOYEE) ?");
					  String role=sc.nextLine().toUpperCase();
					  if(role.equals("ADMIN") || role.equals("EMPLOYEE")) 
					  {
				      System.out.println("Enter the new password ?");
				      String pass=sc.nextLine();
				  admin = new AdminFunctionalities();
				   admin.change_role_pass(role,pass);
					
						 
				   
				  }else {
					  System.out.println("INVALID CREDENTIALS");
					  return;
				  }
				  }
					break;
				 }
			  case 6:{
				  //generate transactional report
				  
				  admin = new AdminFunctionalities();
					 admin.generateTransactionalReport();
				  
					break;
				 }
			 
			}
			
		}while(choice!=0);
	}
}
