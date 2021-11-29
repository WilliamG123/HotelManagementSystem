import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class DAO_TEST_DRIVER {
    public static void main(String[] args)
            throws SQLException, ClassNotFoundException, NoSuchAlgorithmException {

         User user = new User("Will", "G", "will@gmail.com", "888-000-0000", LocalDate.now(), "password", "CUST");
        UserDAOImplementation userDAO
                = new UserDAOImplementation();

        // add
        userDAO.add(user);

        // read
        User u = userDAO.getUser("will@gmail.com");
        System.out.println(u.getFirstName() + " "
                + u.getLastName() + " "
                + u.getEmail());

        // read All
        List<User> ls = userDAO.getUsers();
        for (User allUsers : ls) {
            System.out.println(allUsers);
        }

        // update
        User tempUser = userDAO.getUser("will@gmail.com");
        tempUser.setFirstName("William");
        userDAO.update(tempUser);

        // delete
        userDAO.delete("will@gmail.com");
    }
}

