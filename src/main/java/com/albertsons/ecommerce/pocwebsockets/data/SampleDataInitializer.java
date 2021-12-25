package com.albertsons.ecommerce.pocwebsockets.data;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Component
@org.springframework.context.annotation.Profile("demo")
class SampleDataInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final ItemRepository repository;

    public SampleDataInitializer(ItemRepository repository) {
        this.repository = repository;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("this is to initialise sample data");
        repository
                .deleteAll()
                .thenMany(
                        Flux.just("A", "B", "C", "D")
                                .map(item -> new Item(UUID.randomUUID().toString(), "goat milk", new BigDecimal("2.2")))
                                .flatMap(repository::save)
                )
                .thenMany(repository.findAll())
                .subscribe(item -> log.info("saving " + item.toString()));
    }
}