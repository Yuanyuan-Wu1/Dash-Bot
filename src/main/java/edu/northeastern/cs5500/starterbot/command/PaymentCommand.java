package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.annotation.ExcludeFromJacocoGeneratedReport;
import edu.northeastern.cs5500.starterbot.controller.UserPreferenceController;
import edu.northeastern.cs5500.starterbot.model.Payment;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
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
public class PaymentCommand implements SlashCommandHandler {

    @Inject UserPreferenceController userPreferenceController;

    @Inject
    public PaymentCommand() {}

    /** Set the payment command for user to start with */
    @Override
    public String getName() {
        return "payment";
    }

    /**
     * Interaction part at the front end Tell users what '/payment' is and what you should do with
     * it.
     */
    @Override
    public CommandData getCommandData() {

        List<OptionData> paymentOptions = new ArrayList<OptionData>();

        OptionData cardNumberOption =
                new OptionData(
                                OptionType.STRING,
                                "cardnumber",
                                "The bot will reply to your command with the Card Number")
                        .setRequired(true);

        OptionData expireDataOption =
                new OptionData(
                                OptionType.STRING,
                                "expiredate",
                                "The bot will reply to your command with the Expire Date")
                        .setRequired(true);

        OptionData securityCodeOption =
                new OptionData(
                                OptionType.STRING,
                                "securitycode",
                                "The bot will reply to your command with the Security Code")
                        .setRequired(true);

        paymentOptions.add(cardNumberOption);
        paymentOptions.add(expireDataOption);
        paymentOptions.add(securityCodeOption);

        return new CommandData(getName(), "Tell the bot what is your payment card information")
                .addOptions(paymentOptions);
    }

    /**
     * This is the embed builder for "beautifying" view payment information command
     *
     * @param payment - representing as string, shows the user's payment information
     * @return embedBuilder
     */
    MessageEmbed reviewSetPayment(String payment) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Hi, your payment info :credit_card:  states as follow:");
        embedBuilder.addField("New payment:", payment, false);
        embedBuilder.setFooter("Please enter '/submit' to continue the order.");
        return embedBuilder.build();
    }

    /**
     * This is the embed builder for "beautifying" view payment information command
     *
     * @param currentPayment - representing as string, shows the user's last payment information
     * @param newPayment - representing as string, shows the user's changed payment information
     * @return embedBuilder
     */
    MessageEmbed reviewChangePayment(String currentPayment, String newPayment) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Hi, your payment information :credit_card:  has been changed: ");
        String currentPaymentTitle = "Last payment info: ";
        String newPaymentTitle = "Changed payment info:";
        embedBuilder.addField(currentPaymentTitle, currentPayment, false);
        embedBuilder.addField(newPaymentTitle, newPayment, false);
        embedBuilder.setColor(Color.GREEN);
        embedBuilder.setFooter("Please enter '/submit' to continue the order.");
        return embedBuilder.build();
    }

    /**
     * Reaction part at the front end After the user typing payment, what the user will see. If
     * there has card information, the user will see card number, expiration time and sec code ,
     * otherwise, it will let the user know no payment card information. * @param event discord
     * event
     */
    @ExcludeFromJacocoGeneratedReport
    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        log.info("event: /payment");

        String cardNumber = event.getOption("cardnumber").getAsString();
        String expireDate = event.getOption("expiredate").getAsString();
        String securityCode = event.getOption("securitycode").getAsString();
        String discordUserId = event.getUser().getId();

        Payment currentPayment = userPreferenceController.getPaymentForUser(discordUserId);

        Payment newPaymentModel = new Payment();

        newPaymentModel.setCardNumber(cardNumber);
        newPaymentModel.setExpireDate(expireDate);
        newPaymentModel.setSecurityCode(securityCode);

        userPreferenceController.setPaymentForUser(discordUserId, newPaymentModel);
        if (currentPayment == null) {
            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setEmbeds(reviewSetPayment(newPaymentModel.toString()));
            event.reply(messageBuilder.build()).queue();
        } else {
            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setEmbeds(
                    reviewChangePayment(currentPayment.toString(), newPaymentModel.toString()));

            event.reply(messageBuilder.build()).queue();
        }
    }
}
