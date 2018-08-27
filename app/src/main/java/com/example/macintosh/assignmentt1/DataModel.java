package com.example.macintosh.assignmentt1;

public class DataModel {

    String name;
    String description;
    String webURL;
    String category;

    public DataModel(String name, String description, String webURL, String category) {
        this.name = name;
        this.description = description;
        this.webURL = webURL;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getWebURL() {
        return webURL;
    }

    public String getCategory() {
        return category;
    }
}