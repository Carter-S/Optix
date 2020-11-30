package dev.carter.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import dev.carter.application.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class HomeController implements Initializable {
    
    @FXML
    private Label name;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Sets welcome name to the name of the user logged in
        name.setText(LoginController.userFirstName);
    }
    
    //Switches scene to screen selected on the navigation bar
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
    private void handleBackButton(ActionEvent actionEvent) throws IOException{
        App.setRoot(LoginController.pageHistory.pop());
    }
}
