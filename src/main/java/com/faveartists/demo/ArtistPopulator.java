package com.faveartists.demo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.faveartists.demo.model.FaveNewArtist;
import com.faveartists.demo.model.FeaturedArtist;
import com.faveartists.demo.repository.FaveNewArtistRepository;
import com.faveartists.demo.repository.FeaturedArtistRepository;

import jakarta.annotation.Resource;

@Component
public class ArtistPopulator implements CommandLineRunner {

    @Resource
    private FaveNewArtistRepository aRepository;
    private FeaturedArtistRepository featuredArtistRepo;

    public ArtistPopulator(FaveNewArtistRepository aRepository, FeaturedArtistRepository featuredArtistRepo) {
        this.aRepository = aRepository;
        this.featuredArtistRepo = featuredArtistRepo;
    }

    @Override
    public void run(String... args) throws Exception {
        String filePath = "src/main/resources/data.txt"; // This is searching for the path of the data text
        String genres = ""; // Declaring Variables
        String name = "";
        String description = "";
        String musicUrl = "";
        String imgUrl = "";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) { // reads data txt file
            String line;
            while ((line = br.readLine()) != null) { // reads line by to separate new artists
                String[] parts = line.split("\\+"); // splits variables with a "+"
                if (parts.length == 5) { // sorts variables into parts into an array
                    genres = parts[0];
                    name = parts[1];
                    description = parts[2];
                    musicUrl = parts[3];
                    imgUrl = parts[4];

                    FaveNewArtist artist = new FaveNewArtist(genres, name, description, musicUrl, imgUrl); // organized
                                                                                                           // parts into
                                                                                                           // one
                                                                                                           // section
                    aRepository.save(artist); // upload into database
                } else {
                    System.out.println("Invalid data format: " + line);// sends error when data is invalid
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // prints error details
        }

        filePath = "src/main/resources/FeaturedArtistData.txt"; //creating separate path to featured artist data
        String genre = ""; // declaring attribute
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) { // reading featured artist data txt
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\+");
                if (parts.length == 3) {
                    genre = parts[0];
                    name = parts[1];
                    imgUrl = parts[2];
                    FeaturedArtist artist = new FeaturedArtist(name, genre, imgUrl);
                    featuredArtistRepo.save(artist);
                } else {
                    System.out.println("Invalid data format: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}