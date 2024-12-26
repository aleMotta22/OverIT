package it.motta.overit.interfaces;

import java.util.List;

import it.motta.overit.models.FlickerImage;

public interface IloadImagesListner {

    void onSuccess(List<FlickerImage> images,int totalPages);

    void onLoadStart();

    void onError(String msg);

}