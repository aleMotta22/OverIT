package it.motta.overit.controllers;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import it.motta.overit.activity.OverItApp;
import it.motta.overit.models.FilterImage;
import it.motta.overit.models.FlickerImage;
import it.motta.overit.models.Response;

public class ApiFlickrController {


    private static final String PROTOCOL = "https://";
    private static final String BASE_ENDPOINT = PROTOCOL + "www.flickr.com/services/";
    private static final String REST = BASE_ENDPOINT + "rest";

    private static final String METHOD_SEARCH_PHOTOS = "flickr.photos.search";
    private static final String METHOD_INFO_PHOTO = "flickr.photos.getInfo";
    private static final String FORMAT = "json";

    public static Response ListOfImage(FilterImage filter) throws IOException {
        HashMap<String, String> params = new HashMap<>();
        params.put("format", FORMAT);
        params.put("method", METHOD_SEARCH_PHOTOS);
        params.put("api_key", OverItApp.auth.getKey());
        params.putAll(filter.getParams());
        URL url = new URL(REST + "?" + loadParam(params));
        HttpsURLConnection httpURLConnection = (HttpsURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        Log.i("STATUS", String.valueOf(httpURLConnection.getResponseCode()));
        Log.i("MSG", httpURLConnection.getResponseMessage());
        JSONObject jsonObject = traduceObject(httpURLConnection);
        return new Response(httpURLConnection.getResponseCode(), jsonObject, httpURLConnection.getResponseMessage());
    }

    public static Response GetInfoImage(FlickerImage image) throws IOException {
        HashMap<String, String> params = new HashMap<>();
        params.put("format", FORMAT);
        params.put("method", METHOD_INFO_PHOTO);
        params.put("api_key", OverItApp.auth.getKey());
        params.putAll(image.getParam());
        URL url = new URL(REST + "?" + loadParam(params));
        HttpsURLConnection httpURLConnection = (HttpsURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        Log.i("STATUS", String.valueOf(httpURLConnection.getResponseCode()));
        Log.i("MSG", httpURLConnection.getResponseMessage());
        JSONObject jsonObject = traduceObject(httpURLConnection);
        return new Response(httpURLConnection.getResponseCode(), jsonObject, httpURLConnection.getResponseMessage());
    }

    private static JSONObject traduceObject(HttpsURLConnection httpURLConnection) {
        StringBuilder result = new StringBuilder();
        JSONObject jsonObject = null;
        BufferedReader bufferedReader;
        try {
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
                bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            else
                bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream()));

            for (String line; (line = bufferedReader.readLine()) != null; )
                result.append(line);

            try {
                jsonObject = new JSONObject(result.toString().substring(14, result.length() - 1));
            } catch (Exception ex) {
                if (result.toString().startsWith("{") || result.toString().startsWith("["))
                    jsonObject = new JSONObject("{\"result\": " + result + "}");
                else
                    jsonObject = new JSONObject("{\"result\": \"" + result + "\"}");
            }
        } catch (Exception ex) {
        }
        return jsonObject;
    }


    private static String loadParam(HashMap<String, String> params) {
        StringBuilder result = new StringBuilder();
        params.forEach((key, value) -> {
            try {
                result.append(URLEncoder.encode(key, "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(value, "UTF-8"));
                result.append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });
        return result.toString().substring(0, result.length() - 1);
    }

}