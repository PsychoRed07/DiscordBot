package com.psychored.discordbot.event;

import discord4j.core.event.domain.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EventListener<T extends Event> {

    Logger LOG = LoggerFactory.getLogger(EventListener.class);

    Class<T> getEventType();

    Flux<Object> execute(T event);

    default Mono<Void> handleError(Throwable error) {
        LOG.error("Unable to process " + getEventType().getSimpleName(), error);
        return Mono.empty();
    }
}
