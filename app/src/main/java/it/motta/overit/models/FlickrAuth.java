package it.motta.overit.models;

public class FlickrAuth {

    private String key,segret;

    public FlickrAuth(String key, String segret){
        this.key = key;
        this.segret = segret;
    }

    public String getKey(){
        return key;
    }

    public String getSegret(){
        return segret;
    }

}