package com.rougeux.auction.dal.jpa.repository;

import com.rougeux.auction.dal.jpa.entity.ImageEntity;
import com.rougeux.auction.domain.enums.ImageType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaImageRepository extends JpaRepository<ImageEntity, UUID> {

    List<ImageEntity> findAllByType(ImageType type);

    Optional<ImageEntity> findByFilename(String filename);
}
