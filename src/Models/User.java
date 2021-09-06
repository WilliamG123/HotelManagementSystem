import java.sql.*;
import java.time.LocalDate;

public class User {
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;
    private LocalDate dob;
    private String type;

    public User() {
    }

    public User(String userName, String firstName, String lastName, String email, String phoneNumber, LocalDate dob, String password, String type ) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dob = dob;
        this.password = password;
        this.type =  type;
    }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    public void Customer(String oper, User cust) {
        PreparedStatement ps = null;
        ResultSet result = null;
        try {
            if (oper == "add") {
                DBConnection connect = new DBConnection();
                Connection conn = connect.getConnection();
                ps = conn.prepareStatement("INSERT INTO  users(firstname, lastName, email, phoneNumber, dob) VALUES (?,?,?,?,?)");
                ps.setString(1, cust.firstName);
                ps.setString(2, cust.lastName);
                ps.setString(3, cust.email);
                ps.setString(4, cust.phoneNumber);
                ps.setDate(4, Date.valueOf(cust.dob));

                if (ps.executeUpdate() > 0) {
                    System.out.println("New Customer added");
                    //  JOptionPane.showMessageDialog(null, "New Customer Added");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



    public int validate(String userName, String password) throws SQLException {
        int result = 0;
        Statement statement = null;
        String query = "SELECT * FROM users WHERE BINARY username = ?"; // checking if usernames match
        DBConnection connectNow = new DBConnection();
        try (Connection conn = connectNow.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1,userName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) { //if username exist
                   type =  rs.getString("usertype");
                if (rs.getString("password").equals(password)) {
                    System.out.println("validation successful ");
                    setUserName(userName);
                    setPassword(password);
                    result = 1;
                } else {
                    System.out.println("username exist but incorrect password");
                    System.out.println(rs.getString("password"));
                    System.out.println(rs.getString("username"));
                    result = -1;
                }
            }


            return result;

        }

    }
}