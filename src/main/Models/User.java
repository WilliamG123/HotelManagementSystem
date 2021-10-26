import java.sql.*;
import java.time.LocalDate;

public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;
    private LocalDate dob;
    private String type;

    public User() {
    }

    public User(String firstName, String lastName, String email, String phoneNumber, LocalDate dob, String password, String type ) {
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



    public static void Customer(String oper, User cust) {
        PreparedStatement ps = null;
        ResultSet result = null;
        try {
            if (oper.equals("add")) {
                DBConnection connect = new DBConnection();
                Connection conn = connect.getConnection();
                ps = conn.prepareStatement("INSERT INTO users(email, password, fname, lname, phone, dob, usertype) values(?,?,?,?,?,?,?)");
                ps.setString(1, cust.getEmail());
                ps.setString(2, cust.getPassword());
                ps.setString(3, cust.getFirstName());
                ps.setString(4, cust.getLastName());
                ps.setString(5, cust.getPhoneNumber());
                ps.setDate(6, Date.valueOf(cust.dob));
                ps.setString(7, cust.getType());

                if (ps.executeUpdate() > 0) {
                    System.out.println("New Customer added");
                    //  JOptionPane.showMessageDialog(null, "New Customer Added");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean isValidEmail(String userEmail) throws SQLException, ClassNotFoundException {
       boolean result = true;
        Statement statement = null;
        String query = "SELECT Count(1) FROM users WHERE BINARY email = ?"; // checking if email match
        DBConnection connectNow = new DBConnection();
        try(Connection conn = connectNow.getConnection()){
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1,userEmail);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getInt(1) == 1) {
                    System.out.println("Email already exist");
                    result = false;
                    }
                }
                }
        return result;
    }



    public int validate(String userEmail, String password) throws SQLException, ClassNotFoundException {
        int result = 0;
        Statement statement = null;
        String query = "SELECT * FROM users WHERE BINARY email = ?"; // checking if usernames match
        DBConnection connectNow = new DBConnection();
        try (Connection conn = connectNow.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1,userEmail);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) { //if username exist
                   type =  rs.getString("usertype");
                if (rs.getString("password").equals(password)) {
                    System.out.println("validation successful ");
                    setEmail(userEmail);
                    setPassword(password);
                    setFirstName(rs.getString("fname"));
                    setLastName(rs.getString("lname"));
                    setPhoneNumber(rs.getString("phone"));
                    setDob(rs.getDate("dob").toLocalDate());
                    LoadedUser.getInstance().init(this);
                    result = 1;
                } else {
                    System.out.println("email exist but incorrect password");
                    System.out.println(rs.getString("password"));
                    System.out.println(rs.getString("email"));
                    result = -1;
                }
            }


            return result;

        }

    }


    //TODO: uncomment this method when properly initializing user in the User.validate() method
    @Override
    public String toString(){
        StringBuilder rep = new StringBuilder("User:\n");
        rep.append("\t");rep.append(getFirstName());rep.append("\n");
        rep.append("\t");rep.append(getLastName());rep.append("\n");
        rep.append("\t");rep.append(getEmail());rep.append("\n");
        rep.append("\t");rep.append(getPhoneNumber());rep.append("\n");
        rep.append("\t");rep.append(getDob().toString());rep.append("\n");
        return rep.toString();
    }

}