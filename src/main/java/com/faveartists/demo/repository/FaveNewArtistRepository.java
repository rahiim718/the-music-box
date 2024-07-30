package com.faveartists.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.faveartists.demo.model.FaveNewArtist;

public interface FaveNewArtistRepository extends JpaRepository<FaveNewArtist, Long>{
    List<FaveNewArtist> findDistinctByGenresIn(List<String> genres); //helps to find artist by genre
    
}