package dev.carter.controllers;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import dev.carter.application.App;
import dev.carter.objects.news.Article;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import dev.carter.api.newsapi.NewsManager;

public class NewsController implements Initializable {

    private NewsManager news;
    private ArrayList<Article> articles;
    
    @FXML
    private VBox newsBox;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        news = new NewsManager();
        news.retrieveArticles();
        articles = news.getArticles();
        printArticles();
    }
    
    //Creates four articles and adds them to the news box
    private int position = 0;
    @FXML
    private void printArticles() {
        newsBox.getChildren().clear();
        for (int i = 0; i < 4; i++) {
            if(position < 10){
            newsBox.getChildren().addAll(createArticleBox(position));
            position++;
            }
        }
    }

    //Creates an article box
    @FXML
    private VBox createArticleBox(int position) {
        Label title = new Label(articles.get(position).getTitle());
        title.getStyleClass().addAll("title", "text");
        Label description = new Label(articles.get(position).getDescription());
        description.getStyleClass().addAll("description", "text");
        ImageView image = new ImageView(articles.get(position).getImageLink());
        image.setFitHeight(100);
        image.setFitWidth(175);
        HBox desc = new HBox();
        desc.getStyleClass().addAll("desc");
        desc.getChildren().addAll(description, image);
        Button readMore = new Button("Read More");
        readMore.getStyleClass().clear();
        readMore.getStyleClass().addAll("readMore", "text", "buttons");
        readMore.setOnAction(actionEvent -> {
            try {
                java.awt.Desktop.getDesktop().browse(new URI(articles.get(position).getLink()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        VBox articleBox = new VBox();
        articleBox.getChildren().addAll(title, desc, readMore);
        articleBox.getStyleClass().add("articleBox");
        return articleBox;
    }

    //Prints previous four articles
    @FXML
    private void handlePrevious(ActionEvent actionEvent) {
        if(position <= 4){
            position = 10;
            position -=2;
        }else if(position % 4 != 0){
            while(position % 4 != 0){
                position--;
            }
            position-=4;
        }else{
            position -= 8;
        }
        printArticles();
    }

    //Prints next four articles
    @FXML
    private void handleNext(ActionEvent actionEvent){
        if (position >= 10) {
            position = 0;
        }
        printArticles();
    }

    //Switches scene to screen selected on navigation bar
    @FXML
    private void handleNavButton(ActionEvent actionEvent) throws IOException {
        Button btn = (Button) actionEvent.getSource();
        String page = btn.getText();
        if (page.contains(" ")) {
            page = page.replaceAll(" ", "");
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
