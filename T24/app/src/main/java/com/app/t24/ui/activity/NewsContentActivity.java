package com.app.t24.ui.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.app.t24.R;
import com.app.t24.model.entity.NewsCategoriesItem;
import com.app.t24.model.rest.NewsCategoriesModel;
import com.app.t24.model.rest.NewsContentModel;
import com.app.t24.ui.adapter.NewsAdapter;
import com.app.t24.utils.AndroidUtilities;
import com.app.t24.utils.RecylerScrollListener;
import com.app.t24.utils.RestClient;
import com.app.t24.utils.RestEndPoint;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.facebook.share.widget.ShareDialog.Mode;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;
import com.zplesac.connectionbuddy.ConnectionBuddy;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsContentActivity extends AppCompatActivity {

    // Set all view with Butterknife http://jakewharton.github.io/butterknife/
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.activity_news_content_tv_head)
    TextView activity_news_content_tv_head;
    @BindView(R.id.activity_news_content_tv_content)
    TextView activity_news_content_tv_content;
    @BindView(R.id.activity_news_content_iv_image)
    ImageView activity_news_content_iv_image;
    @BindView(R.id.activity_news_content_rv_list)
    RecyclerView activity_news_content_rv_list;
    @BindView(R.id.activity_news_content_tv_category)
    TextView activity_news_content_tv_category;

    // current news page in recylerview list
    private static Integer currentNewsPage;

    // limit of news pages
    private static Integer limitNewsPage;

    // selected news category
    private static String currentNewsCategory;

    private RecyclerView.Adapter mAdapter;
    private ArrayList<Object> mData;

    // news web url for sharing
    private String newsUrl = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_content);
        ButterKnife.bind(this);

        // init view and get news data
        init();

        // all listener here
        listener();

    }

    private void init(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // get news id from previous avtivity
        String newsId = null;
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            newsId = extras.getString("newsId");
        }

        mData = new ArrayList<Object>();
        mAdapter = new NewsAdapter(this, mData, null);

        activity_news_content_rv_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        activity_news_content_rv_list.setAdapter(mAdapter);

        // get selected news with newsId
        getNews(newsId);
    }


    private void getNews(String param) {

        if (ConnectionBuddy.getInstance().hasNetworkConnection()) {

            // start loading dialog
            final MaterialDialog materialDialog = AndroidUtilities.showProgressDialog(NewsContentActivity.this, R.string.str_uploading);
            materialDialog.show();

            RestEndPoint retroInterface = RestClient.createService(RestEndPoint.class);
            Call<NewsContentModel> call = retroInterface.getNewsContent(param);
            call.enqueue(new Callback<NewsContentModel>() {

                @Override
                public void onResponse(Call<NewsContentModel> call, Response<NewsContentModel> response) {
                    try {
                        if (response.body() != null) {

                            newsUrl = response.body().data.urls.web;

                            Picasso.with(NewsContentActivity.this).load("http://" + Html.fromHtml(response.body().data.images.page).toString().replaceAll("//", "")).into(activity_news_content_iv_image);
                            activity_news_content_tv_head.setText(AndroidUtilities.decodeString(response.body().data.title));
                            activity_news_content_tv_content.setText(AndroidUtilities.decodeString(response.body().data.text));
                            activity_news_content_tv_category.setText(AndroidUtilities.decodeString(response.body().data.category.name));

                            currentNewsCategory = response.body().data.category.id;

                            // get category news with category id
                            getCategoryNews(currentNewsCategory, 1);
                            materialDialog.dismiss();

                        }
                    } catch (NullPointerException ex) {
                        AndroidUtilities.showDialog(NewsContentActivity.this, R.string.str_message_internet_error);
                        materialDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<NewsContentModel> call, Throwable t) {
                    AndroidUtilities.showDialog(NewsContentActivity.this, R.string.str_message_internet_error);
                    materialDialog.dismiss();
                }
            });

        }
        else {
            AndroidUtilities.showDialog(NewsContentActivity.this, R.string.str_message_internet_refused);
        }

    }

    private void getCategoryNews(String param, Integer page) {

        // param: category id
        // page: news page

        if (ConnectionBuddy.getInstance().hasNetworkConnection()) {

            RestEndPoint retroInterface = RestClient.createService(RestEndPoint.class);
            Call<NewsCategoriesModel> call = retroInterface.getNewsCategoryList(param, String.valueOf(page));
            call.enqueue(new Callback<NewsCategoriesModel>() {

                @Override
                public void onResponse(Call<NewsCategoriesModel> call, Response<NewsCategoriesModel> response) {
                    try {
                        if (response.body() != null) {
                            NewsCategoriesModel item = response.body();

                            currentNewsPage = item.paging.current;
                            limitNewsPage = item.paging.pages;

                            for (int i = 0; i < item.data.size(); i++) {
                                mData.add(new NewsCategoriesItem(item.data.get(i).id, Html.fromHtml(item.data.get(i).title).toString(), item.data.get(i).images.list.replaceAll("//", "")));
                            }
                            mAdapter.notifyItemInserted(mData.size() - 1);
                            mAdapter.notifyDataSetChanged();
                        }
                    } catch (NullPointerException ex) {
                        AndroidUtilities.showDialog(NewsContentActivity.this, R.string.str_message_internet_error);
                    }
                }

                @Override
                public void onFailure(Call<NewsCategoriesModel> call, Throwable t) {
                    AndroidUtilities.showDialog(NewsContentActivity.this, R.string.str_message_internet_error);
                }
            });
        }
        else {
            AndroidUtilities.showDialog(NewsContentActivity.this, R.string.str_message_internet_refused);
        }

    }


    // share news on facebook
    private void shareFacebook(){

        if (ConnectionBuddy.getInstance().hasNetworkConnection()) {
            if (newsUrl != null) {
                ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse(newsUrl))
                        .build();

                ShareDialog shareDialog = new ShareDialog(NewsContentActivity.this);
                shareDialog.show(content, Mode.AUTOMATIC);
            }
        }
        else {
            AndroidUtilities.showDialog(NewsContentActivity.this, R.string.str_message_internet_refused);
        }
    }

    // share news on twitter
    private void shareTwitter(){

        if (ConnectionBuddy.getInstance().hasNetworkConnection()) {
            if (newsUrl != null) {
                TweetComposer.Builder builder = null;
                try {
                    builder = new TweetComposer.Builder(this)
                            .url(new URL(newsUrl));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                builder.show();
            }
        }
        else {
            AndroidUtilities.showDialog(NewsContentActivity.this, R.string.str_message_internet_refused);
        }

    }

    private void listener(){
        activity_news_content_rv_list.addOnScrollListener(new RecylerScrollListener(){
            @Override
            public void onLoadMore() {
                int a = currentNewsPage + 1;
                if(a<=limitNewsPage){
                    getCategoryNews(currentNewsCategory,a);
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        // for disable to activity default animation
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_news_content_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_new_content_facebook:
                shareFacebook();
                return true;
            case R.id.menu_new_content_twitter:
                shareTwitter();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
