package com.psguide.uttam.Models.Aurthor;

import com.psguide.uttam.Models.Post.Collection;
import com.psguide.uttam.Models.Post.Self;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Author {
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("url")
        @Expose
        private String url;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("link")
        @Expose
        private String link;
        @SerializedName("slug")
        @Expose
        private String slug;
        @SerializedName("avatar_urls")
        @Expose
        private AvatarUrls avatarUrls;
        @SerializedName("meta")
        @Expose
        private List<Object> meta = null;
        @SerializedName("_links")
        @Expose
        private Links links;

        /**
         * No args constructor for use in serialization
         *
         */
        public Author() {
        }

        /**
         *
         * @param avatarUrls
         * @param meta
         * @param name
         * @param link
         * @param description
         * @param links
         * @param id
         * @param url
         * @param slug
         */
        public Author(Integer id, String name, String url, String description, String link, String slug, AvatarUrls avatarUrls, List<Object> meta, Links links) {
            super();
            this.id = id;
            this.name = name;
            this.url = url;
            this.description = description;
            this.link = link;
            this.slug = slug;
            this.avatarUrls = avatarUrls;
            this.meta = meta;
            this.links = links;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getSlug() {
            return slug;
        }

        public void setSlug(String slug) {
            this.slug = slug;
        }

        public AvatarUrls getAvatarUrls() {
            return avatarUrls;
        }

        public void setAvatarUrls(AvatarUrls avatarUrls) {
            this.avatarUrls = avatarUrls;
        }

        public List<Object> getMeta() {
            return meta;
        }

        public void setMeta(List<Object> meta) {
            this.meta = meta;
        }

        public Links getLinks() {
            return links;
        }

        public void setLinks(Links links) {
            this.links = links;
        }

    }

     class AvatarUrls {

        @SerializedName("24")
        @Expose
        private String _24;
        @SerializedName("48")
        @Expose
        private String _48;
        @SerializedName("96")
        @Expose
        private String _96;

        /**
         * No args constructor for use in serialization
         *
         */
        public AvatarUrls() {
        }

        /**
         *
         * @param _24
         * @param _48
         * @param _96
         */
        public AvatarUrls(String _24, String _48, String _96) {
            super();
            this._24 = _24;
            this._48 = _48;
            this._96 = _96;
        }

        public String get24() {
            return _24;
        }

        public void set24(String _24) {
            this._24 = _24;
        }

        public String get48() {
            return _48;
        }

        public void set48(String _48) {
            this._48 = _48;
        }

        public String get96() {
            return _96;
        }

        public void set96(String _96) {
            this._96 = _96;
        }

    }


     class Links {

        @SerializedName("self")
        @Expose
        private List<Self> self = null;
        @SerializedName("collection")
        @Expose
        private List<Collection> collection = null;

        /**
         * No args constructor for use in serialization
         *
         */
        public Links() {
        }

        /**
         *
         * @param self
         * @param collection
         */
        public Links(List<Self> self, List<Collection> collection) {
            super();
            this.self = self;
            this.collection = collection;
        }

        public List<Self> getSelf() {
            return self;
        }

        public void setSelf(List<Self> self) {
            this.self = self;
        }

        public List<Collection> getCollection() {
            return collection;
        }

        public void setCollection(List<Collection> collection) {
            this.collection = collection;
        }

    }


