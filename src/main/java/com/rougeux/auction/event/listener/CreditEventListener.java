package com.rougeux.auction.event.listener;

import com.rougeux.auction.event.CreditUpdateEvent;
import com.rougeux.auction.web.ws.CreditNotification;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class CreditEventListener {

    private final SimpMessagingTemplate template;
    private final Logger logger = LoggerFactory.getLogger(CreditEventListener.class);

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void onDebit(CreditUpdateEvent event) {
        if (!event.refund()) {
            logger.info("Websocket broadcast send → user/queue/debit for {}",
                    event.username());

            template.convertAndSendToUser(
                    event.username(), "/queue/debit", CreditNotification.builder()
                            .amount(event.amount())
                            .build());
        }
    }


    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void onRefund(CreditUpdateEvent event) {
        if (event.refund()) {
            logger.info("Websocket broadcast send → user/queue/refund for {}",
                    event.username());

            template.convertAndSendToUser(
                    event.username(), "/queue/refund", CreditNotification.builder()
                            .amount(event.amount())
                            .slug(event.slug())
                            .outbid(event.outbid())
                            .build());
        }
    }
}
