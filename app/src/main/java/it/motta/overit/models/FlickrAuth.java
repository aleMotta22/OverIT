package it.motta.overit.models;

import java.io.Serializable;

public class FlickrAuth implements Serializable {

    private String key;

    public FlickrAuth(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }


}