package dev.carter.api.newsapi;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.carter.objects.gson.JsonConverter;
import dev.carter.objects.news.Article;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Map;

public class NewsManager {
    private final static String NEWS_API_LINK = "https://newsapi.org/v2/top-headlines?sources=bbc-news&apiKey=ec2d7b8b03be4a679f700587c259839f";
    public ArrayList<Article> articles;

    public NewsManager(){
        articles = new ArrayList<>();
        retrieveArticles();
    }

    public void retrieveArticles(){
        try {
            //Establishes a connection to the URL
            URL url = new URL(NEWS_API_LINK);
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

            //Gets 'articles' array from map
            JsonObject jsonObject = new JsonParser().parse(result.toString()).getAsJsonObject();
            JsonArray arr = jsonObject.getAsJsonArray("articles");

            //Loops through 'articles' JSON array
            for (int i = 0; i < arr.size(); i++) {
                String title = arr.get(i).getAsJsonObject().get("title").getAsString();
                String description = arr.get(i).getAsJsonObject().get("description").getAsString();
                String link = arr.get(i).getAsJsonObject().get("url").getAsString();
                String imageLink = arr.get(i).getAsJsonObject().get("urlToImage").getAsString();

                //Creates an article object with all the information from the map
                Article article = new Article(title, description, link, imageLink);

                //Adds the article to the articles arraylist
                articles.add(article);
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    //Getter
    public ArrayList<Article> getArticles() {
        return articles;
    }
}
