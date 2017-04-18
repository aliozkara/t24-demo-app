package com.app.t24.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.afollestad.materialdialogs.MaterialDialog;
import com.app.t24.R;
import com.app.t24.model.entity.NewsItem;
import com.app.t24.model.rest.CategoriesModel;
import com.app.t24.model.rest.CategoriesModel.Datum;
import com.app.t24.model.rest.NewsModel;
import com.app.t24.ui.adapter.NewsAdapter;
import com.app.t24.ui.adapter.SliderAdapter;
import com.app.t24.utils.AndroidUtilities;
import com.app.t24.utils.PositionListener;
import com.app.t24.utils.RecylerScrollListener;
import com.app.t24.utils.RestClient;
import com.app.t24.utils.RestEndPoint;
import com.bartoszlipinski.recyclerviewheader2.RecyclerViewHeader;
import com.zplesac.connectionbuddy.ConnectionBuddy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements Spinner.OnItemSelectedListener, PositionListener{


    // Set all view with Butterknife http://jakewharton.github.io/butterknife/
    @BindView(R.id.activity_home_vp_slideshow)
    ViewPager activity_home_vp_slideshow;
    @BindView(R.id.activity_home_ci_slideshow)
    CircleIndicator activity_home_ci_slideshow;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.activity_home_rv_list)
    RecyclerView activity_home_rv_list;
    @BindView(R.id.activity_home_rvh_header)
    RecyclerViewHeader activity_home_rvh_header;
    @BindView(R.id.activity_home_rl_splash)
    RelativeLayout activity_home_rl_splash;
    @BindView(R.id.activity_home_s_category)
    Spinner activity_home_s_category;

    // news update duration
    private static final int newsUpdateDuration = 120 * 1000;

    // slider news update duration
    private static final int sliderUpdateDuration = 3 * 1000;

    // slash screen duration
    private static final int splashScreenDuration = 1 * 1000;

    // current news page in recylerview list
    private int currentNewsPage;

    // current news page in viewpager list
    private int currentPage;

    // limit of news pages
    private int limitNewsPage;

    // keep to recylerview list item position for resfresh news
    private int currentScrollPostion;

    // default = null, get selected category at toolbar in spinner
    private String newsCurrentCategory = null;

    // last news data for viewpager list
    private ArrayList<HashMap<String,String>> lastNewsData;

    // news data for recylerview list
    private ArrayList<Object> newsData;

    // category ids at toolbar in spinner
    private ArrayList<String> categoriesIds;

    // handler for schedule updating all news
    private Handler swipeHandler;
    private Runnable swipeRunnable;

    // listener for keep to recylerview list position
    private PositionListener positionListener;

    private RecyclerView.Adapter mAdapter;

    private MaterialDialog materialDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        // show splash screen
        splashScreen();

        // init view and listener
        init();

        // all listener here
        listener();

        // get all news and category data
        setAllData();


    }

    private void splashScreen(){
        // start loading dialog
        materialDialog = AndroidUtilities.showProgressDialog(HomeActivity.this, R.string.str_uploading);
        materialDialog.show();

        final Handler splashHandler = new Handler();
        Runnable splashRunnable = new Runnable() {
            public void run() {
                activity_home_rl_splash.setVisibility(View.GONE);
            }
        };
        splashHandler.postDelayed(splashRunnable, splashScreenDuration);
    }

    private void init(){

        // Set Toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setLogo(R.mipmap.ic_launcher);
        toolbar.setLogoDescription(getResources().getString(R.string.app_name));

        positionListener = this;

        newsData = new ArrayList<Object>();
        lastNewsData = new ArrayList<HashMap<String,String>>();

        mAdapter = new NewsAdapter(this, newsData, positionListener);
        activity_home_rv_list.setLayoutManager(new LinearLayoutManager(this));
        activity_home_rv_list.setAdapter(mAdapter);

        // lastest news set header to recylerview list
        activity_home_rvh_header.attachTo(activity_home_rv_list);

        activity_home_s_category.setOnItemSelectedListener(this);

    }


    private void setAllData(){

        // set the category list to Toolbar in Spinner
        setCategory();

        // get the lastest news in Viewpager
        getNews(1, false, newsCurrentCategory);

        // get the news list in RecylerView
        getNews(2, false, newsCurrentCategory);

        dissmisDialog();

        // get the news list every 120 seconds in Viewpager and RecylerView
        final Handler updateHandler = new Handler();
        Runnable updateRunnable = new Runnable() {
            public void run() {
                getNews(1, true, newsCurrentCategory);
                getNews(2, true, newsCurrentCategory);
                updateHandler.postDelayed(this, newsUpdateDuration);
            }
        };
        updateHandler.postDelayed(updateRunnable, newsUpdateDuration);

    }

    private void setSlider(){

        activity_home_vp_slideshow.setAdapter(new SliderAdapter(HomeActivity.this, lastNewsData));
        activity_home_ci_slideshow.setViewPager(activity_home_vp_slideshow);
        activity_home_vp_slideshow.setCurrentItem(currentPage);

        // swiping every 3 seconds
        swipeHandler = new Handler();
        swipeRunnable = new Runnable() {
            public void run() {

                // if it is end, take the first
                if (currentPage == lastNewsData.size()) {
                    currentPage = 0;
                }

                activity_home_vp_slideshow.setCurrentItem(currentPage++, true);
                swipeHandler.postDelayed(this, sliderUpdateDuration);
            }
        };
        swipeHandler.postDelayed(swipeRunnable, sliderUpdateDuration);

    }

    // page: wanted the news page.  if page is 1, this are slider news
    // refresh: if is true, clear previous news
    // category: getting only selected category news

    private void getNews(final Integer page, final boolean refresh, @Nullable String category) {


        if (ConnectionBuddy.getInstance().hasNetworkConnection()) {

            RestEndPoint retroInterface = RestClient.createService(RestEndPoint.class);

            Call<NewsModel> call;
            if (category != null) {
                call = retroInterface.getNews(String.valueOf(page), category);
            } else {
                call = retroInterface.getNews(String.valueOf(page));
            }

            call.enqueue(new Callback<NewsModel>() {

                @Override
                public void onResponse(Call<NewsModel> call, Response<NewsModel> response) {
                    try {
                        if (response.body() != null) {

                            NewsModel item = response.body();
                            limitNewsPage = item.paging.pages;

                            // set slider news
                            if (page == 1) {
                                if (refresh) {
                                    swipeHandler.removeCallbacks(swipeRunnable);
                                    lastNewsData = new ArrayList<HashMap<String, String>>();
                                }
                                HashMap<String, String> url_maps;
                                for (int i = 0; i < item.data.size(); i++) {
                                    url_maps = new HashMap<String, String>();
                                    url_maps.put("id", item.data.get(i).id);
                                    url_maps.put("image", item.data.get(i).images.page.replaceAll("//", ""));
                                    url_maps.put("title", AndroidUtilities.decodeString(item.data.get(i).title));
                                    lastNewsData.add(url_maps);
                                }
                                setSlider();
                            }
                            // set list news
                            else {
                                currentNewsPage = page;

                                if (refresh) {
                                    newsData.clear();
                                }

                                for (int i = 0; i < item.data.size(); i++) {
                                    newsData.add(new NewsItem(item.data.get(i).id, AndroidUtilities.decodeString(item.data.get(i).title), item.data.get(i).images.list.replaceAll("//", "")));
                                }

                                mAdapter.notifyItemInserted(newsData.size() - 1);
                                mAdapter.notifyDataSetChanged();

                                if (refresh) {
                                    activity_home_rv_list.scrollToPosition(currentScrollPostion);
                                }
                            }
                        }
                    } catch (NullPointerException ex) {
                        AndroidUtilities.showDialog(HomeActivity.this, R.string.str_message_internet_error);
                        dissmisDialog();
                    }
                }

                @Override
                public void onFailure(Call<NewsModel> call, Throwable t) {
                    AndroidUtilities.showDialog(HomeActivity.this, R.string.str_message_internet_error);
                    dissmisDialog();
                }
            });
        }
        else{
            AndroidUtilities.showDialog(HomeActivity.this, R.string.str_message_internet_refused);
            dissmisDialog();
        }

    }

    private void setCategory(){


        final List<String> categories = new ArrayList<String>();
        categoriesIds = new ArrayList<String>();

        if (ConnectionBuddy.getInstance().hasNetworkConnection()) {

            RestEndPoint retroInterface = RestClient.createService(RestEndPoint.class);
            Call<CategoriesModel> call = retroInterface.getCategories();
            call.enqueue(new Callback<CategoriesModel>() {

                @Override
                public void onResponse(Call<CategoriesModel> call, Response<CategoriesModel> response) {
                    try {
                        if (response.body() != null) {
                            for (int i = 0; i < response.body().data.size(); i++) {
                                Datum data = response.body().data.get(i);
                                categories.add(data.name);
                                categoriesIds.add(data.id);
                            }

                            activity_home_s_category.setPrompt("Kategoriler");
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(HomeActivity.this, android.R.layout.simple_spinner_item, categories);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            activity_home_s_category.setAdapter(dataAdapter);
                        }
                    } catch (NullPointerException ex) {
                        AndroidUtilities.showDialog(HomeActivity.this, R.string.str_message_internet_error);
                        dissmisDialog();
                    }
                }

                @Override
                public void onFailure(Call<CategoriesModel> call, Throwable t) {
                    AndroidUtilities.showDialog(HomeActivity.this, R.string.str_message_internet_error);
                    dissmisDialog();
                }
            });
        }
        else {
            AndroidUtilities.showDialog(HomeActivity.this, R.string.str_message_internet_refused);
            dissmisDialog();
        }

    }

    private void listener(){

        activity_home_vp_slideshow.addOnPageChangeListener(new OnPageChangeListener() {

            boolean isItemLast = false;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == lastNewsData.size() - 1 && isItemLast) {
                    isItemLast = false;
                    activity_home_vp_slideshow.setCurrentItem(0, false);
                } else if(position == lastNewsData.size() - 1 && !isItemLast) {
                    isItemLast = true;
                }
            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {}

        });


        activity_home_rv_list.addOnScrollListener(new RecylerScrollListener(){
            @Override
            public void onLoadMore() {
                int a = currentNewsPage + 1;
                if(a<=limitNewsPage){
                    getNews(a, false, newsCurrentCategory);
                }
            }
        });

    }

    private void dissmisDialog(){
        if(materialDialog != null){
            materialDialog.dismiss();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        // for disable to activity default animation
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.activity_home_item_refresh){
            getNews(1, true, newsCurrentCategory);
            getNews(2, true, newsCurrentCategory);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        newsCurrentCategory = categoriesIds.get(position);
        getNews(1, true, newsCurrentCategory);
        getNews(2, true, newsCurrentCategory);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    @Override
    public void onState(int position) {
        currentScrollPostion = position;
    }
}
