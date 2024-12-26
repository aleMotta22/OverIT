package it.motta.overit.controllers;

import android.content.ContentValues;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import it.motta.overit.models.FlickrAuth;
import it.motta.overit.models.Response;

public class ApiFlickrController {

    private static final String PARAM_KEY = "oauth_consumer_key";
    private static final String PARAM_TOKEN = "oauth_token";
    private static final String PARAM_SIGNATURE_METHOD = "oauth_signature_method";
    private static final String PARAM_TIMESTAMP = "oauth_timestamp";
    private static final String PARAM_NONCE = "oauth_nonce";
    private static final String PARAM_VERSION = "oauth_version";
    private static final String PARAM_SIGNATURE = "oauth_signature";
    private static final String PARAM_CALLBACK = "oauth_callback";
    private static final String HMAC_SHA1 = "HmacSHA1";

    private static final String BASE_ENDPOINT = "https://www.flickr.com/services/";

    private static final String AUTH = BASE_ENDPOINT + "oauth/";

    private static final String REQUEST_TOKEN = AUTH + "request_token";

    private static final String AUTHORIZE = AUTH + "authorize";


    private static ContentValues tokenValues(FlickrAuth flickrAuth) {
        ContentValues cv = new ContentValues();
        cv.put(PARAM_NONCE, "" + (int) (Math.random() * 100000000));
        cv.put(PARAM_TIMESTAMP, System.currentTimeMillis() / 1000);
        cv.put(PARAM_KEY, flickrAuth.getKey());
        cv.put(PARAM_SIGNATURE_METHOD, HMAC_SHA1);
        cv.put(PARAM_VERSION, "1.0");
        cv.put(PARAM_SIGNATURE, flickrAuth.getSegret());
        cv.put(PARAM_CALLBACK, "oob");
        return cv;
    }

    public static Response RequestToken(FlickrAuth flickrAuth) throws IOException {
        URL url = new URL(REQUEST_TOKEN);
        HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
        httpsURLConnection.setRequestMethod("GET");
        ContentValues contentValues = tokenValues(flickrAuth)


    }


}