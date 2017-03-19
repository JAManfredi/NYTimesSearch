package nytimessearch.jm.com.nytimessearch.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Jared12 on 3/18/17.
 */

public class Article {
    @SerializedName("web_url")
    String webUrl;
    String snippet;
    @SerializedName("lead_paragraph")
    String leadParagraph;
    @SerializedName("pub_date")
    String pubDate;
    @SerializedName("news_desk")
    String newsDesk;
    @SerializedName("document_type")
    String documentType;
    @SerializedName("word_count")
    String wordCount;

    Headline headline;
    List<Multimedia> multimedia;

    public String getWebUrl() {
        return webUrl;
    }

    public String getSnippet() {
        return snippet;
    }

    public String getLeadParagraph() {
        return leadParagraph;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getNewsDesk() {
        return newsDesk;
    }

    public String getDocumentType() {
        return documentType;
    }

    public String getWordCount() {
        return wordCount;
    }

    public Headline getHeadline() {
        return headline;
    }

    public List<Multimedia> getMultimedia() {
        return multimedia;
    }
}