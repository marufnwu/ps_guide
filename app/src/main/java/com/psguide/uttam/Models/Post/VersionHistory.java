package com.psguide.uttam.Models.Post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VersionHistory {

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("href")
    @Expose
    private String href;

    /**
     * No args constructor for use in serialization
     *
     */
    public VersionHistory() {
    }

    /**
     *
     * @param count
     * @param href
     */
    public VersionHistory(Integer count, String href) {
        super();
        this.count = count;
        this.href = href;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

}
