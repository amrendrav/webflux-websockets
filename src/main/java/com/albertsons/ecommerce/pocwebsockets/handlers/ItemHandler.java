package com.albertsons.ecommerce.pocwebsockets.handlers;

import com.albertsons.ecommerce.pocwebsockets.data.Item;
import com.albertsons.ecommerce.pocwebsockets.service.ItemService;
import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class ItemHandler {

    private final ItemService itemService;

    public ItemHandler(ItemService itemService) {
        this.itemService = itemService;
    }

    public Mono<ServerResponse> getById(ServerRequest r) {
        return defaultReadResponse(this.itemService.get(id(r)));
    }

    public Mono<ServerResponse> all(ServerRequest r) {
        return defaultReadResponse(this.itemService.all());
    }

    public Mono<ServerResponse> deleteById(ServerRequest r) {
        return defaultReadResponse(this.itemService.delete(id(r)));
    }

    public Mono<ServerResponse> updateById(ServerRequest r) {
        Flux<Item> id = r.bodyToFlux(Item.class)
                .flatMap(p -> this.itemService.update(id(r), p.getName(), p.getPrice()));
        return defaultReadResponse(id);
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        Flux<Item> flux = request
                .bodyToFlux(Item.class)
                .flatMap(toWrite -> this.itemService.create(toWrite.getName(), toWrite.getPrice()));
        return defaultWriteResponse(flux);
    }


    private static Mono<ServerResponse> defaultWriteResponse(Publisher<Item> carts) {
        return Mono
                .from(carts)
                .flatMap(p -> ServerResponse
                        .created(URI.create("/items/" + p.getId()))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .build()
                );
    }


    private static Mono<ServerResponse> defaultReadResponse(Publisher<Item> carts) {
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(carts, Item.class);
    }

    private static String id(ServerRequest r) {
        return r.pathVariable("id");
    }

}
