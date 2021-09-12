package com.psychored.discordbot.command;

import com.psychored.discordbot.command.commands.HelpCommand;
import com.psychored.discordbot.command.commands.NotFoundCommand;
import com.psychored.discordbot.command.commands.TodoCommand;
import com.psychored.discordbot.tool.Pair;
import discord4j.core.object.entity.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CommandExecutor {
    Logger log = LoggerFactory.getLogger(CommandExecutor.class);
    public static CommandExecutor instance = new CommandExecutor();

    public static CommandExecutor getInstance() {
        return instance;
    }

    private final String PREFIX = "!";
    private final HashMap<String, Command> registeredCommands = new HashMap<>();
    private Pair<List<String>, List<String>> levelCommandsCursor;
    private final List<Pair<List<String>, List<String>>> commandsNameDesc = new ArrayList<>();
    
    public boolean isCommand(String content){
        return content.startsWith(PREFIX);
    }

    public HashMap<String, Command> getCommandNames(){
        return registeredCommands;
    }

    public String getPREFIX() {
        return PREFIX;
    }

    private CommandExecutor(){
        registerCommands();
    }

    private void addCommandInfo(String name, Class<? extends Command> commandClass) {
        try {
            levelCommandsCursor.getRight().add(commandClass.newInstance().getDescription());
            levelCommandsCursor.getLeft().add(name);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void addCommand(String syntax, Class<? extends Command> commandClass){
        if(registeredCommands.containsKey(syntax.toLowerCase())){
            log.info("Error on register command with name: " + syntax + ". Already exists.");
            return;
        }

        String commandName = syntax.toLowerCase();
        addCommandInfo(commandName, commandClass);

        try {
            Command commandInstance = commandClass.newInstance();     // thanks Halcyon for noticing commands getting reinstanced every call

            registeredCommands.put(commandName, commandInstance);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void registerCommands(){
        levelCommandsCursor = new Pair<>(new ArrayList<String>(), new ArrayList<String>());

        addCommand("hello", TodoCommand.class);
        addCommand("help", HelpCommand.class);

        commandsNameDesc.add(levelCommandsCursor);
    }

    private Mono<Void> handleInternal(Message message){
        final String splitRegex = "[ ]";
        String[] splitedMessage = message.getContent().substring(1).split(splitRegex, 2);
        if (splitedMessage.length < 2) {
            splitedMessage = new String[]{splitedMessage[0], ""};
        }

        final String commandName = splitedMessage[0].toLowerCase();
        Command command = registeredCommands.get(commandName);
        if (command == null){
            if(message.getContent().startsWith(PREFIX))
                command = new NotFoundCommand();
            else
                return Mono.just(message).then();
        }

        writeLog(message, commandName);
        return command.execute(message);
    }

    public Mono<Void> handle(Message message){
        return handleInternal(message);
    }

    private void writeLog(Message message, String commandName){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        log.info(message.getUserData().username()+ " used: " + commandName + " on " + dtf.format(LocalDateTime.now()));
    }
    
}
