package com.psychored.discordbot.command.commands;

import com.psychored.discordbot.command.Command;
import com.psychored.discordbot.command.CommandExecutor;
import com.psychored.discordbot.command.CommandHelper;
import discord4j.core.object.entity.Message;
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
            text.append(commandExecutor.getPREFIX() + name + " - " + command.getShortDescription());
            text.append("\n");
        });
        return CommandHelper.say(event, text.toString());
    }
}