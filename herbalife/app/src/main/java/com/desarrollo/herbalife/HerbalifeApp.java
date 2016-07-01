package com.desarrollo.herbalife;

import android.content.Context;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.orm.SugarApp;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;

/**
 * Created by oswaldo on 4/02/16.
 */
public class HerbalifeApp extends SugarApp{

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "UrYGZNuJbCiduKL58OCtyGpBx";
    private static final String TWITTER_SECRET = "mranizr3rIV7cqMR0KKSdlFc6FNym2zXFmEROYZgxwxRSJjLbx";
    public static final String EVENT_CLICK = "click";

    private static Tracker mTracker;
    public static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        FacebookSdk.sdkInitialize(getApplicationContext());
        context = getApplicationContext();
    }

    synchronized private static Tracker getTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(context);
            analytics.setLocalDispatchPeriod(20);
            mTracker = analytics.newTracker("UA-72614548-1");
        }
        return mTracker;
    }

    public static void eventTracking(String category, String action , String label) {
        Log.d("TRACKING -> ", label);
        getTracker().send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .setLabel(label)
                .build());
    }

    public static void viewTracking(String screenName) {
        Log.d("TRACKING -> ", screenName);
        getTracker().setScreenName(screenName);
        getTracker().send(new HitBuilders.ScreenViewBuilder().build());
    }

}
