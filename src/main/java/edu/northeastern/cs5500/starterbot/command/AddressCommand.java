package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.annotation.ExcludeFromJacocoGeneratedReport;
import edu.northeastern.cs5500.starterbot.controller.UserPreferenceController;
import edu.northeastern.cs5500.starterbot.model.Address;
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
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

@Singleton
@Slf4j
public class AddressCommand implements SlashCommandHandler {

    @Inject UserPreferenceController userPreferenceController;

    @Inject
    public AddressCommand() {}

    /** Set the address command for user */
    @Override
    public String getName() {
        return "address";
    }

    /**
     * Interaction at the front and Tell users what '/address' is and what you should do with it.
     */
    @Override
    public CommandData getCommandData() {

        List<OptionData> addressOptions = new ArrayList<OptionData>();

        addressOptions.add(
                new OptionData(
                                OptionType.STRING,
                                "address",
                                "Tell the bot the address for delivery")
                        .setRequired(true));

        OptionData cityOD =
                new OptionData(
                        OptionType.STRING,
                        "city",
                        "Tell the bot in which city the order will be delivered.");
        cityOD.addChoice("Seattle", "Seattle");
        cityOD.addChoice("Spokane", "Spokane");
        cityOD.addChoice("Tacoma", "Tacoma");
        cityOD.addChoice("Vancouver", "Vancouver");
        cityOD.addChoice("Kent", "Kent");
        cityOD.addChoice("Everett", "Everett");
        cityOD.addChoice("Renton", "Renton");
        cityOD.addChoice("Kirkland", "Kirkland");
        cityOD.addChoice("Redmond", "Redmond");
        cityOD.addChoice("Sammamish", "Sammamish");
        cityOD.addChoice("Lakewood", "Lakewood");
        cityOD.addChoice("Bothell", "Bothell");
        cityOD.addChoice("Shoreline", "Shoreline");
        cityOD.addChoice("Lynnwood", "Lynnwood");
        cityOD.addChoice("University Place", "University Place");
        cityOD.addChoice("Mercer Island", "Mercer Island");
        cityOD.addChoice("Mill Creek", "Mill Creek");
        cityOD.addChoice("Woodinville", "Woodinville");
        cityOD.addChoice("Monroe", "Monroe");
        cityOD.addChoice("Kenmore", "Kenmore");
        cityOD.addChoice("Bellevue", "Bellevue");
        cityOD.addChoice("Others", "Others");
        cityOD.setRequired(true);

        addressOptions.add(cityOD);

        OptionData stateOD =
                new OptionData(
                        OptionType.STRING,
                        "state",
                        "Tell the bot in which state the order will be delivered.");

        stateOD.addChoice("WA", "WA");
        stateOD.setRequired(true);
        addressOptions.add(stateOD);

        OptionData messageOD =
                new OptionData(
                        OptionType.STRING,
                        "message",
                        "Tell the bot delivery message, such as apartment number.");

        messageOD.addChoice("Contactless food delivery", "Contactless food delivery");
        messageOD.addChoice(
                "Leave outside and ring the doorbell", "Leave outside and ring the doorbell");
        messageOD.addChoice("Meet at outside", "Meet at outside");
        messageOD.addChoice("Need more cutlery", "Need more cutlery");
        messageOD.addChoice("Thank you for your effort", "Thank you for your effort");
        messageOD.setRequired(false);

        addressOptions.add(messageOD);

        return new CommandData(getName(), "Tell the bot what is your delivery address")
                .addOptions(addressOptions);
    }

    /**
     * This is the embed builder for "beautifying" view shopping address command
     *
     * @param address - representing as string, shows user's delivery address
     * @return embedBuilder
     */
    MessageEmbed reviewSetAddress(String address) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Hi, your delivery address   :truck:   has been set to: ");
        embedBuilder.addField("New address:", address, false);
        embedBuilder.setFooter("Please enter '/payment' to continue the order.");
        return embedBuilder.build();
    }

    /**
     * This is the embed builder for "beautifying" view shopping address command
     *
     * @param currentAddressString - representing as string, shows user's last shipping address
     * @param newAddressString - representing as string, shows user's new delivery address
     * @return embedBuilder
     */
    MessageEmbed reviewChangeAddress(String currentAddressString, String newAddressString) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Hi, your delivery address   :truck:   has been changed: ");
        String currentAddressTitle = "Last shipping address: ";
        String changeAddressTitle = "Changed delivery address:";

        embedBuilder.addField(currentAddressTitle, currentAddressString, false);
        embedBuilder.addField(changeAddressTitle, newAddressString, false);
        embedBuilder.setColor(Color.GREEN);
        embedBuilder.setFooter("Please enter '/payment' to continue the order.");
        return embedBuilder.build();
    }

    /**
     * Reaction part at the front end After the user typing the delivery address, what the user will
     * see. If there has address, the user will see delivery address, otherwise, it will let the
     * user know no delivery address in the database. * @param event discord event
     */
    @ExcludeFromJacocoGeneratedReport
    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        log.info("event: /address");

        String discordUserId = event.getUser().getId();

        String address = event.getOption("address").getAsString();
        String city = event.getOption("city").getAsString();
        String state = event.getOption("state").getAsString();

        OptionMapping message = event.getOption("message");
        String messageStr = null;
        if (message != null) {
            messageStr = message.getAsString();
        }

        String currentAddressString =
                userPreferenceController.getAddressStringWithMessageForUser(discordUserId);
        Address newAddressModel = new Address();

        newAddressModel.setAddress(address);
        newAddressModel.setCity(city);
        newAddressModel.setState(state);
        newAddressModel.setMessage(messageStr);

        userPreferenceController.setAddressForUser(discordUserId, newAddressModel);

        String newAddressString =
                userPreferenceController.getAddressStringWithMessageForUser(discordUserId);

        if (currentAddressString == null) {
            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setEmbeds(reviewSetAddress(newAddressString));
            event.reply(messageBuilder.build()).queue();
        } else {
            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setEmbeds(reviewChangeAddress(currentAddressString, newAddressString));
            event.reply(messageBuilder.build()).queue();
        }
    }
}
