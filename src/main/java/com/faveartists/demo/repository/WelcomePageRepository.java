package com.faveartists.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.faveartists.demo.model.WelcomePage;

@Repository
public interface WelcomePageRepository extends JpaRepository<WelcomePage, Long> {
    List<WelcomePage> findByFirstNameAndLastName(String firstName, String lastName); //Generates a query to find the customer by their first and last name

    List<WelcomePage> findByEmailAddressAndUserPassword(String emailAddress, String userPassword);

}
