package com.psychored.discordbot.command.commands;

import com.psychored.discordbot.command.Command;
import com.psychored.discordbot.command.CommandHelper;
import discord4j.core.object.entity.Message;
import reactor.core.publisher.Mono;

public class NotFoundCommand extends Command {
    {
        setShortDescription("");
        setLongDescription("");
    }

    @Override
    public Mono<Void> execute(Message event, String argument) {
        String text = "Command '" + event.getContent() + "' is not available. See !commands for a list of available commands.";
        return CommandHelper.say(event, text);
    }
}