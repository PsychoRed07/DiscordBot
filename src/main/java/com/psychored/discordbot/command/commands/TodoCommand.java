package com.psychored.discordbot.command.commands;

import com.psychored.discordbot.command.Command;
import discord4j.core.object.entity.Message;
import reactor.core.publisher.Flux;

public class TodoCommand extends Command {
    {
        setDescription("Honestly i'm just trying to survive");
    }

    @Override
    public Flux<Object> execute(Message command, String argument) {
        return Flux.just(command)
                .filter(message -> command.getAuthor().map(user -> !user.isBot()).orElse(false))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage("Things to do today:\n - write a bot\n - eat lunch\n - play a game"));
    }
}
