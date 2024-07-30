package com.faveartists.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Fave_New_Artists")
public class FaveNewArtist {

    @Id
    @GeneratedValue
    private long id;
    private String genres;
    private String name;
    @Column (name = "description", length = 2250) //changes the character length of the description column
    private String description;
    private String musicUrl;
    private String imgUrl;

    public FaveNewArtist(String genres, String name, String description, String musicUrl, String imgUrl) {
        this.genres = genres;
        this.name = name;
        this.description = description;
        this.musicUrl = musicUrl;
        this.imgUrl = imgUrl;
    }

    public FaveNewArtist() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMusicUrl() {
        return musicUrl;
    }

    public void setMusicUrl(String musicUrl) {
        this.musicUrl = musicUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public String toString() {
        return "FaveNewArtist [id=" + id + ", genres=" + genres + ", name=" + name + ", description=" + description
                + ", musicUrl=" + musicUrl + ", imgUrl=" + imgUrl + "]";
    }
}