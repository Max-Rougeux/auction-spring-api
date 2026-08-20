package com.rougeux.auction.dal.jpa.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Entity
@Table(name = "REFRESH_TOKENS")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class RefreshTokenEntity {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    private String publicId;
    private String username;
    private String hash;
    @Builder.Default
    private Instant createdAt = Instant.now();
    @Builder.Default
    private Instant expiresAt = Instant.now().plus(7, ChronoUnit.DAYS);
}
