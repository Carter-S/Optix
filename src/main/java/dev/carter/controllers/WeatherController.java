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
import javafx.scene.control.TextField;
import dev.carter.api.openweathermaps.OpenWeatherMaps;

public class WeatherController implements Initializable {

    private OpenWeatherMaps owm;
    
    @FXML
    private TextField locationInput;
    @FXML
    private Label conditions;
    @FXML
    private Label temperature;
    @FXML
    private Label windSpeed;
    @FXML
    private Label humidity;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        owm = new OpenWeatherMaps();
        //Displaying inital weather data for default location (London)
        locationInput.setText("London");
        owm.getWeatherInformation("London");
        conditions.setText(owm.getWeather());
        temperature.setText(owm.getTemp());
        windSpeed.setText(owm.getWindSpeed());
        humidity.setText(owm.getHumidity());
    }
    
    //Retrieves and displays weather data for new entered location
    @FXML
    private void handleSubmit(ActionEvent actionEvent) {
        String temp = owm.getWeather();
        owm.getWeatherInformation(locationInput.getText());
        if(owm.getWeather()!= temp){
            conditions.setText(owm.getWeather());
            temperature.setText(owm.getTemp());
            windSpeed.setText(owm.getWindSpeed());
            humidity.setText(owm.getHumidity());
        }else{
            locationInput.setText("Invalid Location");
            conditions.setText("");
            temperature.setText("");
            windSpeed.setText("");
            humidity.setText("");
        }
    }

    //Switches scene to screen selected on navigation bar
    @FXML
    private void handleNavButton(ActionEvent actionEvent) throws IOException {
        Button btn = (Button) actionEvent.getSource();
        String page = btn.getText();
        if(page.contains(" ")){
            page = page.replaceAll(" ","");
        }
        LoginController.pageHistory.push(page);
        App.setRoot(page);

    }

    @FXML
    private void handleBackButton(ActionEvent actionEvent) throws IOException{
        App.setRoot(LoginController.pageHistory.pop());
    }
}
