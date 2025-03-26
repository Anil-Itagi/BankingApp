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

                            account_number = accounts.get_account_number(email);
                            int choice2 = 0;
                            while (choice2 != 5) {
                                System.out.println();
                                System.out.println("1. Debit Money");
                                System.out.println("2. Credit Money");
                                System.out.println("3. Transfer Money");
                                System.out.println("4. Check Balance");
                                System.out.println("5. Log Out");
                                System.out.println("Enter your chioce : ");

                                choice2 = sc.nextInt();
                                switch (choice2) {
                                    case 1:
                                        accountManager.debit_maney(account_number);
                                        break;
                                    case 2:
                                        accountManager.creadit_many(account_number);
                                        break;
                                    case 3:
                                        accountManager.transfer_money(account_number);
                                        break;
                                    case 4:
                                        accountManager.getBalance(account_number);
                                        break;
                                    case 5:
                                        break;
                                
                                    default:
                                        System.out.println("Enter the valid choice");
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