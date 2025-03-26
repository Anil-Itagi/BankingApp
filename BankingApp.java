import java.sql.*;
import java.util.Scanner;

class BankingApp {
    private static final String url="jdbc:mysql://localhost:3306/bankingApp";
    private static final String username="root";
    private static final String password="appa@123";
    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            Scanner sc = new Scanner(System.in);
            User user = new User(conn, sc);
            Accounts accounts = new Accounts(conn, sc);
            AccountManager accountManager = new AccountManager(conn, sc);
            String email;
            long account_number;

            while (true) {
                System.out.println("*** WELCOME TO BANKING SYSTEM ***");
                System.out.println();
                System.out.println("1 : Register");
                System.out.println("2 : Login");
                System.out.println("3 : Exit");

                System.out.println("Select the choice ...");
                int choice = sc.nextInt();
                switch (choice) {
                    case 1:
                        user.register();
                        System.out.flush();
                        break;
                    
                    case 2:
                        email = user.login();
                        if (email != null) {
                            System.out.println();
                            System.out.println("User logged In ...");
                            if (!accounts.account_exist(email)) {
                                System.out.println();
                                System.out.println("1 : open a new bank account");
                                System.out.println("2 : Exit");
                                if (sc.nextInt() == 1) {
                                    account_number = accounts.open_account(email);
                                    System.out.println("account crated successfully");
                                    System.out.println("Your account number is " + account_number);
                                } else {
                                    break;
                                }
                            }
                        }
                        else {
                            System.out.println("Login failed");
                        }
                
                    case 3:
                        System.out.println("THANK YOU FOR USING BANKING SYSTEM");
                        System.out.println("Exiting System");
                        return;
                        
                    default:
                        System.out.println("Enter the valid choice");
                        break;

                }
            }
        } catch (Exception e) {
             e.printStackTrace();
        }
    }
    
}