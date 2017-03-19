package nytimessearch.jm.com.nytimessearch.network;

import java.util.Map;

import nytimessearch.jm.com.nytimessearch.models.SearchResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by Jared12 on 3/18/17.
 */

public interface NYTService {
    @GET("/svc/search/v2/articlesearch.json?api-key=bfd8f74c0e6c4be7b361a988e1cf5060")
    Call<SearchResponse> response(@QueryMap Map<String,String> options);
}
