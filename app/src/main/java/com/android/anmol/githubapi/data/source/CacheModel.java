package com.android.anmol.githubapi.data.source;

import java.util.Objects;

public class CacheModel {
    private String mQueryText;
    private Integer mPageCount;

    public CacheModel(String queryText, Integer pageCount) {
        this.mQueryText = queryText;
        this.mPageCount = pageCount;
    }

    public String getQueryText() {
        return mQueryText;
    }

    public Integer getPageCount() {
        return mPageCount;
    }

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
