package com.psychored.discordbot.command.commands;

import com.psychored.discordbot.command.Command;
import com.psychored.discordbot.command.CommandExecutor;
import discord4j.core.object.entity.Message;
import reactor.core.publisher.Flux;

public class HelpCommand extends Command {
    {
        setDescription("Helping is my mission !");
    }

    @Override
    public Flux<Object> execute(Message event, String argument) {
        CommandExecutor commandExecutor = CommandExecutor.getInstance();
        StringBuilder text = new StringBuilder();
        commandExecutor.getCommandNames().forEach((name, command) -> {
            text.append(commandExecutor.getPREFIX() + name + " - " + command.getDescription());
            text.append("\n");
        });
        return Flux.just(event)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage(text.toString()));
    }
}