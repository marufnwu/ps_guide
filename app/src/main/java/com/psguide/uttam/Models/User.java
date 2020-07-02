package com.psguide.uttam.Models;


public class User {
   public String fullName, address, phone, photoUrl, email, country, district, gender, uid;
    public float coin, referCoin, ownCoin, archiveCoin;
     public   float balance;
     public String myReferCode;
    public String referBYUserId;
    public String fcmToken;
    public boolean isOnline;

    public User() {
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public void setCoin(float coin) {
        this.coin = coin;
    }

    public float getReferCoin() {
        return referCoin;
    }

    public void setReferCoin(float referCoin) {
        this.referCoin = referCoin;
    }

    public float getOwnCoin() {
        return ownCoin;
    }

    public void setOwnCoin(float ownCoin) {
        this.ownCoin = ownCoin;
    }

    public float getArchiveCoin() {
        return archiveCoin;
    }

    public void setArchiveCoin(float archiveCoin) {
        this.archiveCoin = archiveCoin;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public double getCoin() {
        return coin;
    }


    public double getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public String getMyReferCode() {
        return myReferCode;
    }

    public void setMyReferCode(String myReferCode) {
        this.myReferCode = myReferCode;
    }

    public String getReferBYUserId() {
        return referBYUserId;
    }

    public void setReferBYUserId(String referBYUserId) {
        this.referBYUserId = referBYUserId;
    }
}
