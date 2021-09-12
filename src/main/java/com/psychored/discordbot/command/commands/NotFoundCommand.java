package com.psychored.discordbot.command.commands;

import com.psychored.discordbot.command.Command;
import discord4j.core.object.entity.Message;
import reactor.core.publisher.Mono;

public class NotFoundCommand extends Command {
    {
        setDescription("");
    }

    @Override
    public Mono<Void> execute(Message command) {
        return Mono.just(command)
                .filter(message -> command.getAuthor().map(user -> !user.isBot()).orElse(false))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage("Command '" + command.getContent() + "' is not available. See !commands for a list of available commands."))
                .then();
    }
}