package com.psguide.uttam.Models.Post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Meta {

    @SerializedName("spay_email")
    @Expose
    private String spayEmail;

    /**
     * No args constructor for use in serialization
     *
     */
    public Meta() {
    }

    /**
     *
     * @param spayEmail
     */
    public Meta(String spayEmail) {
        super();
        this.spayEmail = spayEmail;
    }

    public String getSpayEmail() {
        return spayEmail;
    }

    public void setSpayEmail(String spayEmail) {
        this.spayEmail = spayEmail;
    }

}
