package com.app.t24.model.entity;

/**
 * Created by alican on 16.04.2017.
 */


public class NewsCategoriesItem {

    public String postId;
    public String postTitle;
    public String postImage;

    public NewsCategoriesItem(String id, String title, String image) {
        this.postId = id;
        this.postTitle = title;
        this.postImage = image;
    }

    public String getPostId() { return postId;}
    public String getPostTitle() { return postTitle;}
    public String getPostImage() { return postImage;}
}
