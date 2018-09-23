package com.example.macintosh.assignmentt1.ModelClass;
import com.example.macintosh.assignmentt1.R;

public class DataModel {

    private String name;
    private String description;
    private String webURL;
    private String category;
    private String image;

    public DataModel(String name, String description, String webURL, String category,String image) {
        this.name = name;
        this.description = description;
        this.webURL = webURL;
        this.category = category;
        this.image = image;
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

    public String getImage(){return image;}
}