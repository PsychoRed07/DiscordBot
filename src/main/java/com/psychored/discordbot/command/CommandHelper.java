package com.psychored.discordbot.command;

import com.psychored.discordbot.config.BotConfiguration;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.VoiceChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import reactor.core.publisher.Mono;

public class CommandHelper {

    private static final String FOOTER = "Feel free to donate using the !donate command ! \uD83D\uDE0D";

    public static Mono<Void> say(Message event, String text) {
        return Mono.just(event)
                .filter(message -> event.getAuthor().map(user -> !user.isBot()).orElse(false))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage(text))
                .then();
    }

    public static Mono<Void> say(Message event, EmbedCreateSpec embed) {
        return Mono.just(event)
                .filter(message -> event.getAuthor().map(user -> !user.isBot()).orElse(false))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage(embed))
                .then();
    }

    public static VoiceChannel getVoiceChannel(Message event) {
        VoiceChannel channel = Mono.just(event)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .flatMap(Message::getAuthorAsMember)
                .flatMap(Member::asFullMember)
                .flatMap(Member::getVoiceState)
                .flatMap(VoiceState::getChannel)
                .block();
        return channel;
    }

    public static EmbedCreateSpec searchEmbed(String text) {

        EmbedCreateSpec.Builder embedCreateSpec = EmbedCreateSpec.builder();
        embedCreateSpec.title("Search Result");
        embedCreateSpec.color(Color.RED);
        embedCreateSpec.description(text);
        //embedCreateSpec.author(BotConfiguration.getClient().getSelf().block().getUsername(), null ,null);
        //embedCreateSpec.image(BotConfiguration.getClient().getSelf().block().getAvatarUrl());
        embedCreateSpec.footer(FOOTER, BotConfiguration.getClient().getSelf().block().getAvatarUrl());

        return embedCreateSpec.build();
    }

    public static EmbedCreateSpec basicEmbed(String text) {

        EmbedCreateSpec.Builder embedCreateSpec = EmbedCreateSpec.builder();
        embedCreateSpec.color(Color.RED);
        embedCreateSpec.description(text);
        //embedCreateSpec.author(BotConfiguration.getClient().getSelf().block().getUsername(), null ,null);
        //embedCreateSpec.image(BotConfiguration.getClient().getSelf().block().getAvatarUrl());
        embedCreateSpec.footer(FOOTER, BotConfiguration.getClient().getSelf().block().getAvatarUrl());

        return embedCreateSpec.build();
    }
}
