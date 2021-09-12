package com.psychored.discordbot.command;

import discord4j.core.object.entity.Message;
import reactor.core.publisher.Mono;

public abstract class Command {

    protected String description;

    public Mono<Void> execute(Message command) {
        return null;
    }

    public String getDescription() {
        return description;
    }

    protected void setDescription(String description) {
        this.description = description;
    }
}
