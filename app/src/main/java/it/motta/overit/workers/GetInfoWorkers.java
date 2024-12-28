package it.motta.overit.workers;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;

import it.motta.overit.controllers.ApiFlickrController;
import it.motta.overit.enums.InternalError;
import it.motta.overit.interfaces.ICompleteLoading;
import it.motta.overit.models.FlickerImage;
import it.motta.overit.models.Response;

public class GetInfoWorkers extends AsyncTask<Void, Void, Response> {

    private final FlickerImage image;
    private final ICompleteLoading listener;

    public GetInfoWorkers(FlickerImage image, ICompleteLoading listener) {
        super();
        this.image = image;
        this.listener = listener;
    }

    @Override
    protected Response doInBackground(Void... voids) {
        try {
            return ApiFlickrController.GetInfoImage(image);
        } catch (IOException e) {
            return new Response(InternalError.GENERIC_ERROR.getCode(), null, e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(Response response) {
        super.onPostExecute(response);
        if (response.getCode() == HttpURLConnection.HTTP_OK) {
            try {
                image.setDetail(response.getJsonObject().getJSONObject("photo"));
                listener.complete();
                return;
            } catch (Exception ex) {
                response = new Response(InternalError.GENERIC_ERROR.getCode(), null, ex.getMessage());
            }
        }
        listener.error(response.getMessage());
    }

}