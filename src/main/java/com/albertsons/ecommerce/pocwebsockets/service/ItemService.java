package com.albertsons.ecommerce.pocwebsockets.service;

import com.albertsons.ecommerce.pocwebsockets.data.ItemRepository;
import com.albertsons.ecommerce.pocwebsockets.events.ItemAddedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import com.albertsons.ecommerce.pocwebsockets.data.Item;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Slf4j
@Service
public class ItemService {

    private final ApplicationEventPublisher publisher;
    private final ItemRepository itemRepository;

    ItemService(ApplicationEventPublisher publisher, ItemRepository itemRepository) {
        this.publisher = publisher;
        this.itemRepository = itemRepository;
    }

    public Flux<Item> all() {
        return this.itemRepository.findAll();
    }

    public Mono<Item> get(String id) {
        log.debug("fetch item {}", id);
        return this.itemRepository.findById(id);
    }

    public Mono<Item> update(String id, String name, BigDecimal price) {
        log.debug("updating item {}, with price : {}", name, price);
        return this.itemRepository
                .findById(id)
                .map(p -> new Item(p.getId(), name, price))
                .flatMap(this.itemRepository::save);
    }

    public Mono<Item> delete(String id) {
        log.debug("deleting item {}", id);
        return this.itemRepository
                .findById(id)
                .flatMap(p -> this.itemRepository.deleteById(p.getId()).thenReturn(p));
    }

    public Mono<Item> create(String name, BigDecimal price) {
        log.debug("creating an item - name: {}, rice: {}", name, price);
        return this.itemRepository
                .save(new Item(null, name, price))
                .doOnSuccess(item -> this.publisher.publishEvent(new ItemAddedEvent(item)));
    }
}

