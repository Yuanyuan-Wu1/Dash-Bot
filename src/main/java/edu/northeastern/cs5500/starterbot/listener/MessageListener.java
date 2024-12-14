package edu.northeastern.cs5500.starterbot.listener;

import edu.northeastern.cs5500.starterbot.command.ButtonClickHandler;
import edu.northeastern.cs5500.starterbot.command.SlashCommandHandler;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

@Slf4j
public class MessageListener extends ListenerAdapter {

    @Inject Set<SlashCommandHandler> commands;
    @Inject Set<ButtonClickHandler> buttons;

    @Inject
    public MessageListener() {
        super();
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        for (SlashCommandHandler command : commands) {
            if (command.getName().equals(event.getName())) {
                command.onSlashCommand(event);
                return;
            }
        }
    }

    @Override
    public void onButtonClick(@Nonnull ButtonClickEvent event) {
        log.info("onButtonClick: {}", event.getButton().getId());
        String id = event.getButton().getId();
        String handlerName = id.split(":", 2)[0];

        for (ButtonClickHandler buttonClickHandler : buttons) {
            if (buttonClickHandler.getName().equals(handlerName)) {
                buttonClickHandler.onButtonClick(event);
                return;
            }
        }

        log.error("Unknown button handler: {}", handlerName);
    }

    public Collection<CommandData> allCommandData() {
        return commands.stream()
                .map(SlashCommandHandler::getCommandData)
                .collect(Collectors.toList());
    }
}
