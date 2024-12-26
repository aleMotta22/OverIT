package it.motta.overit.adapter;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.motta.overit.R;
import it.motta.overit.databinding.FlickerImageGridBinding;
import it.motta.overit.databinding.FlickerImageListBinding;
import it.motta.overit.models.FlickerImage;
import it.motta.overit.models.IloadImage;

public class AdapterImageList extends RecyclerView.Adapter<AdapterImageList.ViewHolder> implements View.OnClickListener {

    private boolean isGrid;

    private final ArrayList<FlickerImage> flickerImages;
    private final HashMap<Long, Bitmap> bitmapPreview, bitmapImage;
    private final View.OnClickListener handler;
    protected int lastClicked;

    public AdapterImageList(ArrayList<FlickerImage> flickerImages, View.OnClickListener handler, boolean isGrid) {
        this.flickerImages = flickerImages;
        this.handler = handler;
        this.bitmapPreview = new HashMap<>();
        this.bitmapImage = new HashMap<>();
        this.lastClicked = -1;
        this.isGrid = isGrid;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = isGrid ? FlickerImageGridBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false).getRoot() : FlickerImageListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false).getRoot();
        return new ViewHolder(root);
    }

    public void updateGrid(boolean isGrid) {
        this.isGrid = isGrid;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FlickerImage image = getItem(position);
        holder.cardMain.setOnClickListener(this);
        holder.cardMain.setTag(position);
        holder.imgLink.setVisibility(View.INVISIBLE);
        new Thread(new IloadImage(holder.cardMain.getContext(), image, bitmapPreview, IloadImage.LoadType.PREVIEW, holder.imgLink)).start();
        holder.title.setText(image.getTitle());
    }

    @Override
    public int getItemCount() {
        return flickerImages.size();
    }

    public void addItems(List<FlickerImage> images) {
        flickerImages.addAll(images);
        notifyDataSetChanged();
    }

    public ArrayList<FlickerImage> getItems() {
        return flickerImages;
    }

    public void clear() {
        flickerImages.clear();
        lastClicked = -1;
        this.bitmapPreview.clear();
        this.bitmapImage.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        lastClicked = (int) v.getTag();
        handler.onClick(v);
    }

    protected FlickerImage getItem(int pos) {
        return pos >= 0 && pos < flickerImages.size() ? flickerImages.get(pos) : null;
    }

    public FlickerImage getItemClicked() {
        try {
            return getItem(lastClicked);
        } finally {
            lastClicked = -1;
        }
    }


    protected class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imgLink;
        private final TextView title;
        private final CardView cardMain;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgLink = itemView.findViewById(R.id.imgLink);
            cardMain = itemView.findViewById(R.id.cardMain);
            title = itemView.findViewById(R.id.title);
        }

    }

}