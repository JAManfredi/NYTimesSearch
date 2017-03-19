package nytimessearch.jm.com.nytimessearch.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import nytimessearch.jm.com.nytimessearch.R;
import nytimessearch.jm.com.nytimessearch.adapters.ArticleAdapter;
import nytimessearch.jm.com.nytimessearch.databinding.ActivitySearchBinding;
import nytimessearch.jm.com.nytimessearch.dependencies.EndlessRecyclerViewScrollListener;
import nytimessearch.jm.com.nytimessearch.dependencies.SpacesItemDecoration;
import nytimessearch.jm.com.nytimessearch.fragments.FiltersDialogFragment;
import nytimessearch.jm.com.nytimessearch.models.Article;
import nytimessearch.jm.com.nytimessearch.models.Filters;
import nytimessearch.jm.com.nytimessearch.models.SearchResponse;
import nytimessearch.jm.com.nytimessearch.network.NYTService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    private ActivitySearchBinding binding;

    ArrayList<Article> articles;
    ArticleAdapter articleAdapter;

    private int currentPage = 0;
    private Filters currentFilters;
    private Map<String,String> currentQueryMap;
    private EndlessRecyclerViewScrollListener scrollListener;

    @Inject
    NYTService nytService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ((NYTSearchApplication) getApplication()).getNytServiceComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);

        Toolbar toolbar = (Toolbar) binding.toolbarInclude.toolbar;
        setSupportActionBar(toolbar);

        setTitle("The New York Times");

        articles = new ArrayList<>();
        articleAdapter = new ArticleAdapter(this, articles);
        binding.rvArticles.setAdapter(articleAdapter);
        SpacesItemDecoration decoration = new SpacesItemDecoration(16);
        binding.rvArticles.addItemDecoration(decoration);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        binding.rvArticles.setLayoutManager(layoutManager);
        binding.rvArticles.setVisibility(View.INVISIBLE);

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                String query;
                if (currentQueryMap != null) {
                    query = currentQueryMap.get("q");
                    startQueryRequest(query, page);
                }
            }
        };
        binding.rvArticles.addOnScrollListener(scrollListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint("Search for articles");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                startQueryRequest(query, 0);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void onFilterAction(MenuItem mi) {
        FragmentManager fm = getSupportFragmentManager();
        FiltersDialogFragment filtersDialogFragment = FiltersDialogFragment.newInstance(currentFilters);
        filtersDialogFragment.setFiltersDialogListener(new FiltersDialogFragment.FiltersDialogListener() {
            @Override
            public void onFiltersSaved(Filters filters) {
                currentFilters = filters;
            }
        });
        filtersDialogFragment.show(fm, "fragment__filters");
    }

    public Map<String,String> getQueryMap(String query, int page) {
        Map<String,String> queryMap = new HashMap<>();
        queryMap.put("q", query);
        queryMap.put("page", String.valueOf(page));

        if (currentFilters != null) {
            Date beginDate = currentFilters.getBeginDate();
            if (beginDate != null) {
                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd");
                queryMap.put("begin_date",dateFormatter.format(beginDate));
            }

            String sortOrder = currentFilters.getSortOrder();
            if (sortOrder != null) {
                if (!sortOrder.equals("None")) {
                    queryMap.put("sort", sortOrder.toLowerCase());
                }
            }

            StringBuilder sb = new StringBuilder();
            ArrayList<String> deskValues = currentFilters.getDeskValues();
            if (deskValues.size() > 0) {
                sb.append("news_desk:(");
                for (String deskValue : deskValues) {
                    sb.append("\"" + deskValue + "\" ");
                }
                sb.deleteCharAt(sb.length() - 1);
                sb.append(")");

                queryMap.put("fq", sb.toString());
            }
        }
        currentQueryMap = queryMap;
        return queryMap;
    }

    private void startQueryRequest(String query, int page) {
        try {
            if (page == 0) {
                articles.clear();
                articleAdapter.notifyDataSetChanged();
                scrollListener.resetState();
            }

            Call<SearchResponse> call = nytService.response(getQueryMap(query, page));
            call.enqueue(new Callback<SearchResponse>() {
                @Override
                public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                    SearchResponse searchResponse = (SearchResponse) response.body();
                    if (searchResponse != null) {
                        List<Article> responseArticles = searchResponse.getDocs();
                        if (responseArticles != null
                                && responseArticles.size() > 0) {
                            articles.addAll(responseArticles);
                            binding.rvArticles.setVisibility(View.VISIBLE);
                            articleAdapter.notifyDataSetChanged();
                        } else if (page == 0) {
                            articleAdapter.notifyDataSetChanged();
                            binding.rvArticles.setVisibility(View.INVISIBLE);
                            binding.tvInfoText.setText("No Articles Found, Try Again.");
                        }
                    } else if (page == 0) {
                        articleAdapter.notifyDataSetChanged();
                        binding.rvArticles.setVisibility(View.INVISIBLE);
                        binding.tvInfoText.setText("No Articles Found, Try Again.");
                    }
                }

                @Override
                public void onFailure(Call<SearchResponse> call, Throwable t) {
                    Log.d("NYT", "Failure Searching");
                    articles.clear();
                    binding.rvArticles.setVisibility(View.INVISIBLE);
                    articleAdapter.notifyDataSetChanged();
                }
            });
        } catch (Exception e) {
            Log.d("NYT", "Error " + e.getLocalizedMessage());
        }
    }
}