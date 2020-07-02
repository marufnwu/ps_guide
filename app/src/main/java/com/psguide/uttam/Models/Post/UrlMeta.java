package com.psguide.uttam.Models.Post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UrlMeta {

    @SerializedName("origin")
    @Expose
    private Integer origin;
    @SerializedName("position")
    @Expose
    private Integer position;

    /**
     * No args constructor for use in serialization
     *
     */
    public UrlMeta() {
    }

    /**
     *
     * @param origin
     * @param position
     */
    public UrlMeta(Integer origin, Integer position) {
        super();
        this.origin = origin;
        this.position = position;
    }

    public Integer getOrigin() {
        return origin;
    }

    public void setOrigin(Integer origin) {
        this.origin = origin;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

}
