package com.example.macintosh.assignmentt1.ModelClass;
import com.example.macintosh.assignmentt1.R;

public class DataModel {

    public String name;
    public String description;
    public String webURL;
    public String category;
    public String image;

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