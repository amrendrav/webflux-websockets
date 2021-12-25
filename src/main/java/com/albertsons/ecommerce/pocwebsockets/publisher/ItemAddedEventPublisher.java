package com.albertsons.ecommerce.pocwebsockets.publisher;

import com.albertsons.ecommerce.pocwebsockets.events.ItemAddedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import reactor.core.publisher.FluxSink;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

@Slf4j
@Component
public class ItemAddedEventPublisher implements ApplicationListener<ItemAddedEvent>, Consumer<FluxSink<ItemAddedEvent>> {

    private final Executor executor;
    private final BlockingQueue<ItemAddedEvent> queue = new LinkedBlockingQueue<>();

    ItemAddedEventPublisher(Executor executor) {
        this.executor = executor;
    }

    @Override
    public void onApplicationEvent(ItemAddedEvent event) {
        log.debug("Item added event");
        this.queue.offer(event);
    }

    @Override
    public void accept(FluxSink<ItemAddedEvent> sink) {
        log.debug("Item accept event");
        this.executor.execute(() -> {
            while (true)
                try {
                    ItemAddedEvent event = queue.take();
                    sink.next(event);
                }
                catch (InterruptedException e) {
                    ReflectionUtils.rethrowRuntimeException(e);
                }
        });
    }
}
