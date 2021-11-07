import java.util.HashMap;

public class RoomInformation {
    private final HashMap<String, String> roomTypeDesc;
    private final HashMap<String, Double> roomTypeRate;
    private final HashMap<String, String> roomAmenities;

    public RoomInformation(){
        roomTypeDesc = new HashMap<>();
        roomTypeRate = new HashMap<>();
        roomAmenities = new HashMap<>();
        populateMaps();
    }

    private void populateMaps(){
        this.roomTypeDesc.put("Standard", "");
        this.roomTypeDesc.put("Queen", "");
        this.roomTypeDesc.put("King", "");

        this.roomTypeRate.put("Standard", 0.0);
        this.roomTypeRate.put("Queen", 0.0);
        this.roomTypeRate.put("King", 0.0);

        this.roomAmenities.put("Standard", "");
        this.roomAmenities.put("Queen", "");
        this.roomAmenities.put("King", "");
    }

    public String getTypeDesc(String room){
        return this.roomTypeDesc.get(room);
    }

    public Double getRoomRate(String room){
        return this.roomTypeRate.get(room);
    }

    public String getRoomAmenities(String room){
        return this.roomAmenities.get(room);
    }
}
