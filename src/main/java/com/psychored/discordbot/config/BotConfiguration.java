package com.psychored.discordbot.config;

import com.psychored.discordbot.audioplayer.LavaPlayerAudioProvider;
import com.psychored.discordbot.event.EventListener;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.playback.NonAllocatingAudioFrameBuffer;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import discord4j.voice.AudioProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class BotConfiguration {

    private static GatewayDiscordClient client = null;
    private final Logger log = LoggerFactory.getLogger(EventListener.class);
    @Value("${jda.discord.token}")
    private String token;

    public static GatewayDiscordClient getClient() {
        return client;
    }

    @Bean
    public <T extends Event> GatewayDiscordClient gatewayDiscordClient(List<EventListener<T>> eventListeners) {

        try {
            client = DiscordClientBuilder.create(token)
                    .build()
                    .login()
                    .block();

            for (EventListener<T> listener : eventListeners) {
                client.on(listener.getEventType())
                        .flatMap(listener::execute)
                        .onErrorResume(listener::handleError)
                        .subscribe();
            }
        } catch (Exception exception) {
            log.error("Be sure to use a valid bot token!", exception);
        }

        return client;
    }

    @Bean
    public AudioPlayer audioPlayer() {
        // Creates AudioPlayer instances and translates URLs to AudioTrack instances
        final AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
        // This is an optimization strategy that Discord4J can utilize. It is not important to understand
        playerManager.getConfiguration().setFrameBufferFactory(NonAllocatingAudioFrameBuffer::new);
        // Allow playerManager to parse remote sources like YouTube links
        AudioSourceManagers.registerRemoteSources(playerManager);
        // Create an AudioPlayer so Discord4J can receive audio data
        final AudioPlayer player = playerManager.createPlayer();
        return player;
    }

    @Bean
    public AudioProvider audioProvider() {
        AudioPlayer player = audioPlayer();
        // We will be creating LavaPlayerAudioProvider in the next step
        AudioProvider provider = new LavaPlayerAudioProvider(player);

        return provider;
    }
}
