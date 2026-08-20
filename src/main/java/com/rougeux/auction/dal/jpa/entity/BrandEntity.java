package com.rougeux.auction.dal.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "BRANDS")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class BrandEntity {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    private String name;
}
