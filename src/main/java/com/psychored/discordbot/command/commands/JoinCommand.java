package com.psychored.discordbot.command.commands;

import com.psychored.discordbot.audioplayer.GuildAudioManager;
import com.psychored.discordbot.command.Command;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.VoiceChannel;
import discord4j.voice.AudioProvider;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static com.psychored.discordbot.audioplayer.AudioGlobalManager.PLAYER_MANAGER;

public class JoinCommand extends Command {
    {
        setShortDescription("Helping is my mission !");
        setLongDescription("");
    }

    @Override
    public Mono<Void> execute(Message event, String argument) {

//        return Mono.just(event)
//                //join channel
//                .flatMap(member -> member.getAuthorAsMember())
//                .flatMap(memberMono -> memberMono.asFullMember())
//                .flatMap(Member::getVoiceState)
//                .flatMap(VoiceState::getChannel)
//                .filter(Objects::nonNull)
//                .flatMap(voiceChannel -> voiceChannel.join(legacyVoiceChannelJoinSpec -> legacyVoiceChannelJoinSpec.setProvider(audioPlayer)));
        return Mono.just(event).then();
    }

}
