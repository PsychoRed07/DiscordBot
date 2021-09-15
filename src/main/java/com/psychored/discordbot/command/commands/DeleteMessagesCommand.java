package com.psychored.discordbot.command.commands;

import com.psychored.discordbot.command.Command;
import com.psychored.discordbot.errorHandling.ArgumentShouldBeANumber;
import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

public class DeleteMessagesCommand extends Command {
    {
        setShortDescription("Helping is my mission !");
        setLongDescription("");
    }

    @Override
    public Mono<Void> execute(Message event, String argument) {
//        int arg = 0;
//        if (!argument.equals("")) {
//            try {
//                arg = Integer.parseInt(argument);
//            } catch (Exception e) {
//                return ArgumentShouldBeANumber.message(event, argument);
//            }
//            //TODO: add the delete without arguments(numbers).
//        }
//        return deleteMultiple(event, arg);
        return Mono.just(event).then();
    }

//    public Flux<Object> deleteMultiple(Message event, int argument) {
//        return Mono.just(event)
//                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
//                .flatMap(Message::getChannel)
//                .flatMap(channel -> channel.getMessagesBefore(Snowflake.of(Instant.now())))
//                .take(argument)
//                .flatMap(Message::delete);
//    }

}
