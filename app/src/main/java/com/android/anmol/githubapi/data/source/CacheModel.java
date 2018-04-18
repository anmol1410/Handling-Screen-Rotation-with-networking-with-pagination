package com.android.anmol.githubapi.data.source;

import java.util.Objects;

/**
 * Model for cache purposes.
 * Used as the key to identify response for there Request Data.
 */
public class CacheModel {

    /**
     * Query Param to identify the request from.
     */
    private String mQueryText;

    /**
     * Page count to identify the request from.
     */
    private Integer mPageCount;

    CacheModel(String queryText, Integer pageCount) {
        this.mQueryText = queryText;
        this.mPageCount = pageCount;
    }

    // Equals and Hashcode overridden to identify each {@CacheModel} uniquely by their query param and page count value.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CacheModel that = (CacheModel) o;
        return Objects.equals(mQueryText, that.mQueryText) &&
                Objects.equals(mPageCount, that.mPageCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mQueryText, mPageCount);
    }
}
