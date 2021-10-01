import java.util.List;

public class Reservation {

    private String hotelName;
    private String dates; // should change this to a date object or something idk
    private double totalCost;
    private String userEmail;
    private String resID;
    private List<Room> rooms;

    public Reservation(String hotelName, String dates, double totalCost, String userEmail, String resID, List<Room> rooms) {
        this.hotelName = hotelName;
        this.dates = dates;
        this.totalCost = totalCost;
        this.userEmail = userEmail;
        this.resID = resID;
        this.rooms = rooms;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getResID() {
        return resID;
    }

    public void setResID(String resID) {
        this.resID = resID;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }
}
