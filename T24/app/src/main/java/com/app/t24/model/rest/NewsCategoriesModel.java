package com.app.t24.model.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by alican on 17.04.2017.
 */

public class NewsCategoriesModel {


    @SerializedName("data")
    @Expose
    public List<Datum> data = null;
    @SerializedName("paging")
    @Expose
    public Paging paging;
    @SerializedName("result")
    @Expose
    public Boolean result;

    public class Datum {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("title")
        @Expose
        public String title;
        @SerializedName("excerpt")
        @Expose
        public String excerpt;
        @SerializedName("alias")
        @Expose
        public String alias;
        @SerializedName("urls")
        @Expose
        public Urls urls;
        @SerializedName("category")
        @Expose
        public Category category;
        @SerializedName("stats")
        @Expose
        public Stats stats;
        @SerializedName("images")
        @Expose
        public Images images;
        @SerializedName("publishingDate")
        @Expose
        public String publishingDate;

        public class Category {

            @SerializedName("id")
            @Expose
            public String id;
            @SerializedName("name")
            @Expose
            public String name;
            @SerializedName("alias")
            @Expose
            public String alias;

        }

        public class Images {

            @SerializedName("list")
            @Expose
            public String list;
            @SerializedName("box")
            @Expose
            public String box;
            @SerializedName("page")
            @Expose
            public String page;
            @SerializedName("grid")
            @Expose
            public String grid;

        }

        public class Stats {

            @SerializedName("likes")
            @Expose
            public Integer likes;
            @SerializedName("comments")
            @Expose
            public Integer comments;
            @SerializedName("shares")
            @Expose
            public Integer shares;
            @SerializedName("interactions")
            @Expose
            public Integer interactions;
            @SerializedName("reads")
            @Expose
            public Integer reads;
            @SerializedName("pageviews")
            @Expose
            public Integer pageviews;

        }

        public class Urls {

            @SerializedName("web")
            @Expose
            public String web;

        }
    }

    public class Paging {

        @SerializedName("current")
        @Expose
        public Integer current;
        @SerializedName("limit")
        @Expose
        public Integer limit;
        @SerializedName("pages")
        @Expose
        public Integer pages;
        @SerializedName("items")
        @Expose
        public Integer items;

    }
}
