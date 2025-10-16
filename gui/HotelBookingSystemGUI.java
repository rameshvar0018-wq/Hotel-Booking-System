package gui;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;
import dao.RoomDAO;
import model.Room;
    public class HotelBookingSystemGUI extends JFrame {
        RoomDAO dao = new RoomDAO();

        JTable roomTable, bookingTable;
        DefaultTableModel roomModel, bookingModel;
        JTextField nameField, checkInField, checkOutField;
        JButton bookBtn, cancelBtn, addRoomBtn, updateRoomBtn, removeRoomBtn;

        public HotelBookingSystemGUI() {
            setTitle("Hotel Booking System");
            setSize(900,500);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setLayout(new BorderLayout());

            JTabbedPane tabs = new JTabbedPane();

            // --- Rooms Panel ---
            JPanel roomPanel = new JPanel(new BorderLayout());
            roomModel = new DefaultTableModel(new String[]{"ID","No","Type","Price","Status"},0);
            roomTable = new JTable(roomModel);
            roomPanel.add(new JScrollPane(roomTable), BorderLayout.CENTER);

            JPanel roomBtnPanel = new JPanel();
            addRoomBtn = new JButton("Add Room");
            updateRoomBtn = new JButton("Update Room");
            removeRoomBtn = new JButton("Remove Room");
            roomBtnPanel.add(addRoomBtn); roomBtnPanel.add(updateRoomBtn); roomBtnPanel.add(removeRoomBtn);
            roomPanel.add(roomBtnPanel, BorderLayout.SOUTH);
            tabs.add("Rooms", roomPanel);

            // --- Booking Panel ---
            JPanel bookingPanel = new JPanel(new BorderLayout());
            bookingModel = new DefaultTableModel(new String[]{"BookingID","Name","RoomNo","CheckIn","CheckOut"},0);
            bookingTable = new JTable(bookingModel);
            bookingPanel.add(new JScrollPane(bookingTable), BorderLayout.CENTER);

            JPanel bookPanel = new JPanel();
            bookPanel.add(new JLabel("Name:")); nameField = new JTextField(10); bookPanel.add(nameField);
            bookPanel.add(new JLabel("Check-in (yyyy-mm-dd):")); checkInField = new JTextField(10); bookPanel.add(checkInField);
            bookPanel.add(new JLabel("Check-out (yyyy-mm-dd):")); checkOutField = new JTextField(10); bookPanel.add(checkOutField);
            bookBtn = new JButton("Book Room"); cancelBtn = new JButton("Cancel Booking");
            bookPanel.add(bookBtn); bookPanel.add(cancelBtn);
            bookingPanel.add(bookPanel, BorderLayout.SOUTH);
            tabs.add("Bookings", bookingPanel);

            add(tabs, BorderLayout.CENTER);
            loadRooms(); loadBookings();

            // --- Button Actions ---
            bookBtn.addActionListener(e -> bookRoom());
            cancelBtn.addActionListener(e -> cancelBooking());
            addRoomBtn.addActionListener(e -> addRoom());
            updateRoomBtn.addActionListener(e -> updateRoom());
            removeRoomBtn.addActionListener(e -> removeRoom());

            setVisible(true);
        }

        private void loadRooms() {
            roomModel.setRowCount(0);
            List<Room> rooms = dao.getAllRooms();
            for(Room r : rooms)
                roomModel.addRow(new Object[]{r.getRoomId(), r.getRoomNumber(), r.getType(), r.getPrice(), r.getStatus()});
        }

        private void loadBookings() {
            bookingModel.setRowCount(0);
            List<String[]> bookings = dao.getAllBookings();
            for(String[] b : bookings) bookingModel.addRow(b);
        }

        private void bookRoom() {
            int selectedRow = roomTable.getSelectedRow();
            if(selectedRow==-1){ JOptionPane.showMessageDialog(this,"Select a room"); return; }
            int roomId = (int) roomModel.getValueAt(selectedRow,0);
            String user = nameField.getText();
            String in = checkInField.getText();
            String out = checkOutField.getText();
            if(user.isEmpty() || in.isEmpty() || out.isEmpty()){ JOptionPane.showMessageDialog(this,"Fill all fields"); return; }
            if(dao.bookRoom(roomId,user,in,out)){
                JOptionPane.showMessageDialog(this,"Room booked!"); loadRooms(); loadBookings();
            }
        }

        private void cancelBooking() {
            int selectedRow = bookingTable.getSelectedRow();
            if(selectedRow==-1){ JOptionPane.showMessageDialog(this,"Select a booking"); return; }
            int bookingId = Integer.parseInt((String) bookingModel.getValueAt(selectedRow,0));
            if(dao.cancelBooking(bookingId)){
                JOptionPane.showMessageDialog(this,"Booking cancelled!"); loadRooms(); loadBookings();
            }
        }

        private void addRoom() {
            String roomNo = JOptionPane.showInputDialog(this,"Enter Room Number:");
            String type = JOptionPane.showInputDialog(this,"Enter Type:");
            String priceStr = JOptionPane.showInputDialog(this,"Enter Price:");
            if(roomNo==null || type==null || priceStr==null) return;
            double price = Double.parseDouble(priceStr);
            if(dao.addRoom(roomNo,type,price)){ JOptionPane.showMessageDialog(this,"Room added!"); loadRooms(); }
        }

        private void updateRoom() {
            int selectedRow = roomTable.getSelectedRow();
            if(selectedRow==-1){ JOptionPane.showMessageDialog(this,"Select a room"); return; }
            int roomId = (int) roomModel.getValueAt(selectedRow,0);
            String type = JOptionPane.showInputDialog(this,"Enter Type:", roomModel.getValueAt(selectedRow,2));
            String priceStr = JOptionPane.showInputDialog(this,"Enter Price:", roomModel.getValueAt(selectedRow,3));
            String status = JOptionPane.showInputDialog(this,"Enter Status:", roomModel.getValueAt(selectedRow,4));
            if(type==null || priceStr==null || status==null) return;
            double price = Double.parseDouble(priceStr);
            if(dao.updateRoom(roomId,type,price,status)){ JOptionPane.showMessageDialog(this,"Room updated!"); loadRooms(); }
        }

        private void removeRoom() {
            int selectedRow = roomTable.getSelectedRow();
            if(selectedRow==-1){ JOptionPane.showMessageDialog(this,"Select a room"); return; }
            int roomId = (int) roomModel.getValueAt(selectedRow,0);
            if(dao.removeRoom(roomId)){ JOptionPane.showMessageDialog(this,"Room removed!"); loadRooms(); }
        }

        public static void main(String[] args) {
            new HotelBookingSystemGUI();
        }
}
