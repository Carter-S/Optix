package dev.carter.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import dev.carter.application.App;
import dev.carter.objects.stack.History;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController implements Initializable {
    
    public static int userId;
    public static History pageHistory = new History();
    public static String userFirstName;
    
    private Connection con;
    private Statement stmt;
    private ResultSet rs;
    
    @FXML
    private Label error;
    @FXML
    private PasswordField passwordInput;
    @FXML
    private TextField emailInput;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            //establishing a connection with database
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/optix_db", "viewer", "optix");
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }    

    //switches scene to register screen
    @FXML
    private void handleRegister(ActionEvent actionEvent) throws IOException {
        App.setRoot("Register");
    }

    @FXML
    private void handleLogin(ActionEvent actionEvent){
        try{
            //gets user information from TBL_USERS where email entered matches
            String SQL = "SELECT * FROM TBL_USERS WHERE USER_EMAIL='"+emailInput.getText()+"'";
            rs = stmt.executeQuery(SQL);
            rs.next();
            
            //checks if password entered is the password belonging to the account
            if(rs.getString("USER_PASSWORD").equals(passwordInput.getText())){
                userId = rs.getInt("USER_ID");
                userFirstName = rs.getString("USER_FIRSTNAME");
                //if login details are valid switches scene to home screen
                App.setRoot("Home");
            }else{
                 error.setText("Error! Email and password don't match");
            }
        }catch(Exception e){
             error.setText("Error! Invalid email!");
            e.printStackTrace();
        }
    }

}
