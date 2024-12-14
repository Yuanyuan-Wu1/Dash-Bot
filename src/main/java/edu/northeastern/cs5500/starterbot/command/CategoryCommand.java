package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.annotation.ExcludeFromJacocoGeneratedReport;
import edu.northeastern.cs5500.starterbot.controller.CategoryController;
import edu.northeastern.cs5500.starterbot.controller.RecommendationsController;
import edu.northeastern.cs5500.starterbot.controller.UserPreferenceController;
import java.util.ArrayList;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

@Singleton
@Slf4j
public class CategoryCommand implements SlashCommandHandler {

    @Inject CategoryController categoryController;
    @Inject RecommendationsController recommendationsController;
    @Inject UserPreferenceController userPreferenceController;

    @Inject
    public CategoryCommand() {}

    @Override
    public String getName() {
        return "category";
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData(getName(), "Tell the bot what kind of restaurants you want to find")
                .addOptions(
                        new OptionData(
                                        OptionType.STRING,
                                        "type",
                                        "The bot will use this category type to find restaurants that meet your requirement")
                                .setRequired(true));
    }

    /**
     * This method will use category keyword user provided and find in the database for each
     * restaurants category. If the keyword falls into the category list of the specific restauarnt,
     * we will add this restaurant name in to the result array. Finally, we return the result array
     * in string format if any restaurants meet the critieria. If not, we reply a message to ask
     * user to search again.
     *
     * @param event discord event
     */
    @ExcludeFromJacocoGeneratedReport
    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        log.info("event: /category");
        String category = event.getOption("type").getAsString();

        String discordUserId = event.getUser().getId();
        String postalCode = userPreferenceController.getPostalCodeForUser(discordUserId);

        ArrayList<String> resMeetsCategory =
                categoryController.getRestaurantNameByCategory(category, postalCode);

        String restaurantList = categoryController.formatRestaurantList(resMeetsCategory);

        if (resMeetsCategory.size() == 0) {
            String formatted_warning =
                    String.format(
                            "There are no restaurants belongs to %s, please change your keyword and search again.",
                            category);
            event.reply(formatted_warning).queue();
        } else {
            String formatted_results =
                    String.format(
                            "We found following restaurants that meets your requirements:\n%s \nYou may use /restaurant + restaurant name command to check detailed menu of the restaurant.",
                            restaurantList);
            event.reply(formatted_results).queue();
        }
    }
}
