

import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;




public class UserDAOImplementation extends DBConnection
        implements UserDAO {

    Connection con = getConnection();

    public UserDAOImplementation() throws ClassNotFoundException {
    }

    @Override
    public int add(User user)
            throws SQLException
    {

        String query
                = "insert into users(email,password,fname,lname,phone,dob,usertype) "
                + " VALUES (?, ?, ?, ?, ?, ?,?)";
        PreparedStatement ps
                = con.prepareStatement(query);
        ps.setString(1, user.getEmail());
        ps.setString(2, user.getPassword());
        ps.setString(3, user.getFirstName());
        ps.setString(4, user.getLastName());
        ps.setString(5, user.getPhoneNumber());
        ps.setDate(6, Date.valueOf(user.getDob()));
        ps.setString(7, user.getType());
        int n = ps.executeUpdate();
        return n;
    }

    @Override
    public void delete(int id)
            throws SQLException
    {
        String query
                = "delete from employee where emp_id =?";
        PreparedStatement ps
                = con.prepareStatement(query);
        ps.setInt(1, id);
        ps.executeUpdate();
    }



    @Override
    public User getUser(String EMAIL)
            throws SQLException, NoSuchAlgorithmException {

        String query
                = "select * from users where email= ?";
        PreparedStatement ps
                = con.prepareStatement(query);

        ps.setString(1, EMAIL);
        User user = new User();
        ResultSet rs = ps.executeQuery();
        boolean check = false;

        while (rs.next()) {
            check = true;
            user.setFirstName(rs.getString("fname"));
            user.setLastName(rs.getString("lname"));
            user.setEmail(rs.getString("email"));
            user.setPhoneNumber(rs.getString("phone"));
            user.setDob(rs.getDate("dob").toLocalDate());
            user.setType(rs.getString("usertype"));
            user.setPassword(Hasher.getInstance("SHA-256").hash(rs.getString("password")));
        }

        if (check == true) {
            return user;
        }
        else
            return null;
    }

    @Override
    public List<User> getUsers()
            throws SQLException, NoSuchAlgorithmException {
        String query = "select * from users";
        PreparedStatement ps
                = con.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        List<User> ls = new ArrayList();

        while (rs.next()) {
            User user = new User();
            user.setFirstName(rs.getString("fname"));
            user.setLastName(rs.getString("lname"));
            user.setEmail(rs.getString("email"));
            user.setPhoneNumber(rs.getString("phone"));
            user.setDob(rs.getDate("dob").toLocalDate());
            user.setType(rs.getString("usertype"));
            user.setPassword(Hasher.getInstance("SHA-256").hash(rs.getString("password")));
            ls.add(user);
        }
        return ls;
    }

    @Override
    public void update(User user)
            throws SQLException
    {
        int userID = 0;
        CallableStatement cs = con.prepareCall("{call hotel.getID(?,?)}");
        cs.setString(1, user.getFirstName());
        cs.setString(2, user.getType());
        ResultSet rs = cs.executeQuery();
        while(rs.next()){
            userID = rs.getInt("userId");
        }
        String query
                = "update users set password = ?, fname=?, "
                + " lname= ?  ,email = ?,  phone = ?, dob = ?, usertype = ? where userId = ?";

        PreparedStatement ps
                = con.prepareStatement(query);
        ps.setString(1, user.getPassword());
        ps.setString(2, user.getFirstName());
        ps.setString(3, user.getLastName());
        ps.setString(4, user.getEmail());
        ps.setString(5, user.getPhoneNumber());
        ps.setDate(6, Date.valueOf(user.getDob()));
        ps.setString(7, user.getType());
        ps.setInt(8, userID);


        ps.executeUpdate();
    }
}
