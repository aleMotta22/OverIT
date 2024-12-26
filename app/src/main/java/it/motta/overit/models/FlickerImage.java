package it.motta.overit.models;

import android.graphics.Bitmap;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class FlickerImage implements Serializable {

    private static final String URL_IMAGE_FORMAT = "https://live.staticflickr.com/%s/%s_%s_%s.jpg";

    private long id;
    private int server, farm;
    private final String owner, secret, title;
    private String description, username, taken;

    public FlickerImage(JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.getLong("id");
        this.server = jsonObject.getInt("server");
        this.farm = jsonObject.getInt("farm");
        this.secret = jsonObject.getString("secret");
        this.owner = jsonObject.getString("owner");
        this.title = jsonObject.getString("title");
    }

    public long getId() {
        return id;
    }

    public FlickerImage(long id, int server, int farm, String owner, String secret, String title) {
        this.id = id;
        this.server = server;
        this.farm = farm;
        this.owner = owner;
        this.secret = secret;
        this.title = title;
    }

    public URL getUrlPreview() throws MalformedURLException {
        return new URL(String.format(URL_IMAGE_FORMAT, server, id, secret, "q"));
    }

    public URL getUrlImage() throws MalformedURLException {
        return new URL(String.format(URL_IMAGE_FORMAT, server, id, secret, "b"));
    }

    public String getTitle() {
        return title;
    }

    public void setDetail(JSONObject jsonObject) throws JSONException {
        JSONObject data = jsonObject.has("owner") ? jsonObject.getJSONObject("owner") : null;
        if (data != null && data.has("username"))
            this.username = data.getString("username");
        data = jsonObject.has("description") ? jsonObject.getJSONObject("description") : null;
        if (data != null && data.has("_content"))
            this.description = jsonObject.getString("_content");

        data = jsonObject.has("dates") ? jsonObject.getJSONObject("dates") : null;
        if (data != null && data.has("taken"))
            this.taken = jsonObject.getString("taken");

    }

    public HashMap<String, String> getParam() {
        HashMap<String, String> params = new HashMap<>();
        params.put("photo_id", String.valueOf(id));
        return params;
    }

}