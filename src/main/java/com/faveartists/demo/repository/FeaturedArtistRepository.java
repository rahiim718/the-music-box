package com.faveartists.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.faveartists.demo.model.FeaturedArtist;

public interface FeaturedArtistRepository extends JpaRepository<FeaturedArtist, Long>{
    List<FeaturedArtist> findByGenre(String genre);
}
