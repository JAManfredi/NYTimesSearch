package nytimessearch.jm.com.nytimessearch.models;

import java.util.List;

/**
 * Created by Jared12 on 3/18/17.
 */

public class SearchResponse {
    List<Article> docs;

    public SearchResponse() {}

    public List<Article> getDocs() {
        return docs;
    }
}
