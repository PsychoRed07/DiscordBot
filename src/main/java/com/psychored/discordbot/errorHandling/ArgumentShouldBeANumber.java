package com.psychored.discordbot.errorHandling;

import discord4j.core.object.entity.Message;
import reactor.core.publisher.Flux;

public class ArgumentShouldBeANumber {

    //I tried to do an error handling kinda. I TRIED !
    public static Flux<Object> message(Message event, String arg) {
        return Flux.just(event)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage("Argument '" + arg + "' is not a number."));
    }

}
