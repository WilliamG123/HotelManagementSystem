public class SessionSingleton extends User {


    private static SessionSingleton single_instance = null;

    // Declaring a variable of type String
    public String s;

    // Constructor
    // Here we will be creating private constructor
    // restricted to this class itself
    private SessionSingleton()
    {
        s = getFirstName();
    }

    // Static method
    // Static method to create instance of Singleton class
    public static SessionSingleton getInstance()
    {
        if (single_instance == null)
            single_instance = new SessionSingleton();

        return single_instance;
    }

}
