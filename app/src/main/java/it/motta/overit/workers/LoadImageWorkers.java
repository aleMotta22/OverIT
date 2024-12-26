package it.motta.overit.workers;

import android.os.AsyncTask;

import org.json.JSONArray;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import it.motta.overit.controllers.ApiFlickrController;
import it.motta.overit.enums.InternalError;
import it.motta.overit.interfaces.IloadImagesListner;
import it.motta.overit.models.FilterImage;
import it.motta.overit.models.FlickerImage;
import it.motta.overit.models.Response;

public class LoadImageWorkers extends AsyncTask<Void, Void, Response> {

    private final FilterImage filter;
    private final IloadImagesListner listener;

    public LoadImageWorkers(FilterImage filter, IloadImagesListner listener) {
        this.filter = filter;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        listener.onLoadStart();
    }

    @Override
    protected Response doInBackground(Void... voids) {
        try {
            return ApiFlickrController.ListOfImage(filter);
        } catch (IOException e) {
            return new Response(InternalError.GENERIC_ERROR.getCode(), null, e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(Response response) {
        super.onPostExecute(response);
        if (response.getCode() == HttpURLConnection.HTTP_OK) {
            try {
                JSONArray jsonArray = response.getJsonObject().getJSONObject("photos").getJSONArray("photo");
                ArrayList<FlickerImage> filterImages = new ArrayList<>(jsonArray.length());
                for (int i = 0; i < jsonArray.length(); i++)
                    filterImages.add(new FlickerImage(jsonArray.getJSONObject(i)));
                if (!filterImages.isEmpty()) {
                    listener.onSuccess(filterImages, response.getJsonObject().getJSONObject("photos").getInt("pages"));
                    return;
                }
                listener.onError("No images found");
            } catch (Exception ex) {
                response = new Response(InternalError.GENERIC_ERROR.getCode(), null, ex.getMessage());
            }
        }
        listener.onError(response.getMessage());
    }

}