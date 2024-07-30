package com.faveartists.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.faveartists.demo.model.FeaturedArtist;
import com.faveartists.demo.repository.FeaturedArtistRepository;

@Service
public class FeaturedArtistService {
    
    @Autowired
    private FeaturedArtistRepository featuredArtistRepository;

    public List<FeaturedArtist> getAllFeaturedArtists() {
        return featuredArtistRepository.findAll();
    }

}
