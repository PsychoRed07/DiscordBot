package com.psychored.discordbot.command.commands;

import com.psychored.discordbot.audioplayer.service.YoutubeApiService;
import com.psychored.discordbot.audioplayer.service.YoutubeItem;
import com.psychored.discordbot.audioplayer.service.YoutubeSearchManager;
import com.psychored.discordbot.command.Command;
import com.psychored.discordbot.command.CommandHelper;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.VoiceChannel;
import reactor.core.publisher.Mono;

import java.util.List;

public class SearchAudioCommand extends Command {
    {
        setShortDescription("Used to search music audio. To use -> !search <title>");
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

        if (vc == null) {
            return CommandHelper.say(event, "Please connect to a channel to listen to some tunes daddy");
        }

        if (argument.equals("")) {
            return CommandHelper.say(event, "Please specify what you would like to search for using !search <title>");
        }

        List<YoutubeItem> list = YoutubeApiService.youtubeSearch(argument);

        StringBuilder builder = new StringBuilder();
        builder.append("Here are the results : \n\n");
        for (int i = 0; i < list.size(); i++) {
            builder.append(i + 1 + ". **" + list.get(i).getTitle() + "**\n");
        }

        YoutubeSearchManager.addSearchResult(vc.getId().toString(), list);

        return Mono.just(event)
                .filter(message -> event.getAuthor().map(user -> !user.isBot()).orElse(false))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage(CommandHelper.searchEmbed(builder.toString())))
                .then();
    }

}
