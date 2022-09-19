package ru.job4j.passport.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.job4j.passport.model.Passport;

@Slf4j
@Component
public class KafkaMailSender {

    @KafkaListener(topics = "expiredPassports")
    public void sendExpiredPassport(ConsumerRecord<Long, Passport> input) {
        if (log.isInfoEnabled()) {
            log.info("this passport is expired {}, email recipient needs to be identified", input.value());
        }
    }
}
