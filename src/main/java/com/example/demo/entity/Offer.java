package com.example.demo.entity;

import com.example.demo.jsonView.MyJsonView;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.demo.enums.Status;

@Entity
@Table(name = "offer")
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @JsonView({MyJsonView.Offer.class, MyJsonView.Product.class})
    private Integer id;

    @Column(name = "amount")
    @JsonView({MyJsonView.Offer.class, MyJsonView.Product.class})
    private Float amount;

    @Column(name = "status")
    @JsonView({MyJsonView.Offer.class, MyJsonView.Product.class})
    private Status status;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    @JsonView({MyJsonView.Offer.class, MyJsonView.Product.class})
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_product")
    @JsonView({MyJsonView.Offer.class})
    private Product product;

    public Offer() {
        // default constructor
    }

    public Integer getId() {
        return id;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
