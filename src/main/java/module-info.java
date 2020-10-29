module dev.carter {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires mysql.connector.java;
    requires java.desktop;

    opens dev.carter.controllers;
    exports dev.carter.objects.calendar;
    exports dev.carter.objects.exchangerates;
    exports dev.carter.application;
    exports dev.carter.controllers;
    exports dev.carter.objects.gson;
}