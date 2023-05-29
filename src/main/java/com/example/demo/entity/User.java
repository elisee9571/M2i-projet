package com.example.demo.entity;

import com.example.demo.jsonView.MyJsonView;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.demo.enums.Roles;
import com.example.demo.enums.Status;

@Entity
@Table(name = "user")
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @JsonView({MyJsonView.User.class, MyJsonView.Product.class, MyJsonView.Offer.class, MyJsonView.Favorite.class})
    private Integer id;

    @Column(name = "firstname")
    @JsonView({MyJsonView.User.class})
    private String firstname;

    @Column(name = "lastname")
    @JsonView({MyJsonView.User.class})
    private String lastname;

    @Column(name="pseudo", unique = true)
    @JsonView({MyJsonView.User.class, MyJsonView.Product.class, MyJsonView.Offer.class, MyJsonView.Favorite.class})
    private String pseudo;

    @Column(name="email", unique = true)
    @JsonView({MyJsonView.User.class, MyJsonView.Product.class, MyJsonView.Offer.class, MyJsonView.Favorite.class})
    private String email;

    @Column(name="phone", unique = true)
    @JsonView({MyJsonView.User.class})
    private Integer phone;

    @Column(name = "password")
    @JsonView({MyJsonView.User.class})
    private String password;

    @Column(name = "biography")
    @JsonView({MyJsonView.User.class})
    private String biography;

    @Column(name = "address")
    @JsonView({MyJsonView.User.class})
    private String address;

    @Column(name = "additional_address")
    @JsonView({MyJsonView.User.class})
    private String additionalAddress;

    @Column(name = "city")
    @JsonView({MyJsonView.User.class})
    private String city;

    @Column(name = "zip_code")
    @JsonView({MyJsonView.User.class})
    private Integer zipCode;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    @JsonView({MyJsonView.User.class})
    private Roles role;

    @Column(name = "avatar")
    @JsonView({MyJsonView.User.class})
    private String avatar;

    @Column(name = "status")
    private Status status;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user")
    private List<Favorite> favorites;

    public User() {
    }

    public User(String firstname, String lastname, String pseudo, String email, String password, Roles role) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.pseudo = pseudo;
        this.email = email;
        this.password = password;
        this.role = role;
        this.status = Status.VISIBLE;
    }

    public User(String avatar, String firstname, String lastname, String pseudo, String email, Integer phone, String password, Roles role) {
        this.avatar = avatar;
        this.firstname = firstname;
        this.lastname = lastname;
        this.pseudo = pseudo;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.role = role;
        this.status = Status.VISIBLE;
    }

    public Integer getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPhone() {
        return phone;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAdditionalAddress() {
        return additionalAddress;
    }

    public void setAdditionalAddress(String additionalAddress) {
        this.additionalAddress = additionalAddress;
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

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    public List<Favorite> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<Favorite> favorites) {
        this.favorites = favorites;
    }
}
