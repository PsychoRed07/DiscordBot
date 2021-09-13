package com.psychored.discordbot.command;

import discord4j.core.object.entity.Message;
import reactor.core.publisher.Flux;

public abstract class Command {

    protected String description;

    public Flux<Object> execute(Message command, String argument) {
        return null;
    }

    public String getDescription() {
        return description;
    }

    protected void setDescription(String description) {
        this.description = description;
    }


}
