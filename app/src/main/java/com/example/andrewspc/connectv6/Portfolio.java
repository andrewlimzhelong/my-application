package com.example.andrewspc.connectv6;

import java.io.Serializable;

public class Portfolio implements Serializable {

    private String portfolioPic1;
    private String Title;
    private String portfolioPic3;
    private String portfolioPic4;

    public Portfolio() {
    }

    public Portfolio(String portfolioPic1, String Description, String portfolioPic3, String portfolioPic4) {
        this.portfolioPic1 = portfolioPic1;
        this.Title = Title;
        this.portfolioPic3 = portfolioPic3;
        this.portfolioPic4 = portfolioPic4;
    }

    public String getPortfolioPic1() {
        return portfolioPic1;
    }

    public void setPortfolioPic1(String portfolioPic1) {
        this.portfolioPic1 = portfolioPic1;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getPortfolioPic3() {
        return portfolioPic3;
    }

    public void setPortfolioPic3(String portfolioPic3) {
        this.portfolioPic3 = portfolioPic3;
    }

    public String getPortfolioPic4() {
        return portfolioPic4;
    }

    public void setPortfolioPic4(String portfolioPic4) {
        this.portfolioPic4 = portfolioPic4;
    }



}
