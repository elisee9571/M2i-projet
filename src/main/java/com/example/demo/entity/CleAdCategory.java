package com.example.demo.entity;

import java.io.Serializable;

import jakarta.persistence.*;

@Embeddable
public class CleAdCategory implements Serializable {
    @Column(name = "id_ad")
    private Integer adId;
    @Column(name = "id_category")
    private Integer categoryId;
    public Integer getAdId() {
        return adId;
    }
    public void setAdId(Integer adId) {
        this.adId = adId;
    }
    public Integer getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

}
