package com.rougeux.auction.fixtures.initializers;

import com.rougeux.auction.dal.ImageDao;
import com.rougeux.auction.domain.bo.Image;
import com.rougeux.auction.domain.enums.ImageType;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class ImageDataInitializer {

    private final ImageDao repository;
    private final Environment env;

    public void init() throws IOException {
        List<File> userThumbnails = getThumbnails(env.getProperty("app.uploads.dir") + "/user");
        List<File> itemThumbnails = getThumbnails(env.getProperty("app.uploads.dir") + "/item");

        List<Image> temps = new ArrayList<>();

        for (File file : userThumbnails) {
            BufferedImage bufferedImage = ImageIO.read(file);

            Image image = Image.builder()
                    .filename(file.getName())
                    .directory("user")
                    .width(bufferedImage.getWidth())
                    .height(bufferedImage.getHeight())
                    .type(ImageType.USER)
                    .uploadAt(Instant.now())
                    .build();

            temps.add(image);
        }
        repository.saveAll(temps);
        temps.clear();

        for (File file : itemThumbnails) {
            BufferedImage bufferedImage = ImageIO.read(file);

            Image image = Image.builder()
                    .filename(file.getName())
                    .directory("item")
                    .width(bufferedImage.getWidth())
                    .height(bufferedImage.getHeight())
                    .type(ImageType.ITEM)
                    .uploadAt(Instant.now())
                    .build();

            temps.add(image);
        }
        repository.saveAll(temps);
        temps.clear();
    }

    private List<File> getThumbnails(String dir) throws IOException {
        Path uploadDir = Path.of(dir);

        try (Stream<Path> files = Files.list(uploadDir)) {
            return files.filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .toList();
        }
    }
}

