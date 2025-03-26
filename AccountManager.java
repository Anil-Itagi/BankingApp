import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {
    
    private Connection conn;
    private Scanner sc;

    AccountManager(Connection conn, Scanner sc) {
        this.conn = conn;
        this.sc = sc;
    }

    public void creadit_many(long account_number) throws SQLException {
        sc.nextLine();
        System.out.print("Enter Amount : ");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.print("Enter the Email address : ");
        String email = sc.nextLine();
        System.out.print("Enter the security pin ");
        String security_pin = sc.nextLine();

        try {
            conn.setAutoCommit(false);
            if (account_number != 0) {
                PreparedStatement ps = conn.prepareStatement("select * from accounts where security_pin=? and email=?");
                ps.setString(1, security_pin);
                ps.setString(2, email);
                ResultSet res = ps.executeQuery();

                if (res.next()) {
                    String credit_query = "Update accounts set balance=balance+ ? where account_number=?";
                    PreparedStatement ps1 = conn.prepareStatement(credit_query);
                    long accountNumber=res.getLong("account_number");
                    ps1.setDouble(1, amount);
                    ps1.setLong(2, accountNumber);
                    int affectedRows = ps1.executeUpdate();
                    if (affectedRows > 0) {
                        System.out.println("Amount credited succssfully");
                    }
                    else {
                        System.out.println("Amount credit failed");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Amount credition failed"+e.getMessage());
   }

    }
}
