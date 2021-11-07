import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Property {
    private int propertyId;
    private String propertyName;
    private String desc;
    private String address;
    private ArrayList<String> amenities;
    private ArrayList<Room> rooms;
    private ArrayList<Reservation> reservations;
    private HashMap<String, Integer> roomTypes;
    private int numberRooms;
    private int numberAmenities;
    private int rating;
    private int numberAvailableRooms;

    public Property(){
        this.amenities = new ArrayList<>();
        this.reservations = new ArrayList<>();
        this.roomTypes = new HashMap<>();
        this.rooms = new ArrayList<>();
    }

    public Property(String propertyName, String desc, String address, String[] amenities, ArrayList<Room> rooms, ArrayList<Reservation> reservations, int rating, int numberAvailableRooms){
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
        this.rating = rating;
        this.numberAvailableRooms = numberAvailableRooms;
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

    public int getRating(){return this.rating;}

    public int getNumberAvailableRooms(){return this.numberAvailableRooms;}

    public void setAmenities(ArrayList<String> amenities){
        this.amenities = amenities;
        this.numberAmenities = amenities.size();
    }

    public void setRooms(ArrayList<Room> rooms){
        this.rooms = rooms;
        this.numberRooms = rooms.size();
        int avail = 0;
        for(Room room : this.rooms){
            int count = this.roomTypes.getOrDefault(room.getType(), 0);
            this.roomTypes.put(room.getType(), count+1);
            if(room.getIsOccupied() == 0){
                avail++;
            }
        }
        this.numberAvailableRooms = avail;
    }

    public void setRating(int rating){
        this.rating = rating;
    }

    public void setNumberAvailableRooms(int availableRooms){
        this.numberAvailableRooms = availableRooms;
    }

    public void setNumberRooms(int numberRooms){this.numberRooms = numberRooms;}

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

    public void setPropertyId(int propertyId){
        this.propertyId = propertyId;
    }

    public int getPropertyId(){
        return this.propertyId;
    }

    public void addRoom(Room room){
        this.rooms.add(room);
        int count = this.roomTypes.getOrDefault(room.getType(), 0);
        this.roomTypes.put(room.getType(), count+1);
        this.numberRooms++;
        if(room.getIsOccupied() == 0){
            this.numberAvailableRooms++;
        }
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
            if(room.getIsOccupied() == 0){
                this.numberAvailableRooms--;
            }
        }
    }

    public void removeAmenity(String amenity){
        this.amenities.remove(amenity);
        this.numberAmenities--;
    }

    public void removeReservation(Reservation reservation){
        this.reservations.remove(reservation);
    }

    @Override
    public String toString(){
        StringBuilder rep = new StringBuilder(this.propertyName);
        rep.append("{\n\tAddress: ");rep.append(this.address);
        rep.append("\n\tDescription: "); rep.append(this.desc);
        rep.append("\n\tNumber of Rooms: "); rep.append(this.numberRooms);
        rep.append("\n\t"); for(Room room : this.rooms){rep.append(room);rep.append("\n\t");}
        rep.append("\n\tNumber of amenities: "); rep.append(this.numberAmenities);
        rep.append("\n\tAmenities{"); for(String amenity : amenities){rep.append("\n\t");rep.append(amenity);}rep.append("\n}");
        rep.append("\n\tNumber of Reservations: ");rep.append(this.reservations.size());
        rep.append("\n\t");
        for(Reservation reservation : this.reservations) {
            rep.append(reservation);
            rep.append("\n\t");
        }
        rep.append("\n}");
        return rep.toString();
    }
}
