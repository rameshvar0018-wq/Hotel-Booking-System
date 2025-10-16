package model;

public class Room {
    private int roomId;
    private String roomNumber;
    private String type;
    private double price;
    private String status;

    public Room(int roomId, String roomNumber, String type, double price, String status) {
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.type = type;
        this.price = price;
        this.status = status;
    }

    public int getRoomId() { return roomId; }
    public String getRoomNumber() { return roomNumber; }
    public String getType() { return type; }
    public double getPrice() { return price; }
    public String getStatus() { return status; }
}

