package com.psychored.discordbot.command.commands;

import com.psychored.discordbot.command.Command;
import com.psychored.discordbot.command.CommandHelper;
import discord4j.core.object.entity.Message;
import reactor.core.publisher.Mono;

public class TodoCommand extends Command {
    {
        setShortDescription("Honestly i'm just trying to survive");
        setLongDescription("");
    }

    @Override
    public Mono<Void> execute(Message event, String argument) {
        return CommandHelper.say(event, "Things to do today:\n - write a bot\n - eat lunch\n - play a game");
    }
}
