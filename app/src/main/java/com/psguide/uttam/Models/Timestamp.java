package com.psguide.uttam.Models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Timestamp {

    @SerializedName("date")
    @Expose
    private Long date;
    @SerializedName("day")
    @Expose
    private Long day;
    @SerializedName("hours")
    @Expose
    private Long hours;
    @SerializedName("minutes")
    @Expose
    private Long minutes;
    @SerializedName("month")
    @Expose
    private Long month;
    @SerializedName("nanos")
    @Expose
    private Long nanos;
    @SerializedName("seconds")
    @Expose
    private Long seconds;
    @SerializedName("time")
    @Expose
    private Long time;
    @SerializedName("timezoneOffset")
    @Expose
    private Long timezoneOffset;
    @SerializedName("year")
    @Expose
    private Long year;

    /**
     * No args constructor for use in serialization
     *
     */
    public Timestamp() {
    }

    /**
     *
     * @param date
     * @param hours
     * @param seconds
     * @param month
     * @param nanos
     * @param timezoneOffset
     * @param year
     * @param minutes
     * @param time
     * @param day
     */
    public Timestamp(Long date, Long day, Long hours, Long minutes, Long month, Long nanos, Long seconds, Long time, Long timezoneOffset, Long year) {
        super();
        this.date = date;
        this.day = day;
        this.hours = hours;
        this.minutes = minutes;
        this.month = month;
        this.nanos = nanos;
        this.seconds = seconds;
        this.time = time;
        this.timezoneOffset = timezoneOffset;
        this.year = year;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Long getDay() {
        return day;
    }

    public void setDay(Long day) {
        this.day = day;
    }

    public Long getHours() {
        return hours;
    }

    public void setHours(Long hours) {
        this.hours = hours;
    }

    public Long getMinutes() {
        return minutes;
    }

    public void setMinutes(Long minutes) {
        this.minutes = minutes;
    }

    public Long getMonth() {
        return month;
    }

    public void setMonth(Long month) {
        this.month = month;
    }

    public Long getNanos() {
        return nanos;
    }

    public void setNanos(Long nanos) {
        this.nanos = nanos;
    }

    public Long getSeconds() {
        return seconds;
    }

    public void setSeconds(Long seconds) {
        this.seconds = seconds;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getTimezoneOffset() {
        return timezoneOffset;
    }

    public void setTimezoneOffset(Long timezoneOffset) {
        this.timezoneOffset = timezoneOffset;
    }

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

}