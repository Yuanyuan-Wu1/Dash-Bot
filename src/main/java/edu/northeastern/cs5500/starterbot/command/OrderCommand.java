package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.annotation.ExcludeFromJacocoGeneratedReport;
import edu.northeastern.cs5500.starterbot.controller.RestaurantController;
import edu.northeastern.cs5500.starterbot.controller.ShoppingCartController;
import edu.northeastern.cs5500.starterbot.controller.UserPreferenceController;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

@Singleton
@Slf4j
public class OrderCommand implements SlashCommandHandler {

    @Inject UserPreferenceController userPreferenceController;
    @Inject RestaurantController restaurantController;
    @Inject ShoppingCartController shoppingCartController;

    @Inject
    public OrderCommand() {}

    /** Set the order command for user to start order their food */
    @Override
    public String getName() {
        return "order";
    }

    /**
     * Interaction part at the front end Tell users what '/order' command is and what you should do
     * with it.
     */
    @Override
    public CommandData getCommandData() {

        List<OptionData> orderOptions = new ArrayList<>();

        orderOptions.add(
                new OptionData(
                                OptionType.STRING,
                                "dishnumber",
                                "Add the dish number you'd like to order")
                        .setRequired(true));

        orderOptions.add(
                new OptionData(
                                OptionType.STRING,
                                "quantity",
                                "Add the quantity of the dish you'd like to order")
                        .setRequired(true));

        return new CommandData(getName(), "Tell the bot which dish you'd like to order.")
                .addOptions(orderOptions);
    }

    /**
     * Reaction part at the front end After the user typing the order number, what the user will
     * see. If the user as add a dish, the user will see "xxx(dish name) has been added
     * successfully". * @param event discord event
     */
    @ExcludeFromJacocoGeneratedReport
    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        log.info("event: /order");
        String number = event.getOption("dishnumber").getAsString();
        String quantityToString = event.getOption("quantity").getAsString();
        Integer quantity = Integer.valueOf(quantityToString);

        String discordUserId = event.getUser().getId();

        String postalCode = userPreferenceController.getPostalCodeForUser(discordUserId);

        String title = shoppingCartController.getRestaurantNameForCart(discordUserId);

        if (title.equals("")) {
            event.reply(
                            "Please use '/recommendations' or '/category' or '/restaurant' to pick a restaurant first.")
                    .queue();
        } else {
            int dishTotal = restaurantController.getMenuToOrder(title, postalCode).size();

            if (Integer.valueOf(number) <= dishTotal) {
                String dishName =
                        restaurantController.getMenuToOrder(title, postalCode).get(number).get(0);

                Integer price =
                        Integer.valueOf(
                                restaurantController
                                        .getMenuToOrder(title, postalCode)
                                        .get(number)
                                        .get(1));

                shoppingCartController.setOrderToShoppingCart(
                        discordUserId, dishName, quantity, price);
                event.reply(
                                String.format(
                                        "Dish '%s' x%s has been added successfully!\nPlease enter '/cart' to check your order.",
                                        dishName, quantityToString))
                        .queue();
            } else {
                event.reply("Invalid dish number, please enter it again").queue();
            }
        }
    }
}
