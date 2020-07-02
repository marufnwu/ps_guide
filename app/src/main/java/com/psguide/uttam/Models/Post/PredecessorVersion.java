package com.psguide.uttam.Models.Post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PredecessorVersion {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("href")
    @Expose
    private String href;

    /**
     * No args constructor for use in serialization
     *
     */
    public PredecessorVersion() {
    }

    /**
     *
     * @param id
     * @param href
     */
    public PredecessorVersion(Integer id, String href) {
        super();
        this.id = id;
        this.href = href;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

}
