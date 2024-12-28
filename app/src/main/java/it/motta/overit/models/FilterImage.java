package it.motta.overit.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class FilterImage implements Serializable {

    private static final int MAX_PER_PAGE = 50;

    private final int perPage;
    private final List<String> tags;

    private int currPage,totalPages;
    private String searchText;
    private boolean isFirstLoad;

    public FilterImage(String searchText, List<String> tags) {
        this.currPage = 1;
        this.perPage = MAX_PER_PAGE;
        this.searchText = searchText;
        this.tags = tags;
        this.totalPages = 0;
        this.isFirstLoad = true;
    }

    public boolean isFirstLoad() {
        return isFirstLoad;
    }

    public void setFirstLoad(boolean isFirstLoad) {
        this.isFirstLoad = isFirstLoad;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public HashMap<String, String> getParams() {
        HashMap<String, String> params = new HashMap<>();

        if (!searchText.isEmpty())
            params.put("text", searchText);

        if (!tags.isEmpty())
            params.put("tags", String.join(",", tags));
        params.put("page", String.valueOf(currPage));
        params.put("per_page", String.valueOf(perPage));
        return params;
    }

    public void increasePage() {
        currPage++;
    }

    public void reset() {
        currPage = 1;
        tags.clear();
        searchText = "";
        totalPages = 0;
    }

    public void resetPage() {
        currPage = 1;
    }

    public boolean canLoadMore() {
        return currPage <= totalPages;
    }

    public boolean setSearchText(String searchText) {
        boolean isChanged = false;
        if (!searchText.equalsIgnoreCase(this.searchText)) {
            reset();
            isChanged = true;
        }
        this.searchText = searchText;
        return isChanged;
    }

}