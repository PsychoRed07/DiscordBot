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

    public AudioPlayer getPlayer() {
        return player;
    }

    public List<AudioTrack> getQueue() {
        return queue;
    }

    public void play(final AudioTrack track) {
        try {
            player.playTrack(track);
        } catch (IllegalStateException e) {
            player.playTrack(track.makeClone());
        }
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
            isPlaying = playing;
    }

    //returns true if another track is played after.
    public boolean skip() {
        if (!queue.isEmpty()) {
            log.info("Playing next song in queue " + queue.get(0).getInfo().title);
            play(queue.get(0));
            return true;
        } else {
            if (isPlaying) {
                stop();
                log.info("Stopped current playing track");
            }else{
                log.info("bro idk even know how i got here.");
            }
            return false;
        }
    }

    public void stop(){
        player.stopTrack();
    }

    public void pause() {
        player.setPaused(true);
    }

    public void resume() {
        player.setPaused(false);
    }

    public void clear() {
        queue.clear();
        log.info("Track queue has been cleared.");
    }

    @Override
    public void onTrackEnd(final AudioPlayer player, final AudioTrack track, final AudioTrackEndReason endReason) {
        // Advance the player if the track completed naturally (FINISHED) or if the track cannot play (LOAD_FAILED)
        isPlaying = false;
        if(!queue.isEmpty() && track.getInfo().title.equals(queue.get(0).getInfo().title)){
            queue.remove(0);
        }
        skip();
    }

    @Override
    public void trackLoaded(final AudioTrack track) {
        // LavaPlayer found an audio source for us to play
        if (isPlaying) {
            queue.add(track);
            log.info("added track to queue " + track.getInfo().title + "Currently " + queue.size() + " tracks in queue.");
        } else {
            isPlaying = true;
            play(track);
        }
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

    @Override
    public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
        super.onTrackException(player, track, exception);
        log.info("AudioPlayer is stuck.");
        player.stopTrack();
    }

    @Override
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
        super.onTrackStuck(player, track, thresholdMs);
        log.info("AudioPlayer is stuck.");
        player.stopTrack();
    }

    @Override
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs, StackTraceElement[] stackTrace) {
        super.onTrackStuck(player, track, thresholdMs, stackTrace);
        log.info("AudioPlayer is stuck.");
        player.stopTrack();
    }
}