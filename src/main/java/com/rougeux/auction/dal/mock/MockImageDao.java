package com.rougeux.auction.dal.mock;

import com.rougeux.auction.dal.ImageDao;
import com.rougeux.auction.dal.mock.storage.InMemoryStorage;
import com.rougeux.auction.domain.bo.Image;
import com.rougeux.auction.domain.enums.ImageType;
import com.rougeux.auction.dal.mock.batch.ImageBatchDao;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;

import static java.util.function.UnaryOperator.identity;
import static java.util.stream.Collectors.toMap;

@Repository
@Profile("mock")
@RequiredArgsConstructor
public class MockImageDao implements ImageDao, ImageBatchDao {

    @Override
    public List<Image> findAll() {
        return InMemoryStorage.IMAGES;
    }

    @Override
    public List<Image> findAllUserThumbnails() {
        return InMemoryStorage.IMAGES.stream()
                .filter(img -> img.getType() == ImageType.USER)
                .toList();
    }

    @Override
    public List<Image> findAllItemThumbnails() {
        return InMemoryStorage.IMAGES.stream()
                .filter(img -> img.getType() == ImageType.ITEM)
                .toList();
    }

    @Override
    public Optional<Image> findById(String id) {
        return InMemoryStorage.IMAGES.stream()
                .filter(img -> id.equals(img.getId()))
                .findFirst();
    }

    @Override
    public Optional<Image> findByFilename(String filename) {
        return InMemoryStorage.IMAGES.stream()
                .filter(img -> filename.equals(img.getFilename()))
                .findFirst();
    }

    @Override
    public long count() { return InMemoryStorage.IMAGES.size(); }

    @Override
    public void save(Image image) {
        for (int i = 0; i < InMemoryStorage.IMAGES.size(); i++) {
            if (Objects.equals(InMemoryStorage.IMAGES.get(i).getId(), image.getId())) {
                InMemoryStorage.IMAGES.set(i, image);
                return;
            }
        }
        InMemoryStorage.IMAGES.add(image);
    }

    @Override
    public void saveAll(List<Image> images) {
        InMemoryStorage.IMAGES.addAll(images);
    }

    @Override
    public Map<String, Image> findAllByIds(Set<String> ids) {
        return InMemoryStorage.IMAGES.stream()
                .filter(i -> ids.contains(i.getId()))
                .collect(toMap(Image::getId, identity()));
    }
}
