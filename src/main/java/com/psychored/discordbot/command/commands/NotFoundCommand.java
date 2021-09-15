package com.psychored.discordbot.command.commands;

import com.psychored.discordbot.command.Command;
import discord4j.core.object.entity.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class NotFoundCommand extends Command {
    {
        setShortDescription("");
        setLongDescription("");
    }

    @Override
    public Mono<Void> execute(Message command, String argument) {
        return Mono.just(command)
                .filter(message -> command.getAuthor().map(user -> !user.isBot()).orElse(false))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage("Command '" + command.getContent() + "' is not available. See !commands for a list of available commands."))
                .then();
    }
}