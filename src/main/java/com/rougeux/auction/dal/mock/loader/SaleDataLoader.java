package com.rougeux.auction.dal.mock.loader;

import com.rougeux.auction.domain.projection.ItemProjection;
import com.rougeux.auction.domain.projection.SaleProjection;
import com.rougeux.auction.domain.projection.UserProjection;
import com.rougeux.auction.domain.bo.*;
import com.rougeux.auction.dal.mock.batch.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.*;

import static java.util.stream.Collectors.toSet;

@Component
@Profile("mock")
@RequiredArgsConstructor
public class SaleDataLoader {

    private final ItemBatchDao itemBatch;
    private final BrandBatchDao brandBatch;
    private final CategoryBatchDao categoryBatch;
    private final ImageBatchDao imageBatch;
    private final UserBatchDao userBatch;

    public List<SaleProjection> load(List<Sale> sales) {

        Set<String> itemIds = sales.stream()
                .map(Sale::getItemId)
                .collect(toSet());
        Map<String, Item> items = itemBatch.findAllByIds(itemIds);

        Set<String> brandIds    = items.values().stream().map(Item::getBrandId).collect(toSet());
        Set<String> categoryIds = items.values().stream().map(Item::getCategoryId).collect(toSet());
        Set<String> ownerIds    = items.values().stream().map(Item::getUserId).collect(toSet());
        Set<String> imageIds    = items.values().stream().map(Item::getImageId).collect(toSet());

        Map<String, User> users = userBatch.findAllByIds(ownerIds);

        Set<String> userImageIds = users.values().stream()
                .map(User::getImageId)
                .collect(toSet());
        imageIds.addAll(userImageIds);

        Map<String, Brand>    brands     = brandBatch.findAllByIds(brandIds);
        Map<String, Category> categories = categoryBatch.findAllByIds(categoryIds);
        Map<String, Image>    images     = imageBatch.findAllByIds(imageIds);

        return sales.stream()
                .sorted(Comparator.comparing(Sale::getEndedAt, Comparator.reverseOrder()))
                .map(sale -> {
                    Item item = items.get(sale.getItemId());
                    User user = users.get(item.getUserId());

                    ItemProjection itemAggregate = ItemProjection.builder()
                            .item(item)
                            .brand(brands.get(item.getBrandId()))
                            .category(categories.get(item.getCategoryId()))
                            .image(images.get(item.getImageId()))
                            .build();

                    UserProjection userAggregate = UserProjection.builder()
                            .user(user)
                            .image(images.get(user.getImageId()))
                            .build();

                    return SaleProjection.builder()
                            .sale(sale)
                            .itemProjection(itemAggregate)
                            .userProjection(userAggregate)
                            .build();
                })
                .toList();
    }
}
