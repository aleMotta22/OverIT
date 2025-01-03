package it.motta.overit.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.motta.overit.R;
import it.motta.overit.adapter.AdapterImageList;
import it.motta.overit.controllers.OverItController;
import it.motta.overit.databinding.ActivityMainBinding;
import it.motta.overit.databinding.ContentMainBinding;
import it.motta.overit.dialogs.DetailsImageDialog;
import it.motta.overit.interfaces.IloadImagesListner;
import it.motta.overit.models.FilterImage;
import it.motta.overit.models.FlickerImage;
import it.motta.overit.workers.LoadImageWorkers;

public class MainActivity extends AppCompatActivity implements IloadImagesListner {

    private ActivityMainBinding bindingMain;
    private ContentMainBinding bindingContent;
    private AdapterImageList adapter;
    private HashMap<Long, Bitmap> bitmapImag;

    private FilterImage filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindingMain = ActivityMainBinding.inflate(getLayoutInflater());
        bindingContent = ContentMainBinding.bind(bindingMain.getRoot());
        EdgeToEdge.enable(this);
        setContentView(bindingMain.getRoot());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        filter = new FilterImage("", new ArrayList<>());
        bitmapImag = new HashMap<>();
        bindingContent.searchBar.setOnKeyListener((v, actionId, keyEvent) -> {
            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && actionId == KeyEvent.KEYCODE_ENTER) {
                if (filter.setSearchText(bindingContent.searchBar.getText().toString().trim())) {
                    adapter.clear();
                    bitmapImag.clear();
                    new LoadImageWorkers(filter, this).execute();
                }
            }
            return false;
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.search));
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        adapter = new AdapterImageList(new ArrayList<>(), v -> new DetailsImageDialog(this, adapter.getItemClicked(), bitmapImag).show(), true);
        bindingContent.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!filter.isFirstLoad()) {
                    filter.increasePage();
                    if (!recyclerView.canScrollVertically(1) && filter.canLoadMore()) {
                        new LoadImageWorkers(filter, MainActivity.this).execute();
                    }
                }
            }
        });
        bindingContent.swipeRefresh.setOnRefreshListener(() -> {
            adapter.clear();
            filter.resetPage();
            new LoadImageWorkers(filter, this).execute();
        });
        load();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("filter", filter);
        outState.putSerializable("position", getPosLastView());
        outState.putSerializable("itemsLoad", adapter.getItems());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.filter = (FilterImage) savedInstanceState.getSerializable("filter");
        adapter.addItems((ArrayList<FlickerImage>) savedInstanceState.getSerializable("itemsLoad"));
        load();
        if (adapter.getItemCount() > 0)
            bindingContent.recyclerView.smoothScrollToPosition(savedInstanceState.getInt("position"));
    }

    private void load() {
        bindingContent.recyclerView.setLayoutManager(OverItController.GetLastGridView(this) ? new GridLayoutManager(this, 3) : new LinearLayoutManager(this));
        adapter.updateGrid(OverItController.GetLastGridView(this));
        bindingContent.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onSuccess(List<FlickerImage> images, int totalPages) {
        adapter.addItems(images);
        bindingContent.swipeRefresh.setRefreshing(false);
        filter.setTotalPages(totalPages);
        filter.setFirstLoad(false);
    }

    @Override
    public void onLoadStart() {
        bindingContent.swipeRefresh.setRefreshing(true);
    }

    @Override
    public void onError(String msg) {
        bindingContent.swipeRefresh.setRefreshing(false);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        menu.findItem(R.id.gridList).setIcon(OverItController.GetLastGridView(this) ? R.drawable.ic_list : R.drawable.ic_grid);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.gridList) {
            OverItController.SaveLastGridView(this, !OverItController.GetLastGridView(this));
            item.setIcon(OverItController.GetLastGridView(this) ? R.drawable.ic_list : R.drawable.ic_grid);
            int pos = getPosLastView();
            load();
            if (adapter.getItemCount() > 0)
                bindingContent.recyclerView.smoothScrollToPosition(pos);
        }
        return false;
    }


    private int getPosLastView() {
        int pos = 0;
        if (adapter.getItemCount() > 0) {
            if (bindingContent.recyclerView.getLayoutManager() instanceof LinearLayoutManager)
                pos = ((LinearLayoutManager) bindingContent.recyclerView.getLayoutManager()).findLastVisibleItemPosition();
            else if (bindingContent.recyclerView.getLayoutManager() instanceof GridLayoutManager)
                pos = ((GridLayoutManager) bindingContent.recyclerView.getLayoutManager()).findLastVisibleItemPosition();
        }
        return pos;
    }

}