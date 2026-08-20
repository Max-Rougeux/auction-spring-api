package com.rougeux.auction.dal.mock.storage;

import com.rougeux.auction.domain.bo.*;
import lombok.experimental.UtilityClass;
import org.springframework.context.annotation.Profile;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Profile("mock")
@UtilityClass
public final class InMemoryStorage {

    public final List<User> USERS = new CopyOnWriteArrayList<>();
    public final List<Image> IMAGES = new CopyOnWriteArrayList<>();
    public final List<Category> CATEGORIES = new CopyOnWriteArrayList<>();
    public final List<Brand> BRANDS = new CopyOnWriteArrayList<>();
    public final List<Item> ITEMS = new CopyOnWriteArrayList<>();
    public final List<Sale> SALES = new CopyOnWriteArrayList<>();
    public final List<Bid> BIDS = new CopyOnWriteArrayList<>();
    public final List<RefreshToken> REFRESH_TOKENS = new CopyOnWriteArrayList<>();
}
