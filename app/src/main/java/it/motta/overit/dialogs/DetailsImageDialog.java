package it.motta.overit.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.HashMap;

import it.motta.overit.databinding.DialogDetailImageBinding;
import it.motta.overit.interfaces.ICompleteLoading;
import it.motta.overit.models.FlickerImage;
import it.motta.overit.models.IloadImage;
import it.motta.overit.workers.GetInfoWorkers;

public class DetailsImageDialog extends Dialog implements ICompleteLoading {

    private final Context mContext;
    private final FlickerImage image;
    private final HashMap<Long, Bitmap> bitmaps;

    private DialogDetailImageBinding binding;

    public DetailsImageDialog(@NonNull Context mContext, FlickerImage image, HashMap<Long, Bitmap> bitmaps) {
        super(mContext);
        this.mContext = mContext;
        this.bitmaps = bitmaps;
        this.image = image;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DialogDetailImageBinding.inflate(getLayoutInflater());
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(binding.getRoot());
        new Thread(new IloadImage(mContext, image, bitmaps, IloadImage.LoadType.IMAGE, binding.imageMain)).start();
        new GetInfoWorkers(image, this).execute();
        binding.llDesc.setVisibility(View.GONE);
        binding.close.setOnClickListener(v -> dismiss());
    }

    @Override
    public void complete() {
        binding.titleImage.setText(image.getTitle());
        binding.descImage.setText(image.getDescription());
        binding.llDesc.setVisibility(image.getDescription().isEmpty() ? View.GONE : View.VISIBLE);
        binding.owner.setText(image.getUsername());
        binding.taken.setText(image.getTaken());
    }

    @Override
    public void error(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }
}