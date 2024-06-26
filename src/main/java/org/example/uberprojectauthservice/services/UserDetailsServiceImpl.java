package org.example.uberprojectauthservice.services;

import org.example.uberprojectauthservice.helpers.AuthPassengerDetails;
import org.example.uberprojectauthservice.repositories.PassengerRepository;
import org.example.uberprojectentityservice.models.Passenger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
/*
* THis class is responsible for loading the user in the form of UserDetails object for auth
*/
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private PassengerRepository passengerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Passenger> passenger = passengerRepository.findPassengerByEmail(email); //email is the unique identifier
        if(passenger.isPresent()) {
            return new AuthPassengerDetails(passenger.get());
        } else{
            throw new UsernameNotFoundException("Cannot find passenger with email: " + email);
        }
    }
}
