package nytimessearch.jm.com.nytimessearch.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jared12 on 3/18/17.
 */

public class Headline {
    String main;
    @SerializedName("content_kicker")
    String contentKicker;
    String kicker;
    @SerializedName("print_headline")
    String printHeadline;

    public String getMain() {
        return main;
    }

    public String getContentKicker() {
        return contentKicker;
    }

    public String getKicker() {
        return kicker;
    }

    public String getPrintHeadline() {
        return printHeadline;
    }
}
