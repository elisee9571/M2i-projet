package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ad_category")
public class AdCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_ad")
    private Ad ad;

    @ManyToOne
    @JoinColumn(name = "id_category")
    private Category category;

    public AdCategory() {
    }

    public Long getId() {
        return id;
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
}
