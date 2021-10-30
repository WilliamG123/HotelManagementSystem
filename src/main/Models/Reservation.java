import java.sql.Date;
import java.util.List;
import java.time.LocalDate;

public class Reservation {

    private String hotelName;
    private String custId;
    private LocalDate checkIn;
    private LocalDate checkOut; // should change this to a date object or something idk
    private double cost;
    private int adults;
    private int children;
    private int resID;
    private List<Room> rooms;

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }

    public int getAdults() {
        return adults;
    }

    public void setAdults(int adults) {
        this.adults = adults;
    }

    public int getChildren() {
        return children;
    }

    public void setChildren(int children) {
        this.children = children;
    }

    public Reservation(){ }


    // Constructor specifically made for StaffReservation scene do not change
    public Reservation(String custId, String hotelName, LocalDate checkIn, LocalDate checkOut, double cost, int resID) {
        this.hotelName = hotelName;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.cost = cost;
        this.resID = resID;
        this.custId = custId;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "checkIn='" + checkIn + '\'' +
                ", checkOut='" + checkOut + '\'' +
                ", totalCost=" + cost +
                ", adults=" + adults +
                ", children=" + children +
                ", resID='" + resID + '\'' +
                '}';
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getResID() {
        return resID;
    }

    public void setResID(int resID) {
        this.resID = resID;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }
}