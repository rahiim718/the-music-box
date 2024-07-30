package com.faveartists.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="featuredartist")
public class FeaturedArtist {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String genre;
    @Column(name="image_url")
    private String imageUrl;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public String getGenre(){
        return genre;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public FeaturedArtist(){}

    public FeaturedArtist(String name, String genre, String imageUrl){
        this.name = name;
        this.genre = genre;
        this.imageUrl = imageUrl;
    }
    
    @Override
    public String toString() {
        return "FeaturedArtist [id=" + id + ", name=" + name + ", genre=" + genre + ", imageUrl=" + imageUrl
                + "]";
    }


}
