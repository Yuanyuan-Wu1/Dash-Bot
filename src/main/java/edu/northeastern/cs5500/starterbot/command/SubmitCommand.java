package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.annotation.ExcludeFromJacocoGeneratedReport;
import edu.northeastern.cs5500.starterbot.controller.HistoryController;
import edu.northeastern.cs5500.starterbot.controller.RestaurantController;
import edu.northeastern.cs5500.starterbot.controller.ShoppingCartController;
import edu.northeastern.cs5500.starterbot.controller.UserPreferenceController;
import edu.northeastern.cs5500.starterbot.model.Address;
import edu.northeastern.cs5500.starterbot.model.Cart;
import edu.northeastern.cs5500.starterbot.model.HistoryOrder;
import edu.northeastern.cs5500.starterbot.model.Order;
import edu.northeastern.cs5500.starterbot.model.Payment;
import edu.northeastern.cs5500.starterbot.model.Status;
import edu.northeastern.cs5500.starterbot.service.PostalCodeDistanceService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.requests.RestAction;
import org.bson.types.ObjectId;

@Singleton
@Slf4j
public class SubmitCommand implements SlashCommandHandler, ButtonClickHandler {

    @Inject ShoppingCartController shoppingCartController;
    @Inject UserPreferenceController userPreferenceController;
    @Inject HistoryController historyController;
    @Inject RestaurantController restaurantController;

    @Inject
    public SubmitCommand() {}

    @Override
    public String getName() {
        return "submit";
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData(getName(), "To view the order before you place the order.");
    }

    /**
     * This is the embed builder for "beautifying" view order command
     *
     * @param name the user's username
     * @param cart shoping cart
     * @param payment payment method
     * @param address address
     * @return embedBuilder
     */
    MessageEmbed reviewOrder(
            String name, String cart, String payment, String address, String title) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        String formatted_title =
                String.format("Hi %s ,this is a summary of your current order:", name);
        embedBuilder.setTitle(formatted_title);
        String addressTitle = "Your address info states as follow:";
        String paymentTitle = "Your payment info states as follow:";

        String cartTitle = String.format("Your order at %s states a follow: ", title);
        embedBuilder.addField(addressTitle, address, false);
        embedBuilder.addField(paymentTitle, payment, false);
        embedBuilder.addField(cartTitle, cart, false);
        embedBuilder.setFooter("You may keep ordering by using '/order' + dish number.");
        return embedBuilder.build();
    }

    /**
     * This method will get shopping cart information and user information from the database and let
     * users review the order before they place the order. * @param event discord event
     */
    @ExcludeFromJacocoGeneratedReport
    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        log.info("event: /submit");

        String discordUserId = event.getUser().getId();
        String discrodName = event.getUser().getName();

        Cart shoppingCart = shoppingCartController.getShoppingCartByDiscordId(discordUserId);

        ArrayList<Order> dishList = shoppingCart.getShoppingCart();
        Payment payment = userPreferenceController.getPaymentForUser(discordUserId);
        String addressString = userPreferenceController.getAddressStringForUser(discordUserId);

        if (dishList.size() == 0) {
            event.reply(
                            "You have not order a dish, please use '/order' + dish number to put order into the shopping cart.")
                    .queue();
        } else if (payment == null || addressString == null) {
            event.reply(
                            "You have not update your address or payment method, please use '/address' &'/payment' to update your information.")
                    .queue();
        } else {
            String message = shoppingCartController.formatCart(shoppingCart);
            String title = shoppingCart.getRestaurantName();

            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setEmbeds(
                    reviewOrder(discrodName, message, payment.toString(), addressString, title));
            messageBuilder.setActionRows(
                    ActionRow.of(
                            Button.primary(this.getName() + ":submit", "Submit"),
                            Button.danger(this.getName() + ":cancel", "Cancel")));

            event.reply(messageBuilder.build()).queue();
        }
    }

    /**
     * The onClick function will reset user's shopping cart when user click on the submit or cancel
     * button. At the same time, the order information will be stored in the history order. The
     * order will be delivered at a later time using callback function Once the order is delivered,
     * a message will be received by user, and the order status will change from in_progress to
     * delivered. The delivery time is using the distance between user's zipcode to restaurant's
     * zipcode in seconds. * @param event discord event
     */
    @ExcludeFromJacocoGeneratedReport
    @Override
    public void onButtonClick(ButtonClickEvent event) {
        String discordUserId = event.getUser().getId();
        String discordName = event.getUser().getName();

        if (event.getButton().getLabel().equals("Submit")) {

            Cart shoppingCart = shoppingCartController.getShoppingCartByDiscordId(discordUserId);
            Payment payment = userPreferenceController.getPaymentForUser(discordUserId);
            Address address = userPreferenceController.getAddressForUser(discordUserId);
            Double total = shoppingCartController.getTotalFromShoppingCart(shoppingCart);
            Status status = Status.IN_PROGRESS;
            LocalDateTime date = LocalDateTime.now();

            HistoryOrder historyOrder =
                    historyController.setHistoryOrderForUser(
                            shoppingCart, address, payment, total, date, status);
            historyController.setHistoryForUser(discordUserId, historyOrder);

            event.reply(
                            "Your order has been placed! \n"
                                    + "You may use '/history' to check your order status later.")
                    .queue();

            String userZip = this.getUserZipCode(discordUserId);
            log.info(userZip.toString());

            log.info(shoppingCart.getRestaurantName());
            String restaurantZip = this.getRestaurantZipCode(shoppingCart);
            log.info(restaurantZip.toString());

            Long deliveryTime = this.getDistanceInLong(userZip, restaurantZip);

            MessageChannel channel = event.getChannel();
            Consumer<Message> callback =
                    (message) -> {
                        ObjectId id = shoppingCart.getId();
                        historyController.changeStatus(discordUserId, id);
                        Message m = message;
                        log.info("This is where the db status changes");
                        String delivered_message =
                                String.format("Hi, %s, your order has been delivered", discordName);
                        m.editMessage(delivered_message).queue();
                    };
            RestAction<Message> action = channel.sendMessage("order is delivered");
            action.queueAfter(deliveryTime, TimeUnit.SECONDS, callback);

        } else {

            event.reply(
                            "Your order has been cancelled. \n"
                                    + "You may use '/restaurant' to start ordering your dish.")
                    .queue();
        }

        shoppingCartController.resetShoppingCartByDiscordId(discordUserId);
    }

    /**
     * This method is to convert the distance from type double to Long
     *
     * @param originPostalCode the zipcode for user
     * @param zipcodeToCompare the zipcode of restaurant
     * @return distance in Long
     */
    public Long getDistanceInLong(String originPostalCode, String zipcodeToCompare) {
        PostalCodeDistanceService postalCodeDistanceService = new PostalCodeDistanceService();
        Double distance =
                postalCodeDistanceService.getPostalCodesDistance(
                        originPostalCode, zipcodeToCompare);

        return Double.valueOf(distance).longValue();
    }

    /**
     * This method is to get user's current zipcode
     *
     * @param discordId user's discord id
     * @return string of zipcode of user
     */
    @ExcludeFromJacocoGeneratedReport
    public String getUserZipCode(String discordId) {
        return userPreferenceController.getPostalCodeForUser(discordId);
    }

    /**
     * This method is to get zipcode of the restaurant user ordered food at.
     *
     * @param shoppingCart the shopping cart object of the user
     * @return the string of zipcode of the restaurant
     */
    @ExcludeFromJacocoGeneratedReport
    public String getRestaurantZipCode(Cart shoppingCart) {
        String restauranName = shoppingCart.getRestaurantName();
        String zipCode = restaurantController.getRestaurantZipCode(restauranName);
        return zipCode;
    }
}
