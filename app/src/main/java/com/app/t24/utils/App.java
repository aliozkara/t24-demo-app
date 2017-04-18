package com.app.t24.utils;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.zplesac.connectionbuddy.ConnectionBuddy;
import com.zplesac.connectionbuddy.ConnectionBuddyConfiguration;

import io.fabric.sdk.android.Fabric;

/**
 * Created by alican on 14.04.2017.
 */

public class App extends MultiDexApplication {


    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "LqdOY9Wunte9hHQlSMLoDUPOi";
    private static final String TWITTER_SECRET = "cVy1XHMdz6JKxA4Md6TOx4V7px3QZn127Oz8tLdGxiYZHnoMqf";


    @Override
    public void onCreate() {
        super.onCreate();

        // twitter sdk configuration
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        ConnectionBuddyConfiguration networkInspectorConfiguration = new ConnectionBuddyConfiguration.Builder((Application)this).build();
        ConnectionBuddy.getInstance().init(networkInspectorConfiguration);
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
