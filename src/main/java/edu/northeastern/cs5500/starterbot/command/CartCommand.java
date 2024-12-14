package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.annotation.ExcludeFromJacocoGeneratedReport;
import edu.northeastern.cs5500.starterbot.controller.ShoppingCartController;
import edu.northeastern.cs5500.starterbot.model.Cart;
import edu.northeastern.cs5500.starterbot.model.Order;
import java.util.ArrayList;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

@Singleton
@Slf4j
public class CartCommand implements SlashCommandHandler {

    @Inject ShoppingCartController shoppingCartController;

    @Inject
    public CartCommand() {}

    @Override
    public String getName() {
        return "cart";
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData(getName(), "To see what dishes you have in the shopping cart");
    }

    /**
     * This is the embed builder for "beautifying" view shopping cart command
     *
     * @param title the name of the restaurant
     * @param cart the shopping cart object
     * @return embedBuilder
     */
    MessageEmbed viewCart(String title, String cart) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        String formatted_title =
                String.format(
                        "Your order at %s is shown as follow:\nPlease enter '/order' or '/submit' to continue.",
                        title);
        embedBuilder.setTitle(formatted_title);
        embedBuilder.setDescription(cart);

        return embedBuilder.build();
    }

    /**
     * This method will get shopping cart information from the database and let users know what they
     * have put into the shopping cart, and current total price. * @param event discord event
     */
    @ExcludeFromJacocoGeneratedReport
    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        log.info("event: /cart");

        String discordUserId = event.getUser().getId();

        Cart shoppingCart = shoppingCartController.getShoppingCartByDiscordId(discordUserId);

        ArrayList<Order> dishList = shoppingCart.getShoppingCart();

        if (dishList.size() == 0) {
            event.reply(
                            "You have not order a dish, please use /order + dish number to put order into the shopping cart.")
                    .queue();
        } else {
            String title = shoppingCart.getRestaurantName();
            String message = shoppingCartController.formatCart(shoppingCart);

            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setEmbeds(viewCart(title, message));
            event.reply(messageBuilder.build()).queue();
        }
    }
}
