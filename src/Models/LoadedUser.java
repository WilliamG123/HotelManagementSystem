public class LoadedUser {
    private static final LoadedUser instance = new LoadedUser();
    private static User loggedInUser = null;

    private LoadedUser(){}

    public void init(User user){
        if(loggedInUser == null){
            loggedInUser = user;
        }
    }

    public User getUser(){
        return loggedInUser;
    }

    public void resetUser(){
        loggedInUser = null;
    }

    public static LoadedUser getInstance(){
        return instance;
    }
}
