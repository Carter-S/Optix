package dev.carter.api.exchangeratesapi;

import dev.carter.objects.exchangerates.Currency;
import dev.carter.objects.gson.JsonConverter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.Map;

public class ExchangeRates {
    public void getExchangeRates(String base) {
        try {
            //Establishes a connection to the URL
            String urlString = "https://api.exchangeratesapi.io/latest?base=" + base;
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
            //Gets 'rates' from map
            Map<String, Object> mainMap = jsonConverter.jsonToMap(respMap.get("rates").toString());

            //Creates a decimal format for the currency value
            DecimalFormat dFormat = new DecimalFormat("#.00");
            //loops through all the currencies in the enum
            for (Currency cur : Currency.values()) {
                //Resets the value of all currency values to 0
                cur.setValue(0);
                //Gets the value of each currency from the Map
                double value = Double.parseDouble(dFormat.format(mainMap.get(cur.getName())));
                //Sets the value of each currency to the matching one from the map
                cur.setValue(value);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
