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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import dev.carter.objects.reminders.Reminder;

public class RemindersController implements Initializable {

    private Connection con;
    private Statement stmt;
    private ResultSet rs;
    
    @FXML
    private TableView<Reminder> reminderTbl;
    @FXML
    private TextArea reminderInput;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            //Establishing connection with database
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/optix_db", "viewer", "optix");
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String SQL = "SELECT * FROM TBL_REMINDERS";
            rs = stmt.executeQuery(SQL);
            //Setting up table
            TableColumn<Reminder, String> reminder = new TableColumn<>(rs.getMetaData().getColumnLabel(2));
            reminderTbl.getColumns().addAll(reminder);
            reminder.setCellValueFactory(new PropertyValueFactory<>("reminder"));
            reminder.prefWidthProperty().bind(reminderTbl.widthProperty().multiply(1));
            
            updateReminders();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void updateReminders() throws SQLException{
        //Displays all current users reminders to the table
        ArrayList<Reminder> reminders = new ArrayList<>();
        reminderTbl.getItems().clear();
        String SQL = "SELECT * FROM TBL_REMINDERS, TBL_USERTOREMINDER WHERE (TBL_REMINDERS.REMINDER_ID=TBL_USERTOREMINDER.REMINDER_ID) AND (TBL_USERTOREMINDER.USER_ID="+LoginController.userId+")";
        rs = stmt.executeQuery(SQL);
        int i = 0;
        while(rs.next()){
            reminders.add(i, new Reminder(rs.getInt("REMINDER_ID"), rs.getString("REMINDER")));
            reminderTbl.getItems().add(reminders.get(i));
            i++;
        }
    }
    
    private void addReminder(String reminder) throws SQLException{
        //Adds reminder to the database and arraylist
        String SQL = "SELECT MAX(REMINDER_ID) AS maxID FROM TBL_REMINDERS";
        rs = stmt.executeQuery(SQL);
        rs.next();
        int id = rs.getInt("maxID")+1;
        SQL = "INSERT INTO TBL_REMINDERS VALUES(?,?)";
        PreparedStatement pStmt = con.prepareStatement(SQL);
        pStmt.setInt(1, id);
        pStmt.setString(2, reminder);
        pStmt.executeUpdate();
        pStmt.close();
        SQL = "INSERT INTO TBL_USERTOREMINDER VALUES("+LoginController.userId+","+id+")";
        stmt.executeUpdate(SQL);
        updateReminders();
    }
    
    private void removeReminder() throws SQLException{
        //removes selected reminder from the arraylist and database
        Reminder temp = reminderTbl.getSelectionModel().getSelectedItem();
        String SQL = "DELETE FROM TBL_USERTOREMINDER WHERE USER_ID="+LoginController.userId+" AND REMINDER_ID="+temp.getReminderID();
        stmt.executeUpdate(SQL);
        SQL = "DELETE FROM TBL_REMINDERS WHERE REMINDER_ID="+temp.getReminderID();
        stmt.executeUpdate(SQL);
        updateReminders();
    }
    
    @FXML
    private void handleRemove(ActionEvent actionEvent) throws SQLException{
        if(!reminderTbl.getSelectionModel().isEmpty()){
            removeReminder();
        }else{
            reminderInput.setText("No reminders selected!");
        }
    }
    
    @FXML
    private void handleAddReminder(ActionEvent actionEvent) throws SQLException{
        addReminder(reminderInput.getText());
        reminderInput.clear();
    }

    
    //Switches scene to screen selected on navigation bar
    @FXML
    private void handleNavButton(ActionEvent actionEvent) throws IOException{
        Button btn = (Button) actionEvent.getSource();
        String page = btn.getText();
        if(page.contains(" ")){
            page = page.replaceAll(" ","");
        }
        App.setRoot(page);
    }

}
