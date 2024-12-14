package edu.northeastern.cs5500.starterbot.command;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertFalse;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.junit.jupiter.api.Test;

public class SubmitCommandTest {
    @Test
    void testNameMatchesData() {
        SubmitCommand orderCommand = new SubmitCommand();

        String name = orderCommand.getName();
        CommandData commandData = orderCommand.getCommandData();

        assertThat(name).isEqualTo(commandData.getName());
    }

    @Test
    void testEmbedMessage() {
        SubmitCommand orderCommand = new SubmitCommand();
        MessageEmbed embed =
                orderCommand.reviewOrder("name", "cart", "payment", "address", "title");
        assertThat(embed.getTitle()).isNotEmpty();
    }

    @Test
    void testGetDistanceInLong() {
        SubmitCommand orderCommand = new SubmitCommand();
        Long distance1 = orderCommand.getDistanceInLong("98033", "98052");
        Long distance3 = orderCommand.getDistanceInLong("98052", "98033");
        Long distance2 = orderCommand.getDistanceInLong("98105", "98033");
        assertThat(distance1.equals(distance3));
        assertFalse(distance1.equals(distance2));
    }
}
