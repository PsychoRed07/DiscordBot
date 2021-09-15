package com.psychored.discordbot.command.commands;

import com.psychored.discordbot.command.Command;
import com.psychored.discordbot.command.CommandExecutor;
import discord4j.core.object.entity.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class HelpCommand extends Command {
    {
        setShortDescription("Helping is my mission !");
        setLongDescription("");
    }

    @Override
    public Mono<Void> execute(Message event, String argument) {
        CommandExecutor commandExecutor = CommandExecutor.getInstance();
        StringBuilder text = new StringBuilder();
        commandExecutor.getCommandNames().forEach((name, command) -> {
            text.append(commandExecutor.getPREFIX()).append(name).append(" - ").append(command.getShortDescription());
            text.append("\n");
        });
        return Mono.just(event)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage(text.toString()))
                .then();
    }
}