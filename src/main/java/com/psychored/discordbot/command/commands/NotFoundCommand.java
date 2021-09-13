package com.psychored.discordbot.command.commands;

import com.psychored.discordbot.command.Command;
import discord4j.core.object.entity.Message;
import reactor.core.publisher.Flux;

public class NotFoundCommand extends Command {
    {
        setDescription("");
    }

    @Override
    public Flux<Object> execute(Message command, String argument) {
        return Flux.just(command)
                .filter(message -> command.getAuthor().map(user -> !user.isBot()).orElse(false))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage("Command '" + command.getContent() + "' is not available. See !commands for a list of available commands."));
    }
}