package edu.northeastern.cs5500.starterbot.command;

import static com.google.common.truth.Truth.assertThat;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.junit.jupiter.api.Test;

public class CartCommandTest {
    @Test
    void testNameMatchesData() {
        CartCommand cartCommand = new CartCommand();

        String name = cartCommand.getName();
        CommandData commandData = cartCommand.getCommandData();

        assertThat(name).isEqualTo(commandData.getName());
    }

    @Test
    void testEmbedMessage() {
        CartCommand cartCommand = new CartCommand();
        MessageEmbed embed = cartCommand.viewCart("title", "cart");
        assertThat(embed.getTitle()).isNotEmpty();
    }
}
