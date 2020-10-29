package dev.carter.api.openweathermaps;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.carter.objects.gson.JsonConverter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

public class OpenWeatherMaps {
    private String location;
    private String temp;
    private String weather;
    private String windSpeed;
    private String humidity;

    public void getWeatherInformation(String location) {
        try {
            this.location = location;
            //Establishes connection to the URL
            String urlString = "http://api.openweathermap.org/data/2.5/weather?q=" + location + "&appid=90b02e6382065c5128da459a5014da9c&units=metric";
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();

            //Reads information from the URL into a string
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();

            //Converts information from JSON format to a map
            JsonConverter jsonConverter = new JsonConverter();
            Map<String, Object> respMap = jsonConverter.jsonToMap(result.toString());
            //Gets 'main' from map
            Map<String, Object> mainMap = jsonConverter.jsonToMap(respMap.get("main").toString());
            //Gets 'wind' from map
            Map<String, Object> windMap = jsonConverter.jsonToMap(respMap.get("wind").toString());
            //Gets 'weather' array from map
            JsonObject jsonObject = new JsonParser().parse(result.toString()).getAsJsonObject();
            JsonArray arr = jsonObject.getAsJsonArray("weather");

            //Loops through 'weather' JSON array
            for (int i = 0; i < arr.size(); i++) {
                weather = arr.get(i).getAsJsonObject().get("main").getAsString();
            }

            windSpeed = windMap.get("speed") + "m/s";
            temp = mainMap.get("temp") + "°C";
            humidity = mainMap.get("humidity") + "%";

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Getters
    public String getLocation() {
        return location;
    }

    public String getTemp() {
        return temp;
    }

    public String getWeather() {
        return weather;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public String getHumidity() {
        return humidity;
    }
}
