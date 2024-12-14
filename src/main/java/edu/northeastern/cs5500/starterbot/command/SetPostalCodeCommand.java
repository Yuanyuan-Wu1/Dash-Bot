package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.annotation.ExcludeFromJacocoGeneratedReport;
import edu.northeastern.cs5500.starterbot.controller.UserPreferenceController;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

@Singleton
@Slf4j
public class SetPostalCodeCommand implements SlashCommandHandler {

    @Inject UserPreferenceController userPreferenceController;

    @Inject
    public SetPostalCodeCommand() {}

    @Override
    public String getName() {
        return "setpostalcode";
    }

    /**
     * Interaction part at the front end Tell users what '/setpostalcode' is and what you should do
     * with it.
     */
    @Override
    public CommandData getCommandData() {
        return new CommandData(getName(), "Tell the bot what is your address postalcode")
                .addOptions(
                        new OptionData(
                                        OptionType.STRING,
                                        "postalcode",
                                        "The bot will use this postalcode as your order address")
                                .setRequired(true));
    }

    /**
     * Reaction part at the front end After the user typing the postal code, what the user will see.
     * If there has restaurants, the user will see the preferred postalcode has been set. * @param
     * event discord event
     */
    @ExcludeFromJacocoGeneratedReport
    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        log.info("event: /setpostalcode");
        String postalCode = event.getOption("postalcode").getAsString();

        String discordUserId = event.getUser().getId();

        String oldPostalCode = userPreferenceController.getPostalCodeForUser(discordUserId);

        userPreferenceController.setPostalCodeForUser(discordUserId, postalCode);

        if (oldPostalCode == null) {
            StringBuilder sb = new StringBuilder();
            sb.append(
                    String.format(
                            "Your preferred postalcode has been set to :triangular_flag_on_post:  %s: \n \nPlease enter '/recommendations' or '/category' if you'd like to get any recommdation. \nPlease enter '/restaurant' if have an ideal restaurant to start ordering food. \nPlease enter '/history' to check the order history and its status.",
                            postalCode));
            event.reply(sb.toString()).queue();
        } else {
            StringBuilder ssb = new StringBuilder();
            ssb.append(
                    String.format(
                            "Your preferred postalcode has been changed from  %s  to  :triangular_flag_on_post:   %s \n \nPlease enter '/recommendations' or '/category' if you'd like to get any recommdation. \nPlease enter '/restaurant' if have an ideal restaurant to start ordering food. \nPlease enter '/history' to check the order history and its status.",
                            oldPostalCode, postalCode));
            event.reply(ssb.toString()).queue();
        }
    }
}
