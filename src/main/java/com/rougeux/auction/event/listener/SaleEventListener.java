package com.rougeux.auction.event.listener;

import com.rougeux.auction.event.BidPlacedEvent;
import com.rougeux.auction.web.ws.PriceUpdate;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class SaleEventListener {

    private final SimpMessagingTemplate template;
    private final Logger logger = LoggerFactory.getLogger(SaleEventListener.class);

    public void broadcastPriceUpdate(PriceUpdate update) {
        template.convertAndSend("/topic/sales/price", update);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void onBidPlaced(BidPlacedEvent event) {
        logger.info("Websocket broadcast send → /topic/sales/price");

        broadcastPriceUpdate(PriceUpdate.builder()
                .slug(event.bid().slug())
                .price(event.bid().bid().getAmount())
                .build());
    }
}
