package it.motta.overit.activity;

import android.app.Application;

import it.motta.overit.BuildConfig;
import it.motta.overit.models.FlickrAuth;

public class OverItApp extends Application {

    public static FlickrAuth auth = new FlickrAuth(BuildConfig.KEY_FLICKR);

}