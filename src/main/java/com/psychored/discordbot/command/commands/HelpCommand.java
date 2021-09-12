package com.psychored.discordbot.command.commands;

import com.psychored.discordbot.command.Command;
import com.psychored.discordbot.command.CommandExecutor;
import discord4j.core.object.entity.Message;
import reactor.core.publisher.Mono;

public class HelpCommand extends Command {
    {
        setDescription("Helping is my mission !");
    }

    @Override
    public Mono<Void> execute(Message event) {
        CommandExecutor commandExecutor = CommandExecutor.getInstance();
        StringBuilder text = new StringBuilder("");
        commandExecutor.getCommandNames().forEach((name, command) -> {
            text.append( commandExecutor.getPREFIX() + name + " - " + command.getDescription());
            text.append("\n");
        });
        return Mono.just(event)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage(text.toString()))
                .then();
    }
}