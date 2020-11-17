package dev.carter.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import dev.carter.application.App;
import dev.carter.objects.exchangerates.Currency;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import dev.carter.api.exchangeratesapi.ExchangeRates;

public class ExchangeRatesController implements Initializable {
    
    @FXML
    private TableView<Currency> currencyTbl;
    @FXML
    private ComboBox<String> currencySelect;

    @Override
    public void initialize(URL url, ResourceBundle rb) { 
        //Thread one setting up table
        new Thread() {
            public void run() {
                TableColumn<Currency, String> currency = new TableColumn<>("Currency");
                TableColumn<Currency, String> currencySymbol = new TableColumn<>("Symbol");
                TableColumn<Currency, String> currencyValue = new TableColumn<>("Value");
                currencyTbl.getColumns().addAll(currency, currencySymbol, currencyValue);
                currencyValue.setCellValueFactory(new PropertyValueFactory<>("value"));
                currencySymbol.setCellValueFactory(new PropertyValueFactory<>("symbol"));
                currency.setCellValueFactory(new PropertyValueFactory<>("name"));
                currency.prefWidthProperty().bind(currencyTbl.widthProperty().multiply(0.3));
                currencySymbol.prefWidthProperty().bind(currencyTbl.widthProperty().multiply(0.3));
                currencyValue.prefWidthProperty().bind(currencyTbl.widthProperty().multiply(0.4));
                //Adding each currency to the combobox
                for (Currency c : Currency.values()) {
                    currencySelect.getItems().add(c.getName());
                }
                currencySelect.getSelectionModel().selectFirst();
            }
        }.start();
        //Thread two enters initial base currency (GBP) into updateTbl() method
        new Thread(){
            public void run(){
                updateTbl(Currency.GBP.getName());
            }
        }.start();
    }

    //Displays exchange rates in table
    private void updateTbl(String base) {
        ExchangeRates er = new ExchangeRates();
        er.getExchangeRates(base);
        for (Currency c : Currency.values()) {
            if (c.getValue() != 0) {
                currencyTbl.getItems().add(c);
            }
        }
    }

    //Updates table information with new selected base currency
    @FXML
    private void handleSubmit(ActionEvent actionEvent) {
        currencyTbl.getItems().clear();
        updateTbl(currencySelect.getSelectionModel().getSelectedItem());
    }

    //Switches scene to screen selected on navigation bar
    @FXML
    private void handleNavButton(ActionEvent actionEvent) throws IOException {
        Button btn = (Button) actionEvent.getSource();
        String page = btn.getText();
        if (page.contains(" ")) {
            page = page.replaceAll(" ", "");
        }
        LoginController.pageHistory.push(page);
        App.setRoot(page);
    }

    @FXML
    private void handleBackButton(ActionEvent actionEvent) throws IOException{
        App.setRoot(LoginController.pageHistory.pop());
    }
}
