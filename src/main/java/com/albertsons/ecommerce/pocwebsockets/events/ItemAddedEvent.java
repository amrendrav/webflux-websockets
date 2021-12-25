package com.albertsons.ecommerce.pocwebsockets.events;


import com.albertsons.ecommerce.pocwebsockets.data.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;

@Slf4j
public class ItemAddedEvent extends ApplicationEvent {

    public ItemAddedEvent(Item source) {
        super(source);
    }
}

