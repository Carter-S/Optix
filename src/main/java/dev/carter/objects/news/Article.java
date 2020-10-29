package dev.carter.objects.news;

public class Article {

    private String title;
    private String description;
    private String link;
    private String imageLink;

    public Article(String title, String description, String link, String imageLink) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.imageLink = imageLink;
    }

    //Getters and setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getImageLink() {
        return imageLink;
    }
}