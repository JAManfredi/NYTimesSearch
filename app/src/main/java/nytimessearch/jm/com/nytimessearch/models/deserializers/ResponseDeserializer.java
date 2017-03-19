package nytimessearch.jm.com.nytimessearch.models.deserializers;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import nytimessearch.jm.com.nytimessearch.models.SearchResponse;

/**
 * Created by Jared12 on 3/18/17.
 */

public class ResponseDeserializer implements JsonDeserializer<SearchResponse> {
    @Override
    public SearchResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonElement jsonResponse = json.getAsJsonObject().get("response");

        return new Gson().fromJson(jsonResponse, SearchResponse.class);
    }
}
