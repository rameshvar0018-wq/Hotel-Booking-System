package dao;
import model.Room;
import java.sql.*;
import java.util.*;
import java.sql.Date;
public class RoomDAO {
    public List<Room> getAllRooms() {
            List<Room> list = new ArrayList<>();
            try(Connection con = DBConnection.getConnection()) {
                PreparedStatement ps = con.prepareStatement("SELECT * FROM rooms");
                ResultSet rs = ps.executeQuery();
                while(rs.next()) {
                    list.add(new Room(
                            rs.getInt("room_id"),
                            rs.getString("room_number"),
                            rs.getString("type"),
                            rs.getDouble("price"),
                            rs.getString("status")
                    ));
                }
            } catch(Exception e) { e.printStackTrace(); }
            return list;
        }

        public boolean addRoom(String roomNo, String type, double price) {
            try(Connection con = DBConnection.getConnection()) {
                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO rooms(room_number,type,price) VALUES(?,?,?)"
                );
                ps.setString(1, roomNo); ps.setString(2,type); ps.setDouble(3,price);
                ps.executeUpdate();
                return true;
            } catch(Exception e) { e.printStackTrace(); return false; }
        }

        public boolean updateRoom(int roomId, String type, double price, String status) {
            try(Connection con = DBConnection.getConnection()) {
                PreparedStatement ps = con.prepareStatement(
                        "UPDATE rooms SET type=?, price=?, status=? WHERE room_id=?"
                );
                ps.setString(1,type); ps.setDouble(2,price); ps.setString(3,status); ps.setInt(4,roomId);
                ps.executeUpdate();
                return true;
            } catch(Exception e) { e.printStackTrace(); return false; }
        }

        public boolean removeRoom(int roomId) {
            try(Connection con = DBConnection.getConnection()) {
                PreparedStatement ps = con.prepareStatement("DELETE FROM rooms WHERE room_id=?");
                ps.setInt(1,roomId);
                ps.executeUpdate();
                return true;
            } catch(Exception e) { e.printStackTrace(); return false; }
        }

        public boolean bookRoom(int roomId, String userName, String checkIn, String checkOut) {
            try(Connection con = DBConnection.getConnection()) {
                PreparedStatement ps1 = con.prepareStatement(
                        "INSERT INTO bookings(user_name, room_id, check_in, check_out) VALUES(?,?,?,?)"
                );
                ps1.setString(1,userName); ps1.setInt(2,roomId);
                ps1.setDate(3, Date.valueOf(checkIn)); ps1.setDate(4, Date.valueOf(checkOut));
                ps1.executeUpdate();

                PreparedStatement ps2 = con.prepareStatement(
                        "UPDATE rooms SET status='Booked' WHERE room_id=?"
                );
                ps2.setInt(1, roomId); ps2.executeUpdate();

                return true;
            } catch(Exception e) { e.printStackTrace(); return false; }
        }

        public boolean cancelBooking(int bookingId) {
            try(Connection con = DBConnection.getConnection()) {
                // Get room_id
                PreparedStatement ps = con.prepareStatement("SELECT room_id FROM bookings WHERE booking_id=?");
                ps.setInt(1, bookingId);
                ResultSet rs = ps.executeQuery();
                int roomId = 0;
                if(rs.next()) roomId = rs.getInt("room_id");

                // Delete booking
                PreparedStatement ps2 = con.prepareStatement("DELETE FROM bookings WHERE booking_id=?");
                ps2.setInt(1, bookingId);
                ps2.executeUpdate();

                // Update room status
                PreparedStatement ps3 = con.prepareStatement("UPDATE rooms SET status='Available' WHERE room_id=?");
                ps3.setInt(1, roomId);
                ps3.executeUpdate();

                return true;
            } catch(Exception e) { e.printStackTrace(); return false; }
        }

        public List<String[]> getAllBookings() {
            List<String[]> list = new ArrayList<>();
            try(Connection con = DBConnection.getConnection()) {
                PreparedStatement ps = con.prepareStatement(
                        "SELECT b.booking_id,b.user_name,r.room_number,b.check_in,b.check_out FROM bookings b JOIN rooms r ON b.room_id=r.room_id"
                );
                ResultSet rs = ps.executeQuery();
                while(rs.next()) {
                    list.add(new String[]{
                            String.valueOf(rs.getInt("booking_id")),
                            rs.getString("user_name"),
                            rs.getString("room_number"),
                            rs.getDate("check_in").toString(),
                            rs.getDate("check_out").toString()
                    });
                }
            } catch(Exception e){ e.printStackTrace(); }
            return list;
        }
    }
