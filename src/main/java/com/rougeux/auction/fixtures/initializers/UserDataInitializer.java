package com.rougeux.auction.fixtures.initializers;

import com.rougeux.auction.dal.ImageDao;
import com.rougeux.auction.dal.UserDao;
import com.rougeux.auction.domain.bo.Image;
import com.rougeux.auction.domain.enums.Role;
import com.rougeux.auction.domain.bo.User;
import com.rougeux.auction.exception.NotFoundException;
import com.rougeux.auction.utils.SlugUtils;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import static com.rougeux.auction.support.ApiCodes.CD_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class UserDataInitializer {

    private final Faker faker = new Faker();

    private final Environment environment;
    private final PasswordEncoder encoder;
    private final ImageDao imageRepository;
    private final UserDao repository;

    public void init() {
        List<Image> userThumbnails = imageRepository.findAllUserThumbnails().stream()
                .filter(img -> !"pexels-yusuf-alp-2891146-31420959.jpg".equals(img.getFilename()))
                .toList();
        Image adminThumbnail = imageRepository.findByFilename("pexels-yusuf-alp-2891146-31420959.jpg")
                .orElseThrow(() -> new NotFoundException(CD_NOT_FOUND, "images.error.notFound"));

        if(adminThumbnail == null) return;

        User admin = User.builder()
                .username(environment.getProperty("FIXTURE_ADMIN_USERNAME"))
                .password(encoder.encode(environment.getProperty("FIXTURE_ADMIN_PASSWORD")))
                .slug(SlugUtils.slugify("doe-john"))
                .firstname("John")
                .lastname("Doe")
                .phone(faker.phoneNumber().phoneNumber())
                .credit(faker.number().numberBetween(4000, 6000))
                .roles(Set.of(Role.ADMIN))
                .enabled(true)
                .createdAt(Instant.now())
                .imageId(adminThumbnail.getId())
                .build();
        repository.save(admin);

        for(Image thumbnail: userThumbnails) {
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            String email = (lastName + "." + firstName + "@gmail.com")
                    .toLowerCase()
                    .replace(" ", "");

            User user = User.builder()
                    .username(email)
                    .password(encoder.encode(environment.getProperty("FIXTURE_USER_PASSWORD")))
                    .slug(SlugUtils.slugify(lastName + "-" + firstName))
                    .firstname(firstName)
                    .lastname(lastName)
                    .phone(faker.phoneNumber().phoneNumber())
                    .credit(faker.number().numberBetween(1000, 3000))
                    .roles(Set.of(Role.USER))
                    .enabled(true)
                    .createdAt(Instant.now().minusSeconds(faker.random().nextInt(30) * 86400L))
                    .imageId(thumbnail.getId())
                    .build();
            repository.save(user);
        }
    }
}
