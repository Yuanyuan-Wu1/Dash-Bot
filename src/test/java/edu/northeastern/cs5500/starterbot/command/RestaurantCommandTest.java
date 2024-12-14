package edu.northeastern.cs5500.starterbot.command;

import static com.google.common.truth.Truth.assertThat;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.junit.jupiter.api.Test;

class RestaurantCommandTest {
    @Test
    void testRestaurantNameMatchesData() {
        RestaurantCommand restaurantCommand = new RestaurantCommand();
        String name = restaurantCommand.getName();
        CommandData commandData = restaurantCommand.getCommandData();

        assertThat(name).isEqualTo(commandData.getName());
    }

    @Test
    void testEmbedMessage() {
        RestaurantCommand restaurantCommand = new RestaurantCommand();
        MessageEmbed embed = restaurantCommand.viewMenu("title", "menu");
        assertThat(embed.getTitle()).isNotEmpty();
    }
}
