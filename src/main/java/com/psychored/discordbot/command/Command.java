package com.psychored.discordbot.command;

import discord4j.core.object.entity.Message;
import reactor.core.publisher.Mono;

public abstract class Command {

    protected String shortDescription;

    protected String longDescription;

    public Mono<Void> execute(Message command, String argument) {
        return null;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }
}
