package com.psychored.discordbot.command.commands;

import com.psychored.discordbot.command.Command;
import discord4j.core.object.entity.Message;
import reactor.core.publisher.Mono;

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
