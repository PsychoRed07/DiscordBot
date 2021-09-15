package com.psychored.discordbot.command.commands;

import com.psychored.discordbot.audioplayer.AudioTrackScheduler;
import com.psychored.discordbot.audioplayer.GuildAudioManager;
import com.psychored.discordbot.command.Command;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import discord4j.core.event.domain.VoiceStateUpdateEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.VoiceChannel;
import discord4j.voice.AudioProvider;
import discord4j.voice.VoiceConnection;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.function.Consumer;

import static com.psychored.discordbot.audioplayer.AudioGlobalManager.PLAYER_MANAGER;

public class PlayAudioCommand extends Command {
    {
        setShortDescription("Helping is my mission !");
        setLongDescription("");
    }

    @Override
    public Mono<Void> execute(Message event, String argument) {

        VoiceChannel channel = Mono.just(event)
                .flatMap(Message::getAuthorAsMember)
                .flatMap(Member::asFullMember)
                .flatMap(Member::getVoiceState)
                .flatMap(VoiceState::getChannel)
                .block();

        final GuildAudioManager manager = GuildAudioManager.of(channel.getGuildId());
        final AudioProvider provider = manager.getProvider();
        PLAYER_MANAGER.loadItem("https://www.youtube.com/watch?v=dQw4w9WgXcQ", manager.getScheduler());


        final Mono<Void> onDisconnect = channel.join(spec -> spec.setProvider(provider))
                .flatMap(connection -> {
                    // The bot itself has a VoiceState; 1 VoiceState signals bot is alone
                    final Publisher<Boolean> voiceStateCounter = channel.getVoiceStates()
                            .count()
                            .map(count -> 1L == count);

                    // After 10 seconds, check if the bot is alone. This is useful if
                    // the bot joined alone, but no one else joined since connecting
                    final Mono<Void> onDelay = Mono.delay(Duration.ofSeconds(10L))
                            .filterWhen(ignored -> voiceStateCounter)
                            .switchIfEmpty(Mono.never())
                            .then();

                    // As people join and leave `channel`, check if the bot is alone.
                    // Note the first filter is not strictly necessary, but it does prevent many unnecessary cache calls
                    final Mono<Void> onEvent = channel.getClient().getEventDispatcher().on(VoiceStateUpdateEvent.class)
                            .filter(event2 -> event2.getOld().flatMap(VoiceState::getChannelId).map(channel.getId()::equals).orElse(false))
                            .filterWhen(ignored -> voiceStateCounter)
                            .next()
                            .then();

                    // Disconnect the bot if either onDelay or onEvent are completed!
                    return Mono.firstWithSignal(onDelay, onEvent).then(connection.disconnect());
                });

        return onDisconnect;
    }
}
