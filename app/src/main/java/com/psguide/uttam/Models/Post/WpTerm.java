package com.psguide.uttam.Models.Post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WpTerm {

    @SerializedName("taxonomy")
    @Expose
    private String taxonomy;
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
    public WpTerm() {
    }

    /**
     *
     * @param taxonomy
     * @param href
     * @param embeddable
     */
    public WpTerm(String taxonomy, Boolean embeddable, String href) {
        super();
        this.taxonomy = taxonomy;
        this.embeddable = embeddable;
        this.href = href;
    }

    public String getTaxonomy() {
        return taxonomy;
    }

    public void setTaxonomy(String taxonomy) {
        this.taxonomy = taxonomy;
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
