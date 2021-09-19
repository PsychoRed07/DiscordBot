package com.psychored.discordbot.command.commands;

import com.psychored.discordbot.audioplayer.AudioTrackScheduler;
import com.psychored.discordbot.audioplayer.GuildAudioManager;
import com.psychored.discordbot.command.Command;
import com.psychored.discordbot.command.CommandHelper;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.VoiceChannel;
import reactor.core.publisher.Mono;

public class ResumeAudioCommand extends Command {
    {
        setShortDescription("Honestly i'm just trying to survive");
        setLongDescription("");
    }

    @Override
    public Mono<Void> execute(Message event, String argument) {
        VoiceChannel channel = CommandHelper.getVoiceChannel(event);
        final GuildAudioManager manager = GuildAudioManager.of(channel.getId());
        final AudioTrackScheduler scheduler = manager.getScheduler();

        scheduler.resume();

        return CommandHelper.say(event, CommandHelper.basicEmbed("The track is resuming !"));
    }
}
