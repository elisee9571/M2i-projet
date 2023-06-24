package com.example.demo.entity;

import com.example.demo.jsonView.MyJsonView;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.demo.enums.Status;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @JsonView({MyJsonView.Order.class})
    private Integer id;

//    @Column(name = "reference")
//    @JsonView({MyJsonView.Order.class})
//    private String reference;

    @Column(name = "delivery_address")
    @JsonView({MyJsonView.Order.class})
    private String deliveryAddress;

    @Column(name = "additional_delivery_address")
    @JsonView({MyJsonView.Order.class})
    private String additionalDeliveryAddress;

    @Column(name = "city")
    @JsonView({MyJsonView.Order.class})
    private String city;

    @Column(name = "zip_code")
    @JsonView({MyJsonView.Order.class})
    private Integer zipCode;

    @Column(name = "amount")
    @JsonView({MyJsonView.Order.class})
    private float amount;

    @Column(name = "status")
    @JsonView({MyJsonView.Order.class})
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    @JsonView({MyJsonView.Order.class})
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_product")
    @JsonView({MyJsonView.Order.class})
    private Product product;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    @JsonView({MyJsonView.Order.class})
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Order() {
    }

    public Order(String deliveryAddress, String additionalDeliveryAddress, String city, Integer zipCode, float amount, Status status, User user, Product product) {
        this.deliveryAddress = deliveryAddress;
        this.additionalDeliveryAddress = additionalDeliveryAddress;
        this.city = city;
        this.zipCode = zipCode;
        this.amount = amount;
        this.status = status;
        this.user = user;
        this.product = product;
    }

    public Integer getId() {
        return id;
    }

//    public String getReference() {
//        return reference;
//    }
//
//    public void setReference(String reference) {
//        this.reference = reference;
//    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getAdditionalDeliveryAddress() {
        return additionalDeliveryAddress;
    }

    public void setAdditionalDeliveryAddress(String additionalDeliveryAddress) {
        this.additionalDeliveryAddress = additionalDeliveryAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getZipCode() {
        return zipCode;
    }

    public void setZipCode(Integer zipCode) {
        this.zipCode = zipCode;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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

    //
}
