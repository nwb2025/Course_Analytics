package com.example.fragments;

public class Course {

    private int id;
    private  String name;
    private int cost;
    private  String url;
    private String duration;
    private String desc;
    private String hardship;
    private String format;
    private String language;
    private String content;
    private String features;
    private  boolean isShrink=true;
    private String ratio;
    private String results;
    private String url_site;
    private String practice;


    public Course(int id,String name,int cost,String hardship,String duration,String desc,String format,String url,String language,String content,String features,String ratio,String results,String url_site,String practice){
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.hardship = hardship;
        this.duration = duration;
        this.desc = desc;
        this.format = format;
        this.url = url;
        this.language = language;
        this.content = content;
        this.features = features;
        this.ratio = ratio;
        this.results = results;
        this.url_site = url_site;
        this.practice = practice;

    }

    public String getPractice() {
        return practice;
    }

    public void setPractice(String practice) {
        this.practice = practice;
    }

    public String getUrl_site() {
        return url_site;
    }

    public void setUrl_site(String url_site) {
        this.url_site = url_site;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public boolean isShrink(){ return isShrink;}
    public void setShrink(boolean shrink){
        isShrink = shrink;
    }

    public String getContent() {
        return content;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId(){ return  id;}
    public String getLanguage(){return language;}
    public void setLanguage(String language){ this.language = language;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getHardship() {
        return hardship;
    }

    public void setHardship(String hardship) {
        this.hardship = hardship;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }


    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cost=" + cost +
                ", url='" + url + '\'' +
                ", duration='" + duration + '\'' +
                ", desc='" + desc + '\'' +
                ", hardship='" + hardship + '\'' +
                ", format='" + format + '\'' +
                ", language='" + language + '\'' +
                ", content='" + content + '\'' +
                ", features='" + features + '\'' +
                ", isShrink=" + isShrink +
                ", ratio='" + ratio + '\'' +
                ", results='" + results + '\'' +
                ", url_site='" + url_site + '\'' +
                ", practice='" + practice + '\'' +
                '}';
    }
}
