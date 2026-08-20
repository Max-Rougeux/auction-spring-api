package com.rougeux.auction.dal.jpa;

import com.rougeux.auction.dal.ImageDao;
import com.rougeux.auction.dal.jpa.mapper.JpaImageMapper;
import com.rougeux.auction.dal.jpa.repository.JpaImageRepository;
import com.rougeux.auction.domain.bo.Image;
import com.rougeux.auction.domain.enums.ImageType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Profile("postgresql")
@RequiredArgsConstructor
public class JpaImageDao implements ImageDao {

    private final JpaImageRepository repository;

    @Override
    public List<Image> findAll() {
        return repository.findAll().stream()
                .map(JpaImageMapper::toBo)
                .toList();
    }

    @Override
    public List<Image> findAllUserThumbnails() {
        return repository.findAllByType(ImageType.USER).stream()
                .map(JpaImageMapper::toBo)
                .toList();
    }

    @Override
    public List<Image> findAllItemThumbnails() {
        return repository.findAllByType(ImageType.ITEM).stream()
                .map(JpaImageMapper::toBo)
                .toList();
    }

    @Override
    public Optional<Image> findById(String id) {
        return repository.findById(UUID.fromString(id)).map(JpaImageMapper::toBo);
    }

    @Override
    public Optional<Image> findByFilename(String filename) {
        return repository.findByFilename(filename)
                .map(JpaImageMapper::toBo);
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public void save(Image image) {
        repository.save(JpaImageMapper.from(image));
    }

    @Override
    public void saveAll(List<Image> images) {
        repository.saveAll(images.stream()
                .map(JpaImageMapper::from)
                .toList());
    }
}
