package dev.carter.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

import dev.carter.application.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import dev.carter.objects.calendar.Event;

public class CalendarController implements Initializable {

    private Connection con;
    private Statement stmt;
    private ResultSet rs;
    
    @FXML
    private TableView<Event> calendarTbl;
    @FXML
    private TextField eventDateInput;
    @FXML
    private TextArea eventDescInput;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            //Establishing connection with database
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/optix_db", "viewer", "optix");
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String SQL = "SELECT * FROM TBL_CALENDAR";
            rs = stmt.executeQuery(SQL);
            //Setting up table
            TableColumn<Event, Date> eventDate = new TableColumn<>(rs.getMetaData().getColumnLabel(2));
            TableColumn<Event, String> eventDescription = new TableColumn<>(rs.getMetaData().getColumnLabel(3));
            calendarTbl.getColumns().addAll(eventDate,eventDescription);
            eventDate.setCellValueFactory(new PropertyValueFactory<>("eventDate"));
            eventDescription.setCellValueFactory(new PropertyValueFactory<>("eventDesc"));
            eventDate.prefWidthProperty().bind(calendarTbl.widthProperty().multiply(0.3));
            eventDescription.prefWidthProperty().bind(calendarTbl.widthProperty().multiply(0.7));
            
            updateCalendar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void updateCalendar() throws SQLException{
        //Displays all calendar events belonging to the current user
        ArrayList<Event> events = new ArrayList<>();
        calendarTbl.getItems().clear();
        String SQL = "SELECT * FROM TBL_CALENDAR, TBL_USERTOEVENT WHERE (TBL_CALENDAR.EVENT_ID=TBL_USERTOEVENT.EVENT_ID) AND (TBL_USERTOEVENT.USER_ID="+LoginController.userId+") ORDER BY TBL_CALENDAR.EVENT_DATE ASC";
        rs = stmt.executeQuery(SQL);
        int i = 0;
        while(rs.next()){
            events.add(i, new Event(rs.getInt("EVENT_ID"), rs.getDate("EVENT_DATE"), rs.getString("EVENT_DESC")));
            calendarTbl.getItems().add(events.get(i));
            i++;
        }
    }
    
    private void addEvent(Date eventDate, String eventDesc) throws SQLException{
        //Adds new calendar event to the database and arraylist
        String SQL = "SELECT MAX(EVENT_ID) AS maxID FROM TBL_CALENDAR";
        rs = stmt.executeQuery(SQL);
        rs.next();
        int id = rs.getInt("maxID")+1;
        SQL = "INSERT INTO TBL_CALENDAR VALUES(?,?,?)";
        PreparedStatement pStmt = con.prepareStatement(SQL);
        pStmt.setInt(1, id);
        pStmt.setDate(2, eventDate);
        pStmt.setString(3, eventDesc);
        pStmt.executeUpdate();
        pStmt.close();
        SQL = "INSERT INTO TBL_USERTOEVENT VALUES("+LoginController.userId+","+id+")";
        stmt.executeUpdate(SQL);
        updateCalendar();
    }
    
    private void removeEvent() throws SQLException{
        //Removes selected calendar event from database and arraylist
        Event temp = calendarTbl.getSelectionModel().getSelectedItem();
        String SQL = "DELETE FROM TBL_USERTOEVENT WHERE USER_ID="+LoginController.userId+" AND EVENT_ID="+temp.getEventId();
        stmt.executeUpdate(SQL);
        SQL = "DELETE FROM TBL_CALENDAR WHERE EVENT_ID="+temp.getEventId();
        stmt.executeUpdate(SQL);
        updateCalendar();
    }
    
    //Switches scene to screen selected on navigation bar
    @FXML
    private void handleNavButton(ActionEvent actionEvent) throws IOException{
        Button btn = (Button) actionEvent.getSource();
        String page = btn.getText();
        if(page.contains(" ")){
            page = page.replaceAll(" ","");
        }
        LoginController.pageHistory.push(LoginController.pageHistory.getCurrentPage());
        LoginController.pageHistory.setCurrentPage(page);
        App.setRoot(page);
    }

    @FXML
    private void handleAddEvent(ActionEvent actionEvent){
        try{
            java.sql.Date date = Date.valueOf(eventDateInput.getText());
            addEvent(date, eventDescInput.getText());
            eventDateInput.clear();
            eventDescInput.clear();
        }catch(Exception e){
            eventDateInput.setText("Incorrect date format!");
        }
    }

    @FXML
    private void handleRemoveEvent(ActionEvent actionEvent) throws SQLException {
        if(!calendarTbl.getSelectionModel().isEmpty()){
            removeEvent();
        }else{
            eventDescInput.setText("No events selected!");
        }
    }

    @FXML
    private void handleBackButton(ActionEvent actionEvent) throws IOException{
        App.setRoot(LoginController.pageHistory.pop());
    }
}
