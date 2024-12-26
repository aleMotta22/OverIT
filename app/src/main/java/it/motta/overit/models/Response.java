package it.motta.overit.models;

import org.json.JSONObject;

public class Response {

    private int code;
    private JSONObject jsonObject;
    private String message;

    public Response(int code, JSONObject jsonObject, String message) {
        this.code = code;
        this.jsonObject = jsonObject;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public String getMessage() {
        return message;
    }

}