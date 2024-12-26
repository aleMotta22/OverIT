package it.motta.overit.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;

import java.util.HashMap;

import it.motta.overit.databinding.DialogDetailImageBinding;
import it.motta.overit.models.FlickerImage;

public class DetailsImageDialog extends Dialog {

    private DialogDetailImageBinding binding;
    private FlickerImage image;
    private HashMap<Long, Bitmap> bitmaps;

    public DetailsImageDialog(@NonNull Context context, FlickerImage image, HashMap<Long, Bitmap> bitmaps) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DialogDetailImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}