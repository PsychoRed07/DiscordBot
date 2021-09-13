package com.psychored.discordbot.event;

import com.psychored.discordbot.command.CommandExecutor;
import discord4j.core.object.entity.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class MessageListener {
    Logger log = LoggerFactory.getLogger(MessageListener.class);

    CommandExecutor commandExecutor = CommandExecutor.getInstance();

    public Flux<Object> processCommand(Message eventMessage) {
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
