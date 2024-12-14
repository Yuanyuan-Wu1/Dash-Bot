package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.annotation.ExcludeFromJacocoGeneratedReport;
import edu.northeastern.cs5500.starterbot.controller.RecommendationsController;
import edu.northeastern.cs5500.starterbot.controller.UserPreferenceController;
import java.util.Collection;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

@Singleton
@Slf4j
public class RecommendationsCommand implements SlashCommandHandler {

    @Inject RecommendationsController recommendationsController;
    @Inject UserPreferenceController userPreferenceController;

    @Inject
    public RecommendationsCommand() {}

    /** Set the recommendations command for user to show recommended restaurants */
    @Override
    public String getName() {
        return "recommendations";
    }

    /**
     * Interaction part at the front end Tell users what '/recommendations' is and what you should
     * do with it.
     */
    @Override
    public CommandData getCommandData() {
        return new CommandData(
                getName(), "Recommended restaurants within 10 kilometers of setpostalcode.");
    }

    /**
     * Reaction part at the front end and show recommended restaurants within 10 kilometers of
     * setpostalcode.. If there has restaurants, the user will see restaurants name and categories,
     * otherwise, it will let the user know no restaurants belongs to this setpostalcode. * @param
     * event discord event
     */
    @ExcludeFromJacocoGeneratedReport
    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        log.info("event: /recommendations");

        String discordUserId = event.getUser().getId();

        String postalCodeAround = userPreferenceController.getPostalCodeForUser(discordUserId);

        Collection<String> resMeetsPostalCode =
                recommendationsController.getRestaurantsInPostalCode(postalCodeAround);

        if (resMeetsPostalCode.size() == 0) {
            StringBuilder ssb = new StringBuilder();
            ssb.append(String.format("There is no restaurants belongs to %s", postalCodeAround));
            ssb.append(", please change your postalcode.");
            event.reply(ssb.toString()).queue();
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(
                    String.format(
                            "We found %d :department_store:  restaurants around :arrows_counterclockwise:  %s: \n",
                            resMeetsPostalCode.size(), postalCodeAround));
            sb.append(
                    resMeetsPostalCode
                            .toString()
                            .substring(1, resMeetsPostalCode.toString().length() - 1));

            sb.append("\n\n Please enter '/category' or '/restaurant' to choose your favorites.");

            event.reply(sb.toString()).queue();
        }
    }
}
