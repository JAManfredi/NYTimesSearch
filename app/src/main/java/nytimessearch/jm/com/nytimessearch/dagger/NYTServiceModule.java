package nytimessearch.jm.com.nytimessearch.dagger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import nytimessearch.jm.com.nytimessearch.models.Multimedia;
import nytimessearch.jm.com.nytimessearch.models.deserializers.MultimediaDeserializer;
import nytimessearch.jm.com.nytimessearch.models.deserializers.ResponseDeserializer;
import nytimessearch.jm.com.nytimessearch.models.SearchResponse;
import nytimessearch.jm.com.nytimessearch.network.NYTService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Jared12 on 3/18/17.
 */

@Module
public class NYTServiceModule {
    @Provides
    @Singleton
    Gson Gson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(SearchResponse.class, new ResponseDeserializer());
        gsonBuilder.registerTypeAdapter(Multimedia.class, new MultimediaDeserializer());
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    Retrofit Retrofit(Gson gson) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.nytimes.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit;
    }

    @Provides
    @Singleton
    NYTService NYTService(Retrofit retrofit) {
        NYTService nytService = retrofit.create(NYTService.class);
        return nytService;
    }
}
