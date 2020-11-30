package dev.carter.application;

import dev.carter.controllers.LoginController;
import dev.carter.objects.stack.History;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("Login"));
        stage.setTitle("Optix");
        stage.getIcons().add(new Image(App.class.getResourceAsStream("/dev/carter/images/logo.png")));
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml){
        try {
            scene.setRoot(loadFXML(fxml));
        }catch (Exception e) {
            System.out.println("Unable to execute command. No more previous pages");
        }
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/dev/carter/fxml/"+ fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}