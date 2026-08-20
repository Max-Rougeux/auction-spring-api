package com.rougeux.auction.dal;


import com.rougeux.auction.domain.bo.Image;

import java.util.List;
import java.util.Optional;

public interface ImageDao {

    List<Image> findAll();
    List<Image> findAllUserThumbnails();
    List<Image> findAllItemThumbnails();

    Optional<Image> findById(String id);
    Optional<Image> findByFilename(String filename);

    long count();
    void save(Image image);
    void saveAll(List<Image> images);
}
