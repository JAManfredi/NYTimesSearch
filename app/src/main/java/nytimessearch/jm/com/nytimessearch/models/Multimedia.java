package nytimessearch.jm.com.nytimessearch.models;

/**
 * Created by Jared12 on 3/18/17.
 */

public class Multimedia {
    int width;
    String url;
    int height;
    String subtype;
    String type;

    public int getWidth() {
        return width;
    }

    public String getUrl() {
        return "http://www.nytimes.com/" + url;
    }

    public int getHeight() {
        return height;
    }

    public String getSubtype() {
        return subtype;
    }

    public String getType() {
        return type;
    }
}