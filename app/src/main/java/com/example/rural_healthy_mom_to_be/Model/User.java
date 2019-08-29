package com.example.rural_healthy_mom_to_be.Model;

public class User {
    private String user_name;
    private Double height;
    private Double pre_weight;
    private Double cur_weight;

    public User() {
    }

    public User(String user_name, Double height, Double pre_weight, Double cur_weight) {
        this.user_name = user_name;
        this.height = height;
        this.pre_weight = pre_weight;
        this.cur_weight = cur_weight;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getPre_weight() {
        return pre_weight;
    }

    public void setPre_weight(Double pre_weight) {
        this.pre_weight = pre_weight;
    }

    public Double getCur_weight() {
        return cur_weight;
    }

    public void setCur_weight(Double cur_weight) {
        this.cur_weight = cur_weight;
    }
}
