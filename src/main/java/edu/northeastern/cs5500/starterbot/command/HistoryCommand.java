package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.annotation.ExcludeFromJacocoGeneratedReport;
import edu.northeastern.cs5500.starterbot.controller.HistoryController;
import edu.northeastern.cs5500.starterbot.controller.ShoppingCartController;
import edu.northeastern.cs5500.starterbot.controller.UserPreferenceController;
import edu.northeastern.cs5500.starterbot.model.History;
import edu.northeastern.cs5500.starterbot.model.HistoryOrder;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
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
public class HistoryCommand implements SlashCommandHandler {
    @Inject ShoppingCartController shoppingCartController;
    @Inject UserPreferenceController userPreferenceController;
    @Inject HistoryController historyController;

    @Inject
    public HistoryCommand() {}

    @Override
    public String getName() {
        return "history";
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData(getName(), "To view the user's order history.");
    }

    /**
     * This is the embed builder for "beautifying" view history command
     *
     * @param history history object
     * @return embedBuilder
     */
    MessageEmbed reviewHistory(History history) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Hi, this is a summary of your history order: ");

        ArrayList<HistoryOrder> historyOrders = history.getHistoryOrder();
        if (historyOrders != null) {
            Collections.sort(historyOrders);
            for (int i = 0; i < Math.min(10, historyOrders.size()); i++) {
                embedBuilder.addField(
                        String.format(
                                "\t :receipt:  order %s  :receipt: \t", String.valueOf(i + 1)),
                        historyOrders.get(i).toString(),
                        false);
            }
        }

        embedBuilder.setColor(Color.YELLOW);
        embedBuilder.setFooter(
                "You may order again by using '/restaurant' or using '/recommendations' to select your favorites.");
        return embedBuilder.build();
    }

    /**
     * This method will get uesr's history order information from the database and let users review
     * the history.
     *
     * @param event event from discord
     */
    @ExcludeFromJacocoGeneratedReport
    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        log.info("event: /history");

        String discordUserId = event.getUser().getId();

        History history = historyController.getHistoryForUser(discordUserId);

        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.setEmbeds(reviewHistory(history));

        event.reply(messageBuilder.build()).queue();
    }
}
