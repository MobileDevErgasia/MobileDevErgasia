package com.example.mobiledevergasia;

public class CustomItem {
    private String path,desc;
    private boolean playing;

    public CustomItem(String path,String desc){
        this.path=path;
        this.desc=desc;
        playing=false;
    }

    public String getDesc() {
        return desc;
    }

    public String getPath(){
        return path;
    }

    public Boolean isPlaying(){ return playing;}

    public void start(){
        playing=true;
    }

    public void stop(){
        playing=false;
    }

    @Override
    public String toString() {
        return "CustomList{" +
                "path='" + path + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
