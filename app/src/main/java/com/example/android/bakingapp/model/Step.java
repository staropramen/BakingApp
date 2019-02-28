package com.example.android.bakingapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Step implements Serializable {

    @SerializedName("id")
    private int stepId;
    private String shortDescription;
    private String description;
    private String videoURL;
    private String thumbnailURL;

    public Step(int stepId, String shortDescription, String description, String videoURL, String thumbnailURL) {
        this.stepId = stepId;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
    }

    public int getStepId() {
        return stepId;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }
}
