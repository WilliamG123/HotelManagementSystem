import java.util.List;
/*
* Hotel class is our model to store all data on each hotel from the database
* Getters & Setters for all class variables except reservations list
*
 */
public class Hotel {

    private String name;
    private String description;
    private String address;
    private int rating;
    private List<String> amenities;
    private List<Room> rooms;
    private double weekendRate;
    private List<Reservation> reservations; // not sure how necessary this is

    // Constructor has all class variable except for reservations list
    public Hotel(String name, String description, String address, int rating, List<String> amenities, List<Room> rooms, double weekendRate) {
        this.name = name;
        this.description = description;
        this.address = address;
        this.rating = rating;
        this.amenities = amenities;
        this.rooms = rooms;
        this.weekendRate = weekendRate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<String> amenities) {
        this.amenities = amenities;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public double getWeekendRate() {
        return weekendRate;
    }

    public void setWeekendRate(double weekendRate) {
        this.weekendRate = weekendRate;
    }
}
