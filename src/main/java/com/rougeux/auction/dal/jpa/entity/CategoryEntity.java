package com.rougeux.auction.dal.jpa.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "CATEGORIES")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class CategoryEntity {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    private String slug;
    private String label;

    @Transient
    private long count;
}
