package com.rougeux.auction.dal.mock.loader;

import com.rougeux.auction.dal.mock.batch.ImageBatchDao;
import com.rougeux.auction.domain.bo.Image;
import com.rougeux.auction.domain.bo.User;
import com.rougeux.auction.domain.projection.UserProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Component
@Profile("mock")
@RequiredArgsConstructor
public class UserDataLoader {

    private final ImageBatchDao imageBatch;

    public List<UserProjection> load(List<User> users) {

        Set<String> userImageIds = users.stream()
                .map(User::getImageId)
                .collect(toSet());
        Map<String, Image> images = imageBatch.findAllByIds(userImageIds);

        return users.stream()
                .sorted(Comparator.comparing(User::getCreatedAt, Comparator.reverseOrder()))
                .map(user -> {
                    Image image = images.get(user.getImageId());

                    return UserProjection.builder()
                            .user(user)
                            .image(image)
                            .build();
                })
                .toList();
    }
}
