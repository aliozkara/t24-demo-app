package com.app.t24.model.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alican on 16.04.2017.
 */

public class NewsContentModel {

    @SerializedName("result")
    @Expose
    public Boolean result;
    @SerializedName("data")
    @Expose
    public Data data;

    public class Data {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("title")
        @Expose
        public String title;
        @SerializedName("alias")
        @Expose
        public String alias;
        @SerializedName("urls")
        @Expose
        public Urls urls;
        @SerializedName("category")
        @Expose
        public Category category;
        @SerializedName("images")
        @Expose
        public Images images;
        @SerializedName("author")
        @Expose
        public Author author;
        @SerializedName("publishingDate")
        @Expose
        public String publishingDate;
        @SerializedName("excerpt")
        @Expose
        public String excerpt;
        @SerializedName("text")
        @Expose
        public String text;
        @SerializedName("stats")
        @Expose
        public Stats stats;

        public class Urls {

            @SerializedName("web")
            @Expose
            public String web;

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

        public class Author {

            @SerializedName("name")
            @Expose
            public Object name;
            @SerializedName("alias")
            @Expose
            public Object alias;
            @SerializedName("images")
            @Expose
            public Images_ images;

            public class Images_ {

                @SerializedName("page")
                @Expose
                public String page;
                @SerializedName("grid")
                @Expose
                public String grid;
                @SerializedName("list")
                @Expose
                public String list;

            }
        }

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
    }


}
