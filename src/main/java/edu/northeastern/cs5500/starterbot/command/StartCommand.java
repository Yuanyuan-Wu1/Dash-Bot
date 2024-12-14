package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.annotation.ExcludeFromJacocoGeneratedReport;
import edu.northeastern.cs5500.starterbot.controller.UserPreferenceController;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

@Singleton
@Slf4j
public class StartCommand implements SlashCommandHandler {

    @Inject UserPreferenceController userPreferenceController;

    @Inject
    public StartCommand() {}

    /** Set the start command for user to begin with */
    @Override
    public String getName() {
        return "start";
    }

    /**
     * Interaction part at the front end Tell users what '/start' is and what you should do with it.
     */
    @Override
    public CommandData getCommandData() {
        return new CommandData(getName(), "Start the bot and begin ordering the food");
    }

    /**
     * Reaction part at the front end After the user typing the restaurant name, what the user will
     * see. If the restaurant is in our list, the user will see the menu and start order, otherwise,
     * it will let the user know this restaurant is not in our list.
     */
    @ExcludeFromJacocoGeneratedReport
    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        log.info("event: /start");

        String discordUserId = event.getUser().getId();

        String discordUserName = event.getUser().getName();

        userPreferenceController.setDiscordUserName(discordUserId, discordUserName);

        String postalCode = userPreferenceController.getPostalCodeForUser(discordUserId);

        if (postalCode == null) {
            String m =
                    String.format(
                            "Hi %s,\nPlease enter '/setpostalcode' to tell us your location. \n",
                            discordUserName);
            event.reply(m).queue();
        } else {
            String m =
                    String.format(
                            "Hi %s, Here's your location: %s.\nPlease enter '/setpostalcode' if you are not in this zipcode. \nPlease enter '/recommendations' or '/category' if you'd like to get any recommdation. \nPlease enter '/restaurant' if have an ideal restaurant to start ordering food. \nPlease enter '/history' to check the order history and its status.",
                            discordUserName, postalCode);
            event.reply(m).queue();
        }
    }
}
