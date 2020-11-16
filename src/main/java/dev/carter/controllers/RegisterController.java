package dev.carter.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import dev.carter.application.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController implements Initializable {

    private Connection con;
    private Statement stmt;
    private ResultSet rs;
    
    @FXML
    private Label success;
    @FXML
    private Label error;
    @FXML
    private PasswordField confirmPasswordInput;
    @FXML
    private PasswordField passwordInput;
    @FXML
    private TextField emailInput;
    @FXML
    private TextField lastInput;
    @FXML
    private TextField firstInput;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            //establishes a connection with the database
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/optix_db", "viewer", "optix");
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //switches scene to login screen
    @FXML
    private void handleLogin(ActionEvent actionEvent) throws IOException {
        App.setRoot("Login");
    }
    
    @FXML
    private void handleRegister(ActionEvent actionEvnet) {
        try{
            error.setText("");
            //Checks if user has entered both a first name and last name
        if (!(firstInput.getText().isEmpty() || lastInput.getText().isEmpty())) {
            //Checks if the entered email is valid and hasn't been used already
            if (isValid(emailInput.getText()) && !userExists(emailInput.getText())) {
                //Checks if both password inputs are identical
                if (passwordInput.getText().equals(confirmPasswordInput.getText())) {
                    //Checks if user was successfully added to TBL_USERS in the database
                    if (addUser(firstInput.getText(), lastInput.getText(), emailInput.getText(), passwordInput.getText())) {
                        success.setText("Successfully registered!");
                    } else {
                        error.setText("Error! Registration failed");
                    }
                } else {
                    error.setText("Passwords don't match");
                    passwordInput.clear();
                    confirmPasswordInput.clear();
                }
            } else {
                error.setText("Email invalid or already taken");
            }
        } else {
            error.setText("Error! Enter a name");
        }
            
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }

    //adds a user to TBL_USERS in the database 
    private boolean addUser(String firstName, String lastName, String email, String password) {
        try {
            String SQL = "SELECT MAX(USER_ID) AS maxID FROM TBL_USERS";
            rs = stmt.executeQuery(SQL);
            rs.next();
            int id = rs.getInt("maxID") + 1;
            SQL = "INSERT INTO TBL_USERS VALUES(?,?,?,?,?)";
            PreparedStatement pStmt = con.prepareStatement(SQL);
            pStmt.setInt(1, id);
            pStmt.setString(2, email);
            pStmt.setString(3, capitalise(firstName));
            pStmt.setString(4, capitalise(lastName));
            pStmt.setString(5, password);
            pStmt.executeUpdate();
            pStmt.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    //capitalises the first letter of a string
    private String capitalise(String str){
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    //checks if user with entered email already exists
    private boolean userExists(String email) throws SQLException {
        String SQL = "SELECT * FROM TBL_USERS";
        rs = stmt.executeQuery(SQL);
        while (rs.next()) {
            if (email.equals(rs.getString("USER_EMAIL"))) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    //checks if entered email is valid
    private boolean isValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."
                + "[a-zA-Z0-9_+&*-]+)*@"
                + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
                + "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        if (email == null) {
            return false;
        }
        return pat.matcher(email).matches();
    }

}
