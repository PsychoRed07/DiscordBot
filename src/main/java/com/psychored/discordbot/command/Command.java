package com.psychored.discordbot.command;

import discord4j.core.object.entity.Message;
import reactor.core.publisher.Flux;

public abstract class Command {

    protected String shortDescription;

    protected String longDescription;

    public Flux<Object> execute(Message command, String argument) {
        return null;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    protected void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    protected void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }
}
