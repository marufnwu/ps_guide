package com.psguide.uttam.Models;

import java.sql.Timestamp;

public class Transaction {
    public double reward;
    public String details;
    public Timestamp timestamp;
    public String types;
    public boolean pending;
    public String rewardBy;

    public Transaction() {
    }

    public double getReward() {
        return reward;
    }

    public void setReward(double reward) {
        this.reward = reward;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }

    public String getRewardBy() {
        return rewardBy;
    }

    public void setRewardBy(String rewardBy) {
        this.rewardBy = rewardBy;
    }
}
