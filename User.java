import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;


public class User {
    private Connection conn;
    private Scanner sc;

    public User(Connection conn, Scanner sc) {
        this.conn = conn;
        this.sc = sc;
    }

    public void register() {
        sc.nextLine();
        System.out.print("Full Name : ");
        String fullname = sc.nextLine();
        System.out.print("Email :");
        String email = sc.nextLine();
        System.out.print("Password : ");
        String password = sc.nextLine();

        if (user_exist(email)) {
            System.out.println("User already registerd ");
            return;

        }

        String register_query = "Insert into User(full_name,email,password) values (?,?,?)";
        try {

            PreparedStatement preparedStatement = conn.prepareStatement(register_query);
            preparedStatement.setString(1, fullname);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Registration successful");
            } else {
                System.out.println("Registration failed");
            }
        } catch (Exception e) {
            System.out.println("Registration failed" + e.getMessage());
        }

    }
    
    public  boolean user_exist(String email) {
        String query="select * from user where email=?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(query);
               preparedStatement.setString(1,email);
               ResultSet res=preparedStatement.executeQuery();
               return res.next(); 
     
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String login() {
        sc.nextLine();
        System.out.print("Email : ");
        String email = sc.nextLine();
        System.out.println("Password");
        String password = sc.nextLine();
        String query = "select * from user where email=? and password=?";
        try {

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet res = ps.executeQuery();
            if (res.next()) {
                // System.out.println(res+" login successful in login method");
                return email;
            } else {
                return null;
            }

        } catch (Exception e) {
            System.out.println("Login successfull"+e.getMessage());
        }
        return null;

    }
    
}
