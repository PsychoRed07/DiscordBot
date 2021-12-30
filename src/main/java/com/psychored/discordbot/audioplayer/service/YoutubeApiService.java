package com.psychored.discordbot.audioplayer.service;


import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Service
public class YoutubeApiService {

    private static final String PROPERTIES_FILENAME = "application";
    private static final String YOUTUBE_API_APP = "google-youtube-api-search";
    private static final String YOUTUBE_SEARCH_TYPE = "video";
    private static final String YOUTUBE_SEARCH_FIELDS = "items(id/kind,id/videoId,snippet/title,snippet/description,snippet/thumbnails/default/url)";
    private static final String GOOGLE_YOUTUBE_URL = "https://www.youtube.com/watch?v=";
    private static final long NUMBER_OF_VIDEOS_RETURNED = 5;
    private static final Logger log = LoggerFactory.getLogger(YoutubeApiService.class);
    private static final ResourceBundle propertiesBundle;
    public static HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    public static JsonFactory jsonFactory = new JacksonFactory();
    public static YouTube youTube;

    @Value("${jya.youtube.token}")
    private String YOUTUBE_API_TOKEN;

    private static String STATIC_YOUTUBE_API_TOKEN;

    @Value("${jya.youtube.token}")
    public void setNameStatic(String name){
        YoutubeApiService.STATIC_YOUTUBE_API_TOKEN = YOUTUBE_API_TOKEN;
    }

    static {
        propertiesBundle = ResourceBundle.getBundle(PROPERTIES_FILENAME);
        youTube = new YouTube.Builder(HTTP_TRANSPORT, jsonFactory, httpRequest -> {

        }).setApplicationName(YOUTUBE_API_APP).build();
    }

    public static List<YoutubeItem> youtubeSearch(String searchQuery) {
        log.info("Starting Youtube search for '" + searchQuery+ "'");

        List<YoutubeItem> youtubeItems = new ArrayList<>();

        try {

            if (youTube != null) {
                YouTube.Search.List search = youTube.search().list("id,snippet");
                log.info("Youtube token " + STATIC_YOUTUBE_API_TOKEN);
                String apiKey = STATIC_YOUTUBE_API_TOKEN;

                search.setKey(apiKey);
                search.setQ(searchQuery);
                search.setType(YOUTUBE_SEARCH_TYPE);
                search.setFields(YOUTUBE_SEARCH_FIELDS);
                search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);

                SearchListResponse listResponse = search.execute();
                List<SearchResult> searchResults = listResponse.getItems();

                if (searchResults != null && searchResults.size() > 0) {

                    for (SearchResult r : searchResults) {
                        YoutubeItem item = new YoutubeItem(
                                GOOGLE_YOUTUBE_URL + r.getId().getVideoId(),
                                r.getSnippet().getTitle());
                        youtubeItems.add(item);
                    }

                } else {
                    log.info("No search results got from YouTube API");
                }
            } else {
                log.warn("YouTube API not initialized correctly!");
            }

        } catch (GoogleJsonResponseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            log.warn("There was an IO error: " + e.getCause() + " : " + e.getMessage());
        } catch (Throwable t) {
            log.warn("Severe errors!", t);
        }
        return youtubeItems;
    }
}
