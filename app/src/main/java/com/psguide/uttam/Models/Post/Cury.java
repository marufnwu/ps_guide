package com.psguide.uttam.Models.Post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cury {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("href")
    @Expose
    private String href;
    @SerializedName("templated")
    @Expose
    private Boolean templated;

    /**
     * No args constructor for use in serialization
     *
     */
    public Cury() {
    }

    /**
     *
     * @param templated
     * @param name
     * @param href
     */
    public Cury(String name, String href, Boolean templated) {
        super();
        this.name = name;
        this.href = href;
        this.templated = templated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public Boolean getTemplated() {
        return templated;
    }

    public void setTemplated(Boolean templated) {
        this.templated = templated;
    }

}
