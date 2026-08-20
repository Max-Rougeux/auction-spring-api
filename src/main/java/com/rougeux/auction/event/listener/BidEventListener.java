package com.rougeux.auction.event.listener;

import com.rougeux.auction.event.BidPlacedEvent;
import com.rougeux.auction.mapper.BidMapper;
import com.rougeux.auction.web.bid.BidDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class BidEventListener {

    private final SimpMessagingTemplate template;
    private final Logger logger = LoggerFactory.getLogger(BidEventListener.class);

    public void broadcastLiveBid(BidDto bid) {
        Stream.of(
                "/topic/live-bids",
                "/topic/live-bids/" + bid.slug()
        ).forEach(topic -> template.convertAndSend(topic, bid));
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void onBidPlaced(BidPlacedEvent event) {
        logger.info("Websocket broadcast send → /topic/live-bids/{}", event.bid().slug());

        BidDto dto = BidMapper.toDto(event.bid());
        broadcastLiveBid(dto);
    }
}
