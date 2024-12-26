package it.motta.overit.activity;

import it.motta.overit.BuildConfig;
import it.motta.overit.models.FlickrAuth;

public class OverItApp {

    public static FlickrAuth auth = new FlickrAuth(BuildConfig.KEY_FLICKR);

}
