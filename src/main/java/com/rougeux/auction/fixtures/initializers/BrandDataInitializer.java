package com.rougeux.auction.fixtures.initializers;

import com.rougeux.auction.dal.BrandDao;
import com.rougeux.auction.domain.bo.Brand;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BrandDataInitializer {

    private final BrandDao repository;

    private static final List<String> BRANDS = List.of(
            "Moog", "Korg", "Roland", "Yamaha", "Sequential", "Arturia",
            "Nord", "Behringer", "Elektron", "Novation", "Waldorf",
            "Oberheim", "Dreadbox", "ASM", "Access", "Dave Smith Instruments",
            "Alesis", "Casio", "M-Audio", "Kurzweil", "UDO", "Crumar",
            "Akai", "E-mu", "Ensoniq", "Vermona", "Polyend", "Modal Electronics",
            "Doepfer", "Pittsburgh Modular", "Intellijel", "Make Noise",
            "Mutable Instruments", "Studio Electronics", "Radikal Technologies",
            "GForce", "Cherry Audio", "Native Instruments", "Artisan", "Black Corporation"
    );

    public void init() {
        Faker faker = new Faker();

        for(String name: BRANDS) {

            Brand brand = Brand.builder()
                    .name(name)
                    .partnerSince(Instant.now().minusSeconds(faker.random().nextInt(30) * 86400L))
                    .build();
            repository.save(brand);
        }
    }
}
