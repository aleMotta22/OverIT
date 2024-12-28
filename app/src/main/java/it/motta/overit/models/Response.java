package it.motta.overit.models;

import org.json.JSONObject;

public class Response {

    private final int code;
    private final JSONObject jsonObject;
    private final String message;

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