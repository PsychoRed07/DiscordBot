package com.psychored.discordbot.command;

import com.psychored.discordbot.command.commands.*;
import com.psychored.discordbot.tool.Pair;
import discord4j.core.object.entity.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommandExecutor {
    public static final CommandExecutor instance = new CommandExecutor();
    final Logger log = LoggerFactory.getLogger(CommandExecutor.class);
    private final String PREFIX = "!";
    private final HashMap<String, Command> registeredCommands = new HashMap<>();
    private final List<Pair<List<String>, List<String>>> commandsNameDesc = new ArrayList<>();
    private Pair<List<String>, List<String>> levelCommandsCursor;

    private CommandExecutor() {
        registerCommands();
    }

    public static CommandExecutor getInstance() {
        return instance;
    }

    public HashMap<String, Command> getCommandNames() {
        return registeredCommands;
    }

    public String getPREFIX() {
        return PREFIX;
    }

    private void addCommandInfo(String name, Class<? extends Command> commandClass) {
        try {
            levelCommandsCursor.getRight().add(commandClass.getDeclaredConstructor().newInstance().getShortDescription());
            levelCommandsCursor.getLeft().add(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addCommand(String syntax, Class<? extends Command> commandClass) {
        if (registeredCommands.containsKey(syntax.toLowerCase())) {
            log.info("Error on register command with name: " + syntax + ". Already exists.");
            return;
        }

        String commandName = syntax.toLowerCase();
        addCommandInfo(commandName, commandClass);

        try {
            Command commandInstance = commandClass.getDeclaredConstructor().newInstance();     // thanks Halcyon for noticing commands getting reinstanced every call

            registeredCommands.put(commandName, commandInstance);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private void registerCommands() {
        levelCommandsCursor = new Pair<>(new ArrayList<>(), new ArrayList<>());

        addCommand("hello", TodoCommand.class);
        addCommand("help", HelpCommand.class);
        addCommand("deleteLast", DeleteMessagesCommand.class);
        addCommand("join", JoinCommand.class);
        addCommand("search", SearchAudioCommand.class);
        addCommand("play", PlayAudioCommand.class);

        commandsNameDesc.add(levelCommandsCursor);
    }

    private Mono<Void> handleInternal(Message message) {
        final String splitRegex = "[ ]";
        String messageContent = message.getContent();
        if (messageContent.isEmpty()) {
            //temp solution for String index out of range :-1 (Message is empty.)
            messageContent = "a a";
        }
        String[] splitedMessage = messageContent.substring(1).split(splitRegex, 2);
        if (splitedMessage.length < 2) {
            splitedMessage = new String[]{splitedMessage[0], ""};
        }

        final String commandName = splitedMessage[0].toLowerCase();
        final String argument = splitedMessage[1].toLowerCase();
        Command command = registeredCommands.get(commandName);
        if (command == null) {
            if (message.getContent().startsWith(PREFIX))
                command = new NotFoundCommand();
            else
                return Mono.just(message).then();
        }

        writeLog(message, commandName);
        return command.execute(message, argument);
    }

    public Mono<Void> handle(Message message) {
        return handleInternal(message);
    }

    private void writeLog(Message message, String commandName) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        log.info(message.getUserData().username() + " used: " + commandName + " on " + dtf.format(LocalDateTime.now()));
    }

}
