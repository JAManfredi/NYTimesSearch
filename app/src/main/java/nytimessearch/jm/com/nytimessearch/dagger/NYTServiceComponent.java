package nytimessearch.jm.com.nytimessearch.dagger;

import javax.inject.Singleton;

import dagger.Component;
import nytimessearch.jm.com.nytimessearch.activities.SearchActivity;

/**
 * Created by Jared12 on 3/18/17.
 */

@Singleton
@Component(modules={NYTServiceModule.class})
public interface NYTServiceComponent {
    void inject(SearchActivity activity);
}
