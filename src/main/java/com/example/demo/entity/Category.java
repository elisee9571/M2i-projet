package com.example.demo.entity;

import com.example.demo.jsonView.MyJsonView;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;

import java.io.Serializable;

import java.util.List;

@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false)
    @JsonView({MyJsonView.Product.class, MyJsonView.Category.class})
    private Integer id;

    @Column(name="title", unique = true)
    @JsonView({MyJsonView.Product.class, MyJsonView.Category.class})
    private String title;

    @ManyToOne
    @JoinColumn(name = "id_parent")
    @JsonView({MyJsonView.Category.class})
    private Category parent;

    @OneToMany(mappedBy = "category")
    @JsonView({MyJsonView.Category.class})
    private List<Product> products;

    public Category(String title, Category parent) {
        this.title = title;
        this.parent = parent;
    }
    public Category() {
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
