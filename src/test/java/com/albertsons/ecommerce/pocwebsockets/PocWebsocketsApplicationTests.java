package com.albertsons.ecommerce.pocwebsockets;

import com.albertsons.ecommerce.pocwebsockets.data.Item;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.net.URI;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

import static wiremock.org.checkerframework.checker.units.UnitsTools.min;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class PocWebsocketsApplicationTests {

	private final WebSocketClient socketClient = new ReactorNettyWebSocketClient();


	private final WebClient webClient = WebClient.builder().build();


	private Item generateRandomItem() {
		return new Item(UUID.randomUUID().toString(), "milk"+ ThreadLocalRandom.current().nextInt(min, 5), new BigDecimal("2.5" + ThreadLocalRandom.current().nextInt(min, 5)));
	}

	@Test
	public void testNotificationsOnUpdates() throws Exception {

		int count = 2;
		AtomicLong counter = new AtomicLong();
		URI uri = URI.create("ws://localhost:8080/ws/items");


		socketClient.execute(uri, (WebSocketSession session) -> {
			Mono<WebSocketMessage> out = Mono.just(session.textMessage("test"));
			Flux<String> in = session
					.receive()
					.map(WebSocketMessage::getPayloadAsText);

			return session
					.send(out)
					.thenMany(in)
					.doOnNext(str -> counter.incrementAndGet())
					.then();

		}).subscribe();


		Flux.<Item>generate(sink -> sink.next(generateRandomItem()))
				.take(count)
				.flatMap(this::write)
				.blockLast();

		Thread.sleep(5000);

		Assertions.assertThat(counter.get()).isEqualTo(count);
	}

	private Publisher<Item> write(Item p) {
		return this.webClient
				.post()
				.uri("http://localhost:8080/items")
				.body(BodyInserters.fromObject(p))
				.retrieve()
				.bodyToMono(String.class)
				.thenReturn(p);
	}
}
