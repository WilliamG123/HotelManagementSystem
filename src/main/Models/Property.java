import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Property {
    private String propertyName;
    private String desc;
    private String address;
    private ArrayList<String> amenities;
    private ArrayList<Room> rooms;
    private ArrayList<Reservation> reservations;
    private HashMap<String, Integer> roomTypes;
    private int numberRooms;
    private int numberAmenities;

    public Property(String propertyName, String desc, String address, String[] amenities, ArrayList<Room> rooms, ArrayList<Reservation> reservations){
        this.propertyName = propertyName;
        this.desc = desc;
        this.address = address;
        this.amenities = new ArrayList<>(Arrays.asList(amenities));
        this.rooms = rooms; //new ArrayList<>(Arrays.asList(rooms));
        this.reservations = reservations; //new ArrayList<>(reservations);
        this.roomTypes = new HashMap<>();
        for(Room room : this.rooms){
            int count = this.roomTypes.getOrDefault(room.getType(), 0);
            this.roomTypes.put(room.getType(), count+1);
        }
        this.numberRooms = rooms.size();
        this.numberAmenities = amenities.length;
    }

    public ArrayList<String> getAmenities(){
        return this.amenities;
    }

    public ArrayList<Room> getRooms(){
        return this.rooms;
    }

    public ArrayList<Reservation> getReservations(){
        return this.reservations;
    }

    public HashMap<String, Integer> getRoomTypes(){
        return this.roomTypes;
    }

    public String getPropertyName(){
        return this.propertyName;
    }

    public String getDesc(){
        return this.desc;
    }

    public String getAddress(){
        return this.address;
    }

    public int getNumberRooms(){
        return this.numberRooms;
    }

    public int getNumberAmenities(){
        return this.numberAmenities;
    }

    public void setAmenities(ArrayList<String> amenities){
        this.amenities = amenities;
    }

    public void setRooms(ArrayList<Room> rooms){
        this.rooms = rooms;
    }

    public void setReservations(ArrayList<Reservation> reservations){
        this.reservations = reservations;
    }

    public void setRoomTypes(HashMap<String, Integer> roomTypes){
        this.roomTypes = roomTypes;
    }

    public void setPropertyName(String propertyName){
        this.propertyName = propertyName;
    }

    public void setDesc(String desc){
        this.desc = desc;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public void addRoom(Room room){
        this.rooms.add(room);
        int count = this.roomTypes.getOrDefault(room.getType(), 0);
        this.roomTypes.put(room.getType(), count+1);
        this.numberRooms++;
    }

    public void addAmenity(String amenity){
        this.amenities.add(amenity);
        this.numberAmenities++;
    }

    public void addReservation(Reservation reservation){
        this.reservations.add(reservation);
    }

    public void removeRoom(Room room){
        if(this.rooms.contains(room) && this.roomTypes.containsKey(room.getType())){
            this.rooms.remove(room);
            int count = this.roomTypes.get(room.getType());
            this.roomTypes.put(room.getType(), count -1);
            this.numberRooms--;
        }
    }

    public void removeAmenity(String amenity){
        this.amenities.remove(amenity);
        this.numberAmenities--;
    }

    public void removeReservation(Reservation reservation){
        this.reservations.remove(reservation);
    }
}
