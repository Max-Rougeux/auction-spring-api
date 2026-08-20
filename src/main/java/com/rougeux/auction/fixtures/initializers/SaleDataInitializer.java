package com.rougeux.auction.fixtures.initializers;

import com.rougeux.auction.dal.BrandDao;
import com.rougeux.auction.dal.ItemDao;
import com.rougeux.auction.dal.SaleDao;
import com.rougeux.auction.domain.bo.Brand;
import com.rougeux.auction.domain.bo.Item;
import com.rougeux.auction.domain.bo.Sale;
import com.rougeux.auction.domain.enums.State;
import com.rougeux.auction.exception.NotFoundException;
import com.rougeux.auction.utils.SlugUtils;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

import static com.rougeux.auction.support.ApiCodes.CD_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class SaleDataInitializer {

    private final ItemDao itemRepository;
    private final BrandDao brandRepository;
    private final SaleDao saleRepository;

    public void init() {

        Faker faker = new Faker();
        List<Item> items = itemRepository.findAll();

        for(Item item: items) {
            Brand brand = brandRepository.findById(item.getBrandId())
                    .orElseThrow(() -> new NotFoundException(CD_NOT_FOUND, "brands.error.notFound"));

            String slug = SlugUtils.slugify(brand.getName() + "-" + item.getModel());
            Instant startAt = Instant.now().minusSeconds(86400L * faker.random().nextInt(7));
            Instant endAt = Instant.now().plusSeconds(86400L * (faker.random().nextInt(29) + 1));
            int startPrice = faker.number().numberBetween(20, 100);

            Sale sale = Sale.builder()
                    .slug(slug)
                    .createdAt(startAt.minusSeconds(86400L * (faker.random().nextInt(7))))
                    .startedAt(startAt)
                    .endedAt(endAt)
                    .startingPrice(startPrice)
                    .currentPrice(startPrice)
                    .likes(faker.random().nextInt(386))
                    .state(State.OPEN)
                    .itemId(item.getId())
                    .build();
            saleRepository.save(sale);
        }
    }
}
