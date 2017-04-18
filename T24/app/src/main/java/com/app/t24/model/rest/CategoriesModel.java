package com.app.t24.model.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by alican on 16.04.2017.
 */

public class CategoriesModel {

    @SerializedName("result")
    @Expose
    public Boolean result;
    @SerializedName("data")
    @Expose
    public List<Datum> data = null;

    public class Datum {

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
