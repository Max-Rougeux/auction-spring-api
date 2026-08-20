package com.rougeux.auction.fixtures.initializers;

import com.rougeux.auction.dal.BidDao;
import com.rougeux.auction.dal.ItemDao;
import com.rougeux.auction.dal.SaleDao;
import com.rougeux.auction.dal.UserDao;
import com.rougeux.auction.domain.bo.Bid;
import com.rougeux.auction.domain.bo.Item;
import com.rougeux.auction.domain.bo.Sale;
import com.rougeux.auction.domain.bo.User;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BidDataInitializer {

    private final SaleDao saleRepository;
    private final ItemDao itemRepository;
    private final UserDao userRepository;
    private final BidDao bidRepository;

    private static final long MIN_SALE_AGE_SECONDS = 300L;

    public void init() {

        Faker faker = new Faker();
        List<User> users = userRepository.findAll();
        Map<String, Item> itemsById = itemRepository.findAll().stream()
                .collect(Collectors.toMap(Item::getId, i -> i));

        for (Sale sale : saleRepository.findAll()) {
            Instant now = Instant.now();
            Item item = itemsById.get(sale.getItemId());

            boolean tooRecent = sale.getStartedAt().isAfter(now.minusSeconds(MIN_SALE_AGE_SECONDS));

            List<User> possibleBidders = item == null
                    ? List.of()
                    : users.stream()
                    .filter(u -> !u.getId().equals(item.getUserId()))
                    .toList();

            if (tooRecent || possibleBidders.isEmpty()) continue;

            int nbBids = faker.random().nextInt(15, 35);

            long totalSeconds = now.getEpochSecond() - sale.getStartedAt().getEpochSecond();
            long maxJitter = Math.max(1L, (totalSeconds / nbBids) / 4);
            long interval = Math.max(MIN_SALE_AGE_SECONDS, (totalSeconds - maxJitter * nbBids) / nbBids);

            int currentPrice = sale.getStartingPrice();
            String lastBidderId = null;
            Instant bidTime = sale.getStartedAt();

            for (int i = 0; i < nbBids; i++) {
                boolean bigJump = faker.number().numberBetween(1, 10) == 1;
                currentPrice += bigJump
                        ? faker.number().numberBetween(50, 100)
                        : faker.number().numberBetween(5, 10);

                String finalLastBidderId = lastBidderId;
                List<User> candidates = possibleBidders.stream()
                        .filter(u -> !u.getId().equals(finalLastBidderId))
                        .toList();

                User bidder = candidates.isEmpty()
                        ? possibleBidders.get(faker.random().nextInt(possibleBidders.size()))
                        : candidates.get(faker.random().nextInt(candidates.size()));

                lastBidderId = bidder.getId();

                bidTime = bidTime.plusSeconds(
                        interval + faker.random().nextLong(Math.max(1L, interval / 4))
                );

                bidRepository.save(Bid.builder()
                        .amount(currentPrice)
                        .time(bidTime)
                        .userId(bidder.getId())
                        .saleId(sale.getId())
                        .build());
            }

            saleRepository.save(sale.toBuilder()
                    .currentPrice(currentPrice)
                    .build());
        }    }
}