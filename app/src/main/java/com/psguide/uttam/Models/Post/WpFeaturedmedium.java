package com.psguide.uttam.Models.Post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WpFeaturedmedium {

    @SerializedName("embeddable")
    @Expose
    private Boolean embeddable;
    @SerializedName("href")
    @Expose
    private String href;

    /**
     * No args constructor for use in serialization
     *
     */
    public WpFeaturedmedium() {
    }

    /**
     *
     * @param href
     * @param embeddable
     */
    public WpFeaturedmedium(Boolean embeddable, String href) {
        super();
        this.embeddable = embeddable;
        this.href = href;
    }

    public Boolean getEmbeddable() {
        return embeddable;
    }

    public void setEmbeddable(Boolean embeddable) {
        this.embeddable = embeddable;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

}
