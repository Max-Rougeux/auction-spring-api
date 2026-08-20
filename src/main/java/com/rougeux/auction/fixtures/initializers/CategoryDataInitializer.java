package com.rougeux.auction.fixtures.initializers;

import com.rougeux.auction.dal.CategoryDao;
import com.rougeux.auction.domain.bo.Category;
import com.rougeux.auction.utils.SlugUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoryDataInitializer {

    private final CategoryDao repository;

    private static final List<String> LABELS = List.of(
            "Analog",
            "Digital",
            "Modular / Semi-modular",
            "Hardware virtual / Hybrid",
            "Monophonic",
            "Polyphonic"
    );

    public void init() {
        for(String label: LABELS) {
            Category category = Category.builder()
                    .label(label)
                    .slug(SlugUtils.slugify(label))
                    .build();
            repository.save(category);
        }
    }
}
