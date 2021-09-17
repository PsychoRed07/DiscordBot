package com.psychored.discordbot.audioplayer.service;

import java.util.HashMap;
import java.util.List;

public class YoutubeSearchManager {

    private static HashMap<String, List<YoutubeItem>> searchResults = new HashMap<String, List<YoutubeItem>>();

    public static void addSearchResult(String channelId ,List<YoutubeItem> items){
        searchResults.put(channelId, items);
    }

    public static List<YoutubeItem> getSearchResult(String channelId){
        return searchResults.get(channelId);
    }
}
