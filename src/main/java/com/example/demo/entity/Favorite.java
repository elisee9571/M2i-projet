package com.example.demo.entity;

import com.example.demo.jsonView.MyJsonView;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;

@Entity
@Table(name = "favorite")
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @JsonView({MyJsonView.Favorite.class, MyJsonView.Product.class, MyJsonView.User.class})
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_user")
    @JsonView({MyJsonView.Favorite.class, MyJsonView.Product.class})
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_product")
    @JsonView({MyJsonView.Favorite.class, MyJsonView.User.class})
    private Product product;

    public Favorite() {
    }

    public Integer getId() {
        return id;
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
