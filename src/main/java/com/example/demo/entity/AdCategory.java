package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ad_category")
@IdClass(CleAdCategory.class)
public class AdCategory {

    @Id
    private Integer adId;

    @Id
    private Integer categoryId;

    @ManyToOne
    @MapsId("id_ad")
    @JoinColumn(name = "id_ad")
    private Ad ad;

    @ManyToOne
    @MapsId("id_category")
    @JoinColumn(name = "id_category")
    private Category category;

    public AdCategory() {
    }

    public Ad getAd() {
        return ad;
    }

    public void setAd(Ad ad) {
        this.ad = ad;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

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
