public class LoadedUser {
    private static User loggedInUser = null;

    private LoadedUser(){}

    public boolean init(User user){
        if(loggedInUser == null){
            loggedInUser = user;
            return true;
        }
        return false;
    }

    public static LoadedUser getInstance(){
        return new LoadedUser();
    }
}
