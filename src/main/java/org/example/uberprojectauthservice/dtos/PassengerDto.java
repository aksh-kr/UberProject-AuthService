package org.example.uberprojectauthservice.dtos;

import lombok.*;
import org.example.uberprojectentityservice.models.Passenger;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PassengerDto {

    private String id;

    private String name;

    private String email;

    private String password; //encrypted password

    private String phoneNumber;

    private Date createdAt;

    public static PassengerDto from(Passenger p) {
        PassengerDto result = PassengerDto.builder()
                .id(p.getId().toString())
                .email(p.getEmail())
                .createdAt(p.getCreatedAt())
                .password(p.getPassword())
                .phoneNumber(p.getPhoneNumber())
                .name(p.getName())
                .build();

        return result;
    }
}
