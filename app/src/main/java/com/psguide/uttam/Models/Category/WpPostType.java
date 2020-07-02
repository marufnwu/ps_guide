package com.psguide.uttam.Models.Category;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WpPostType {

        @SerializedName("href")
        @Expose
        private String href;

        /**
         * No args constructor for use in serialization
         *
         */
        public WpPostType() {
        }

        /**
         *
         * @param href
         */
        public WpPostType(String href) {
            super();
            this.href = href;
        }

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }

    }
