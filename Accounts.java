import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Accounts {


    private Connection conn;
    private Scanner sc;
   
    public Accounts(Connection conn, Scanner sc) {
        this.conn = conn;
        this.sc = sc;
    }

    public long open_account(String email) {

        if (!account_exist(email)) {
            String query = "insert into accounts(full_name ,email,balance , security_pin) values (?,?,?,?)";

            System.out.print("Enter the Full Name : ");
            String fullName = sc.next();
            // System.out.print("Enter the Email : ");
            // String email = sc.nextLine();

            System.out.print("Enter the balance amount : ");
            double balance = sc.nextDouble();

            System.out.print("Enter the 4 Digit security_pin :");
            String security_pin = sc.next();
         
            try {
                PreparedStatement ps = conn.prepareStatement(query);

                ps.setString(1, fullName);
                ps.setString(2, email);
                ps.setDouble(3, balance);
                ps.setString(4, security_pin);

                int affectedRows = ps.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Account created successfully");
                    return get_account_number(email);

                } else {
                    System.out.println("Something went wrong in creation of account");
                }

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Something went wrong" + e.getMessage());
                return (long) 0;

            }
        }
        return (long) 0;

    }
    
    public boolean account_exist(String email) {

        String query = "select * from accounts where email =? ";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, email);
            ResultSet res = ps.executeQuery();
            return res.next();

        } catch (Exception e) {
            System.out.println("Error in finding account existing or not" + e.getMessage());
            return false;
        }

    }
    

    public long get_account_number(String email) {


        System.out.println("Email from "+ email);
        String query = "select account_number from accounts where email= ?;";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, email);
            ResultSet res = ps.executeQuery();
            if (res.next()) {

                long accountNumber = res.getLong("account_number");
                return accountNumber;
            }
            else {
                System.out.println("Email is not valid");
                return 0;
            }
        } catch (Exception e) {
            System.out.println("error"+e.getMessage());
            return 0;
        }

    }
    

}
