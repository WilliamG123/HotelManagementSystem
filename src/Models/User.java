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

    public User() {
    }

    public User(String userName, String firstName, String lastName, String email, String phoneNumber, LocalDate dob, String password) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dob = dob;
        this.password = password;
    }

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


    /**
     * This method is not complete just testing phase but will work as table is implemented
     * @param oper takes in operation add, delete, insert
     * @param cust object that is defined from constructor
     */
    public void Customer(String oper, User cust) {
        PreparedStatement ps = null;
        ResultSet result = null;
        try {
            if (oper == "add") {
                DBConnection connect = new DBConnection();
                Connection conn = connect.getConnection();
                ps = conn.prepareStatement("INSERT INTO customer (firstname, lastName, email, phoneNumber, dob) VALUES (?,?,?,?,?)");
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


    /**Under Construction
     * Method to validate credentials of the user.  Used both in login and signup events
     * queries the userid (PK) if there is one
     * queries the count of usernames for verification
     * @param userName <-- of customer
     * @param password
     * @return will return true if there exist a user already or false if does not exist
     * @throws SQLException
     */
    public boolean validate(String userName, String password) throws SQLException {
        boolean result = false;
        Statement statement = null;
        DBConnection connectNow = new DBConnection();
        try (Connection connectDB = connectNow.getConnection()) {
            String verifyUserId = "SELECT userid FROM customer WHERE username= '" + userName + "' AND Password = '" + password + "'";
            String verifyUsername = "SELECT Count(1) FROM customer WHERE username = '" + userName + "' AND Password= '" + password + "'";

            PreparedStatement st = connectDB.prepareStatement(verifyUsername);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                if (rs.getInt(1) == 1) {
                    System.out.println("username & password exist");
                    setUserName(userName);
                    setPassword(password);
                    result = true;
                } else {

                    System.out.println("no such username & password exist");
                }
            }
            PreparedStatement st1 = connectDB.prepareStatement(verifyUserId);
            ResultSet rs2 = st1.executeQuery();
            while (rs2.next()) {
                if (rs.getInt(1) == 1) {
                    System.out.println("userid exist");
                } else {
                    System.out.println("no such userid exist");
                }
            }

            return result;

        }

    }
}