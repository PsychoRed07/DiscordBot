package com.psychored.discordbot.command.commands;

import com.psychored.discordbot.audioplayer.AudioTrackScheduler;
import com.psychored.discordbot.audioplayer.GuildAudioManager;
import com.psychored.discordbot.command.Command;
import com.psychored.discordbot.command.CommandHelper;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.VoiceChannel;
import reactor.core.publisher.Mono;

import java.util.List;

public class QueueAudioCommand extends Command {
    {
        setShortDescription("Honestly i'm just trying to survive");
        setLongDescription("");
    }

    @Override
    public Mono<Void> execute(Message event, String argument) {
        VoiceChannel channel = CommandHelper.getVoiceChannel(event);
        final GuildAudioManager manager = GuildAudioManager.of(channel.getId());
        final AudioTrackScheduler scheduler = manager.getScheduler();

        List<AudioTrack> trackList = scheduler.getQueue();
        StringBuilder builder = new StringBuilder();
        builder.append("Audio in queue : \n\n");
        for (int i = 0; i < trackList.size(); i++) {
            builder.append(i + 1 + ". **" + trackList.get(i).getInfo().title + "**\n");
        }

        return CommandHelper.say(event, CommandHelper.basicEmbed(builder.toString()));
    }
}
