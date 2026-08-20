package com.rougeux.auction.dal.jpa.entity;

import com.rougeux.auction.dal.jpa.converter.RolesConverter;
import com.rougeux.auction.domain.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "USERS")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class UserEntity {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    private String username;
    private String password;

    private String slug;
    private String firstname;
    private String lastname;
    private String phone;

    private int credit;

    @Convert(converter = RolesConverter.class)
    private Set<Role> roles;

    private boolean enabled;
    private Instant createdAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private ImageEntity thumbnail;
}
