package com.albertsons.ecommerce.pocwebsockets.routers;

import com.albertsons.ecommerce.pocwebsockets.handlers.ItemHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Slf4j
@Configuration
public class ItemRouter {

    @Bean
    RouterFunction<ServerResponse> routes(ItemHandler handler) {
        log.debug("inside tem router");
        return route((GET("/items")), handler::all)
                .andRoute((GET("/items/{id}")), handler::getById)
                .andRoute((DELETE("/items/{id}")), handler::deleteById)
                .andRoute((POST("/items")), handler::create)
                .andRoute((PUT("/items/{id}")), handler::updateById);
    }


}
