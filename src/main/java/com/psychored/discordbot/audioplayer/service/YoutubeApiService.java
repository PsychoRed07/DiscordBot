package com.psychored.discordbot.audioplayer.service;


import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Value;
import com.google.api.services.youtube.YouTube;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ResourceBundle;

@Service
public class YoutubeApiService {

    private static Logger log = LoggerFactory.getLogger(YoutubeApiService.class);

    public static HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    public static JsonFactory jsonFactory = new JacksonFactory();

    private static final String PROPERTIES_FILENAME = "GOOGLEYOUTUBE";

    @Value("jya.youtube.token")
    private static String token;

    private static String YOUTUBE_APP_NAME = "Discord Music Bot";

    public static YouTube youTube;

    private static ResourceBundle propertiesBundle;

    static {
        propertiesBundle = ResourceBundle.getBundle(PROPERTIES_FILENAME);

        youTube = new YouTube.Builder(HTTP_TRANSPORT, jsonFactory, new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest httpRequest) throws IOException {

            }
        }).setApplicationName(YOUTUBE_APP_NAME).build();
    }
}
