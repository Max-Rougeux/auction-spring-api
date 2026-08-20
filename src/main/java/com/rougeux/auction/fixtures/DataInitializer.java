package com.rougeux.auction.fixtures;

import com.rougeux.auction.dal.ImageDao;
import com.rougeux.auction.fixtures.initializers.*;
import com.rougeux.auction.fixtures.initializers.BidDataInitializer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final ImageDataInitializer imageInit;
    private final CategoryDataInitializer categoryInit;
    private final BrandDataInitializer brandInit;
    private final UserDataInitializer userInit;
    private final ItemDataInitializer itemInit;
    private final SaleDataInitializer saleInit;
    private final BidDataInitializer bidInit;
    private final ImageDao imageRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        if (imageRepository.count() > 0) {
            logger.info("Data already exists, skipping initialization");
            return;
        }

        long start = System.currentTimeMillis();

        imageInit.init();
        userInit.init();
        categoryInit.init();
        brandInit.init();
        itemInit.init();
        saleInit.init();
        bidInit.init();

        long duration = System.currentTimeMillis() - start;
        logger.info("Data initialization completed in {}.{} seconds", duration / 1000, duration % 1000);
    }
}
