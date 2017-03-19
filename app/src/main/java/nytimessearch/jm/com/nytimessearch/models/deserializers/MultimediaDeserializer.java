package nytimessearch.jm.com.nytimessearch.models.deserializers;

import android.text.TextUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import nytimessearch.jm.com.nytimessearch.models.Multimedia;

/**
 * Created by Jared12 on 3/18/17.
 */

public class MultimediaDeserializer implements JsonDeserializer<List<Multimedia>> {

    @Override
    public List<Multimedia> deserialize(JsonElement json, Type typeOfT,
                                        JsonDeserializationContext context) throws JsonParseException {

        ArrayList<Multimedia> multimedias = new ArrayList<>();
        String str = null;

        try {
            str = json.getAsString();
        } catch (IllegalStateException e) {}

        if (str != null && TextUtils.isEmpty(str)) {
            return multimedias;
        } else {
            JsonArray jsonArray = json.getAsJsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                multimedias.add((Multimedia) context.deserialize(jsonArray.get(i), Multimedia.class));
            }
        }
        return multimedias;
    }
}
