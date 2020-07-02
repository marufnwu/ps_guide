package com.psguide.uttam.Models.Post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JetpackRelatedPost {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("url_meta")
    @Expose
    private UrlMeta urlMeta;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("format")
    @Expose
    private Boolean format;
    @SerializedName("excerpt")
    @Expose
    private String excerpt;
    @SerializedName("rel")
    @Expose
    private String rel;
    @SerializedName("context")
    @Expose
    private String context;
    @SerializedName("img")
    @Expose
    private Img img;
    @SerializedName("classes")
    @Expose
    private List<Object> classes = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public JetpackRelatedPost() {
    }

    /**
     *
     * @param date
     * @param img
     * @param classes
     * @param format
     * @param rel
     * @param context
     * @param id
     * @param urlMeta
     * @param title
     * @param excerpt
     * @param url
     */
    public JetpackRelatedPost(Integer id, String url, UrlMeta urlMeta, String title, String date, Boolean format, String excerpt, String rel, String context, Img img, List<Object> classes) {
        super();
        this.id = id;
        this.url = url;
        this.urlMeta = urlMeta;
        this.title = title;
        this.date = date;
        this.format = format;
        this.excerpt = excerpt;
        this.rel = rel;
        this.context = context;
        this.img = img;
        this.classes = classes;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public UrlMeta getUrlMeta() {
        return urlMeta;
    }

    public void setUrlMeta(UrlMeta urlMeta) {
        this.urlMeta = urlMeta;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Boolean getFormat() {
        return format;
    }

    public void setFormat(Boolean format) {
        this.format = format;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public Img getImg() {
        return img;
    }

    public void setImg(Img img) {
        this.img = img;
    }

    public List<Object> getClasses() {
        return classes;
    }

    public void setClasses(List<Object> classes) {
        this.classes = classes;
    }

}
