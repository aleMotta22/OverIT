package it.motta.overit.models;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

import it.motta.overit.R;

public class IloadImage implements Runnable {

    public enum LoadType {
        PREVIEW, IMAGE
    }

    private final Context mContext;
    private final FlickerImage flickImage;
    private final LoadType loadType;
    private final HashMap<Long, Bitmap> bitmaps;
    private final ImageView imageView;

    public IloadImage(Context mContext, FlickerImage flickImage, HashMap<Long, Bitmap> bitmaps, LoadType loadType, ImageView imageView) {
        this.mContext = mContext;
        this.flickImage = flickImage;
        this.loadType = loadType;
        this.bitmaps = bitmaps;
        this.imageView = imageView;
    }

    @Override
    public void run() {
        Bitmap bitmap = bitmaps.get(flickImage.getId());
        if (bitmap != null) {
            ((Activity) mContext).runOnUiThread(() -> {
                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(View.VISIBLE);
            });
            return;
        }
        try {
            URL url = loadType.equals(LoadType.PREVIEW) ? flickImage.getUrlPreview() : flickImage.getUrlImage();
            InputStream in = url.openStream();
            Bitmap mIcon = BitmapFactory.decodeStream(in);
            ((Activity) mContext).runOnUiThread(() -> {
                imageView.setImageBitmap(mIcon);
                imageView.setVisibility(View.VISIBLE);
            });
            bitmaps.put(flickImage.getId(), mIcon);
        } catch (Exception e) {
            ((Activity) mContext).runOnUiThread(() -> {
                imageView.setImageDrawable(new ContextWrapper(mContext).getDrawable(R.drawable.ic_alert));
                imageView.setVisibility(View.VISIBLE);
            });
            e.printStackTrace();
        }
    }

}