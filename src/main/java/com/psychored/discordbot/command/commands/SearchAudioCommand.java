package com.psychored.discordbot.command.commands;

import com.psychored.discordbot.audioplayer.service.YoutubeApiService;
import com.psychored.discordbot.audioplayer.service.YoutubeItem;
import com.psychored.discordbot.audioplayer.service.YoutubeSearchManager;
import com.psychored.discordbot.command.Command;
import com.psychored.discordbot.config.BotConfiguration;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.VoiceChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import reactor.core.publisher.Mono;

import java.util.List;

public class SearchAudioCommand extends Command {
    {
        setShortDescription("Honestly i'm just trying to survive");
        setLongDescription("");
    }

    @Override
    public Mono<Void> execute(Message event, String argument) {
        VoiceChannel vc = Mono.just(event)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .flatMap(Message::getAuthorAsMember)
                .flatMap(Member::asFullMember)
                .flatMap(Member::getVoiceState)
                .flatMap(VoiceState::getChannel)
                .block();

        List<YoutubeItem> list = YoutubeApiService.youtubeSearch(argument);

        StringBuilder builder = new StringBuilder();
        builder.append("Here are the results : \n\n");
        for (int i = 0; i < list.size(); i++) {
            builder.append(i + 1 + ".     " + list.get(i).getTitle() + "\n");
        }

        YoutubeSearchManager.addSearchResult(vc.getId().toString(), list);

        return Mono.just(event)
                .filter(message -> event.getAuthor().map(user -> !user.isBot()).orElse(false))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createEmbed(createMessageCustom(builder.toString())))
                .then();
    }

    public EmbedCreateSpec createMessageCustom(String text) {

        EmbedCreateSpec.Builder embedCreateSpec = EmbedCreateSpec.builder();
        embedCreateSpec.title("Search Result");
        embedCreateSpec.color(Color.RED);
        embedCreateSpec.description(text);
        //embedCreateSpec.author(BotConfiguration.getClient().getSelf().block().getUsername(), null ,null);
        //embedCreateSpec.image(BotConfiguration.getClient().getSelf().block().getAvatarUrl());
        embedCreateSpec.footer("feel free to donate using the !donate command \uD83D\uDE0D", BotConfiguration.getClient().getSelf().block().getAvatarUrl());

        return embedCreateSpec.build();
    }

}
