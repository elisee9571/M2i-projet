package com.example.demo.entity;

import com.example.demo.jsonView.MyJsonView;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;

@Entity
@Table(name = "image")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @JsonView({MyJsonView.Product.class})
    private Integer id;

    @Column(name = "url", unique = true)
    @JsonView({MyJsonView.Product.class})
    private String url;

    @Column(name = "type")
    @JsonView({MyJsonView.Product.class})
    private String type;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_product")
    private Product product;

    public Image(String url, String type) {
        this.url = url;
        this.type = type;
    }

    public Image(){

    }

    public Integer getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public Product getProduct() {
        return product;
    }

    public void setProduct(Product ad) {
        this.product = product;
    }
}