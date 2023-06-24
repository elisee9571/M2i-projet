package com.example.demo.entity;

import com.example.demo.enums.Currencies;
import com.example.demo.jsonView.MyJsonView;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.demo.enums.Status;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @JsonView({MyJsonView.Product.class, MyJsonView.Category.class, MyJsonView.Offer.class, MyJsonView.Favorite.class, MyJsonView.Order.class})
    private Integer id;

    @Column(name = "title")
    @JsonView({MyJsonView.Product.class, MyJsonView.Category.class, MyJsonView.Offer.class, MyJsonView.Favorite.class, MyJsonView.Order.class})
    private String title;

    @Column(name = "content")
    @JsonView({MyJsonView.Product.class, MyJsonView.Category.class})
    private String content;

    @Column(name = "price")
    @JsonView({MyJsonView.Product.class, MyJsonView.Category.class, MyJsonView.Offer.class, MyJsonView.Favorite.class, MyJsonView.Order.class})
    private Float price;

    @JsonView({MyJsonView.Product.class, MyJsonView.Offer.class, MyJsonView.Order.class})
    private Float priceOffer;

    @Column(name = "currency_code")
    @JsonView({MyJsonView.Product.class, MyJsonView.Category.class, MyJsonView.Order.class})
    private Currencies currencyCode;

    @Column(name = "status")
    @JsonView({MyJsonView.Product.class, MyJsonView.Category.class, MyJsonView.Offer.class, MyJsonView.Favorite.class})
    private Status status;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    @JsonView({MyJsonView.Product.class})
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    @JsonView({MyJsonView.Product.class})
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    @JsonView({MyJsonView.Product.class, MyJsonView.Favorite.class, MyJsonView.Offer.class, MyJsonView.Order.class})
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_category")
    @JsonView({MyJsonView.Product.class, MyJsonView.Favorite.class})
    private Category category;

    @OneToMany(mappedBy = "product")
    @JsonView({MyJsonView.Product.class, MyJsonView.Favorite.class})
    private List<Image> images;

    @Transient
    private List<MultipartFile> fileUploads;

    @OneToMany(mappedBy = "product")
    @JsonView({MyJsonView.Product.class})
    private List<Offer> offers = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<Favorite> favorites;
    @JsonView({MyJsonView.Product.class, MyJsonView.Favorite.class})
    private int favoritesCount;

    public Product(String title, String content, Float price, Currencies currencyCode ,Status status, User user, Category category) {
        this.title = title;
        this.content = content;
        this.price = price;
        this.currencyCode = currencyCode;
        this.status = status;
        this.user = user;
        this.category = category;
    }

    public Product() {
    }

    // getters and setters
    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getPriceOffer() {
        return priceOffer;
    }

    public void setPriceOffer(Float priceOffer) {
        this.priceOffer = priceOffer;
    }

    public Currencies getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(Currencies currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public List<Offer> getOffers() {
        return offers;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }

    public List<Favorite> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<Favorite> favorites) {
        this.favorites = favorites;
    }

    public int getFavoritesCount() {
        return getFavorites().size();
    }
//
    public List<MultipartFile> getFileUploads() {
        return fileUploads;
    }

    public void setFileUploads(List<MultipartFile> fileUploads) {
        this.fileUploads = fileUploads;
    }
}
