package com.psychored.discordbot.audioplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public final class AudioTrackScheduler extends AudioEventAdapter implements AudioLoadResultHandler {

    private final Logger log = LoggerFactory.getLogger(AudioTrackScheduler.class);
    private final List<AudioTrack> queue;
    private final AudioPlayer player;
    private boolean isPlaying = false;

    public AudioTrackScheduler(final AudioPlayer player) {
        // The queue may be modifed by different threads so guarantee memory safety
        // This does not, however, remove several race conditions currently present
        queue = Collections.synchronizedList(new LinkedList<>());
        this.player = player;
    }

    public List<AudioTrack> getQueue() {
        return queue;
    }

    public boolean play(final AudioTrack track) {
        return play(track, false);
    }

    public boolean play(final AudioTrack track, final boolean force) {
        boolean playing;
        try {
            playing = player.startTrack(track, force);
        } catch (IllegalStateException e) {
            playing = player.startTrack(track.makeClone(), force);
        }
        ;

        if (!playing) {
            queue.add(track);
            log.info("added track to queue " + track.getInfo().title);
        }

        isPlaying = true;
        return playing;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public boolean skip() {
        return !queue.isEmpty() && play(queue.get(0), true);
    }

    public void stop() {
        player.setPaused(true);
    }

    public void resume() {
        player.setPaused(false);
    }

    public void clear() {
        queue.clear();
    }

    @Override
    public void onTrackEnd(final AudioPlayer player, final AudioTrack track, final AudioTrackEndReason endReason) {
        // Advance the player if the track completed naturally (FINISHED) or if the track cannot play (LOAD_FAILED)
        skip();
        isPlaying = false;
    }

    @Override
    public void trackLoaded(final AudioTrack track) {
        // LavaPlayer found an audio source for us to play
        isPlaying = true;
        play(track);
    }

    @Override
    public void playlistLoaded(final AudioPlaylist playlist) {
        // LavaPlayer found multiple AudioTracks from some playlist
        for (AudioTrack track : playlist.getTracks()) {
            play(track);
        }
    }

    @Override
    public void noMatches() {
        // LavaPlayer did not find any audio to extract
        log.info("No extractable track found.");
    }

    @Override
    public void loadFailed(final FriendlyException exception) {
        // LavaPlayer could not parse an audio source for some reason
        log.error(exception.getMessage(), exception);
    }
}