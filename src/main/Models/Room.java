import java.time.DayOfWeek;
import java.time.LocalDate;

public class Room {
    private int roomID;
    private int roomNum;
    private int isOccupied;
    private double price;
    private double weekendRate;
    private double weekendPrice;
    private String type;

    public Room(){}

    public Room(int roomNum, double price, String type, int isOccupied, double weekendRate){
        //this.roomID = roomID;
        this.roomNum = roomNum;
        this.price = price;
        this.type = type;
        this.weekendRate = weekendRate;
        this.weekendPrice = this.price * (1.0+this.weekendRate);
        this.isOccupied = isOccupied;
    }

    public String getType(){
        return this.type;
    }

    public void setType(String type){
        this.type = type;
    }

    public void setPrice(double price){
        this.price = price;
    }

    public void setWeekendRate(double weekendRate){
        this.weekendRate = weekendRate;
    }

    public void setRoomID(int id){this.roomID = id;}

    public void setRoomNum(int num){this.roomNum = num;}

    public void setIsOccupied(int occupied){
        this.isOccupied = occupied;
    }

    public double getWeekendPrice(){
        return this.weekendPrice;
    }

    public double getPrice(){
        return this.price;
    }

    public int getRoomID(){
        return this.roomID;
    }

    public int getRoomNum(){
        return this.roomNum;
    }

    public int getIsOccupied(){return this.isOccupied;}

    public void recalculateWeekendPrice(){
        this.weekendPrice = this.price * (1.0+this.weekendRate);
    }

    @Override
    public String toString(){
        return "Room{" + "\n\tType: " + this.type +
                "\n\tRoom Number: " + this.roomNum +
                "\n\tBase Price: " + this.price +
                "\n\tWeekend Rate: " + this.weekendRate +
                "\n\tWeekend Price: " + this.weekendPrice +
                "\n\tOccupied: " + ((this.isOccupied == 0) ? "No" : "Yes") +
                "\n}";
    }
}
