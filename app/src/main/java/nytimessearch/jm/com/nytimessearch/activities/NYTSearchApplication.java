package nytimessearch.jm.com.nytimessearch.activities;

import android.app.Application;

import nytimessearch.jm.com.nytimessearch.dagger.DaggerNYTServiceComponent;
import nytimessearch.jm.com.nytimessearch.dagger.NYTServiceComponent;

/**
 * Created by Jared12 on 3/18/17.
 */

public class NYTSearchApplication extends Application {
    private NYTServiceComponent mNytServiceComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mNytServiceComponent = DaggerNYTServiceComponent.create();
    }

    public NYTServiceComponent getNytServiceComponent() {
        return mNytServiceComponent;
    }
}
