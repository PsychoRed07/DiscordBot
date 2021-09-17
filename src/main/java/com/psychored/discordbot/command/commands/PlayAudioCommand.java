package com.psychored.discordbot.command.commands;

import com.psychored.discordbot.audioplayer.GuildAudioManager;
import com.psychored.discordbot.audioplayer.service.YoutubeApiService;
import com.psychored.discordbot.audioplayer.service.YoutubeItem;
import com.psychored.discordbot.audioplayer.service.YoutubeSearchManager;
import com.psychored.discordbot.command.Command;
import discord4j.core.event.domain.VoiceStateUpdateEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.VoiceChannel;
import discord4j.voice.AudioProvider;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

import static com.psychored.discordbot.audioplayer.AudioGlobalManager.PLAYER_MANAGER;

public class PlayAudioCommand extends Command {
    {
        setShortDescription("Helping is my mission !");
        setLongDescription("");
    }

    @Override
    public Mono<Void> execute(Message event, String argument) {
        int arg = -1;
        if (!argument.equals("")){
            try {
                arg = Integer.parseInt(argument);
                if (arg > 5){
                    return returnMessage(event,"Please choose between option 1 to 5 using !play *<number>*");
                }
            }catch (Exception e){
                return returnMessage(event, "The argument is not valid.");
            }
        }

        User userCheck = Mono.just(event)
                .flatMap(Message::getAuthorAsMember)
                .block();

        VoiceChannel channel = Mono.just(event)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .flatMap(Message::getAuthorAsMember)
                .flatMap(Member::asFullMember)
                .flatMap(Member::getVoiceState)
                .flatMap(VoiceState::getChannel)
                .block();

        if (userCheck.isBot() || channel == null) {
            return Mono.just(event).then();
        }

        final GuildAudioManager manager = GuildAudioManager.of(channel.getGuildId());
        final AudioProvider provider = manager.getProvider();

        List<YoutubeItem> list = YoutubeSearchManager.getSearchResult(channel.getId().toString());

        if (list == null){
            return returnMessage(event, "Please use the !search command before playing a song.");
        }

        PLAYER_MANAGER.loadItem(list.get(0).getUrl(), manager.getScheduler());


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

    private Mono<Void> returnMessage(Message event, String text){
        return Mono.just(event)
                .filter(message -> event.getAuthor().map(user -> !user.isBot()).orElse(false))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage(text))
                .then();
    }
}
