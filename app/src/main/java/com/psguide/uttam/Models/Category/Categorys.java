package com.psguide.uttam.Models.Category;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Categorys {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("count")
        @Expose
        private Integer count;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("link")
        @Expose
        private String link;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("slug")
        @Expose
        private String slug;
        @SerializedName("taxonomy")
        @Expose
        private String taxonomy;
        @SerializedName("parent")
        @Expose
        private Integer parent;
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
        public Categorys() {
        }

        /**
         *
         * @param parent
         * @param meta
         * @param count
         * @param link
         * @param name
         * @param description
         * @param links
         * @param id
         * @param taxonomy
         * @param slug
         */
        public Categorys(Integer id, Integer count, String description, String link, String name, String slug, String taxonomy, Integer parent, List<Object> meta, Links links) {
            super();
            this.id = id;
            this.count = count;
            this.description = description;
            this.link = link;
            this.name = name;
            this.slug = slug;
            this.taxonomy = taxonomy;
            this.parent = parent;
            this.meta = meta;
            this.links = links;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSlug() {
            return slug;
        }

        public void setSlug(String slug) {
            this.slug = slug;
        }

        public String getTaxonomy() {
            return taxonomy;
        }

        public void setTaxonomy(String taxonomy) {
            this.taxonomy = taxonomy;
        }

        public Integer getParent() {
            return parent;
        }

        public void setParent(Integer parent) {
            this.parent = parent;
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



