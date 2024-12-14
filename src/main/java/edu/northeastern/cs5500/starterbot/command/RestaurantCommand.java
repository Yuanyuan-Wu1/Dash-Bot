package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.annotation.ExcludeFromJacocoGeneratedReport;
import edu.northeastern.cs5500.starterbot.controller.RestaurantController;
import edu.northeastern.cs5500.starterbot.controller.ShoppingCartController;
import edu.northeastern.cs5500.starterbot.controller.UserPreferenceController;
import edu.northeastern.cs5500.starterbot.model.Restaurants;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

@Singleton
@Slf4j
public class RestaurantCommand implements SlashCommandHandler {

    @Inject UserPreferenceController userPreferenceController;
    @Inject RestaurantController restaurantController;
    @Inject ShoppingCartController shoppingCartController;

    @Inject
    public RestaurantCommand() {}

    /** Set the restaurant command for user to start with */
    @Override
    public String getName() {
        return "restaurant";
    }

    /**
     * Interaction part at the front end Tell users what '/restaurantname' is and what you should do
     * with it.
     */
    @Override
    public CommandData getCommandData() {
        return new CommandData(
                        getName(), "Tell the bot what restaurant you'd like to take the order from")
                .addOptions(
                        new OptionData(
                                        OptionType.STRING,
                                        "name",
                                        "The bot will show the restaurant info that you are typing with")
                                .setRequired(true));
    }

    /**
     * This is the embed builder for "beautifying" view shopping cart command
     *
     * @param title - representing as string, shows the restaurant's name
     * @param menu - representing as string, shows the restaurant's menu
     * @return embedBuilder
     */
    MessageEmbed viewMenu(String title, String menu) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        String formatted_title = String.format("Here's the %s's menu: ", title);
        embedBuilder.setTitle(formatted_title);
        embedBuilder.setDescription(menu);
        embedBuilder.setFooter(
                "Please enter '/order' + dish number and dish quantity to start order your food.");
        return embedBuilder.build();
    }

    /**
     * Reaction part at the front end After the user typing the restaurant name, what the user will
     * see. If the restaurant is in our list, the user will see the menu and start order, otherwise,
     * it will let the user know this restaurant is not in our list. * @param event discord event
     */
    @ExcludeFromJacocoGeneratedReport
    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        log.info("event: /restaurant");
        String title = event.getOption("name").getAsString();

        String discordUserId = event.getUser().getId();

        String postalCode = userPreferenceController.getPostalCodeForUser(discordUserId);

        Restaurants restaurant = restaurantController.getRestaurantForUser(title, postalCode);

        if (restaurant == null) {
            event.reply("This restaurant can not be delivered.").queue();
        }

        String restaurantTitle = restaurant.getTitle();

        shoppingCartController.setRestaurantNameForCart(discordUserId, restaurantTitle);

        String message = restaurantController.getMenuForUser(title, postalCode);

        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.setEmbeds(viewMenu(restaurantTitle, message));
        event.reply(messageBuilder.build()).queue();
    }
}
