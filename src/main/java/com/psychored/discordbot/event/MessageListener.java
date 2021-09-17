package com.psychored.discordbot.event;

import com.psychored.discordbot.command.CommandExecutor;
import discord4j.core.object.entity.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class MessageListener {
    final Logger log = LoggerFactory.getLogger(MessageListener.class);

    final CommandExecutor commandExecutor = CommandExecutor.getInstance();

    public Mono<Void> processCommand(Message eventMessage) {
        String username = eventMessage.getUserData().username();
        String messageContent = eventMessage.getContent();
        String channelName = eventMessage.getGuild().block().getChannelById(eventMessage.getChannelId()).block().getName();
        String localTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        log.info(localTime
                + ": "
                + username
                + " said '"
                + messageContent
                + "' in "
                + channelName);
        return commandExecutor.handle(eventMessage);
    }
}
