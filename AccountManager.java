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
        
        System.out.print("Enter the security pin ");
        String security_pin = sc.nextLine();

        try {
            conn.setAutoCommit(false);
            if (account_number != 0) {
                PreparedStatement ps = conn.prepareStatement("select * from accounts where security_pin=? and account_number=?");
                ps.setString(1, security_pin);
                ps.setLong(2, account_number);
                ResultSet res = ps.executeQuery();

                if (res.next()) {
                    String credit_query = "Update accounts set balance=balance+ ? where account_number=?";
                    PreparedStatement ps1 = conn.prepareStatement(credit_query);
                    long accountNumber=res.getLong("account_number");
                    ps1.setDouble(1, amount);
                    ps1.setLong(2, accountNumber);
                    int affectedRows = ps1.executeUpdate();
                    if (affectedRows > 0) {
                        System.out.println("Rs "+amount+"Amount credited succssfully");
                        conn.commit();
                        conn.setAutoCommit(true);
                    }
                    else {
                        System.out.println("Amount credit failed");
                        conn.rollback();
                        conn.setAutoCommit(true);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Amount credition failed"+e.getMessage());
   }
   conn.setAutoCommit(true);

    }

    public void debit_maney(long account_number) throws SQLException {
        sc.nextLine();
        System.out.println("Enter amount : ");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.println("Enter the security Pin :  ");
        String security_pin = sc.nextLine();

        try {
            conn.setAutoCommit(false);
            if (account_number != 0) {
                PreparedStatement ps = conn
                        .prepareStatement("select * from accounts where security_pin=? and account_number=?");
                ps.setString(1, security_pin);
                ps.setLong(2, account_number);

                ResultSet res = ps.executeQuery();

                if (res.next()) {
                    double current_balance = res.getDouble("balance");
                    if (amount <= current_balance) {
                        String debit_query = "update accounts set balance =balance - ? where account_number=?";
                        PreparedStatement ps1 = conn.prepareStatement(debit_query);
                        ps1.setDouble(1, amount);
                        ps1.setLong(2, account_number);
                        int affectedRows = ps1.executeUpdate();
                        if (affectedRows > 0) {
                            System.out.println("Rs " + amount + " debited successfully");
                            conn.commit();
                            conn.setAutoCommit(true);
                            return;

                        } else {
                            System.out.println("Transaction failed");
                            conn.rollback();
                            conn.setAutoCommit(true);
                            return;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Something went wrong " + e.getMessage());
        }
        conn.setAutoCommit(true);
    }
    

    public void getBalance(long account_number) {
        sc.nextLine();
        System.out.println("Enter the Security Pin : ");
        String security_pin = sc.nextLine();

        try {
            PreparedStatement ps = conn
                    .prepareStatement("select balance from accounts where account_number=? and security_pin=?");
            ps.setLong(1, account_number);
            ps.setString(2, security_pin);
            ResultSet res = ps.executeQuery();

            if (res.next()) {
                double balance = res.getDouble("balance");
                System.out.println("Balance is : " + balance);
            } else {
                System.out.println("Invalid Pin !!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

    public void transfer_money(long sender_account_number) throws SQLException {
        sc.nextLine();
        System.out.print("Enter the receiver account number : ");
        long receiver_account_number = sc.nextLong();
        System.out.print("Enter the amount to transfer : ");
        double amount = sc.nextDouble();
        System.out.print("Enter the security pin : ");
        String security_pin = sc.nextLine();

        try {
            conn.setAutoCommit(false);
            if (sender_account_number != 0 && receiver_account_number != 0) {
                PreparedStatement ps = conn
                        .prepareStatement("select * from accounts where account_number =? and security_pin=?");
                ps.setLong(1, sender_account_number);
                ps.setString(2, security_pin);
                ResultSet res = ps.executeQuery();
                if (res.next()) {

                    double current_balance = res.getDouble("balance");
                    if (current_balance >= amount) {
                        String debit_query = "update accounts set balance=balance -? where account_number=?";
                        String credit_query = "update accounts set balance=balance+ ? where account_number= ?";

                        PreparedStatement ps1 = conn.prepareStatement(debit_query);
                        PreparedStatement ps2 = conn.prepareStatement(credit_query);

                        ps1.setDouble(1, amount);
                        ps1.setLong(2, sender_account_number);

                        int affectedRows1 = ps1.executeUpdate();

                        ps2.setDouble(1, amount);
                        ps2.setLong(2, receiver_account_number);

                        int affectedRows2 = ps2.executeUpdate();

                        if (affectedRows1 > 0 && affectedRows2 > 0) {
                            System.out.println("Rs " + amount + " Transfer successfull");
                            conn.commit();
                            conn.setAutoCommit(true);

                        } else {
                            System.out.println("Something went wrong at Transfering the money");
                            conn.rollback();
                            conn.setAutoCommit(true);
                        }
                    }
                } else {
                    System.out.println("Invalid Security pin");
                }
                
            } else {
                System.out.println("Invalid account number");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        conn.setAutoCommit(true);
    }
}
