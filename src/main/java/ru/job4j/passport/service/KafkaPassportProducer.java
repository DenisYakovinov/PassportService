package ru.job4j.passport.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.job4j.passport.model.Passport;

@Service
@EnableScheduling
public class KafkaPassportProducer {

    private final PassportService passportService;
    private final KafkaTemplate<Long, Passport> kafkaTemplate;

    public KafkaPassportProducer(PassportService passportService, KafkaTemplate<Long, Passport> kafkaTemplate) {
        this.passportService = passportService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedRate = 10000)
    public void sendToConsumer() {
        passportService.getExpiredPassports().forEach(passport -> {
            kafkaTemplate.send("expiredPassports", passport.getId(), passport);
            kafkaTemplate.flush();
        });
    }
}
