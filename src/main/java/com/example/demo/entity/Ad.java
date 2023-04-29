package com.example.demo.entity;

import com.example.demo.enums.Currencies;
import com.example.demo.jsonView.MyJsonView;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.demo.enums.Status;

@Entity
@Table(name = "ad")
public class Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @JsonView({MyJsonView.Ad.class, MyJsonView.Category.class})
    private Integer id;

    @Column(name = "title")
    @JsonView({MyJsonView.Ad.class, MyJsonView.Category.class})
    private String title;

    @Column(name = "content")
    @JsonView({MyJsonView.Ad.class, MyJsonView.Category.class})
    private String content;

    @Column(name = "price")
    @JsonView({MyJsonView.Ad.class, MyJsonView.Category.class})
    private Float price;

    @Column(name = "currency_code")
    @JsonView({MyJsonView.Ad.class, MyJsonView.Category.class})
    private Currencies currencyCode;

    @Column(name = "status")
    @JsonView({MyJsonView.Ad.class, MyJsonView.Category.class})
    private Status status;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    @JsonView({MyJsonView.Ad.class})
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_user")
    @JsonView({MyJsonView.Ad.class})
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_category")
    @JsonView({MyJsonView.Ad.class})
    private Category category;

    public Ad(String title, String content, Float price, Currencies currencyCode ,Status status, User user, Category category) {
        this.title = title;
        this.content = content;
        this.price = price;
        this.currencyCode = currencyCode;
        this.status = status;
        this.user = user;
        this.category = category;
    }

    public Ad() {
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
}
