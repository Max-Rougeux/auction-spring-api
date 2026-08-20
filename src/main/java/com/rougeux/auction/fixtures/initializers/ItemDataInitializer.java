package com.rougeux.auction.fixtures.initializers;

import com.rougeux.auction.dal.*;
import com.rougeux.auction.domain.bo.*;
import com.rougeux.auction.domain.enums.Condition;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ItemDataInitializer {

    private final ImageDao imageRepository;
    private final CategoryDao categoryRepository;
    private final BrandDao brandRepository;
    private final UserDao userRepository;
    private final ItemDao itemRepository;

    private static final List<String> MODELS = List.of(
            "Minimoog Model D", "PolySix", "Juno-6", "Juno-60", "Juno-106", "Jupiter-8",
            "Jupiter-4", "Prophet-5", "Prophet-6", "OB-Xa", "OB-8", "Memorymoog", "Polymoog",
            "CS-80", "DX7", "SY77", "M1", "Mono/Poly", "MS-20", "ARP Odyssey", "ARP 2600",
            "SH-101", "TB-303", "TR-808", "TR-909", "AX60", "AX80", "DW-8000",
            "Sub 37", "Grandmother", "Matriarch", "Minitaur", "MicroBrute", "MiniBrute 2S",
            "MatrixBrute", "PolyBrute", "DeepMind 12", "Prologue 16", "Minilogue XD", "Monologue",
            "Take 5", "Pro 3", "OB-X8", "Hydrasynth", "Argon8", "Cobalt8", "Peak", "Summit",
            "Digitone", "Digitakt", "Syntakt", "Analog Four", "Analog Rytm", "Modwave", "Opsix"
    );

    public void init() {
        List<Image> itemThumbnails = imageRepository.findAllItemThumbnails();
        List<Category> categories = categoryRepository.findAll();
        List<Brand> brands = brandRepository.findAll();
        List<User> users = userRepository.findAll();

        Faker faker = new Faker();

        for(Image thumbnail: itemThumbnails) {
            Category category = categories.get(faker.random().nextInt(categories.size()));
            Brand brand = brands.get(faker.random().nextInt(brands.size()));
            User user = users.get(faker.random().nextInt(users.size()));

            Item item = Item.builder()
                    .categoryId(category.getId())
                    .brandId(brand.getId())
                    .model(MODELS.get(faker.random().nextInt(MODELS.size())))
                    .description(faker.lorem().paragraph(6))
                    .condition(Condition.randomCondition())
                    .year(faker.number().numberBetween(1960, 2026))
                    .isGem(Math.random() < 0.2)
                    .imageId(thumbnail.getId())
                    .userId(user.getId())
                    .build();
            itemRepository.save(item);
        }
    }
}
