package com.psychored.discordbot.command.commands;

import com.psychored.discordbot.audioplayer.AudioTrackScheduler;
import com.psychored.discordbot.audioplayer.GuildAudioManager;
import com.psychored.discordbot.command.Command;
import com.psychored.discordbot.command.CommandHelper;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.VoiceChannel;
import reactor.core.publisher.Mono;

public class StopAudioCommand extends Command {
    {
        setShortDescription("Stops the track currently playing.");
        setLongDescription("");
    }

    @Override
    public Mono<Void> execute(Message event, String argument) {
        VoiceChannel channel = CommandHelper.getVoiceChannel(event);

        final GuildAudioManager manager = GuildAudioManager.of(channel.getId());
        final AudioTrackScheduler scheduler = manager.getScheduler();

        scheduler.pause();

        return CommandHelper.say(event, CommandHelper.basicEmbed("Music stopped. use !resume to continue where you left off."));
    }
}
