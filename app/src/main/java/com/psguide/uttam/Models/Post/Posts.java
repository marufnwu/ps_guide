package com.psguide.uttam.Models.Post;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Posts{
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("date_gmt")
    @Expose
    private String dateGmt;
    @SerializedName("guid")
    @Expose
    private Guid guid;
    @SerializedName("modified")
    @Expose
    private String modified;
    @SerializedName("modified_gmt")
    @Expose
    private String modifiedGmt;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("title")
    @Expose
    private Title title;
    @SerializedName("content")
    @Expose
    private Content content;
    @SerializedName("excerpt")
    @Expose
    private Excerpt excerpt;
    @SerializedName("author")
    @Expose
    private Integer author;
    @SerializedName("featured_media")
    @Expose
    private Integer featuredMedia;
    @SerializedName("comment_status")
    @Expose
    private String commentStatus;
    @SerializedName("ping_status")
    @Expose
    private String pingStatus;
    @SerializedName("sticky")
    @Expose
    private Boolean sticky;
    @SerializedName("template")
    @Expose
    private String template;
    @SerializedName("format")
    @Expose
    private String format;
    @SerializedName("meta")
    @Expose
    private Meta meta;
    @SerializedName("categories")
    @Expose
    private List<Integer> categories = null;
    @SerializedName("tags")
    @Expose
    private List<Integer> tags = null;
    @SerializedName("jetpack_featured_media_url")
    @Expose
    private String jetpackFeaturedMediaUrl;
    @SerializedName("jetpack-related-posts")
    @Expose
    private List<JetpackRelatedPost> jetpackRelatedPosts = null;
    @SerializedName("_links")
    @Expose
    private Links links;

    /**
     * No args constructor for use in serialization
     *
     */
    public Posts() {
    }

    /**
     *
     * @param date
     * @param template
     * @param jetpackRelatedPosts
     * @param dateGmt
     * @param link
     * @param type
     * @param title
     * @param pingStatus
     * @param content
     * @param modified
     * @param links
     * @param id
     * @param categories
     * @param slug
     * @param author
     * @param format
     * @param jetpackFeaturedMediaUrl
     * @param commentStatus
     * @param tags
     * @param modifiedGmt
     * @param meta
     * @param sticky
     * @param guid
     * @param featuredMedia
     * @param excerpt
     * @param status
     */
    public Posts(Integer id, String date, String dateGmt, Guid guid, String modified, String modifiedGmt, String slug, String status, String type, String link, Title title, Content content, Excerpt excerpt, Integer author, Integer featuredMedia, String commentStatus, String pingStatus, Boolean sticky, String template, String format, Meta meta, List<Integer> categories, List<Integer> tags, String jetpackFeaturedMediaUrl, List<JetpackRelatedPost> jetpackRelatedPosts, Links links) {
        super();
        this.id = id;
        this.date = date;
        this.dateGmt = dateGmt;
        this.guid = guid;
        this.modified = modified;
        this.modifiedGmt = modifiedGmt;
        this.slug = slug;
        this.status = status;
        this.type = type;
        this.link = link;
        this.title = title;
        this.content = content;
        this.excerpt = excerpt;
        this.author = author;
        this.featuredMedia = featuredMedia;
        this.commentStatus = commentStatus;
        this.pingStatus = pingStatus;
        this.sticky = sticky;
        this.template = template;
        this.format = format;
        this.meta = meta;
        this.categories = categories;
        this.tags = tags;
        this.jetpackFeaturedMediaUrl = jetpackFeaturedMediaUrl;
        this.jetpackRelatedPosts = jetpackRelatedPosts;
        this.links = links;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDateGmt() {
        return dateGmt;
    }

    public void setDateGmt(String dateGmt) {
        this.dateGmt = dateGmt;
    }

    public Guid getGuid() {
        return guid;
    }

    public void setGuid(Guid guid) {
        this.guid = guid;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getModifiedGmt() {
        return modifiedGmt;
    }

    public void setModifiedGmt(String modifiedGmt) {
        this.modifiedGmt = modifiedGmt;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public Excerpt getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(Excerpt excerpt) {
        this.excerpt = excerpt;
    }

    public Integer getAuthor() {
        return author;
    }

    public void setAuthor(Integer author) {
        this.author = author;
    }

    public Integer getFeaturedMedia() {
        return featuredMedia;
    }

    public void setFeaturedMedia(Integer featuredMedia) {
        this.featuredMedia = featuredMedia;
    }

    public String getCommentStatus() {
        return commentStatus;
    }

    public void setCommentStatus(String commentStatus) {
        this.commentStatus = commentStatus;
    }

    public String getPingStatus() {
        return pingStatus;
    }

    public void setPingStatus(String pingStatus) {
        this.pingStatus = pingStatus;
    }

    public Boolean getSticky() {
        return sticky;
    }

    public void setSticky(Boolean sticky) {
        this.sticky = sticky;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<Integer> getCategories() {
        return categories;
    }

    public void setCategories(List<Integer> categories) {
        this.categories = categories;
    }

    public List<Integer> getTags() {
        return tags;
    }

    public void setTags(List<Integer> tags) {
        this.tags = tags;
    }

    public String getJetpackFeaturedMediaUrl() {
        return jetpackFeaturedMediaUrl;
    }

    public void setJetpackFeaturedMediaUrl(String jetpackFeaturedMediaUrl) {
        this.jetpackFeaturedMediaUrl = jetpackFeaturedMediaUrl;
    }

    public List<JetpackRelatedPost> getJetpackRelatedPosts() {
        return jetpackRelatedPosts;
    }

    public void setJetpackRelatedPosts(List<JetpackRelatedPost> jetpackRelatedPosts) {
        this.jetpackRelatedPosts = jetpackRelatedPosts;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

}


