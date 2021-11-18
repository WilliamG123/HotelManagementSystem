import javafx.beans.property.*;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Hotels {

    // using ALT + IN key for shortcut
    private final StringProperty hotelname = new SimpleStringProperty();
    private final StringProperty hoteladdr = new SimpleStringProperty();
    private final StringProperty hoteldesc = new SimpleStringProperty();
    private final IntegerProperty rooms = new SimpleIntegerProperty();
    private final IntegerProperty amenities = new SimpleIntegerProperty();
    private final DoubleProperty price = new SimpleDoubleProperty();
    private final IntegerProperty rating = new SimpleIntegerProperty();
    private final StringProperty amenitiesdesc = new SimpleStringProperty();
    private final StringProperty hotelId = new SimpleStringProperty();

    public Hotels(){ }

//Amenities

    public String getAmenitiesdesc() { return amenitiesdesc.get(); }

    public StringProperty amenitiesdescProperty() { return amenitiesdesc; }

    public void setAmenitiesdesc(String amenitiesdesc) { this.amenitiesdesc.set(amenitiesdesc); }


//RATING
    public int getRating() { return rating.get(); }

    public IntegerProperty ratingProperty() { return rating; }

    public void setRating(int rating) { this.rating.set(rating); }



//Hotel Address
    public String getHoteladdr() {
        return hoteladdr.get();
    }

    public StringProperty hoteladdrProperty() {
        return hoteladdr;
    }

    public void setHoteladdr(String hoteladdr) {
        this.hoteladdr.set(hoteladdr);
    }


//Hoteldescription
    public String getHoteldesc() {
        return hoteldesc.get();
    }

    public StringProperty hoteldescProperty() {
        return hoteldesc;
    }

    public void setHoteldesc(String hoteldesc) {
        this.hoteldesc.set(hoteldesc);
    }


//Hotelname
    public String getHotelname() {
        return hotelname.get();
    }

    public StringProperty hotelnameProperty() {
        return hotelname;
    }

    public void setHotelname(String hotelname) {
        this.hotelname.set(hotelname);
    }


//Price
    public double getPrice() {
        return price.get();
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public void setPrice(double price) {
        this.price.set(price);
    }


//Amenities totalAmenities
    public int getAmenities() { return amenities.get(); }

    public IntegerProperty amenitiesProperty() { return amenities; }

    public void setAmenities(int amenities) { this.amenities.set(amenities); }


//total rooms
    public int getRooms() {
        return rooms.get();
    }

    public IntegerProperty roomsProperty() {
        return rooms;
    }

    public void setRooms(int rooms) {
        this.rooms.set(rooms);
    }

// hotel Id
    public String getHotelId() {return hotelId.get();}

    public StringProperty hotelIdProperty() {return hotelId;}

    public void setHotelId(String hotelId) {this.hotelId.set(hotelId);}
}


