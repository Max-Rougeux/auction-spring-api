package com.rougeux.auction.domain.bo;

import com.rougeux.auction.domain.enums.Role;
import lombok.*;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {

    @Builder.Default
    private String id = UUID.randomUUID().toString();

    private String username;
    private String password;

    private String slug;
    private String firstname;
    private String lastname;
    private String phone;

    private int credit;
    private Set<Role> roles;
    private boolean enabled;
    private Instant createdAt;

    private String imageId;
}

