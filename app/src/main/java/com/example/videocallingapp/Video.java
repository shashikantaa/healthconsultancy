package com.example.videocallingapp;

public class Video {
    private String videoName;
    private String videoUrl;
    private Video(){

    }
    public Video(String name, String vUrl){
        if(name.trim().equals(""))
        {
            name="No Name";
        }
        videoName=name;
        videoUrl=vUrl;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
